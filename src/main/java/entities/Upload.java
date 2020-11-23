package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllUploads",
                query = "SELECT u FROM Upload u ORDER BY u.id" // JPQL
        ),
        @NamedQuery(
                name = "getAllProjectUploads",
                query = "SELECT u FROM Upload u WHERE u.project.id =:idProject"
        )
})
public class Upload extends AbstractTimestampEntity {
    @Version
    private int version;

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String filepath;

    @NotNull
    private String filename;

    @ManyToOne
    private Project project;

    public Upload(String filepath, String filename, Project project) {
        this.filepath = filepath;
        this.filename = filename;
        this.project = project;
    }

    public Upload() {
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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
