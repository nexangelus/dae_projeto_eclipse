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
    private Set<Family> families;

    @OneToMany(mappedBy = "manufacturer")
    private Set<Material> materials;

    private String address;

    private String website;

    private String contact;

    public Manufacturer(String username, String password, String name, String email, Set<Material> materials, Set<Family> families, String address, String website, String contact) {
        super(username, password, name, email);
        this.address = address;
        this.website = website;
        this.contact = contact;
        this.materials = materials;
        this.families = families;
    }

    public Manufacturer(String username, String password, String name, String email, String address, String website, String contact) {
        super(username, password, name, email);
        this.address = address;
        this.website = website;
        this.contact = contact;
        this.materials = new LinkedHashSet<>();
        this.families = new LinkedHashSet<>();
    }

    public Manufacturer() {
        super();
        this.materials = new LinkedHashSet<>();
        this.families = new LinkedHashSet<>();
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

    public Set<Family> getFamilies() {
        return families;
    }

    public void setFamilies(Set<Family> families) {
        this.families = families;
    }

    public void addFamily(Family family) {
        this.families.add(family);
    }

    public void removeFamily(Family family) {
        this.families.remove(family);
    }
}
