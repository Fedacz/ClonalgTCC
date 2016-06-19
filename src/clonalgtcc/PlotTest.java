/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clonalgtcc;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetGroup;
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
    public PlotTest (ArrayList<Anticorpo> iniciais, ArrayList<Antigeno> antigenos, ArrayList<Anticorpo> anticorpos, int it) {
        dataset = new XYSeriesCollection();
        XYSeries data1 = new XYSeries("Antígenos");
//        XYSeries data2 = new XYSeries("Class2");
//        XYSeries data3 = new XYSeries("Class3");
//        XYSeries data4 = new XYSeries("Class4");
        XYSeries dataAnticorpos = new XYSeries("Anticorpos");
        XYSeries dataInicial = new XYSeries("Pop. Inicial");
        
        for (Antigeno antigeno : antigenos) {
                data1.add(antigeno.getVars().get(0), antigeno.getVars().get(1));
        }
        for(Anticorpo anticorpo : anticorpos){
            dataAnticorpos.add(anticorpo.getVars().get(0),anticorpo.getVars().get(1));
        }
        for(Anticorpo inicial : iniciais){
            dataInicial.add(inicial.getVars().get(0),inicial.getVars().get(1));
        }
        dataset.addSeries(data1);
//        dataset.addSeries(data2);
//        dataset.addSeries(data3);
//        dataset.addSeries(data4);
        dataset.addSeries(dataAnticorpos);
        dataset.addSeries(dataInicial);

        
        showGraph(String.valueOf(it));
    }
    private void showGraph(String it) {
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new java.awt.Dimension(500, 380)); //270
        final ApplicationFrame frame = new ApplicationFrame("Dados "+it);
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
        XYPlot xyPlot = chart.getXYPlot();
        XYItemRenderer renderer = xyPlot.getRenderer();
 
//        renderer.setSeriesPaint( 4, Color.BLUE );
//        renderer.setSeriesShape(1, new Ellipse2D.Double(-3, -3, 6, 6));
//        renderer.setSeriesShape(2, new Ellipse2D.Double(-3, -3, 6, 6));
//        renderer.setSeriesShape(3, new Ellipse2D.Double(-3, -3, 6, 6));
//        renderer.setSeriesShape(4, new Ellipse2D.Double(-3, -3, 6, 6));
        
 
        // Comentando essas linhas abaixo;
//        XYPlot plot = (XYPlot) chart.getPlot();
//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesLinesVisible(0, true);
//        plot.setRenderer(renderer);
        // Até aqui
        return chart;
    }
}
