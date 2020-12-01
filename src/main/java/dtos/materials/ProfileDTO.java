package dtos.materials;

import javax.persistence.Lob;
import java.util.LinkedHashMap;

public class ProfileDTO {

	private double weff_p;
	private double weff_n;
	private double ar;
	private double sigmaC;
	private double pp;
	private LinkedHashMap<Double,Double> mcr_p;
	private LinkedHashMap<Double,Double> mcr_n;

	public ProfileDTO(double weff_p, double weff_n, double ar, double sigmaC, double pp, LinkedHashMap<Double, Double> mcr_p, LinkedHashMap<Double, Double> mcr_n) {
		this.weff_p = weff_p;
		this.weff_n = weff_n;
		this.ar = ar;
		this.sigmaC = sigmaC;
		this.pp = pp;
		this.mcr_p = mcr_p;
		this.mcr_n = mcr_n;
	}

	public ProfileDTO() {
	}

	public double getWeff_p() {
		return weff_p;
	}

	public void setWeff_p(double weff_p) {
		this.weff_p = weff_p;
	}

	public double getWeff_n() {
		return weff_n;
	}

	public void setWeff_n(double weff_n) {
		this.weff_n = weff_n;
	}

	public double getAr() {
		return ar;
	}

	public void setAr(double ar) {
		this.ar = ar;
	}

	public double getSigmaC() {
		return sigmaC;
	}

	public void setSigmaC(double sigmaC) {
		this.sigmaC = sigmaC;
	}

	public double getPp() {
		return pp;
	}

	public void setPp(double pp) {
		this.pp = pp;
	}

	public LinkedHashMap<Double, Double> getMcr_p() {
		return mcr_p;
	}

	public void setMcr_p(LinkedHashMap<Double, Double> mcr_p) {
		this.mcr_p = mcr_p;
	}

	public LinkedHashMap<Double, Double> getMcr_n() {
		return mcr_n;
	}

	public void setMcr_n(LinkedHashMap<Double, Double> mcr_n) {
		this.mcr_n = mcr_n;
	}
}
