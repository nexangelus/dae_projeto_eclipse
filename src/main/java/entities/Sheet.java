package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllSheets",
                query = "SELECT s FROM Sheet s ORDER BY s.name" // JPQL
        )
})
public class Sheet extends Material{

    @NotNull
    private double thickness;

    @NotNull
    private double mass;

    @NotNull
    @Column(name = "STEEL_GRADE")
    private String steelGrade;

    public Sheet(String name, String description, Manufacturer manufacturer, String family, double thickness, double mass, String steelGrade) {
        super(name, description, manufacturer, family);
        this.thickness = thickness;
        this.mass = mass;
        this.steelGrade = steelGrade;
    }

    public Sheet() {
        super();
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public String getSteelGrade() {
        return steelGrade;
    }

    public void setSteelGrade(String steelGrade) {
        this.steelGrade = steelGrade;
    }
}
