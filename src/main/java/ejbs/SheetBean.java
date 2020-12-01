package ejbs;

import entities.Family;
import entities.Manufacturer;
import entities.Profile;
import entities.Sheet;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class SheetBean extends BaseBean {

	public List<Sheet> getAll() {
		return (List<Sheet>) em.createNamedQuery("getAllSheets").getResultList();
	}

	public Sheet getSheet(long id) {
		return em.find(Sheet.class, id);
	}

	public void create(long familyID, String manufacturerUsername, String name, double thickness) throws MyConstraintViolationException, MyEntityNotFoundException {
		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);

		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");

		Family family = em.find(Family.class, familyID);

		if (family == null)
			throw new MyEntityNotFoundException("Family with ID: " + familyID + " doesn't exist");


		try {
			Sheet sheet = new Sheet(name, manufacturer, family, thickness);
			em.persist(sheet);
			manufacturer.addMaterial(sheet);
			family.addMaterial(sheet);
		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	/*public void update(long id, String name, String description, String family, String manufacturerUsername, ...)
			throws MyEntityNotFoundException, MyConstraintViolationException {


	}*/

	public void delete(long id) throws MyEntityNotFoundException {
		Sheet sheet = getSheet(id);
		if (sheet == null)
			throw new MyEntityNotFoundException("Sheet with id: " + id + " doesn't exist");

		sheet.getManufacturer().removeMaterial(sheet);
		sheet.getFamily().removeMaterial(sheet);
		em.remove(sheet);
	}

}
