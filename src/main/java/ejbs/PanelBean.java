package ejbs;

import entities.Manufacturer;
import entities.Panel;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class PanelBean extends BaseBean {

	public List<Panel> getAllPanels() {
		return (List<Panel>) em.createNamedQuery("getAllPanels").getResultList();
	}

	public Panel getPanel(long id) {
		return em.find(Panel.class, id);
	}

	public void create(String name, String description, String manufacturerUsername, String family, double thickness, double width, double lengthMin, double lengthMax, double weight) throws MyEntityNotFoundException, MyConstraintViolationException {
		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);
		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username " + manufacturerUsername + " doesn't exist");

		try {
			Panel panel = new Panel(name, description, manufacturer, family, thickness, width, lengthMin, lengthMax, weight);
			em.persist(panel);
			manufacturer.addMaterial(panel);
		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	public void update(long id, String name, String description, String manufacturerUsername, String family, double thickness, double width, double lengthMin, double lengthMax, double weight) throws MyEntityNotFoundException, MyConstraintViolationException {
		Panel panel = getPanel(id);
		if (panel == null)
			throw new MyEntityNotFoundException("Panel with id: " + id + " doesn't exist");

		Manufacturer manufacturer = em.find(Manufacturer.class, manufacturerUsername);
		if (manufacturer == null)
			throw new MyEntityNotFoundException("Manufacturer with username: " + manufacturerUsername + " doesn't exist");
		try {
			panel.setName(name);
			panel.setDescription(description);
			panel.setManufacturer(manufacturer);
			panel.setFamily(family);
			panel.setThickness(thickness);
			panel.setWidth(width);
			panel.setLengthMin(lengthMin);
			panel.setLengthMax(lengthMax);
			panel.setWeight(weight);

		} catch (ConstraintViolationException e) {
			throw new MyConstraintViolationException(e);
		}
	}

	public void delete(long id) throws MyEntityNotFoundException {
		Panel panel = getPanel(id);
		if (panel == null)
			throw new MyEntityNotFoundException("Panel with id: " + id + " doesn't exist");

		panel.getManufacturer().removeMaterial(panel);
		em.remove(panel);
	}
}
