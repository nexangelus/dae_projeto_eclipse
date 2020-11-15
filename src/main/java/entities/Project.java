package entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
		@NamedQuery(
				name = "getAllProjects",
				query = "SELECT p FROM Project p ORDER BY p.id" // JPQL
		)
})
public class Project extends AbstractTimestampEntity {
	@Version
	private int version;

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private Client client;

	@ManyToOne
	private Designer designer;

	@OneToMany(mappedBy = "project")
	private Set<Structure> structures;

	@OneToMany(mappedBy = "project")
	private Set<Upload> uploads;

	@NotNull
	private String title;

	@NotNull
	private String description;

	private String observations;

	private LocalDateTime finished;

	public Project(Client client, Designer designer, String title, String description, String observations, LocalDateTime finished) {
		this.client = client;
		this.designer = designer;
		this.structures = new LinkedHashSet<>();
		this.uploads = new LinkedHashSet<>();
		this.title = title;
		this.description = description;
		this.observations = observations;
		this.finished = finished;
	}

	public Project(Client client, Designer designer, String title, String description) {
		this.client = client;
		this.designer = designer;
		this.structures = new LinkedHashSet<>();
		this.uploads = new LinkedHashSet<>();
		this.title = title;
		this.description = description;
	}

	public Project() {
		this.structures = new LinkedHashSet<>();
		this.uploads = new LinkedHashSet<>();
	}

	public long getId() {
		return id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Designer getDesigner() {
		return designer;
	}

	public void setDesigner(Designer designer) {
		this.designer = designer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public LocalDateTime getFinished() {
		return finished;
	}

	public void setFinished(LocalDateTime finished) {
		this.finished = finished;
	}

	public Set<Structure> getStructures() {
		return structures;
	}

	public void setStructures(Set<Structure> structures) {
		this.structures = structures;
	}

	public Set<Upload> getUploads() {
		return uploads;
	}

	public void setUploads(Set<Upload> uploads) {
		this.uploads = uploads;
	}

	public void addStructure(Structure structure) {
		this.structures.add(structure);
	}

	public void removeStructure(Structure structure) {
		this.structures.remove(structure);
	}

	public void addUpload(Upload upload) {
		this.uploads.add(upload);
	}

	public void removeUpload(Upload upload) {
		this.uploads.remove(upload);
	}
}
