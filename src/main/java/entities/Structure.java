package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Structure {
    @Version
    private int version;

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Project project;

    @NotNull
    private String name;

    @NotNull
    private String parameters;

    @NotNull
    private boolean visibleToClient;

    private boolean clientAccepted;

    private String clientObservations;
    
    /*
    TODO @ManyToMany Material incluindo campo adicional (quantidade)
    TODO Refazer as estruturas e os contrutores verificar se estam bem feitos
    */

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
