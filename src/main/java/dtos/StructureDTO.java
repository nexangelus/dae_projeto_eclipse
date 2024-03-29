package dtos;

import entities.Material;
import entities.Project;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class StructureDTO extends TimestampDTO implements Serializable {
	private long id;
	private String name;
	private int nb;
	private double LVao;
	private int q;
	private String clientObservations;
	private ProjectDTO project;
	private boolean visibleToClient;
	private Boolean clientAccepted;
	private List<MaterialDTO> materials;

	public StructureDTO(long id, String name, int nb, double LVao, int q, String clientObservations, boolean visibleToClient, Boolean clientAccepted, List<MaterialDTO> materials, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.nb = nb;
		this.LVao = LVao;
		this.q = q;
		this.clientObservations = clientObservations;
		this.visibleToClient = visibleToClient;
		this.clientAccepted = clientAccepted;
		this.materials = materials;
	}

	public StructureDTO(long id, String name, int nb, double LVao, int q, String clientObservations, ProjectDTO project, boolean visibleToClient, Boolean clientAccepted, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.nb = nb;
		this.LVao = LVao;
		this.q = q;
		this.clientObservations = clientObservations;
		this.project = project;
		this.visibleToClient = visibleToClient;
		this.clientAccepted = clientAccepted;
		this.materials = new LinkedList<>();
	}

	public StructureDTO(long id, String name, int nb, double LVao, int q, String clientObservations, boolean visibleToClient, Boolean clientAccepted, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.nb = nb;
		this.LVao = LVao;
		this.q = q;
		this.clientObservations = clientObservations;
		this.visibleToClient = visibleToClient;
		this.clientAccepted = clientAccepted;
		this.materials = new LinkedList<>();
	}

	public StructureDTO(long id, String name, MaterialDTO material, LocalDateTime created, LocalDateTime updated) {
		super(created, updated);
		this.id = id;
		this.name = name;
		this.materials = new LinkedList<>();

		materials.add(material);
	}

	public StructureDTO() {
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

	public String getClientObservations() {
		return clientObservations;
	}

	public void setClientObservations(String clientObservations) {
		this.clientObservations = clientObservations;
	}

	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	public boolean isVisibleToClient() {
		return visibleToClient;
	}

	public void setVisibleToClient(boolean visibleToClient) {
		this.visibleToClient = visibleToClient;
	}

	public Boolean isClientAccepted() {
		return clientAccepted;
	}

	public void setClientAccepted(Boolean clientAccepted) {
		this.clientAccepted = clientAccepted;
	}

	public int getNb() {
		return nb;
	}

	public void setNb(int nb) {
		this.nb = nb;
	}

	public double getLVao() {
		return LVao;
	}

	public void setLVao(double LVao) {
		this.LVao = LVao;
	}

	public int getQ() {
		return q;
	}

	public void setQ(int q) {
		this.q = q;
	}

	public List<MaterialDTO> getMaterials() {
		return materials;
	}

	public void setMaterials(List<MaterialDTO> materials) {
		this.materials = materials;
	}
}
