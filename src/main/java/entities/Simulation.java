package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Simulation {

	@Id
	@GeneratedValue
	private long id;

	private int totalNumber;

	private int numProfilesAllowed;

	private int numProfilesRefused;

	@ManyToMany
	@JoinTable(name = "SIMULATION_PROFILES",
			joinColumns = @JoinColumn(name = "SIMULATION_ID", referencedColumnName = "ID"),
			inverseJoinColumns = @JoinColumn(name = "PROFILE_ID", referencedColumnName = "ID"))
	private Set<Profile> profilesAllowed;

	@ManyToOne
	private Structure structure;

	public Simulation(int totalNumber, int numProfilesAllowed, int numProfilesRefused, Set<Profile> profilesAllowed, Structure structure) {
		this.totalNumber = totalNumber;
		this.numProfilesAllowed = numProfilesAllowed;
		this.numProfilesRefused = numProfilesRefused;
		this.profilesAllowed = profilesAllowed;
		this.structure = structure;
	}

	public Simulation(int totalNumber, int numProfilesAllowed, int numProfilesRefused, Structure structure) {
		this.totalNumber = totalNumber;
		this.numProfilesAllowed = numProfilesAllowed;
		this.numProfilesRefused = numProfilesRefused;
		this.profilesAllowed = new LinkedHashSet<>();
		this.structure = structure;
	}

	public Simulation() {
		this.profilesAllowed = new LinkedHashSet<>();
	}

	public long getId() {
		return id;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public int getNumProfilesAllowed() {
		return numProfilesAllowed;
	}

	public void setNumProfilesAllowed(int numProfilesAllowed) {
		this.numProfilesAllowed = numProfilesAllowed;
	}

	public int getNumProfilesRefused() {
		return numProfilesRefused;
	}

	public void setNumProfilesRefused(int numProfilesRefused) {
		this.numProfilesRefused = numProfilesRefused;
	}

	public Set<Profile> getProfilesAllowed() {
		return profilesAllowed;
	}

	public void setProfilesAllowed(Set<Profile> profilesAllowed) {
		this.profilesAllowed = profilesAllowed;
	}

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public void addProfileAllowed(Profile profile) {
		this.profilesAllowed.add(profile);
	}

	public void removeProfileAllowed(Profile profile) {
		this.profilesAllowed.remove(profile);
	}
}
