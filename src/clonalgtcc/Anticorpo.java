package clonalgtcc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author joao
 */
public class Anticorpo {
    private double x;
    private double y;
    private Antigeno antigeno; // armazena o antigeno de maior afinidade
    // A afinidade entre antígeno e anticorpo é dada pela distância Euclidiana normalizada (0,1].
    private double afinidade; // Distância euclidiana dividida pela soma de todas distâncias.
    
    
    // Para a normalização, fazer a distância do antigeno a todas os anticorpos e dividi-la pela maior distância.
    // Dividir a distância Euclidiana (entre objeto e protótipo) pela soma de todas as distâncias 
    // (entre objeto e todos os protótipos). Com isso temos uma distância relativa, que representa a afinidade 
    // relativa dos anticorpos (protótipos) a um antígeno (objeto). Assim, teremos distâncias (normalizadas) no 
    //intervalo (0,1].
    // Distância euclidiana = raiz((px-qx)^2 + (py - qy)^2).

    public Anticorpo() {
    }

    public Anticorpo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Atualiza a afinidade.
     * @param antigenos array com todos antigenos.
     */
//    public void distEuclidiana(ArrayList<Antigeno> antigenos){
//        getDistancias().clear();
//        for (Antigeno antigeno : antigenos) {
//            double dist = Math.sqrt(Math.pow(getX() - antigeno.getX(),2) + Math.pow(getY() - antigeno.getY(),2));
//            getDistancias().add(dist);
//        }
//        double soma = 0;
//        // Somátorio do array
//        for (Double distancia : getDistancias()) {
//            soma = soma + distancia;
//        }
//
//        Collections.sort(distancias); // ordena para pegar ultimo elemento (maior distância).
//        double maiorDistancia = distancias.get(distancias.size()-1); // maior elemento.
//        
//        setAfinidade(maiorDistancia/soma);
//    }
    
    /**   ESTA FUNCIONANDO OK!, FALTA AFINIDADE
     * Atualiza a afinidade do anticorpo 
     * @param anticorpos Lista de anticorpos.
     * @param antigeno Objeto a ser comparado.
     */
    public void atualizaAfi(ArrayList<Anticorpo> anticorpos, Antigeno antigeno){
        // A afinidade entre antígeno e anticorpo é dada pela distância Euclidiana normalizada (0,1]. 
        // Para a normalização, fazer a distância do objeto a todas as células e dividi-la pela
        // soma de todas as distâncias (do objeto em questão em relação à todos os protótipos)
        // Distância euclidiana = raiz((px-qx)^2 + (py - qy)^2).
        ArrayList<Double> distancias = new ArrayList<>(); // Vetor de distâncias
        
        ArrayList<ArrayList> aux2 = new ArrayList<>();
        ArrayList aux3 = new ArrayList<>();
        
//        getDistancias().clear();
        double somatorio = 0;
        for (Anticorpo anticorpo : anticorpos) {
            ArrayList aux = new ArrayList<>();
            double dist = Math.sqrt(Math.pow(antigeno.getX() - anticorpo.getX(),2) + Math.pow(antigeno.getY() - anticorpo.getY(),2));
            System.out.println("Dist: "+dist);
            somatorio = somatorio + dist;
            aux.add(anticorpo);
            aux.add(dist);
            distancias.add(dist);
            aux2.add(aux);
        }
        // Criar um array com todas afinidades e pegar a maior afinidade .
        System.out.println(somatorio);
        
//        aux3.set(0,aux2.get(0)); // inicializando
//        aux3.set(1,aux2.get(1)); // inicializando
        for (ArrayList arrayList : aux2) {
            arrayList.set(1, ((Double)arrayList.get(1)/somatorio));
            System.out.println(arrayList.get(1));
            
//            if(arrayList.get(1) < aux3.get(1)){
//                
//            }
        }
    }

    public void attAfinidade(){
        
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


    /**
     * @return the antigeno
     */
    public Antigeno getAntigeno() {
        return antigeno;
    }

    /**
     * @param antigeno the antigeno to set
     */
    public void setAntigeno(Antigeno antigeno) {
        this.antigeno = antigeno;
    }

    /**
     * @return the afinidade
     */
    public double getAfinidade() {
        return afinidade;
    }

    /**
     * @param afinidade the afinidade to set
     */
    public void setAfinidade(double afinidade) {
        this.afinidade = afinidade;
    }
    
//        public static void main(String[] args) {
//            Anticorpo a = new Anticorpo(0.1, 0.4);
//            Anticorpo a2 = new Anticorpo(1.0, 1.0);
//            Antigeno an = new Antigeno(0.0 , 0.0);
//            ArrayList<Anticorpo> array = new ArrayList<>();
//            array.add(a);
//            array.add(a2);
//            
//            a.atualizaAfi(array, an);
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("X: %f Y: %f, Afinidade: %f", x,y,afinidade));
        if(antigeno != null){
            sb.append(String.format(" com X: %f Y: %f\n", antigeno.getX(),antigeno.getY()));
        }
        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }

}
