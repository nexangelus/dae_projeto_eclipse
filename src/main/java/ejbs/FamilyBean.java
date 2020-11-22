package ejbs;

import entities.Family;
import entities.Manufacturer;
import entities.Profile;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class FamilyBean extends BaseBean {

	public List<Family> getAll() {
		return (List<Family>) em.createNamedQuery("getAllFamilies").getResultList();
	}

	public Family getFamily(long id) {
		return em.find(Family.class, id);
	}

	public long create(String name, String manufacturerUsername) throws MyConstraintViolationException, MyEntityNotFoundException {
		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);

		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");

		try {
			Family family = new Family(name, manufacturer);
			em.persist(family);
			manufacturer.addFamily(family);
			return family.getId();
		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	/*public void update(long id, String name, String description, String family, String manufacturerUsername, double height,
	                   double thickness, double weight, double areaPainting, String steelGrade)
			throws MyEntityNotFoundException, MyConstraintViolationException {

		Profile profile = getProfile(id);
		if (profile == null)
			throw new MyEntityNotFoundException("Profile with id: " + id + " doesn't exist");
		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);
		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");
		try {
			profile.setName(name);
			profile.setDescription(description);
			profile.setFamily(family);
			profile.setManufacturer(manufacturer);
			profile.setHeight(height);
			profile.setThickness(thickness);
			profile.setWeight(weight);
			profile.setAreaPainting(areaPainting);
			profile.setSteelGrade(steelGrade);

		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}*/

	public void delete(long id) throws MyEntityNotFoundException {
		Family family = getFamily(id);
		if (family == null)
			throw new MyEntityNotFoundException("Family with id: " + id + " doesn't exist");

		family.getManufacturer().removeFamily(family);
		em.remove(family);
	}
}
