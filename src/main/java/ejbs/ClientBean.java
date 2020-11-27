package ejbs;

import entities.Client;
import entities.Project;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ClientBean extends BaseBean {

	public List<Client> getAllClients() {
		return (List<Client>) em.createNamedQuery("getAllClients").getResultList();
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

	public void update(String username, String newPassword, String oldPassword, String name, String email, String contact, String address)
			throws MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {

		Client client = getClient(username);
		if (client == null)
			throw new MyEntityNotFoundException("Client with username: " + username + " doesn't exist");
		try {

			if(!User.hashPassword(oldPassword).equals(client.getPassword())){
				throw new MyIllegalArgumentException("Old Password is wrong");
			}

			if(newPassword != null && !newPassword.equals(""))
				client.setPassword(newPassword);

			client.setName(name);
			client.setEmail(email);
			client.setContact(contact);
			client.setAddress(address);
		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		} catch (MyIllegalArgumentException e) {
			throw new MyIllegalArgumentException(e.getMessage());
		}
	}

	public void delete(String username) throws MyEntityNotFoundException {
		Client client = getClient(username);
		if (client == null)
			throw new MyEntityNotFoundException("Client with username: " + username + " doesn't exist");
		em.remove(client);
	}

	public List<Project> getAllClientProjects(String username) {
		return (List<Project>) em.createNamedQuery("getClientProjects").setParameter("username", username).getResultList();
	}

	public List<Client> searchClientsByName(String name) {
		return (List<Client>) em.createNamedQuery("searchClientsByName").setMaxResults(10).setParameter("name", name).getResultList();
	}

}
