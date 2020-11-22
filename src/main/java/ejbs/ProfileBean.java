package ejbs;

import entities.Family;
import entities.Manufacturer;
import entities.Profile;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ProfileBean extends BaseBean {

	public List<Profile> getAll() {
		return (List<Profile>) em.createNamedQuery("getAllProfiles").getResultList();
	}

	public Profile getProfile(long id) {
		return em.find(Profile.class, id);
	}

	public void create(long familyID, String manufacturerUsername, String name, double weff_p, double weff_n, double ar, double sigmaC) throws MyConstraintViolationException, MyEntityNotFoundException {
		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);

		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");

		Family family = em.find(Family.class, familyID);

		if (family == null)
			throw new MyEntityNotFoundException("Family with ID: " + familyID + " doesn't exist");


		try {
			Profile profile = new Profile(name, manufacturer, family, weff_p, weff_n, ar, sigmaC);
			em.persist(profile);
			manufacturer.addMaterial(profile);
			family.addMaterial(profile);
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
		Profile profile = getProfile(id);
		if (profile == null)
			throw new MyEntityNotFoundException("Profile with id: " + id + " doesn't exist");

		profile.getManufacturer().removeMaterial(profile);
		profile.getFamily().removeMaterial(profile);
		em.remove(profile);
	}

	public Profile getProfileByNameAndFamily(String name, long familyID) throws MyEntityNotFoundException, MyIllegalArgumentException {
		return getProfileByNameAndFamily(name, em.find(Family.class, familyID));
	}

	public Profile getProfileByNameAndFamily(String name, Family family) throws MyIllegalArgumentException, MyEntityNotFoundException {
		if(family == null)
			throw new MyIllegalArgumentException("Family can't be null");

		if(!em.contains(family))
			throw new MyEntityNotFoundException("Family doesn't exist");

		return (Profile) em.createNamedQuery("getProfileByNameAndFamily").setParameter("profileName", name)
				.setParameter("family",family).getSingleResult();
	}

}
