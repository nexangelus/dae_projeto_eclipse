package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllUploads",
                query = "SELECT d FROM Document d ORDER BY d.id" // JPQL
        ),
        @NamedQuery(
                name = "getAllProjectUploads",
                query = "SELECT d FROM Document d WHERE d.project.id =:idProject"
        )
})
public class Document extends AbstractTimestampEntity {
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

    public Document(String filepath, String filename, Project project) {
        this.filepath = filepath;
        this.filename = filename;
        this.project = project;
    }

    public Document() {
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
