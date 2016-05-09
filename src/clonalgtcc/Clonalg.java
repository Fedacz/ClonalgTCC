package clonalgtcc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Normalização, 20% melhores clones, permutação.
 * Classe Main, onde roda o Clonalg
 * Mutar Clones 
 * Ver o k-médias.
 * Erro Quadrático (o algoritmo deve parar quando o valor dessa função for menor q 10^-3.
 * @author joao
 */
public class Clonalg {

    private final Random r = new Random();
    private ArrayList<Anticorpo> populacao = new ArrayList<>();
    
//    private static final int tamAnticorpo = 15; // ordem da matriz-1

    // Variavéis do Sistema
    // Obter Antigenos
    Leitor leitor = new Leitor();
    private ArrayList<Antigeno> antigenos = leitor.leAntigenos();

    // Configurações.
    private static final int tamPop = 1;
//    private static final int numSel = 1; // numero de anticorpos selecionados para serem clonados
    private static final int numClo = 5; // cada selecionado possuirá este número de clones
//    private static final int numSubstituidos = 5; // tem q sem menor q numSel*numClo. Nº Clones que entraram da Pop.
    private static int numGeracoes = 200;
    private static double erroQuadratico = 0.001;

    /**
     * Gera uma população de anticorpos aleatrios no intervalo (0,1].
     *
     * @param tamPop tamanho da população desejada.
     */
    public void geraPop(int tamPop) {
        for (int i = 0; i < tamPop; i++) {
            Anticorpo ant = new Anticorpo();
            ant.setX(r.nextFloat());
            ant.setY(r.nextFloat());
            
            ant.setAfinidade(-1); // afinidade negativa quando iniciado.
            populacao.add(ant);
        }
    }

    /**
     * Para cada antigeno selecionado calcular o anticorpo de maior afinidade e
     * então setar sua afinidade e seu antigeno. Atualiza afinidade de um
     * anticorpo.
     *
     * @param anticorpos Lista de anticorpos para atualizar a afinidade.
     */
    public void atualizaAfinidadePop(ArrayList<Anticorpo> anticorpos) {
//        anticorpo.distEuclidiana(antigenos);
// Quando o número de anticorpos for menor que o de antigenos
        for (Antigeno antigeno : getAntigenos()) { // Para cada antigeno selecionado...
//            double somatorio = 0;
            Auxx aux = new Auxx(1.0); // Guarda o anticorpo de menor distancia, e a menor distancia.
            aux.setAnticorpo(anticorpos.get(0)); // inicializa um anticorpo para o aux
            for (Anticorpo anticorpo : anticorpos) { // Encontrar o anticorpo de maior afinidade
                double dist = Math.sqrt(Math.pow(antigeno.getX() - anticorpo.getX(), 2) + Math.pow(antigeno.getY() - anticorpo.getY(), 2)); // distância euclidiana
//                somatorio = somatorio + dist;
//                System.out.println("Distância: "+dist);
                if (dist < aux.getDist()) { // atualiza quem tem maior afinidade
                    aux.setDist(dist);
                    aux.setAnticorpo(anticorpo);
                }
            }
            // atualiza afinidade
//            double afinidade = aux.getDist() / somatorio;
            double afinidade = aux.getDist();
//            System.out.println("Somatorio: "+somatorio);
//            System.out.println("Distância: "+aux.getDist());
//            System.out.println("Afinidade: "+afinidade);
            aux.getAnticorpo().setAfinidade(afinidade);
            aux.getAnticorpo().setAntigeno(antigeno);
        }
    }
    

    /**
     * Retorna um clone de um anticorpo.
     *  Clone = (randn*Ab+Ab)/2
     * @param ant anticorpo que deseja clonar.
     * @return retorna um clone de um anticorpo.
     */
    public Anticorpo cloneBackup(Anticorpo ant) {
        // Gets
        Anticorpo clone = new Anticorpo();
        double afinidade = ant.getAfinidade();
        double x = ant.getX();
        double y = ant.getY();
        Antigeno antigeno = null;
        
        if(ant.getAntigeno() != null){
            double xx = ant.getAntigeno().getX();
            double yy = ant.getAntigeno().getY();
            antigeno = new Antigeno(xx, yy);
        }
        
//        antigeno = ant.getAntigeno();
        // Sets      
        clone.setX(x);
        clone.setY(y);
        clone.setAfinidade(afinidade);
        clone.setAntigeno(antigeno);
//        System.out.println("Clone: ");
//        System.out.println(clone.toString());
        return clone;
    }
    
    /**
     * Retorna um clone de um anticorpo.
     *  Clone = (randn*Ab+Ab)/2
     * @param ant anticorpo que deseja clonar.
     * @return retorna um clone de um anticorpo.
     */
    public Anticorpo clone(Anticorpo ant) {
        // Gets
        Anticorpo clone = new Anticorpo();
        double afinidade = ant.getAfinidade();
        double x = (r.nextGaussian()*(ant.getX())+ant.getX())/2;
        double y = (r.nextGaussian()*(ant.getY())+ant.getY())/2;
        Antigeno antigeno = null;
        
        if(ant.getAntigeno() != null){
            double xx = ant.getAntigeno().getX();
            double yy = ant.getAntigeno().getY();
            antigeno = new Antigeno(xx, yy);
        }
        
//        antigeno = ant.getAntigeno();
        // Sets      
        clone.setX(x);
        clone.setY(y);
        clone.setAfinidade(afinidade);
        clone.setAntigeno(antigeno);
//        System.out.println("Clone: ");
//        System.out.println(clone.toString());
        return clone;
    }
    
    /** MY CLONE
     * Retorna um clone de um anticorpo.
     *  Clone = (randn*Ab+Ab)/2
     * @param anticorpo anticorpo que deseja clonar.
     * @return retorna um clone de um anticorpo.
     */
    public Anticorpo cloneM(Anticorpo anticorpo) {
        // Gets
        Anticorpo clone = new Anticorpo();
        double afinidade = anticorpo.getAfinidade();
        double x= 0;
        double y= 0;

        if(r.nextFloat()>0.5){
            x = anticorpo.getX() + ((1-anticorpo.getX())*r.nextFloat());
            y = anticorpo.getY()+ ((1-anticorpo.getY())*r.nextFloat());
        }
        else{
            x = (r.nextFloat()*(anticorpo.getX()+anticorpo.getX()))/2;
            y = (r.nextFloat()*(anticorpo.getY()+anticorpo.getY()))/2;
        }
        Antigeno antigeno = null;
        
        if(anticorpo.getAntigeno() != null){
            double xx = anticorpo.getAntigeno().getX();
            double yy = anticorpo.getAntigeno().getY();
            antigeno = new Antigeno(xx, yy);
        }
        
//        antigeno = ant.getAntigeno();
        // Sets      
        clone.setX(x);
        clone.setY(y);
        clone.setAfinidade(afinidade);
        clone.setAntigeno(antigeno);
//        System.out.println("Clone: ");
//        System.out.println(clone.toString());
        return clone;
    }

    
    
    /**
     * Muta um anticorpo e jnão atualiza a afinidade.
     *
     * @param anticorpo anticorpo que deseja mutar.
     */
    public void mutaAnticorpo(Anticorpo anticorpo) {
        // CRIAR ALGORITMO DE MATURAÇÃO (MUTAÇÃO).
        // A maturação (mutação) dos clones pode ser dada deslocando o anticorpo na direção do antigeno 
        // (anticorpo = anticorpo - alfa*(anticorpo-antigeno)), ponderada por uma taxa de mutação alfa.
        // alfa = (afinidade)
//        System.out.println("X: "+anticorpo.getX()+ " Y: "+anticorpo.getY()+"Afinidade: "+anticorpo.getAfinidade()+ " Xgeno: "+anticorpo.getAntigeno().getX()+ " Ygeno: "+anticorpo.getAntigeno().getY());
        // X
        anticorpo.setX(anticorpo.getX() - (anticorpo.getAfinidade()*(anticorpo.getX() - anticorpo.getAntigeno().getX())));
        // Y
        anticorpo.setY(anticorpo.getY() - (anticorpo.getAfinidade()*(anticorpo.getY() - anticorpo.getAntigeno().getY())));
        anticorpo.setAfinidade(-1); // deixa afinidade como -1 para depois na atualização ver se será selecionado e atualizado.
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

    /**
     * ONDE RODA O CLONALG
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        QuickSort quick = new QuickSort();
        // Gera população
        Clonalg clo = new Clonalg();
        clo.geraPop(tamPop);
        System.out.println("População Inicial: ");
        System.out.println(clo.getPopulacao().toString());
        ArrayList<Anticorpo> popInicial = (ArrayList<Anticorpo>)clo.getPopulacao().clone();
//        System.out.println("Antigenos: ");
//        System.out.println(clo.antigenos.toString());
        // Atualiza a afinidade da população
        clo.atualizaAfinidadePop(clo.getPopulacao());
        // Imprime População Inicial
        
        double objetivo =1;
        // Laço do algoritmo, inicia as gerações
        for (int i = 0; i < numGeracoes; i++) {
            if(objetivo>erroQuadratico){
                objetivo=0;
            System.out.println(i + 1 + "ª Geração");
            
            // selecionar anticorpos; serão selecionados e já inseridos na lista de clones
            ArrayList<Anticorpo> selecionados = new ArrayList<>();
            // é selecionado os anticorpos que possuem afinidade != -1.0
            // criar uma lista com os selecionados clonados
            for(Anticorpo anticorpo : clo.getPopulacao()){
                if(anticorpo.getAfinidade() != (double)-1){
                    int nClones = (int) (numClo*anticorpo.getAfinidade());
                    if(nClones == 0){
                        nClones =1;
                    }
                    for (int k = 0; k < nClones; k++) { // insere k vezes o mesmo elemento na lista de clones
                    selecionados.add(clo.clone(anticorpo));
                }
                }
            }
            // Muta clones. 
            clo.mutaClones(selecionados);
            // insere os clones na população
            clo.getPopulacao().addAll(selecionados); // adiciona todos clones
//            clo.getPopulacao().clear(); // adiciona todos clones
//            clo.getPopulacao().addAll(selecionados); // adiciona todos clones
                
            // Setar todos anticorpos como se não tivessem antigenos
            // Comentar para ver rastros
            for(Anticorpo anticorpo : clo.getPopulacao()){
                    anticorpo.setAntigeno(null);
            }
            
            //Atualiza afinidade da população
            clo.atualizaAfinidadePop(clo.getPopulacao());
            
            ArrayList<Anticorpo> delets = new ArrayList<>();
                for(Anticorpo anticorpo : clo.getPopulacao()){
                    if(anticorpo.getAfinidade() == (double)-1 || anticorpo.getAntigeno()==null){
                        delets.add(anticorpo);
                    }
                }
                clo.getPopulacao().removeAll(delets);
            
            // Imprime a população no final da geração
//            System.out.println("População no final da geração: ");
//            quick.quickSort(clo.getPopulacao());
//            System.out.println(clo.getPopulacao().toString());
            // Calcula função objetivo (Erro Quadrático)
            for (Anticorpo anticorpo : clo.getPopulacao()) {
                   objetivo = objetivo + (anticorpo.getAfinidade()*Math.pow(anticorpo.getAfinidade(), 2));
            }
            System.out.println("Objetivo: "+objetivo);
        }
            else{
                i = numGeracoes; // finaliza o alg.
            }

        }
    
        //Mostra matriz
        Matriz matriz = new Matriz(clo.getPopulacao(), clo.getAntigenos());
        matriz.criaMatriz(true);
        
        System.out.println("N. Anticorpos: "+clo.getPopulacao().size());
        System.out.println("N. Antígenos: "+clo.getAntigenos().size());
        
        // Gerar gráfico com objetos e protótipos
        PlotTest plot = new PlotTest(popInicial, clo.getAntigenos(), clo.getPopulacao());
        System.out.println(popInicial.toString());
        
        // Imprime a população no final da geração
//        System.out.println(clo.getPopulacao().get(0));
//        clo.imprimeCaminho(clo.getPopulacao().get(0));
//            for(int h=0;h<clo.getPopulacao().size();h++){
//                System.out.println(h+1+"º "+clo.getPopulacao().get(h).toString());
//            }
    }

    /**
     * @return the antigenos
     */
    public ArrayList<Antigeno> getAntigenos() {
        return antigenos;
    }

}
