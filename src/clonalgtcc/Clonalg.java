package clonalgtcc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author joao
 */
public class Clonalg {
    // Utilitários
    private final Random r = new Random();
    DecimalFormat df = new DecimalFormat("#.##");
    
    // Variavéis Sistema
    private ArrayList<Anticorpo> populacao = new ArrayList<>();
    ArrayList<Anticorpo> tests = new ArrayList<>();
    Leitor leitor = new Leitor();
    QuickSort quick = new QuickSort();
    private ArrayList<Antigeno> antigenos = leitor.leAntigenos();

    // Configurações.
    private static final int numExecu = 100; // número de testes
    private static double limiar = 0.0; // número de anticorpos selecionados para serem clonados
    private static final int numClo = 5;//6 // cada selecionado possuirá este número de clones ou menos
    private static final double numSel = 0.5; // número de clones selecionados para entra na população 50%.
    private static int numGeracoes = 200;
    private static final double erroQuadratico = 0.001; // diferença necessária para parada
    private static final int tamanhoBase = 4; // dimensões da base
    private static boolean graficosG = false; // graficos de cada geração
    private static boolean graficosF = false; // graficos para cada final de execução



    /**
     * Gera uma várias populações iniciais de anticorpos aleatórios no intervalo (0,1].
     *
     * @param numExec número de execuções..
     */
    public ArrayList<Anticorpo> geraPop(int numExec) {
        ArrayList<Anticorpo> iniciais = new ArrayList<>();
        for (int i = 0; i < numExec; i++) {
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

    public double distanciaEuclidiana(Anticorpo anticorpo, Antigeno antigeno) {
        double soma = 0;

        for (int v = 0; v < tamanhoBase; v++) {
            soma += Math.pow((antigeno.getVars().get(v) - anticorpo.getVars().get(v)), 2);
        }

        return Math.sqrt(soma);
    }

    /**
     * Para cada antigeno selecionado calcular o anticorpo de maior afinidade e
     * então setar sua afinidade e seu antigeno. Atualiza afinidade de um
     * anticorpo. 
     *
     * @param anticorposs Lista de anticorpos para atualizar a afinidade.
     */
    public void atualizaAfinidadePop(ArrayList<Anticorpo> anticorposs) {
        ArrayList<Anticorpo> anticorpos = new ArrayList<>();
        anticorpos.addAll(anticorposs);
//        ArrayList<Anticorpo> anticorpos = (ArrayList<Anticorpo>)anticorposs.clone();
        Collections.shuffle(getAntigenos()); //Fisher–Yates shuffle @https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
//        for (int i = 0; i < Math.min(anticorposs.size(), antigenos.size()); i++) {// for de antigenos
        for (int i = 0; i < antigenos.size(); i++) {// for de antigenos
            Auxx aux = new Auxx(Double.MIN_VALUE); // Guarda o anticorpo de menor distância, e a menor distância.
            aux.setAnticorpo(null); // inicializa um anticorpo para o aux
            for (Anticorpo anticorpo : anticorpos) { // Encontrar o anticorpo de maior afinidade
                double dist =  1/distanciaEuclidiana(anticorpo, antigenos.get(i));
                //quanto maior afinidade melhor
                if (dist > aux.getDist()) { // atualiza quem tem maior afinidade
                    if (dist > anticorpo.getAfinidade() && anticorpo.getAntigeno() != null) {
                        aux.setDist(dist);
                        aux.setAnticorpo(anticorpo);
                    }
                    if (anticorpo.getAntigeno() == null) {
                        aux.setDist(dist);
                        aux.setAnticorpo(anticorpo);
                    }

                }
            }

            // atualiza afinidade
            double afinidade = 0;
            if (aux.getAnticorpo() != null) {
                afinidade = aux.getDist();
                aux.getAnticorpo().setAfinidade(afinidade);
                aux.getAnticorpo().setAntigeno(antigenos.get(i));
            }
//            System.out.println("Distância: "+aux.getDist()+" Soma: "+somatorio);
//            System.out.println("Afinidade: "+afinidade);
//            System.out.println("Somatorio: "+somatorio);
//            System.out.println("Distância: "+aux.getDist());
//            System.out.println("Afinidade: "+afinidade);

        }
        // Depois que calcula todas afinidades, normalizar todas as diferentes de -1
        //Pegando máximo e mínimo
        double somatorio = 0;
//        System.out.println("Afinidades não normalizadas: ");
        for (Anticorpo anticorpo : anticorposs) {
            if (anticorpo.getAfinidade() != -1) {
//                System.out.println("Afinidade: "+anticorpo.getAfinidade());
                somatorio += anticorpo.getAfinidade();
            }
//            if (anticorpo.getAfinidade() < min && anticorpo.getAfinidade() != -1) {
//                min = anticorpo.getAfinidade();
//            }
//            if (anticorpo.getAfinidade() > max) {
//                max = anticorpo.getAfinidade();
//            }
        }
//        System.out.println("Afinidades Antigas: ");
//        System.out.println(anticorposs.toString());
//         Setanto afinidades normalizadas
        for (Anticorpo anticorpo : anticorposs) {
            if (anticorposs.size() == 1) {
//                System.out.println("caiu: "+anticorpo.toString());
                anticorpo.setAfinidade(0.3); // único entao 0
            } else if (anticorpo.getAfinidade() == -1) {
                anticorpo.setAfinidade((anticorpo.getAfinidade())); // -1 continua -1
            } else {
                anticorpo.setAfinidade((anticorpo.getAfinidade()) / (somatorio));
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
            if (i == 0) {
//                System.out.println(" X: "+varsClone.get(i));
            }
            double x = Math.abs((((1 - r.nextGaussian()) * varsClone.get(i)) + varsClone.get(i)) / 2);
            if (i == 0) {
//                System.out.println("Novo X: "+x);
            }

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
//        System.out.println("Anticorpo Antigo: "+anticorpo.toString());
        for (int i = 0; i < tamanhoBase; i++) {
//            System.out.println("Anticorpo: "+anticorpo.toString());
            anticorpo.getVars().set(i, anticorpo.getVars().get(i) - ((1-anticorpo.getAfinidade()) * (anticorpo.getVars().get(i) - anticorpo.getAntigeno().getVars().get(i))));
        }
//        System.out.println("Anticorpo Mutado: "+anticorpo.toString());
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
        if (getPopulacao().isEmpty()) {
            System.out.println("POPULAÇÂO VAZIA!");
        }
        Matriz matriz = new Matriz(getPopulacao(), getAntigenos());
        int m[][] = matriz.criaMatriz(false);
        // Pega uma coluna e percorre suas linhas
        for (int k = 0; k < getPopulacao().size(); k++) { //coluna (Anticorpos/Protótipos)
            int count = 0;
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
//        System.out.println("PCC: " + df.format((double) ((double) soma / (double) getAntigenos().size()) * 100));
        return (double) ((double) soma / (double) getAntigenos().size()) * 100;
    }

    /**
     * ONDE RODA O CLONALG
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        //QuickSort quick = new QuickSort();
        // Gera população
        Clonalg clo = new Clonalg();
        ArrayList<Anticorpo> iniciais = clo.geraPop(numExecu);
//        ArrayList<Anticorpo> iniciais = clo.geraPopC();
//        clo.geraPopC();
        // COMEÇA ITERAÇÕES
        clo.executa(iniciais);

    }

    /**
     * Atualiza um ArrayList deixando somente os melhores.
     *
     * @param clones
     */
    public void selecionaMelhoresClones(ArrayList<Anticorpo> clones) {
        ArrayList<Anticorpo> rejeitados = new ArrayList<>();
        for (Anticorpo clone : clones) {
            double dist = distanciaEuclidiana(clone, clone.getAntigeno());
            clone.setAfinidade(dist);
        }
        quick.quickSort(clones); // Ordem crescete, melhores por primeiro
        int reSel = (int) (clones.size() * numSel); // selecionando x% melhores
        if (reSel < 1) {
            reSel = 1;
        }
        for (int j = reSel; j < clones.size(); j++) {
            rejeitados.add(clones.get(j));
        }
        clones.removeAll(rejeitados);
    }

    public void mediaMesmosGrupos() {
        ArrayList<Anticorpo> novos = new ArrayList<>();
        ArrayList<Anticorpo> remover = new ArrayList<>();
        for (Antigeno antigeno : getAntigenos()) {
            ArrayList<Anticorpo> mesmos = new ArrayList<>();

            for (Anticorpo pop : populacao) {
                if (pop.getAntigeno() == antigeno) {
                    mesmos.add(pop);
                }
            }
            if (mesmos.size() >= 2) {
                ArrayList<Double> aux = new ArrayList<>();
                for (int i = 0; i < tamanhoBase; i++) {
                    aux.add(0.0);
                }
                for (Anticorpo mesmo : mesmos) {
                    for (int i = 0; i < tamanhoBase; i++) {
                        aux.set(i, aux.get(i) + mesmo.getVars().get(i));
                    }
                }
                for (int i = 0; i < aux.size(); i++) {
                    aux.set(i, aux.get(i) / mesmos.size());
                }
                Anticorpo a = new Anticorpo(aux, antigeno, 1); // PODE SER QUE TENHA QUE MUDAR O 1
                novos.add(a);
                remover.addAll(mesmos);
            }
        }
        populacao.removeAll(remover);
        populacao.addAll(novos);
    }

    // VAI EXECUTAR O NUMERO DE VEZES DO TAMANHO DO ARRAY EXECUCOES
    public void executa(ArrayList<Anticorpo> execucoes) throws InterruptedException {
        //geraPopC();
        // COMEÇA ITERAÇÕES
        
        
        ArrayList<String> resultados = new ArrayList<>();
        double limi=0.0;
        for(int z=0;z<9;z++){
            ArrayList<Double> desvioPCC = new ArrayList();
            ArrayList<Double> desvioPro = new ArrayList();
            ArrayList<Double> desvioGer = new ArrayList();
            double mediaPCC = 0, melhorPCC=0, piorPCC=1000.0;
        double mediaGeracoes = 0;
        double mediaPrototipos = 0;
        int maxNumProt = 0, minNumProt = Integer.MAX_VALUE, maxGer = 0, minGer = Integer.MAX_VALUE;
            int it = 1;
            limi += 0.1;
            limiar = limi;
        long start = System.currentTimeMillis();
        
        for (Anticorpo exec : execucoes) {
            int gers = 0;
//            System.out.println("Test " + it + ": ");
            it++;
            getPopulacao().removeAll(getPopulacao());
            getPopulacao().add(exec);
            ArrayList<Anticorpo> popInicial = new ArrayList<>();
            Anticorpo inicial = new Anticorpo((ArrayList<Double>) getPopulacao().get(0).getVars().clone());
            popInicial.add(inicial);

            double objetivoAtual = 0;
            double objetivo = 0;
            
            atualizaAfinidadePop(getPopulacao());
            // Laço do algoritmo, inicia as gerações
            for (int i = 0; i < numGeracoes; i++) {

//                if (objetivo > erroQuadratico || i == 0 || getPopulacao().size()<2) {
                if (objetivo > erroQuadratico || i == 0) {
                    objetivo = objetivoAtual; // a objetivo é atualizada

//                    System.out.println(i + 1 + "ª Geração");
                    // 4) Seleciona candidatos a clones, conforme menor que o limiar. Clona de acordo com a afinidade.
                    // Selecionar anticorpos para clone 
                    ArrayList<Anticorpo> selecionados = new ArrayList<>();
                    // é selecionado os anticorpos que possuem afinidade < limiar
                    for (Anticorpo anticorpo : getPopulacao()) {
                        if ((anticorpo.getAfinidade() > limiar || i == 0) && anticorpo.getAfinidade() != -1) { // limiar
//                        if ((anticorpo.getAfinidade() > limiar) && anticorpo.getAfinidade() != -1 ) { // limiar
                            int nClones = (int) (numClo * (anticorpo.getAfinidade())); // modificado 1- anti
                            if (nClones < 1) {
                                nClones = 1;
                            }
//                            System.out.println("S: "+nClones);
//                    System.out.println("CLONA. Afinidade: "+anticorpo.getAfinidade() +"nClones: "+nClones);
                            for (int k = 0; k < nClones; k++) { // insere k vezes o mesmo elemento na lista de clones
                                selecionados.add(clone(anticorpo));
                            }
                        }
//                        else{System.out.println("N "+anticorpo.getAfinidade());}
                    }

                    // 5) Muta clones. (afinidades ficam desatualizadas). 
                    mutaClones(selecionados);
                    //MUDAR 1 &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
                    // 6) Comparar a distância euclidiana de cada clone com seu antígeno e ordenar os melhores.
                    // 7) Re-selecionar somente os clones mais adaptados.
                    // 8) Inserir os mais adaptados na população geral.
                    selecionaMelhoresClones(selecionados);
                    getPopulacao().addAll(selecionados);
//                    if (getPopulacao().size() > getAntigenos().size()) {
//                        mediaMesmosGrupos(); // população toda
//                    }
                    // MUDADO 1 

                    // Limpar população, eliminando quem não possui relação com nenhum objeto
//                    for (Anticorpo anticorpo : getPopulacao()) {
//                        if (anticorpo.getAfinidade() == (double) -1 || anticorpo.getAntigeno() == null) {
//                            delets.add(anticorpo);
//                        }
//                    }
//                    getPopulacao().removeAll(delets);
//                    System.out.println("Tam Pop2: "+getPopulacao().size());
                    // Imprime a população no final da geração
//            System.out.println("População no final da geração: ");
//            System.out.println(clo.getPopulacao().toString());
                    // 9) Calcular o erro quadrático.
                    // Calcula função objetivo (Erro Quadrático)
                    kMedias();
                    // Atualiza a afinidade da população (Reseta afinidades)
                    for (Anticorpo anticorpo : getPopulacao()) {
                        anticorpo.setAntigeno(null);
                        anticorpo.setAfinidade((double) -1);
                    }
                    // 2) Atualiza afinidade da população, e normaliza a afinidade. (Competem entre Si).
                    atualizaAfinidadePop(getPopulacao());
                    // 3) Remove anticorpos da população que não identificam qualquer antígeno.
                    ArrayList<Anticorpo> deletss = new ArrayList<>();
//                    deletss.removeAll(deletss);
                    for (Anticorpo anticorpo : getPopulacao()) {
                        if (anticorpo.getAfinidade() == (double) -1 || anticorpo.getAntigeno() == null) {
                            deletss.add(anticorpo);
                        }
                    }
                    getPopulacao().removeAll(deletss);

                    // Função objetivo
                    objetivoAtual = 0;
                    for (Antigeno antigeno : getAntigenos()) {
                        for (Anticorpo anticorpo : getPopulacao()) {
                            if (anticorpo.getAntigeno() == antigeno) {
//                                objetivoAtual = objetivoAtual + (1 * Math.pow(anticorpo.getAfinidade(), 2));
                                objetivoAtual = objetivoAtual + (1 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
                            } else {
//                                objetivoAtual = objetivoAtual + (0 * Math.pow(anticorpo.getAfinidade(), 2));
                                objetivoAtual = objetivoAtual + (0 * Math.pow(distanciaEuclidiana(anticorpo, antigeno), 2));
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
                    gers = i + 1;
                    // K-MÉDIAS ao final de cada geração.
//                if(i >= 2){
                    //10) k-Médias na população geral.
//                    kMedias();
//////                    atualizaAfinidadePop(getPopulacao());   //*  
//                }
                } else {
                    gers = i + 1;
                    i = numGeracoes; // finaliza o alg.
                }
//                System.out.println("Final Geração: "+getPopulacao().toString());
                if (graficosG) {
                    Thread.sleep(500);
                    PlotTest plot = new PlotTest(popInicial, getAntigenos(), getPopulacao(), gers);
                }

            }
            

//            System.out.println("Geração: " + gers);
            
            
            // Faz uma média das redondezas
//            mediasInt(0.2);
//            kMedias();

            //Mostra matriz
//            Matriz matriz = new Matriz(getPopulacao(), getAntigenos());
            //int[][] m = matriz.criaMatriz(true);
//            System.out.println("N. Anticorpos: " + getPopulacao().size());
//            System.out.println("N. Antígenos: " + clo.getAntigenos().size());

            // Grupos
//        clo.grupos(clo.getPopulacao(),m);
            // Gerar gráfico com objetos e protótipos
            if (getAntigenos().get(0).getVars().size() == 2 && graficosF) {
                PlotTest plot = new PlotTest(popInicial, getAntigenos(), getPopulacao(), it - 1);
            }
//            System.out.println(popInicial.toString());
            //Calculos PCC
            double PCC = matrizGrupos();
            desvioPCC.add(PCC);
            mediaPCC += PCC;
            //Calculos Gerações
            desvioGer.add((double)gers);
            mediaGeracoes += gers;
            //Calculos de protótipos
            desvioPro.add((double)getPopulacao().size());
            mediaPrototipos += getPopulacao().size();
            
            // Maxs e Mins
            if(melhorPCC<(PCC)){
                melhorPCC = PCC;
            }
            if(piorPCC>PCC){
                piorPCC =  PCC;
            }
            if(maxNumProt<getPopulacao().size()){
                maxNumProt = getPopulacao().size();
            }
            if(minNumProt>getPopulacao().size()){
                minNumProt = getPopulacao().size();
            }
            if(maxGer<gers){
                maxGer = gers;
            }
            if(minGer>gers){
                minGer = gers;
            }

            gers = 0;
        }
        
        System.out.println("Limiar: "+limi);
        System.out.println("Número de Antígenos: " + getAntigenos().size());
        System.out.println("Média PCC: " + df.format(mediaPCC / execucoes.size())+"±"+df.format(desvioPadrao(desvioPCC, mediaPCC/execucoes.size()))+"\t Min: "+df.format(piorPCC)+"\t Máx: "+df.format(melhorPCC));
        System.out.println("M. Protótipos: " + mediaPrototipos / execucoes.size()+"±"+df.format(desvioPadrao(desvioPro, mediaPrototipos/execucoes.size()))+"\t Min: "+minNumProt+"\t\t Máx: "+maxNumProt);
        System.out.println("M. Gerações: " + mediaGeracoes / execucoes.size()+"±"+df.format(desvioPadrao(desvioGer, mediaGeracoes/execucoes.size()))+"\t Min: "+minGer+"\t\t Máx: "+maxGer);
        
        // Pra excel
        System.out.println("");
//        System.out.print(df.format(piorPCC)+"\t"+df.format(mediaPCC / execucoes.size())+"\t"+df.format(melhorPCC)+"\t");
//        System.out.print(minNumProt +"\t"+ df.format(mediaPrototipos / execucoes.size())+"\t"+maxNumProt+"\t");
//        System.out.print(minGer+"\t"+ df.format(mediaGeracoes / execucoes.size())+"\t"+maxGer);
//        System.out.println("");
//        String sb = "Limiar: "+limi+"\n"+df.format(piorPCC)+"\t"+df.format(mediaPCC / execucoes.size())+"\t"+df.format(melhorPCC)+"\t"+minNumProt +"\t"+ df.format(mediaPrototipos / execucoes.size())+"\t"+maxNumProt+"\t"+minGer+"\t"+ df.format(mediaGeracoes / execucoes.size())+"\t"+maxGer+"\n";
        long finish = System.currentTimeMillis();
        double time = (finish - start);
        String sb = df.format(piorPCC)+"\t"+df.format(mediaPCC / execucoes.size())+"±"+df.format(desvioPadrao(desvioPCC, mediaPCC/execucoes.size()))+"\t"+df.format(melhorPCC)+"\t"+
                minNumProt +"\t"+ df.format(mediaPrototipos / execucoes.size())+"±"+df.format(desvioPadrao(desvioPro, mediaPrototipos/execucoes.size()))+"\t"+maxNumProt+"\t"+
                minGer+"\t"+ df.format(mediaGeracoes / execucoes.size())+"±"+df.format(desvioPadrao(desvioGer, mediaGeracoes/execucoes.size()))+"\t"+maxGer+"\t"+df.format(time)+"\n";
        resultados.add(sb);
    }
        
        System.out.println("Resutados: ");
        for (String resultado : resultados) {
            System.out.print(resultado);
        }
        
        
        
//        System.out.println(mediaPCC / execucoes.size()+" "+ mediaPrototipos / execucoes.size()+" "+mediaGeracoes / execucoes.size());

    }
    
    //@link{http://www.guj.com.br/t/desvio-padrao/38312/2}
    public double desvioPadrao(ArrayList<Double> desvio, double media) {
        int somatorio=0;
        for (Double desv : desvio) {
            somatorio+= Math.pow((desv - media), 2);
        }
        double desvioPadrao = Math.sqrt((double)((double)1/((double)desvio.size()-1))*somatorio);
        return desvioPadrao;
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
