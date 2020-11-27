package ejbs;

import entities.Family;
import entities.Manufacturer;
import entities.Profile;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

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

	public void update(long id, String name, String manufacturerUsername, boolean isAdmin) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {

		Family family = getFamily(id);
		if (family == null)
			throw new MyEntityNotFoundException("Family with id: " + id + " doesn't exist");

		if (!isAdmin && !family.getManufacturer().getUsername().equals(manufacturerUsername))
			throw new MyIllegalArgumentException("No permission");


		try {
			family.setName(name);

		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	public void delete(long id) throws MyEntityNotFoundException {
		Family family = getFamily(id);
		if (family == null)
			throw new MyEntityNotFoundException("Family with id: " + id + " doesn't exist");

		family.getManufacturer().removeFamily(family);
		em.remove(family);
	}

	public List<Family> getAllFromManufacturer(String name) {
		return (List<Family>) em.createNamedQuery("getAllFamiliesFromManufacturer").getResultList();
	}
}
