/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author joao
 */
public class PlotTest {
    private XYSeriesCollection dataset;
//    public static void main (String[] args) {
//        Leitor l = new Leitor();
//        ArrayList<Antigeno> antigenos = l.leAntigenos();
//        new PlotTest(antigenos, anticrpos);
//    }
    public PlotTest (ArrayList<Anticorpo> iniciais, ArrayList<Antigeno> antigenos, ArrayList<Anticorpo> anticorpos) {
        dataset = new XYSeriesCollection();
        XYSeries data = new XYSeries("Antígenos");
        XYSeries data2 = new XYSeries("Anticorpos");
        XYSeries data3 = new XYSeries("Pop. Inicial");
        for (Antigeno antigeno : antigenos) {
            data.add(antigeno.getX(), antigeno.getY());
        }
        for(Anticorpo anticorpo : anticorpos){
            data2.add(anticorpo.getX(),anticorpo.getY());
        }
        for(Anticorpo inicial : iniciais){
            data3.add(inicial.getX(),inicial.getY());
        }
        dataset.addSeries(data3);
        dataset.addSeries(data);
        dataset.addSeries(data2);
        
        showGraph();
    }
    private void showGraph() {
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new java.awt.Dimension(500, 380)); //270
        final ApplicationFrame frame = new ApplicationFrame("Dados");
        frame.setContentPane(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createScatterPlot(
            "Ruspini",                  // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );
        // Comentando essas linhas abaixo;
//        XYPlot plot = (XYPlot) chart.getPlot();
//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesLinesVisible(0, true);
//        plot.setRenderer(renderer);
        // Até aqui
        return chart;
    }
}
