package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
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

    @ManyToOne
    private Manufacturer manufacturer;

    @NotNull
    private String family;

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
}
