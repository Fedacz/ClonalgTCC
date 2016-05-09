/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

/**
 *
 * @author joao
 */
public class Auxx {
    private Anticorpo anticorpo;
    private double dist;

    public Auxx() {
    }
    
    public Auxx(double dist) {
        this.dist = dist;
    }
    
    public Auxx(Anticorpo anticorpo, double dist) {
        this.anticorpo = anticorpo;
        this.dist = dist;
    }

    /**
     * @return the anticorpo
     */
    public Anticorpo getAnticorpo() {
        return anticorpo;
    }

    /**
     * @param anticorpo the anticorpo to set
     */
    public void setAnticorpo(Anticorpo anticorpo) {
        this.anticorpo = anticorpo;
    }

    /**
     * @return the dist
     */
    public double getDist() {
        return dist;
    }

    /**
     * @param dist the dist to set
     */
    public void setDist(double dist) {
        this.dist = dist;
    }
}
