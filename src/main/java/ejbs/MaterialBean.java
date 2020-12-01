package ejbs;

import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.List;

@Stateless
public class MaterialBean extends BaseBean {

	public List<Material> getAllMaterials() {
		return (List<Material>) em.createNamedQuery("getAllMaterials").getResultList();
	}

	public Material getMaterial(long id) {
		return em.find(Material.class, id);
	}

	public long createProfile(long familyID, String manufacturerUsername, String name, double weff_p, double weff_n, double ar, double sigmaC, LinkedHashMap<Double, Double> mcr_p, LinkedHashMap<Double, Double> mcr_n) throws MyEntityNotFoundException, MyConstraintViolationException {
		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);
		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");

		Family family = em.find(Family.class, familyID);
		if (family == null)
			throw new MyEntityNotFoundException("Family with id: " + familyID + " doesn't exist");

		try {
			Profile profile = new Profile(name, manufacturer, family, weff_p, weff_n, ar, sigmaC, mcr_p, mcr_n);
			em.persist(profile);
			family.addMaterial(profile);
			manufacturer.addMaterial(profile);
			return profile.getId();
		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	public long createSheet(long familyID, String manufacturerUsername, String name, double thickness) throws MyEntityNotFoundException, MyConstraintViolationException {
		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);
		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");

		Family family = em.find(Family.class, familyID);
		if (family == null)
			throw new MyEntityNotFoundException("Family with id: " + familyID + " doesn't exist");

		try {
			Sheet sheet = new Sheet(name, manufacturer, family, thickness);
			em.persist(sheet);
			family.addMaterial(sheet);
			manufacturer.addMaterial(sheet);
			return sheet.getId();
		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	public void delete(long id) throws MyEntityNotFoundException {
		Material material = getMaterial(id);
		if (material == null)
			throw new MyEntityNotFoundException("Material with id: " + id + " doesn't exist");

		material.getManufacturer().removeMaterial(material);
		material.getFamily().removeMaterial(material);
		em.remove(material);
	}

	public void updateProfile(long id, long familyID, String name, double weff_p, double weff_n, double ar, double sigmaC, LinkedHashMap<Double, Double> mcr_p, LinkedHashMap<Double, Double> mcr_n) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {

		Profile profile = em.find(Profile.class, id);
		if (profile == null)
			throw new MyEntityNotFoundException("Profile with id: " + id + " doesn't exist");

		try {
			if(profile.getFamily().getId() != familyID) { // if family changed
				profile.getFamily().removeMaterial(profile);

				Family newFamily = em.find(Family.class, familyID);
				if (newFamily == null)
					throw new MyEntityNotFoundException("Family with id: " + id + " doesn't exist");

				profile.setFamily(newFamily);
				newFamily.addMaterial(profile);
			}

			profile.setName(name);
			profile.setWeff_p(weff_p);
			profile.setWeff_n(weff_n);
			profile.setAr(ar);
			profile.setSigmaC(sigmaC);
			profile.setMcr_p(mcr_p);
			profile.setMcr_n(mcr_n);



		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	public void updateSheet(long id, long familyID, String name, double thickness) throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {

		Sheet sheet = em.find(Sheet.class, id);
		if (sheet == null)
			throw new MyEntityNotFoundException("Sheet with id: " + id + " doesn't exist");

		try {
			if(sheet.getFamily().getId() != familyID) { // if family changed
				sheet.getFamily().removeMaterial(sheet);

				Family newFamily = em.find(Family.class, familyID);
				if (newFamily == null)
					throw new MyEntityNotFoundException("Family with id: " + id + " doesn't exist");

				sheet.setFamily(newFamily);
				newFamily.addMaterial(sheet);
			}

			sheet.setName(name);
			sheet.setThickness(thickness);



		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}
}
