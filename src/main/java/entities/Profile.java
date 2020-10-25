package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllProfiles",
                query = "SELECT p FROM Profile p ORDER BY p.name" // JPQL
        )
})
public class Profile extends Material{
    @NotNull
    private double height;

    @NotNull
    private double thickness;

    @NotNull
    private double weight;

    @NotNull
    @Column(name = "AREA_PAINTING")
    private double areaPainting;

    @NotNull
    @Column(name = "STEEL_GRADE")
    private String steelGrade;




    public Profile() {
        super();
    }


}
