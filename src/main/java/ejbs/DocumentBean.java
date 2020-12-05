package ejbs;

import entities.Document;
import entities.Project;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class DocumentBean extends BaseBean {

    public Project findProject(long id) {
        return em.find(Project.class, id);
    }

    public Document findDocument(long id) {
        return em.find(Document.class, id);
    }

    public List<Document> getStudentDocuments(Long idProject) {
        return (List<Document>) em.createNamedQuery("getAllProjectUploads").setParameter("idProject", idProject).getResultList();
    }

    public void create(Long project_id, String filepath, String filename) throws MyEntityNotFoundException, MyConstraintViolationException {
        Project project = findProject(project_id);
        if (project == null)
            throw new MyEntityNotFoundException("Project with id: " + project_id + " doesn't exists");
        try {
            Document document = new Document(filepath, filename, project);
            em.persist(document);
            project.addDocument(document);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void delete(long id) throws MyEntityNotFoundException, MyConstraintViolationException {
        Document document = findDocument(id);
        Project project = document.getProject();
        if (document == null)
            throw new MyEntityNotFoundException("Document with id: " + id + " doesn't exists");
        if (project == null)
            throw new MyEntityNotFoundException("Project with id: " + project.getId() + " doesn't exists");
        try {
            em.remove(document);
            project.removeDocument(document);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

}
