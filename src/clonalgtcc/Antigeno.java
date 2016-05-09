
package clonalgtcc;

/**
 *
 * @author joao
 */
public class Antigeno {
    private double x;
    private double y;

    public Antigeno() {
    }

    public Antigeno(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("X: ");
        sb.append(x);
        sb.append(" Y: ");
        sb.append(y);
        sb.append("\n");
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
