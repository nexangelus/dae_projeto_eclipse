package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Upload {
    @Id
    int id;



    @NotNull
    String path;


}
