package ejbs;

import entities.Manufacturer;
import entities.Material;
import entities.Profile;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ProfileBean extends BaseBean {

    public List<Profile> getAllProfiles() {
        return (List<Profile>) em.createNamedQuery("getAllProfiles").getResultList();
    }

    public Profile getProfile(long id) {
        return em.find(Profile.class, id);
    }

    public void create(String name, String description, String manufacturerUsername, String family, double height, double thickness, double weight, double areaPainting, String steelGrade)
            throws MyConstraintViolationException, MyEntityNotFoundException {

        Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);

        if (manufacturer == null)
            throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");
        try {
            Profile profile = new Profile(name,  description,  manufacturer,  family,  height,  thickness,  weight,  areaPainting,  steelGrade);
            em.persist(profile);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(long id, String name, String description, String family, String manufacturerUsername, double height,
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
    }

    public void delete(long id) throws MyEntityNotFoundException {
        Profile profile = getProfile(id);
        if (profile == null)
            throw new MyEntityNotFoundException("Profile with id: " + id + " doesn't exist");
        em.remove(profile);
    }
}
