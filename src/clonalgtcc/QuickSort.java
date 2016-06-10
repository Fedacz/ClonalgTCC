/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.util.ArrayList;

/**
 * Forma Crescente
 * @author joao
 */
public class QuickSort {
    public QuickSort() {
    }

    public void quickSort(ArrayList<Anticorpo> vetor){
        int inicio = 0; int fim = vetor.size()-1;
        quickSort(vetor, inicio, fim);
    }
    private void quickSort(ArrayList<Anticorpo> vetor, int inicio, int fim) {
        if (inicio < fim) {
            int posicaoPivo = separar(vetor, inicio, fim);
            quickSort(vetor, inicio, posicaoPivo - 1);
            quickSort(vetor, posicaoPivo + 1, fim);
        }
    }

    private int separar(ArrayList<Anticorpo> vetor, int inicio, int fim) {
        Anticorpo pivo = vetor.get(inicio);
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor.get(i).getAfinidade() <= pivo.getAfinidade()) {
                i++;
            } else if (pivo.getAfinidade() < vetor.get(f).getAfinidade()) {
                f--;
            } else {
                Anticorpo troca = vetor.get(i);
                vetor.set(i, vetor.get(f));
                vetor.set(f, troca);
                i++;
                f--;
            }
        }
        vetor.set(inicio, vetor.get(f));
        vetor.set(f, pivo);
        return f;
    }
//    public static void main(String[] args) {
//        Anticorpo a = new Anticorpo(null, null, 5.0);
//        Anticorpo a2 = new Anticorpo(null, null, 2.0);
//        ArrayList<Anticorpo> ants = new ArrayList<>();
//        ants.add(a);
//        ants.add(a2);
//        QuickSort q = new QuickSort();
//        q.quickSort(ants);
//        for (Anticorpo ant : ants) {
//            System.out.println(ant.getAfinidade());
//        }
//    }

}
