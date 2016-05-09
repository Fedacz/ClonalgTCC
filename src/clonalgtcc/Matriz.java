/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * i = Protótipos j = Objetos
 *
 * @author joao
 */
public class Matriz {
//    ArrayList<ArrayList> matriz = new ArrayList<>();

    ArrayList<Anticorpo> anticorpos;
    ArrayList<Antigeno> antigenos;
    DecimalFormat df = new DecimalFormat("#.##");

    public Matriz() {
    }

    public Matriz(ArrayList<Anticorpo> anticorpos, ArrayList<Antigeno> antigenos) {
        this.anticorpos = anticorpos;
        this.antigenos = antigenos;
    }

    public void criaMatriz(boolean imprimir) {
        int[][] matriz = new int[antigenos.size()][anticorpos.size()];
        int linha = 0; // linha
        for (Antigeno antigeno : antigenos) {
            double melhorDist = 1;
            Anticorpo melhorAnticorpo;
            int cont = 0;
            int coluna = 0;
            for (Anticorpo anticorpo : anticorpos) {
                // calcular a distância de todos anticorpos em relação ao antigeno, e marcar o mais próximo
                double dist = Math.sqrt(Math.pow(antigeno.getX() - anticorpo.getX(), 2) + Math.pow(antigeno.getY() - anticorpo.getY(), 2)); // distância euclidiana
                if (dist < melhorDist) {
                    melhorDist = dist;
                    melhorAnticorpo = anticorpo;
                    coluna = cont;
                }
                cont++;
            }
            matriz[linha][coluna] = 1;
            coluna = 0;
            linha++;
        }

        if (imprimir) {
            //Imprime matriz
            System.out.println("Matriz (Antigenos/Anticorpos): ");
            System.out.print("\n\t");
            for (Anticorpo anticorpo : anticorpos) {
                System.out.print("\tX:" + df.format(anticorpo.getX()) + " ");
            }
            System.out.print("\n\t");
            for (Anticorpo anticorpo : anticorpos) {
                System.out.print("\tY:" + df.format(anticorpo.getY()) + " ");
            }
            System.out.println("");
            ArrayList<Integer> somas = new ArrayList<>();
            for (int i = 0; i < anticorpos.size(); i++) {
                somas.add(0);
            }
            // Imprime dados
            for (int i = 0; i < antigenos.size(); i++) {
                System.out.print("X:" + df.format(antigenos.get(i).getX()) + "\tY:" + df.format(antigenos.get(i).getY()) + "\t ");
                for (int j = 0; j < anticorpos.size(); j++) {
                    System.out.print(matriz[i][j] + "\t");
                    if (matriz[i][j] == 1) {
                        somas.set(j, somas.get(j) + 1);
                    }
                }
                System.out.println("");
            }

            //Imprimir linha final
            System.out.print("\t\t");
            for (Integer soma : somas) {
                System.out.print(soma + "\t");
            }
            System.out.println("");
        }
    }

}
