package ejbs;

import entities.Project;
import entities.Structure;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class StructureBean extends BaseBean {

    public Structure findStructure(long id){
        return em.find(Structure.class, id);
    }

    public List<Structure> getAllStructures(){
        return (List<Structure>) em.createNamedQuery("getAllStructures").getResultList();
    }

    public List<Structure> getStructures(Long idProject){
        return (List<Structure>) em.createNamedQuery("getAllStructuresFromProject").setParameter("idProject", idProject).getResultList();
    }

    public long create(long project_id, String name, int nb, double LVao, int q) throws MyConstraintViolationException, MyEntityNotFoundException {
        Project project = em.find(Project.class, project_id);
        if (project == null)
            throw new MyEntityNotFoundException("Project with id: " + project_id + " doesn't exists");
        try {
            Structure structure = new Structure(project, name, nb, LVao, q, false, null, null,null);
            em.persist(structure);
            project.addStructure(structure);
            return structure.getId();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }
    /*
    public void update(long idS, String name, String parameters, boolean clientAccepted, boolean visibleToClient, String observations) throws MyEntityNotFoundException, MyConstraintViolationException {
        Structure structure = findStructure(idS);
        if (structure == null)
            throw new MyEntityNotFoundException("Structure with id: " + idS + " doesn't exists");
        try {
            structure.setName(name);
            structure.setParameters(parameters);//TODO
            structure.setClientAccepted(clientAccepted);
            structure.setVisibleToClient(visibleToClient);
            structure.setClientObservations(observations);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }*/

    public void delete(long id) throws MyEntityNotFoundException {
        Structure structure = findStructure(id);
        if (structure == null)
            throw new MyEntityNotFoundException("Structure with id: " + id + " doesn't exist");
        structure.getProject().removeStructure(structure);
        em.remove(structure);
    }

    public void visibleToClient(long id) throws MyEntityNotFoundException, MyConstraintViolationException {
        Structure structure = findStructure(id);
        if (structure == null)
            throw new MyEntityNotFoundException("Structure with id: " + id + " doesn't exist");
        try {
            structure.setVisibleToClient(true);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void setStatus(long id, boolean status) throws MyEntityNotFoundException, MyConstraintViolationException {
        Structure structure = findStructure(id);
        if (structure == null)
            throw new MyEntityNotFoundException("Structure with id: " + id + " doesn't exist");
        try {
            structure.setClientAccepted(status);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }


}
