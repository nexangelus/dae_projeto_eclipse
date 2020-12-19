package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
		@NamedQuery(
				name = "getAllStructures",
				query = "SELECT s FROM Structure s ORDER BY s.name" // JPQL
		),
		@NamedQuery(
				name = "getAllStructuresFromProject",
				query = "SELECT s FROM Structure s WHERE s.project.id=:idProject ORDER BY s.name " // JPQL
		)
})
public class Structure extends AbstractTimestampEntity {
	@Version
	private int version;

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private Project project;

	@NotNull
	private String name;

	private int nb;

	private double LVao;

	private int q;

	@NotNull
	@Column(name = "VISIBLE_TO_CLIENT")
	private boolean visibleToClient;

	@Column(name = "CLIENT_ACCEPTED")
	private Boolean clientAccepted;

	@Column(name = "CLIENT_OBSERVATIONS")
	private String clientObservations;

	@ManyToMany
	@JoinTable(name = "MATERIALS_STRUCTURES",
			joinColumns = @JoinColumn(name = "STRUCTURE_ID", referencedColumnName = "ID"),
			inverseJoinColumns = @JoinColumn(name = "MATERIAL_ID", referencedColumnName = "ID"))
	private Set<Material> materials;

    /*
    TODO Rever as estruturas e os contrutores verificar se estam bem feitos
    */

	public Structure(Project project, @NotNull String name, int nb, double LVao, int q, @NotNull boolean visibleToClient, Boolean clientAccepted, String clientObservations, Set<Material> materials) {
		this.project = project;
		this.name = name;
		this.nb = nb;
		this.LVao = LVao;
		this.q = q;
		this.visibleToClient = visibleToClient;
		this.clientAccepted = clientAccepted;
		this.clientObservations = clientObservations;
		this.materials = new LinkedHashSet<>();
	}

	public Structure() {
		this.materials = new LinkedHashSet<>();
	}

	public long getId() {
		return id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getClientObservations() {
		return clientObservations;
	}

	public void setClientObservations(String clientObservations) {
		this.clientObservations = clientObservations;
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
}
