/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author mandi
 */
public class GraphForm extends javax.swing.JFrame {

    protected JPanel okvirSlike;
    protected JPanel donjiOkvir;
    protected JButton saveButton;
    protected JFileChooser jchooser;
    protected JFreeChart barChart;
    protected ChartPanel cp;
    
    protected ArrayList<Integer> brOtvorenih; 
    protected ArrayList<Integer> brZatvorenih;
    protected ArrayList<String> algorithms;
    
    /**
     * Creates new form GraphForm
     */
    public GraphForm() {
        initComponents();
        initialization();
    }
    
    public GraphForm(ArrayList<Integer> brOtvorenih, ArrayList<Integer> brZatvorenih, ArrayList<String> algorithms){
        initComponents();
        initialization();
        
        this.brOtvorenih = brOtvorenih;
        this.brZatvorenih = brZatvorenih;
        this.algorithms = algorithms;
        
        //creating dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        //Pretpostavljamo ispravnost ulaznih podataka.
        
        for(int i = 0; i < algorithms.size(); ++i){
            dataset.addValue(brOtvorenih.get(i), "Otvoreni cvorovi", algorithms.get(i));
        }
        
        for(int i = 0; i < algorithms.size(); ++i){
            dataset.addValue(brZatvorenih.get(i), "Zatvoreni cvorovi", algorithms.get(i));
        }
        
        barChart = ChartFactory.createBarChart("Usporedba algoritama pretrage", "Algoritmi", "Broj vrhova", dataset, PlotOrientation.VERTICAL, true, true, false);
        cp = new ChartPanel(barChart);
        cp.setPreferredSize(new Dimension(600, 400));
        cp.setVisible(true);
        okvirSlike.add(cp);
    }

    private void initialization(){
        this.setTitle("Comparation");
        this.setLayout(new BorderLayout());
        
        jchooser = new JFileChooser();
        //FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
        //jchooser.setFileFilter(filter);
        
        okvirSlike = new JPanel();
        
        this.add(okvirSlike, BorderLayout.CENTER);
        
        donjiOkvir = new JPanel();
        saveButton = new JButton();
        saveButton.setText("Save image");
        saveButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                saveButtonClicked(evt);
            }
        } );
        donjiOkvir.add(saveButton);
        this.add(donjiOkvir, BorderLayout.SOUTH);
    }
    
    protected void saveButtonClicked(ActionEvent evt){
        int resultSave = jchooser.showSaveDialog((Component)evt.getSource());
        if(resultSave == JFileChooser.APPROVE_OPTION){
            
            File file = jchooser.getSelectedFile();
            try{
                System.out.println("Odabrano za spremanje: " + file.toString());
                ChartUtils.saveChartAsPNG(file, barChart, 600, 400);
            }
            catch(IOException e){
                System.out.println("Pogreska kod spremanja grafa u datoteku.");
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 481, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GraphForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GraphForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GraphForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GraphForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        /* Create and display the form */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphForm().setVisible(true);
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

