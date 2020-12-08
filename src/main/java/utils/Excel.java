package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ejbs.FamilyBean;
import ejbs.ProfileBean;
import entities.Family;
import entities.Manufacturer;
import entities.Material;
import entities.Profile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

@Stateless
public class Excel {
	
	// variant => profile
	// product => family
	@EJB
	ProfileBean profileBean;

	@EJB
	FamilyBean familyBean;

	public void readFromExcel(String folder, String manufacturer) {

		try {
			File f = new File(folder);
			String[] files = f.list();

			for (String fileName : files) {

				long familyID = familyBean.create(fileName.replace(".xlsx", ""), manufacturer);

				FileInputStream file = new FileInputStream(new File(folder + fileName));
				XSSFWorkbook workbook = new XSSFWorkbook(file);

				XSSFSheet sheet1 = workbook.getSheetAt(1);
				XSSFSheet sheet2 = workbook.getSheetAt(2);

				int numberRowsSheet1 = sheet1.getPhysicalNumberOfRows();

				XSSFRow rowSheet1, rowSheet2;
				for (int i = 5; i < numberRowsSheet1; i++) {
					rowSheet1 = sheet1.getRow(i);
					rowSheet2 = sheet2.getRow(i);

					if (rowSheet1.getCell(0) == null || rowSheet2.getCell(0) == null) {
						break;
					}

					String name = rowSheet1.getCell(0).getStringCellValue();
					double weff_p = rowSheet2.getCell(5).getNumericCellValue();
					double weff_n = rowSheet2.getCell(7).getNumericCellValue();
					double ar = rowSheet1.getCell(5).getNumericCellValue();
					int sigmaC = 220000; // TODO

					profileBean.create(familyID, manufacturer, name, weff_p, weff_n, ar, sigmaC);

				}

				// add mcr_p and mcr_n when it exists
				if (workbook.getNumberOfSheets() > 3) {
					XSSFSheet sheet3 = workbook.getSheetAt(3);
					int numberRowsSheet3 = sheet3.getPhysicalNumberOfRows();

					Profile variant = null;
					XSSFRow row;
					XSSFCell nameCell;
					for (int i = 1; i < numberRowsSheet3; i++) {
						row = sheet3.getRow(i);
						nameCell = row.getCell(0);
						if (nameCell != null && !nameCell.getStringCellValue().isEmpty()) {
							//int j = getProfileIndex(product, nameCell.getStringCellValue());
							// = product.getMaterials().get(j);
							variant = profileBean.getProfileByNameAndFamily(nameCell.getStringCellValue(), familyID);
						}

						if(variant != null) {
							if(row.getCell(1) == null) {
								break;
							}

							double l = row.getCell(1).getNumericCellValue();
							double mcr = row.getCell(2).getNumericCellValue();

							variant.addMcr_p(l /1000, mcr);

						} else {
							System.err.println(nameCell.getStringCellValue() + " AVISO!!");
						}
					}


				}


				file.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*private Profile getProfile(Family family, String variant) {
		Set<Material> variants = family.getMaterials();
		for (Material material : variants) {
			if(material.getName().equals(variant)) {
				return (Profile)material;
			}
		}
		return null;
	}*/
}
