package ejbs;

import entities.Project;
import entities.Upload;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class UploadBean extends BaseBean {

    public void create(Long project_id, String filepath, String filename) throws MyEntityNotFoundException, MyConstraintViolationException {
        Project project = em.find(Project.class, project_id);
        if (project == null)
            throw new MyEntityNotFoundException("Project with id: " + project_id + " doesn't exists");
        try {
            Upload upload = new Upload(filepath,filename,project);
            em.persist(upload);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Upload findUpload(long id){
        return em.find(Upload.class, id);
    }

    public List<Upload> getStudentDocuments(Long idProject){
        return (List<Upload>) em.createNamedQuery("getAllProjectUploads").setParameter("idProject", idProject).getResultList();
    }
}
