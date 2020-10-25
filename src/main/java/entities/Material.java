package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllMaterials",
                query = "SELECT m FROM Material m ORDER BY m.name" // JPQL
        )
})
public class Material {
    @Version
    private int version;

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String family;

    @ManyToOne
    private Manufacturer manufacturer;

    @Column(name = "IMAGE_PATH")
    private String imagePath;

    //TODO @ManyToMany Struct incluindo campo adicional (quantidade)

    public Material(String name, String description, Manufacturer manufacturer, String family) {
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.family = family;
    }

    public Material() {
    }

    public long getId() {
        return id;
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

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
