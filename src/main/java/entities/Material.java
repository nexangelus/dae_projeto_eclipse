package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Material {

    @Id
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @OneToMany
    Set<Manufacturer> manufacturers;

    public Material(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manufacturers = new LinkedHashSet<Manufacturer>();
    }

    public Material() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(Set<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }
}
