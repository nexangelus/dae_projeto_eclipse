package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllPanels",
                query = "SELECT p FROM Panel p ORDER BY p.name" // JPQL
        )
})
public class Panel extends Material{

    @NotNull
    private double thickness;

    @NotNull
    private double width;

    @NotNull
    @Column(name = "LENGTH_MIN")
    private double lengthMin;

    @NotNull
    @Column(name = "LENGTH_MAX")
    private double lengthMax;

    @NotNull
    private double weight;

    public Panel(String name, String description, Manufacturer manufacturer, String family, double thickness, double width, double lengthMin, double lengthMax, double weight) {
        super(name, description, manufacturer, family);
        this.thickness = thickness;
        this.width = width;
        this.lengthMin = lengthMin;
        this.lengthMax = lengthMax;
        this.weight = weight;
    }

    public Panel() {
        super();
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLengthMin() {
        return lengthMin;
    }

    public void setLengthMin(double lengthMin) {
        this.lengthMin = lengthMin;
    }

    public double getLengthMax() {
        return lengthMax;
    }

    public void setLengthMax(double lengthMax) {
        this.lengthMax = lengthMax;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
