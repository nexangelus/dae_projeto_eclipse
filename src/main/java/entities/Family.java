package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
		@NamedQuery(name = "getAllFamilies", query = "SELECT p FROM Family p ORDER BY p.name"),
		@NamedQuery(name = "getAllFamiliesFromManufacturer", query = "SELECT f FROM Family f WHERE f.manufacturer.name = :manufacturer ORDER by f.name")
})
public class Family extends AbstractTimestampEntity {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	@ManyToOne
	private Manufacturer manufacturer;

	@OneToMany(mappedBy = "family")
	private Set<Material> materials;

	public Family(String name, Manufacturer manufacturer, Set<Material> materials) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.materials = materials;
	}

	public Family(String name, Manufacturer manufacturer) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.materials = new LinkedHashSet<>();
	}

	public Family() {
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

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Set<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(Set<Material> materials) {
		this.materials = materials;
	}

	public void addMaterial(Material material) {
		this.materials.add(material);
	}

	public void removeMaterial(Material material) {
		this.materials.remove(material);
	}
}
