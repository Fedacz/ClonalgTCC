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
        String nome = "BaseRuspini.txt";
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
                Antigeno ant = new Antigeno(Double.parseDouble(separa[0]), Double.parseDouble(separa[1]));
                antigenos.add(ant);
            }
            return antigenos;
    }
//    public static void main(String[] args) {
//        Leitor l = new Leitor();
//        ArrayList<Antigeno> antigenos = l.leAntigenos();
////        System.out.println(l.leAntigenos().toString());
//        for (Antigeno antigeno : antigenos) {
//            System.out.println("X: "+antigeno.getX()+" Y: "+antigeno.getY());
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

