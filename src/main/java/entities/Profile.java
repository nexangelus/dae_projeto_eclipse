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

    public Profile(String name, String description, Manufacturer manufacturer, String family, double height, double thickness, double weight, double areaPainting, String steelGrade) {
        super(name, description, manufacturer, family);
        this.height = height;
        this.thickness = thickness;
        this.weight = weight;
        this.areaPainting = areaPainting;
        this.steelGrade = steelGrade;
    }

    public Profile() {
        super();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAreaPainting() {
        return areaPainting;
    }

    public void setAreaPainting(double areaPainting) {
        this.areaPainting = areaPainting;
    }

    public String getSteelGrade() {
        return steelGrade;
    }

    public void setSteelGrade(String steelGrade) {
        this.steelGrade = steelGrade;
    }
}
