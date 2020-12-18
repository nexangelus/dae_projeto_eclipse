package dtos;

import com.sun.istack.Nullable;
import dtos.materials.ProfileDTO;
import dtos.materials.SheetDTO;

import java.time.LocalDateTime;

public class MaterialDTO extends TimestampDTO {

	private long id;
	private String name;
	private String manufacturerName;
	private String manufacturerUsername;
	private FamilyDTO family;

	private ProfileDTO profile;
	private SheetDTO sheet;

	public MaterialDTO(long id, String name, String manufacturerName, String manufacturerUsername, FamilyDTO family, ProfileDTO profile, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.manufacturerName = manufacturerName;
		this.manufacturerUsername = manufacturerUsername;
		this.family = family;
		this.profile = profile;
	}

	public MaterialDTO(long id, String name, String manufacturerName, String manufacturerUsername, FamilyDTO family, SheetDTO sheet, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.manufacturerName = manufacturerName;
		this.manufacturerUsername = manufacturerUsername;
		this.family = family;
		this.sheet = sheet;
	}

	public MaterialDTO() {
		super();
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

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getManufacturerUsername() {
		return manufacturerUsername;
	}

	public void setManufacturerUsername(String manufacturerUsername) {
		this.manufacturerUsername = manufacturerUsername;
	}

	public FamilyDTO getFamily() {
		return family;
	}

	public void setFamily(FamilyDTO family) {
		this.family = family;
	}

	public ProfileDTO getProfile() {
		return profile;
	}

	public void setProfile(ProfileDTO profile) {
		this.profile = profile;
	}

	public SheetDTO getSheet() {
		return sheet;
	}

	public void setSheet(SheetDTO sheet) {
		this.sheet = sheet;
	}
}
