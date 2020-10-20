package ejbs;

import entities.Manufacturer;
import entities.Material;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class MaterialBean extends BaseBean{

    public List<Material> getAllMaterials(){
        return (List<Material>)em.createNamedQuery("getAllMaterials").getResultList();
    }

    public Material getMaterial(long id) {
        return em.find(Material.class, id);
    }

    public void create(String name, String description, long manufacturerUsername, String family)
            throws MyEntityExistsException, MyConstraintViolationException {

        Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);

        if (manufacturer == null)
            throw new MyEntityExistsException("Manufacturer with username: " + manufacturerUsername + " doesn't exist"); //TODO qual é a exception
        try {
            Material material = new Material(name, description, manufacturer, family);
            em.persist(material);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(long id, String name, String description, String family, long manufacturerUsername)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        Material material = getMaterial(id);
        if (material== null)
            throw new MyEntityNotFoundException("Material with id: " + id + " doesn't exist");
        Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);
        if (manufacturer == null)
            throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");
        try {
            material.setName(name);
            material.setDescription(description);
            material.setFamily(family);
            material.setManufacturer(manufacturer);

        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }

    public void delete(long id) throws MyEntityNotFoundException {
        Material material = getMaterial(id);
        if (material== null)
            throw new MyEntityNotFoundException("Material with id: " + id + " doesn't exist");
        em.remove(material);
    }
}
