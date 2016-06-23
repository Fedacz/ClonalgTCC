/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author joao
 */
public class Leitor {
    public ArrayList<Antigeno> leAntigenos(){
        ArrayList<String> linhas = new ArrayList<>();
        ArrayList<Antigeno> antigenos = new ArrayList<>();
//        String nome = "BaseRuspini.txt";
        //     D    G
        //Pima 8    2
        //Iris 4    3
        //Wine 13   3
        //Glass9    6
        //Rusp 2    4
        //S1   2    3
        //S2   2    2
        //Bal  4    3
        //heart13   2
        //liver6    2
        String nome ="liver.txt";
        try {
            FileReader arq = new FileReader(nome);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha = lerArq.readLine(); 
            while (linha != null) { // adicionando todas linhas do arquivo no array linhas.
                linhas.add(linha);
                linha = lerArq.readLine();
            }
            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        

        // Separa os X e Y e cria os antigenos
            for (String linha : linhas) {
                String[] separa = linha.split(" ");
                ArrayList<Double> vars = new ArrayList<>();
                for(int i=0;i<separa.length-1;i++){
                    vars.add(Double.parseDouble(separa[i]));
                }
                Antigeno ant = new Antigeno(vars, Integer.parseInt(separa[separa.length-1]));
//                Antigeno ant = new Antigeno(Double.parseDouble(separa[0]), Double.parseDouble(separa[1]), Integer.parseInt(separa[2]));
                antigenos.add(ant);
            }
            return antigenos;
    }
//    public static void main(String[] args) {
//        Leitor l = new Leitor();
//        ArrayList<Antigeno> antigenos = l.leAntigenos();
////        System.out.println(l.leAntigenos().toString());
//        for (Antigeno antigeno : antigenos) {
//            System.out.println(antigeno.toString());
//        }
//    }
    // Gerando gráfico
//    public static void main(String[] args) {
//        Leitor l = new Leitor();
//        ArrayList<Antigeno> antigenos = l.leAntigenos();
////        System.out.println(l.leAntigenos().toString());
//        for (Antigeno antigeno : antigenos) {
//            System.out.println("X: "+antigeno.getX()+" Y: "+antigeno.getY());
//        }
//    }
}

