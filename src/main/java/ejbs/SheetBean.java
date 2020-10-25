package ejbs;

import entities.Manufacturer;
import entities.Material;
import entities.Sheet;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class SheetBean extends BaseBean {

	public List<Sheet> getAllSheets() {
		return (List<Sheet>) em.createNamedQuery("getAllSheets").getResultList();
	}

	public Sheet getSheet(long id) {
		return em.find(Sheet.class, id);
	}

	public void create(String name, String description, String manufacturerUsername, String family, double thickness, double mass, String steelGrade) throws MyEntityNotFoundException, MyConstraintViolationException {
		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);
		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username " + manufacturerUsername + " doesn't exist");

		try {
			Sheet sheet = new Sheet(name, description, manufacturer, family, thickness, mass, steelGrade);
			em.persist(sheet);
			manufacturer.addMaterial(sheet);
		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	public void update(long id, String name, String description, String manufacturerUsername, String family, double thickness, double mass, String steelGrade) throws MyEntityNotFoundException, MyConstraintViolationException {
		Sheet sheet = getSheet(id);
		if (sheet == null)
			throw new MyEntityNotFoundException("Sheet with id: " + id + " doesn't exist");

		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);
		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");
		try {
			sheet.setName(name);
			sheet.setDescription(description);
			sheet.setManufacturer(manufacturer);
			sheet.setFamily(family);
			sheet.setThickness(thickness);
			sheet.setMass(mass);
			sheet.setSteelGrade(steelGrade);

		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	public void delete(long id) throws MyEntityNotFoundException {
		Sheet sheet = getSheet(id);
		if (sheet == null)
			throw new MyEntityNotFoundException("Sheet with id: " + id + " doesn't exist");

		sheet.getManufacturer().removeMaterial(sheet);
		em.remove(sheet);
	}
}
