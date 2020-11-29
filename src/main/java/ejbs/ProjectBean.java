package ejbs;

import entities.Client;
import entities.Designer;
import entities.Manufacturer;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class ProjectBean extends BaseBean {

    public List<Project> getAllProjects() {
        return (List<Project>) em.createNamedQuery("getAllProjects").getResultList();
    }

    public Project getProject(long id) {
        return em.find(Project.class, id);
    }

    public long create(String clientUsername, String designerUsername, String title, String description) throws MyEntityNotFoundException, MyConstraintViolationException {
        Client client = em.find(Client.class, clientUsername);
        if (client == null)
            throw new MyEntityNotFoundException("Client with username: " + clientUsername + " doesn't exist");
        Designer designer = em.find(Designer.class, designerUsername);
        if (designer == null)
            throw new MyEntityNotFoundException("Designer with username: " + designerUsername + " doesn't exist");
        try {
            Project project = new Project(
                    client,
                    designer,
                    title,
                    description
            );
            // TODO adicionar ao client e designer o projeto
            em.persist(project);
            client.addProject(project);
            designer.addProject(project);
            return project.getId();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(long id, String title,String description, String observations)
            throws MyEntityNotFoundException, MyConstraintViolationException {

        Project project = getProject(id);
        if (project == null)
            throw new MyEntityNotFoundException("Project with id: " + id + " doesn't exist");
        try {
            project.setTitle(title);
            project.setDescription(description);
            project.setObservations(observations);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void delete(long id) throws MyEntityNotFoundException {
        Project project = getProject(id);
        if (project == null)
            throw new MyEntityNotFoundException("Project with id: " + id + " doesn't exist");
        //TODO to be finished
    }


}
