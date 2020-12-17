package dtos;

import entities.Profile;

import java.io.Serializable;

public class SimulateDTO implements Serializable {
    private int nb;
    private double LVao;
    private int q;

    public SimulateDTO(int nb, double LVao, int q, Profile profile) {
        this.nb = nb;
        this.LVao = LVao;
        this.q = q;
    }

    public SimulateDTO() {
    }

    public int getNb() {
        return nb;
    }

    public void setNb(int nb) {
        this.nb = nb;
    }

    public double getLVao() {
        return LVao;
    }

    public void setLVao(double LVao) {
        this.LVao = LVao;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

}
