package entities;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Manufacturer extends User {

    @OneToMany
    private Set<Material> materials;

    public Manufacturer() {
        super();
        this.materials = new LinkedHashSet<>();
    }

    public Manufacturer(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.materials = new LinkedHashSet<>();
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }

    public void addMaterial(Material material){
        this.materials.add(material);
    }

    public void removeMaterial(Material material){
        this.materials.remove(material);
    }

}
