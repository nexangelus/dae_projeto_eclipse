package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllManufacturers",
                query = "SELECT m FROM Manufacturer m ORDER BY m.name" // JPQL
        )
})
public class Manufacturer extends User {

    @OneToMany(mappedBy = "manufacturer")
    private Set<Material> materials;

    private String address;

    private String website;

    private String contact;

    public Manufacturer(String username, String password, String name, String email, Set<Material> materials, String address, String website, String contact) {
        super(username, password, name, email);
        this.address = address;
        this.website = website;
        this.contact = contact;
        this.materials = new LinkedHashSet<>();
    }

    public Manufacturer(String username, String password, String name, String email){
        super(username, password, name, email);
    }

    public Manufacturer() {
        super();
        this.materials = new LinkedHashSet<>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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
