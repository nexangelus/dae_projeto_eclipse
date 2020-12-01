package dtos.materials;

public class SheetDTO {

	private double thickness;

	public SheetDTO(double thickness) {
		this.thickness = thickness;
	}

	public SheetDTO() {
	}

	public double getThickness() {
		return thickness;
	}

	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
}
