package ejbs;

import entities.Client;
import entities.Manufacturer;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ManufacturerBean extends BaseBean{

    public List<Manufacturer> getAllManugactures(){
        return (List<Manufacturer>)em.createNamedQuery("getAllManufacturers").getResultList();
    }

    public Manufacturer getManufacturer(String username) {
        return em.find(Manufacturer.class, username);
    }

    public void create(String username, String password, String name, String email)
            throws MyEntityExistsException, MyConstraintViolationException {

        if (getManufacturer(username) != null)
            throw new MyEntityExistsException("Client with username: " + username + " already exists");
        try {
            Manufacturer manufacturer = new Manufacturer(username, password, name, email);
            em.persist(manufacturer);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(String username, String password, String name, String email)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        Manufacturer manufacturer = getManufacturer(username);
        if (manufacturer== null)
            throw new MyEntityNotFoundException("Manufacturer with username: " + username + " doesn't exist");
        try {
            manufacturer.setPassword(password);
            manufacturer.setName(name);
            manufacturer.setEmail(email);
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Manufacturer manufacturer = getManufacturer(username);
        if (manufacturer== null)
            throw new MyEntityNotFoundException("Manufacturer with username: " + username + " doesn't exist");
        em.remove(manufacturer);
    }
}
