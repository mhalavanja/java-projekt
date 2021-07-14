package projekt;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
//import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.plaf.basic.BasicComboBoxUI.KeyHandler;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;


public class Form extends javax.swing.JFrame {

    String algorithm = "findPath";

    //Oznacava odabir opcije: vizualizacija jednog algoritma ili pokretanje vise njih i spremanje u bazu
    final int visualization = 0, runAll = 1;
    int odabirOpcije = visualization;
        
    public Form() {
        initComponents();
        
        initialization();
    }

    private void initialization(){
        grid = new Grid(this.algorithm);
        this.setSize(1200, 600); //560
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pathfinding visualization");
        this.add(grid, BorderLayout.CENTER);

        menu = new JPanel();
        startCoordinetesLabel = new JLabel();
        startCoordinetesLabel.setText("Start node coordinates(numbers with space between):");
        endCoordinetesLabel = new JLabel();
        endCoordinetesLabel.setText("End node coordinates(numbers with space between):");

        grid.start.setColumns(2);
        menu.add(startCoordinetesLabel);
        menu.add(grid.start);
        grid.end.setColumns(2);
        menu.add(endCoordinetesLabel);
        menu.add(grid.end);

        startButton = new JButton();
        startButton.setText("Start");
        //startButton.addActionListener(grid.new startButtonPushed());
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonClicked(evt);
            }
        });
        menu.add(startButton);

        newButton = new JButton();
        newButton.setText("New");
        newButton.addActionListener(grid.new newButtonPushed());
        menu.add(newButton);

        graphButton = new JButton();
        graphButton.setText("Graph");
        graphButton.addActionListener(new graphButtonPushed());
        menu.add(graphButton);

        saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.addActionListener(new saveButtonPushed());
        menu.add(saveButton);

        String[] algorithmList = {"findPath", "BFS", "DFS", "A*", "Dijkstra", "Greedy best first search"};
        algorithmComboBox = new JComboBox<>(algorithmList);
        algorithmComboBox.addActionListener(new algorithmComboBoxSelected(algorithmComboBox));
        //menu.add(algorithmComboBox);

        this.add(menu, BorderLayout.SOUTH);

        toolBar = new JToolBar();
        toolBar.setOrientation(toolBar.VERTICAL);
        vizualizacijaButton = new JRadioButton();
        vizualizacijaButton.setText("Choose one for visualization");
        vizualizacijaButton.setMargin(new Insets(20, 5, 5, 5));
        vizualizacijaButton.setSelected(true);
        vizualizacijaButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                visualizationButtonClicked(evt);
            }
        } );

        izvrednjavanjeSvihButton = new JRadioButton();
        izvrednjavanjeSvihButton.setText("Run all without visualization");
        izvrednjavanjeSvihButton.setMargin(new Insets(0, 5, 30, 5));
        izvrednjavanjeSvihButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                runAllButtonClicked(evt);
            }
        } );
        
        buttonGroup = new ButtonGroup(); 
        buttonGroup.add(vizualizacijaButton);
        buttonGroup.add(izvrednjavanjeSvihButton);


        toolBar.add(vizualizacijaButton);
        toolBar.add(izvrednjavanjeSvihButton);
        toolBar.add(algorithmComboBox);

        this.add(toolBar, BorderLayout.WEST);
    }
    
    
    private void visualizationButtonClicked(ActionEvent evt){
        odabirOpcije = visualization;
        algorithmComboBox.setEnabled(true);
    }

    private void runAllButtonClicked(ActionEvent evt){
        odabirOpcije = runAll;
        algorithmComboBox.setEnabled(false);
    }
    
    private void startButtonClicked(ActionEvent evt){
        if(odabirOpcije == visualization)
            grid.startButtonPushedVisualization(evt);
        else if(odabirOpcije == runAll)
            grid.startButtonPushedRunAll(evt);
    }
    
    public class algorithmComboBoxSelected implements ActionListener {

        private final JComboBox<String> algorithmComboBox;

        public algorithmComboBoxSelected(JComboBox<String> algorithmComboBox) {
            this.algorithmComboBox = algorithmComboBox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            algorithm = Objects.requireNonNull(algorithmComboBox.getSelectedItem()).toString();
            grid.algorithm = algorithm;
        }
    }

    //Potrebne varijable u grafickom sucelju
    protected Grid grid;
    protected JPanel menu;
    protected JLabel startCoordinetesLabel;
    protected JLabel endCoordinetesLabel;
    protected JButton startButton;
    protected JButton newButton;
    protected JButton graphButton;
    protected JButton saveButton;
    protected JComboBox<String> algorithmComboBox;
    protected JToolBar toolBar;
    protected JRadioButton vizualizacijaButton;
    protected JRadioButton izvrednjavanjeSvihButton;
    protected ButtonGroup buttonGroup;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 568, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 347, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                Form window = new Form();
                window.setVisible(true);
            }
        });
    }
}

class graphButtonPushed implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

class saveButtonPushed implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}