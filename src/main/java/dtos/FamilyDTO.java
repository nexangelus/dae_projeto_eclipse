package dtos;

import java.time.LocalDateTime;

public class FamilyDTO extends TimestampDTO {

	private long id;
	private String name;
	private String manufacturerUsername;
	private ManufacturerDTO manufacturer;
	private int materialsCount;

	public FamilyDTO(long id, String name, String manufacturerUsername, ManufacturerDTO manufacturer, int materialsCount, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.manufacturerUsername = manufacturerUsername;
		this.manufacturer = manufacturer;
		this.materialsCount = materialsCount;
	}

	public FamilyDTO(long id, String name, ManufacturerDTO manufacturer, int materialsCount, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.manufacturer = manufacturer;
		this.materialsCount = materialsCount;
	}

	public FamilyDTO(long id, String name, String manufacturerUsername, int materialsCount, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.manufacturerUsername = manufacturerUsername;
		this.materialsCount = materialsCount;
	}

	public FamilyDTO() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ManufacturerDTO getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(ManufacturerDTO manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturerUsername() {
		return manufacturerUsername;
	}

	public void setManufacturerUsername(String manufacturerUsername) {
		this.manufacturerUsername = manufacturerUsername;
	}

	public int getMaterialsCount() {
		return materialsCount;
	}

	public void setMaterialsCount(int materialsCount) {
		this.materialsCount = materialsCount;
	}
}
