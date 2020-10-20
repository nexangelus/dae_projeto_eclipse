package entities;

import javax.persistence.*;

@Entity
public class Structure {
    @Version
    private int version;

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Project project;

    //TODO @ManyToMany Material incluindo campo adicional (quantidade)

    public Structure(Project project) {
        this.project = project;
    }

    public Structure() {
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
}
