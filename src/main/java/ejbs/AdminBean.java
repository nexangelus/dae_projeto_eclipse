package ejbs;

import entities.Admin;
import entities.Client;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class AdminBean extends BaseBean{

    public List<Admin> getAllAdmins(){
        return (List<Admin>)em.createNamedQuery("getAllAdmins").getResultList();
    }

    public Admin getAdmin(String username) {
        return em.find(Admin.class, username);
    }

    public void create(String username, String password, String name, String email)
            throws MyEntityExistsException, MyConstraintViolationException {

        if (getAdmin(username) != null)
            throw new MyEntityExistsException("Admin with username: " + username + " already exists");
        try {
            Admin admin = new Admin(username, password, name, email);
            em.persist(admin);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(String username, String password, String name, String email)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        Admin admin = getAdmin(username);
        if (admin== null)
            throw new MyEntityNotFoundException("Admin with username: " + username + " doesn't exist");
        try {
            admin.setPassword(password);
            admin.setName(name);
            admin.setEmail(email);
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Admin admin = getAdmin(username);
        if (admin== null)
            throw new MyEntityNotFoundException("Admin with username: " + username + " doesn't exist");
        em.remove(admin);
    }
}
