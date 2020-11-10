package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Material {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	@ManyToOne
	private Manufacturer manufacturer;

	@ManyToOne
	private Family family;

	@ManyToMany(mappedBy = "materials")
	private Set<Structure> structures;

	public Material(String name, Manufacturer manufacturer, Family family, Set<Structure> structures) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.family = family;
		this.structures = structures;
	}

	public Material(String name, Manufacturer manufacturer, Family family) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.family = family;
		this.structures = new LinkedHashSet<>();
	}

	public Material() {
		this.structures = new LinkedHashSet<>();
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

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	public Set<Structure> getStructures() {
		return structures;
	}

	public void setStructures(Set<Structure> structures) {
		this.structures = structures;
	}

	public void addStructure(Structure structure) {
		this.structures.add(structure);
	}

	public void removeStructure(Structure structure) {
		this.structures.remove(structure);
	}
}
