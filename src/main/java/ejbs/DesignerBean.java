package ejbs;

import entities.Client;
import entities.Designer;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class DesignerBean extends BaseBean {

    public List<Designer> getAllDesigners() {
        return (List<Designer>) em.createNamedQuery("getAllDesigners").getResultList();
    }

    public Designer getDesigner(String username) {
        return em.find(Designer.class, username);
    }

    public void create(String username, String password, String name, String email)
            throws MyEntityExistsException, MyConstraintViolationException {

        if (getDesigner(username) != null)
            throw new MyEntityExistsException("Designer with username: " + username + " already exists");
        try {
            Designer designer = new Designer(username, password, name, email);
            em.persist(designer);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(String username, String password, String name, String email)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        Designer designer = getDesigner(username);
        if (designer== null)
            throw new MyEntityNotFoundException("Designer with username: " + username + " doesn't exist");
        try {
            designer.setPassword(password);
            designer.setName(name);
            designer.setEmail(email);
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Designer designer = getDesigner(username);
        if (designer== null)
            throw new MyEntityNotFoundException("Client with username: " + username + " doesn't exist");
        em.remove(designer);
    }


}
