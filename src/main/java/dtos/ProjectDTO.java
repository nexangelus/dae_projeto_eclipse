package dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	private List<UploadDTO> uploadDTOS;

	public ProjectDTO(long id, String clientUsername, String designerUsername, String title, String description, String observations, LocalDateTime finished, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.clientUsername = clientUsername;
		this.designerUsername = designerUsername;
		this.title = title;
		this.description = description;
		this.observations = observations;
		this.finished = finished;
		uploadDTOS = new LinkedList<>();
	}

	public ProjectDTO() {
		super();
		uploadDTOS = new LinkedList<>();
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

	public ClientDTO getClient() {
		return client;
	}

	public void setClient(ClientDTO client) {
		this.client = client;
	}

	public DesignerDTO getDesigner() {
		return designer;
	}

	public void setDesigner(DesignerDTO designer) {
		this.designer = designer;
	}

	public List<UploadDTO> getUploadDTOS() {
		return uploadDTOS;
	}

	public void setUploadDTOS(List<UploadDTO> uploadDTOS) {
		this.uploadDTOS = uploadDTOS;
	}
}
