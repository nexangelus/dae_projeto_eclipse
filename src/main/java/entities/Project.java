package entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Project {
    @Version
    private int version;

    @Id
    private int id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Designer designer;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private String observations;

    //missing structs
}
