package dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ProjectDTO extends TimestampDTO implements Serializable {
	private long id;
	private String clientUsername;
	private ClientDTO client;
	private String designerUsername;
	private DesignerDTO designer;
	private String title;
	private String description;
	private String observations;
	private LocalDateTime finished;

	public ProjectDTO(long id, String clientUsername, String designerUsername, String title, String description, String observations, LocalDateTime finished, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.clientUsername = clientUsername;
		this.designerUsername = designerUsername;
		this.title = title;
		this.description = description;
		this.observations = observations;
		this.finished = finished;
	}

	public ProjectDTO() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClientUsername() {
		return clientUsername;
	}

	public void setClientUsername(String clientUsername) {
		this.clientUsername = clientUsername;
	}

	public String getDesignerUsername() {
		return designerUsername;
	}

	public void setDesignerUsername(String designerUsername) {
		this.designerUsername = designerUsername;
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
}
