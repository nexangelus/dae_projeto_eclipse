package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllUploads",
                query = "SELECT u FROM Upload u ORDER BY u.id" // JPQL
        )
})
public class Upload {
    @Version
    private int version;

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String path;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Project project;

    public Upload(String path, Client client, Project project) {
        this.path = path;
        this.client = client;
        this.project = project;
    }

    public Upload() {
    }

    public long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
