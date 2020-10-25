package ejbs;

import entities.Client;
import entities.Designer;
import entities.Material;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ProjectBean extends BaseBean {

    public List<Project> getAllProjects() {
        return (List<Project>) em.createNamedQuery("getAllProjects").getResultList();
    }

    public Project getProject(long id) {
        return em.find(Project.class, id);
    }

    public void create(String clientUsername, String designerUsername, String title, String description) throws MyEntityNotFoundException, MyConstraintViolationException {
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

            em.persist(project);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void update(){
        // TODO
    }

    public void delete(long id) throws MyEntityNotFoundException {
        Project project = getProject(id);
        if (project == null)
            throw new MyEntityNotFoundException("Project with id: " + id + " doesn't exist");

    }


}
