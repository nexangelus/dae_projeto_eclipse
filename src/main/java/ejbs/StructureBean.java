package ejbs;

import dtos.MaterialDTO;
import entities.Material;
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

    public void update(long idS, String name, int nb, double LVao, int q) throws MyEntityNotFoundException, MyConstraintViolationException {
        Structure structure = findStructure(idS);
        if (structure == null)
            throw new MyEntityNotFoundException("Structure with id: " + idS + " doesn't exists");
        try {
            structure.setName(name);
            structure.setNb(nb);
            structure.setLVao(LVao);
            structure.setQ(q);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

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

    public void addMaterial(long id, long idM) throws MyEntityNotFoundException, MyConstraintViolationException {
        Structure structure = findStructure(id);
        if (structure == null)
            throw new MyEntityNotFoundException("Structure with id: " + id + " doesn't exist");
        Material material = em.find(Material.class, idM);
        if (material == null)
            throw new MyEntityNotFoundException("Material with id: " + id + " doesn't exist");
        try {
            structure.addMaterial(material);
            material.addStructure(structure);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public void removeMaterial(long id, long idM) throws MyEntityNotFoundException, MyConstraintViolationException {
        Structure structure = findStructure(id);
        if (structure == null)
            throw new MyEntityNotFoundException("Structure with id: " + id + " doesn't exist");
        Material material = em.find(Material.class, idM);
        if (material == null)
            throw new MyEntityNotFoundException("Material with id: " + id + " doesn't exist");
        try {
            structure.removeMaterial(material);
            material.removeStructure(structure);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public List<Structure> getStructuresFromManufacturerMaterials(String manufacturerUsername) {
        return (List<Structure>) em.createNamedQuery("getStructuresWithMaterialsFromManufacturer").setParameter("manufacturerUsername", manufacturerUsername).getResultList();
    }

}
