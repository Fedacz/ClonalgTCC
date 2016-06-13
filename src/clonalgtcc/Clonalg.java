package clonalgtcc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
// NUNCA ATUALIZAR A POPULAÇÃO, SOMENTE OS CLONES
// FAZER MATRIZ DE PERTINENCIA E TESTAR BASES MAIORES

/**
 * ARRUMAR NORMALIZAÇÃO DE DISTÂNCIAS DA AFINIDADE.
 * Arrumar o erro. Normalização das distâncias, classificação. Mexi no nClones.
 * Normalização, 20% melhores clones, permutação. Classe Main, onde roda o
 * Clonalg Mutar Clones Ver o k-médias. Erro Quadrático (o algoritmo deve parar
 * quando o valor dessa função for menor q 10^-3.
 *
 * @author joao
 */
public class Clonalg {

    private final Random r = new Random();
    DecimalFormat df = new DecimalFormat("#.##");
    private ArrayList<Anticorpo> populacao = new ArrayList<>();
    ArrayList<Anticorpo> tests = new ArrayList<>();

//    private static final int tamAnticorpo = 15; // ordem da matriz-1
    // Variavéis do Sistema
    // Obter Antigenos
    Leitor leitor = new Leitor();
    QuickSort quick = new QuickSort();
    private ArrayList<Antigeno> antigenos = leitor.leAntigenos();

    // Configurações.
    private static final int tamPop = 1;
    private static final double limiar = 0.2; // numero de anticorpos selecionados para serem clonados
    private static final int numClo = 5;//6 // cada selecionado possuirá este número de clones ou menos
    private static final double numSel = 0.2; // numero de clones selecionados para entra na população 20%.
    private static int numGeracoes = 200;
    private static final double erroQuadratico = 0.01;
    private static final int tamanhoBase = 2;

    /**
     * Gera uma população de anticorpos aleatrios no intervalo (0,1].
     *
     * @param tamPop tamanho da população desejada.
     */
    public void geraPop2(int tamPop) {
        for (int i = 0; i < tamPop; i++) {
            Anticorpo ant = new Anticorpo(tamanhoBase);

//            for (double var : ant.getVars()) {
//                var = r.nextFloat();
//            }
            for (int v = 0; v < tamanhoBase; v++) {
                ant.getVars().set(v, (double) r.nextFloat());
            }

            ant.setAfinidade(-1); // afinidade negativa quando iniciado.
            populacao.add(ant);
        }
    }

        public ArrayList<Anticorpo> geraPop(int tamPop) {
            ArrayList<Anticorpo> iniciais = new ArrayList<>(); 
        for (int i = 0; i < tamPop; i++) {
            Anticorpo ant = new Anticorpo(tamanhoBase);

//            for (double var : ant.getVars()) {
//                var = r.nextFloat();
//            }
            for (int v = 0; v < tamanhoBase; v++) {
                ant.getVars().set(v, (double) r.nextFloat());
            }

            ant.setAfinidade(-1); // afinidade negativa quando iniciado.
            iniciais.add(ant);
        }
        return iniciais;
    }
        
    // ARRAY LIST COM 10 ANTICORPOS INICIAS
    public ArrayList<Anticorpo> geraPopC() {
        ArrayList<String> linhas = new ArrayList<>();
        ArrayList<Anticorpo> anticorpos = new ArrayList<>();
        
        String nome = "iniciais2.txt"; //17 variaveis
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

        // Separa as variáveis e cria anticorpos
        for (String linha : linhas) {
            String[] separa = linha.split(" ");
            ArrayList<Double> vars = new ArrayList<>();
            for (int i = 0; i < tamanhoBase; i++) {
                vars.add(Double.parseDouble(separa[i]));
            }
            Anticorpo ant = new Anticorpo(vars, null, -1);
            anticorpos.add(ant);
//            tests.add(ant);
        }
        return anticorpos;
    }

    /**
     * Para cada antigeno selecionado calcular o anticorpo de maior afinidade e
     * então setar sua afinidade e seu antigeno. Atualiza afinidade de um
     * anticorpo. x-min/max-min (normalização)
     *
     * @param anticorposs Lista de anticorpos para atualizar a afinidade.
     */
    public void atualizaAfinidadePop(ArrayList<Anticorpo> anticorposs) {
        ArrayList<Anticorpo> anticorpos = new ArrayList<>();
        anticorpos.addAll(anticorposs);
//        ArrayList<Anticorpo> anticorpos = (ArrayList<Anticorpo>)anticorposs.clone();
        
//        double min = 0;
//        double max = Math.sqrt(tamanhoBase);
        double min = Double.MAX_VALUE;
        double max = 0;
        Collections.shuffle(getAntigenos()); //Fisher–Yates shuffle @https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
        for (int i = 0; i < Math.min(anticorpos.size(), antigenos.size()); i++) {// for de antigenos
//            double somatorio = 0;
            Auxx aux = new Auxx(Double.MAX_VALUE); // Guarda o anticorpo de menor distância, e a menor distância.
            aux.setAnticorpo(null); // inicializa um anticorpo para o aux
            for (Anticorpo anticorpo : anticorpos) { // Encontrar o anticorpo de maior afinidade
                double soma = 0;
                for (int v = 0; v < tamanhoBase; v++) {
                    soma += Math.pow((antigenos.get(i).getVars().get(v) - anticorpo.getVars().get(v)), 2);
                }
                double dist = Math.sqrt(soma); // distância euclidiana
//                somatorio = somatorio + dist;
//                System.out.println("Distância: "+dist);
                if (dist < aux.getDist()) { // atualiza quem tem maior afinidade
                    aux.setDist(dist);
                    aux.setAnticorpo(anticorpo);
                }
            }
//            if(anticorposs.size()<10){
                anticorpos.remove(aux.getAnticorpo());
//            }
            // atualiza afinidade
//            double afinidade = aux.getDist() / somatorio;
            double afinidade = 0;
//            if (anticorpos.size() > 1) {
//                afinidade = (aux.getDist() - min) / (max - min);
//            } else {
//                afinidade = (aux.getDist() - min) / (max - min);
//            }
            afinidade = aux.getDist();
//            System.out.println("Distância: "+aux.getDist()+" Soma: "+somatorio);
//            System.out.println("Afinidade: "+afinidade);
//            System.out.println("Somatorio: "+somatorio);
//            System.out.println("Distância: "+aux.getDist());
//            System.out.println("Afinidade: "+afinidade);
            aux.getAnticorpo().setAfinidade(afinidade);
            aux.getAnticorpo().setAntigeno(antigenos.get(i));
        }
        // Depois que calcula todas afinidades, normalizar todas as diferentes de -1
        //Pegando máximo e mínimo
        for (Anticorpo anticorpo : anticorposs) {
            if(anticorpo.getAfinidade()<min && anticorpo.getAfinidade() != -1){
                min = anticorpo.getAfinidade();
            }
            if(anticorpo.getAfinidade()>max){
                max = anticorpo.getAfinidade();
            }
        }
//        System.out.println("Afinidades Antigas: ");
//        System.out.println(anticorposs.toString());
//         Setanto afinidades normalizadas
        for (Anticorpo anticorpo : anticorposs) {
            if(max==min){
                anticorpo.setAfinidade(0);
            } else
            if(anticorpo.getAfinidade()==-1){
                anticorpo.setAfinidade((anticorpo.getAfinidade()));
            }
            else{
                anticorpo.setAfinidade((anticorpo.getAfinidade() - min)/(max-min));
            }
        }
//        System.out.println("Max: "+max+" Min: "+min);
//        System.out.println("Afinidades Normalizadas: ");
//        System.out.println(anticorposs.toString());
    }

    /**
     * Retorna um clone de um anticorpo. Clone = (randn*Ab+Ab)/2
     *
     * @param ant anticorpo que deseja clonar.
     * @return retorna um clone de um anticorpo.
     */
    public Anticorpo clone(Anticorpo ant) {
        // Gets
        Anticorpo clone = new Anticorpo();
        double afinidade = ant.getAfinidade();
        ArrayList<Double> varsClone = (ArrayList<Double>) ant.getVars().clone();
        // Sets Vars
        for (int i = 0; i < tamanhoBase; i++) {
            double x = Math.abs((r.nextGaussian() * (varsClone.get(i)) + varsClone.get(i)) / 2);
//            System.out.println("X"+i+": "+x);
            if (x > 1) {
                varsClone.set(i, (double) 1);
            } else if (x < 0) {
                varsClone.set(i, (double) 0);
            } else {
                varsClone.set(i, x);
            }
        }

        // Sets      
        clone.setVars(varsClone);
        clone.setAfinidade(afinidade);
        clone.setAntigeno(ant.getAntigeno());
//        clone.setAntigeno(null);
        return clone;
    }

    /**
     * Muta um anticorpo e jnão atualiza a afinidade.
     *
     * @param anticorpo anticorpo que deseja mutar.
     */
    public void mutaAnticorpo(Anticorpo anticorpo) {
        // A maturação (mutação) dos clones pode ser dada deslocando o anticorpo na direção do antigeno 
        // (anticorpo = anticorpo - alfa*(anticorpo-antigeno)), ponderada por uma taxa de mutação alfa.
        // alfa = (afinidade)
//        System.out.println("X: "+anticorpo.getX()+ " Y: "+anticorpo.getY()+"Afinidade: "+anticorpo.getAfinidade()+ " Xgeno: "+anticorpo.getAntigeno().getX()+ " Ygeno: "+anticorpo.getAntigeno().getY());
        for (int i = 0; i < tamanhoBase; i++) {
//            System.out.println("Anticorpo: "+anticorpo.toString());
            anticorpo.getVars().set(i, anticorpo.getVars().get(i) - (anticorpo.getAfinidade() * (anticorpo.getVars().get(i) - anticorpo.getAntigeno().getVars().get(i))));
        }
//        anticorpo.setAfinidade(-1); // deixa afinidade como -1 para depois na atualização ver se será selecionado e atualizado.
//        System.out.println("X: "+anticorpo.getX()+ " Y: "+anticorpo.getY()+"Anfinidade: "+anticorpo.getAfinidade()+ " Xgeno: "+anticorpo.getAntigeno().getX()+ " Ygeno: "+anticorpo.getAntigeno().getY());
    }

    // Muta uma lista de clones
    /**
     * Muta um Array de Clones.
     *
     * @param clones Array de clones que deseja mutar.
     */
    public void mutaClones(ArrayList<Anticorpo> clones) {
        for (int i = 0; i < clones.size(); i++) {
            mutaAnticorpo(clones.get(i));
        }
    }

    /**
     * Executa o k-médias.
     */
    public void kMedias() {
        // k-médias
        Matriz matriz = new Matriz(getPopulacao(), getAntigenos());
        int m[][] = matriz.criaMatriz(false);
        // Pega uma coluna e percorre suas linhas
        for (int k = 0; k < getPopulacao().size(); k++) { //coluna (Anticorpos/Protótipos)
            int count = 0;
            double x = 0, y = 0;
            ArrayList<Double> aux = new ArrayList<>();
            for (int i = 0; i < tamanhoBase; i++) {
                aux.add((double) 0);
            }
            for (int j = 0; j < getAntigenos().size(); j++) { // linha (Antigenos/Objetos)
                if (m[j][k] == 1) {
                    // Pegar todos objetos e fazer a média e setar no protótipo
                    count++;
                    for (int v = 0; v < tamanhoBase; v++) {
                        aux.set(v, aux.get(v) + getAntigenos().get(j).getVars().get(v));
                    }
//                    x = x + getAntigenos().get(j).getX();
//                    y = y + getAntigenos().get(j).getY();
                }
            }
            if (count >= 2) {
//                System.out.println("Count = " + count + " X: " + getPopulacao().get(k).getX() + " nX: " + x / count + " somaX: " + x);
                for (int v = 0; v < tamanhoBase; v++) {
                    getPopulacao().get(k).getVars().set(v, aux.get(v) / count);
                }
//                getPopulacao().get(k).setX(x / count);
//                getPopulacao().get(k).setY(y / count);
            }
        }
    }

    public void mediasInt(double limite) {
        ArrayList<Anticorpo> anticorpos = new ArrayList<>();
        // Pegar um anticorpo aleatório e medir distâncias perto dele
        double min = 0;
        double max = Math.sqrt(tamanhoBase);
        
        for (int i = 0; i < populacao.size(); i++) {
            Anticorpo sel = populacao.get(r.nextInt(populacao.size()));
//            System.out.println("Selecionado: " + sel.toString());
            for (Anticorpo anticorpo : getPopulacao()) {
                double soma = 0;
                for (int v = 0; v < tamanhoBase; v++) {
                    // distância eucliadina
                    soma += Math.pow(sel.getVars().get(v) - anticorpo.getVars().get(v), 2);
                }
                double distEuclidiana = Math.sqrt(soma);
                distEuclidiana = (distEuclidiana - min) / (max - min); // normalizando a distância
                if (distEuclidiana <= limite && distEuclidiana != 0) {
                    anticorpos.add(anticorpo);
                }
            }
            // Faz média
//            double somaX = 0, somaY = 0;
            ArrayList<Double> somas = new ArrayList<>();
            for (Anticorpo anticorpo : anticorpos) {
                // inicializa com zeros
                for (int v = 0; v < tamanhoBase; v++) {
                    somas.add((double) 0);
                }
                // somatorios
                for (int v = 0; v < tamanhoBase; v++) {
                    somas.set(v, somas.get(v) + anticorpo.getVars().get(v));
                }
//                somaX = somaX + anticorpo.getX();
//                somaY = somaY + anticorpo.getY();
            }
//            System.out.println("SomaX: " + somaX + " SomaY: " + somaY);
//            if (somaX != 0 || somaY != 0) {
            if (verificaZeros(somas)) {
//                System.out.println("X: " + somaX / anticorpos.size() + " Y: " + somaY / anticorpos.size());
                for (int v = 0; v < tamanhoBase; v++) {
                    sel.getVars().set(v, somas.get(v) / anticorpos.size());
                }

//                sel.setX(somaX / anticorpos.size());
//                sel.setY(somaY / anticorpos.size());
                populacao.removeAll(anticorpos);
            }

            anticorpos.removeAll(anticorpos);
        }
    }

    public boolean verificaZeros(ArrayList<Double> somas) {
        boolean zero = false;
        for (Double soma : somas) {
            // se algum diferente de zero então retorna true
            if (soma != 0) {
                zero = true;
            }
        }
        return zero;
    }

    //OK
    public double matrizGrupos() {
        // Número de rótulos
        int max = 0;
        for (Antigeno antigeno : antigenos) {
            if (antigeno.getRotulo() > max) {
                max = antigeno.getRotulo();
            }
        }

        int matriz[][] = new int[getPopulacao().size()][max];
        //inicializa matriz com zeros
        for (int i = 0; i < getPopulacao().size(); i++) {
            for (int j = 0; j < max; j++) {
                matriz[i][j] = 0;
            }
        }

        Matriz matrix = new Matriz(getPopulacao(), getAntigenos());
        int[][] m = matrix.criaMatriz(false); //antigeno/anticorpo

        // pegar uma coluna e ver todas linhas
        for (int j = 0; j < getPopulacao().size(); j++) { //linhas
            for (int i = 0; i < getAntigenos().size(); i++) { //colunas
                for (int k = 0; k < max; k++) { //rotulos
                    if (m[i][j] == 1) {
                        if (getAntigenos().get(i).getRotulo() == k + 1) {
                            matriz[j][k] += 1;
                        }
                    }
                }
            }
        }

        //imprime matriz
        // extrair max de cada linha
        int soma = 0;
        for (int i = 0; i < getPopulacao().size(); i++) {
            int maior = -1;
            for (int j = 0; j < max; j++) {
//                System.out.print("\t" + matriz[i][j]);
                if (matriz[i][j] > maior) {
                    maior = matriz[i][j];
                }
            }
            soma += maior;
//            System.out.println("");
        }
        System.out.println("PCC: " + df.format((double) ((double) soma / (double) getAntigenos().size()) * 100));
        return (double) ((double) soma / (double) getAntigenos().size()) * 100;
    }

    /**
     * ONDE RODA O CLONALG
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //QuickSort quick = new QuickSort();
        // Gera população
        Clonalg clo = new Clonalg();
        ArrayList<Anticorpo> iniciais = clo.geraPop(tamPop);
//        ArrayList<Anticorpo> iniciais = clo.geraPopC();
//        clo.geraPopC();
        // COMEÇA ITERAÇÕES
        clo.executa(iniciais);
        

    }

    // VAI EXECUTAR O NUMERO DE VEZES DO TAMANHO DO ARRAY EXECUCOES
    public void executa(ArrayList<Anticorpo> execucoes) {
        geraPopC();
        // COMEÇA ITERAÇÕES
        int it = 1;
        double mediaPCC = 0;
        double mediaGeracoes = 0;
        double mediaPrototipos = 0;

        for (Anticorpo exec : execucoes) {
            System.out.println("Test " + it + ": ");
            it++;
            getPopulacao().removeAll(getPopulacao());
            getPopulacao().add(exec);
            ArrayList<Anticorpo> popInicial = new ArrayList<>();
            Anticorpo inicial = new Anticorpo((ArrayList<Double>) getPopulacao().get(0).getVars().clone());
            popInicial.add(inicial);

            double objetivoAtual = 0;
            double objetivo = 0;
            int gers =0;
            
            // Laço do algoritmo, inicia as gerações
            for (int i = 0; i < numGeracoes; i++) {
                
                if (objetivo > erroQuadratico || i == 0 || getPopulacao().size()<2) {
                    // Atualiza a afinidade da população
                    atualizaAfinidadePop(getPopulacao());
                    objetivo = objetivoAtual; // a objetivo é atualizada
//                    System.out.println(i + 1 + "ª Geração");

                    // Selecionar anticorpos para clone 
                    ArrayList<Anticorpo> selecionados = new ArrayList<>();
                    // é selecionado os anticorpos que possuem afinidade < limiar
                    for (Anticorpo anticorpo : getPopulacao()) {
                        if ((anticorpo.getAfinidade() < limiar || i == 0) && anticorpo.getAfinidade() != -1) { // limiar
                            int nClones = (int) (numClo * (1 - anticorpo.getAfinidade())); // modificado 1- anti
                            if(nClones<1){nClones=1;}
//                            System.out.println("S: "+nClones);
//                    System.out.println("Afinidade: "+anticorpo.getAfinidade() +"nClones: "+nClones);
                            for (int k = 0; k < nClones; k++) { // insere k vezes o mesmo elemento na lista de clones
                                selecionados.add(clone(anticorpo));
                            }
                        }
//                        else{System.out.println("N");}
                    }

                    // Muta clones. 
                    mutaClones(selecionados);
                    // Insere os clones na população
                    getPopulacao().addAll(selecionados); // adiciona todos clones
                    
                    // Setar todos anticorpos como se não tivessem antigenos
                    // Comentar para ver rastros ***************************************************************************
                    for (Anticorpo anticorpo : getPopulacao()) {
                        anticorpo.setAntigeno(null);
                        anticorpo.setAfinidade((double) -1);
                    }
//                System.out.println("Pop: "+clo.getPopulacao().toString());
                    //Atualiza afinidade da população
                    atualizaAfinidadePop(getPopulacao());
                    
                    // Re-seleciona clones mutados **********************************************
//                    if(i>=5){
                        //1º Passo
                        ArrayList<Anticorpo> rejeitados = new ArrayList<>();
                        for (Anticorpo selecionado : selecionados) {
                            //remover os -1
                            if(selecionado.getAfinidade()==-1){
                                rejeitados.add(selecionado);
                            }
                        }
                        selecionados.removeAll(rejeitados);
                        rejeitados.removeAll(rejeitados);
                        
                        //2º Passo
                        quick.quickSort(selecionados); // Ordem crescete, melhores por primeiro
                        int reSel = (int)(selecionados.size()*numSel); // selecionando x% melhores
                        if(reSel < 1){reSel=1;}
//                        System.out.println("reSel: "+reSel);
                        for (int j=reSel;j<selecionados.size();j++) {
                            rejeitados.add(selecionados.get(j));
                        }
                        selecionados.removeAll(rejeitados);
                        getPopulacao().removeAll(rejeitados);
//                    }
                    // Acaba re-seleção *********************************************************

                    // Limpar população, eliminando quem não possui relação com nenhum objeto
                    ArrayList<Anticorpo> delets = new ArrayList<>();
                    for (Anticorpo anticorpo : getPopulacao()) {
                        if (anticorpo.getAfinidade() == (double) -1 || anticorpo.getAntigeno() == null) {
                            delets.add(anticorpo);
                        }
                    }
                    getPopulacao().removeAll(delets);

                    // Imprime a população no final da geração
//            System.out.println("População no final da geração: ");
//            System.out.println(clo.getPopulacao().toString());
                    // Calcula função objetivo (Erro Quadrático)
                    objetivoAtual = 0;
                    for (Antigeno antigeno : getAntigenos()) {
                        for (Anticorpo anticorpo : getPopulacao()) {
                            if (anticorpo.getAntigeno() == antigeno) {
                                objetivoAtual = objetivoAtual + (1 * Math.pow(anticorpo.getAfinidade(), 2));
                            } else {
                                objetivoAtual = objetivoAtual + (0 * Math.pow(anticorpo.getAfinidade(), 2));
                            }
                        }
                    }
                    objetivo = Math.abs(objetivo - objetivoAtual);
                    //************************************
//                    if(getPopulacao().size() > 2){
//                        kMedias();
//                    }
//                    atualizaAfinidadePop(getPopulacao());   //*
//                System.out.println("Objetivo: " + objetivo);
                    gers = i+1;
                // K-MÉDIAS ao final de cada geração.
                if(i >= 3){
                    kMedias();
//////                    atualizaAfinidadePop(getPopulacao());   //*  
                }
                } else {
                    gers = i+1;
                    i = numGeracoes; // finaliza o alg.
                }
                PlotTest plot = new PlotTest(popInicial, getAntigenos(), getPopulacao(), gers);
            }
            
            System.out.println("Geração: " + gers);
            mediaGeracoes += gers;
            gers=0;
            // Faz uma média das redondezas
//            mediasInt(0.2);
//            kMedias();
            
            
            //Mostra matriz
            Matriz matriz = new Matriz(getPopulacao(), getAntigenos());
            //int[][] m = matriz.criaMatriz(true);

            System.out.println("N. Anticorpos: " + getPopulacao().size());
//            System.out.println("N. Antígenos: " + clo.getAntigenos().size());

          // Grupos
//        clo.grupos(clo.getPopulacao(),m);
          // Gerar gráfico com objetos e protótipos
        if(getAntigenos().get(0).getVars().size()==2){
            PlotTest plot = new PlotTest(popInicial, getAntigenos(), getPopulacao(), it);
        }
//            System.out.println(popInicial.toString());
            mediaPCC += matrizGrupos();

            // Imprime a população no final da geração
//        System.out.println(clo.getPopulacao().get(0));
//        clo.imprimeCaminho(clo.getPopulacao().get(0));
//            for(int h=0;h<clo.getPopulacao().size();h++){
//                System.out.print(h+1+"º "+clo.getPopulacao().get(h).toString());
//            }
            System.out.println("");
            mediaPrototipos += getPopulacao().size();
        }
        System.out.println("Número de Antígenos: "+getAntigenos().size());
        System.out.println("Média PCC: " + mediaPCC / execucoes.size());
        System.out.println("Média Protótipos: " + mediaPrototipos / execucoes.size());
        System.out.println("Media Gerações: " + mediaGeracoes / execucoes.size());
//        System.out.println(mediaPCC / execucoes.size()+" "+ mediaPrototipos / execucoes.size()+" "+mediaGeracoes / execucoes.size());

    }

    /**
     * @return the antigenos
     */
    public ArrayList<Antigeno> getAntigenos() {
        return antigenos;
    }

    /**
     * @return the populacao
     */
    public ArrayList<Anticorpo> getPopulacao() {
        return populacao;
    }

    /**
     * @param populacao the populacao to set
     */
    public void setPopulacao(ArrayList<Anticorpo> populacao) {
        this.populacao = populacao;
    }

    /**
     * @return the numGeracoes
     */
    public int getNumGeracoes() {
        return numGeracoes;
    }

    /**
     * @param numGeracoes the numGeracoes to set
     */
    public void setNumGeracoes(int numGeracoes) {
        this.numGeracoes = numGeracoes;
    }

}
