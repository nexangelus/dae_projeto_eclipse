package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.Set;

@Entity
@NamedQueries({
})
public class Sheet extends Material {

	private double thickness;

	public Sheet(String name, Manufacturer manufacturer, Family family, double thickness) {
		super(name, manufacturer, family);
		this.thickness = thickness;
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
}
