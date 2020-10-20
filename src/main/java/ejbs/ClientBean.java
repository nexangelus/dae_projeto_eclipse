package ejbs;

import entities.Client;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ClientBean {

    @PersistenceContext
    EntityManager em;

    public List<Client> getAllClients(){
        return (List<Client>)em.createNamedQuery("getAllClients").getResultList();
    }

    public Client getClient(String username) {
        return em.find(Client.class, username);
    }

    public void create(String username, String password, String name, String email, String contact, String address)
            throws MyEntityExistsException, MyConstraintViolationException {

        if (getClient(username) != null)
            throw new MyEntityExistsException("Client with username: " + username + " already exists");
        try {
            Client client = new Client(username, password, name, email, contact, address);
            em.persist(client);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(String username, String password, String name, String email, String contact, String address)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        Client client = getClient(username);
        if (client== null)
            throw new MyEntityNotFoundException("Client with username: " + username + " doesn't exist");
        try {
            client.setPassword(password);
            client.setName(name);
            client.setEmail(email);
            client.setContact(contact);
            client.setAddress(address);
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }
    }

    public void delete(String username) throws MyEntityNotFoundException {
        Client client = getClient(username);
        if (client== null)
            throw new MyEntityNotFoundException("Client with username: " + username + " doesn't exist");
        em.remove(client);
    }

}
