package projekt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.StringTokenizer;


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
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        //Konstruita
        
        grid = new Grid(this.algorithm);
        grid.setName("grid");
        this.setSize(1200, 600); //560
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Pathfinding visualization");
        this.add(grid, BorderLayout.CENTER);

        menu = new JPanel();
        menu.setName("menu");
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

        graphButton = new JButton();
        graphButton.setName("graphButton");
        graphButton.setText("Graph");
        graphButton.addActionListener(new graphButtonPushed());
        menu.add(graphButton);

        saveButton = new JButton();
        saveButton.setName("saveButton");
        saveButton.setText("Save");
        saveButton.addActionListener(new saveButtonPushed());
        menu.add(saveButton);

        String[] algorithmList = {"findPath", "BFS", "DFS", "A*", "Dijkstra", "Greedy best first search"};
        algorithmComboBox = new JComboBox<>(algorithmList);
        algorithmComboBox.setName("algorithmComboBox");
        algorithmComboBox.addActionListener(new algorithmComboBoxSelected(algorithmComboBox));

        this.add(menu, BorderLayout.SOUTH);

        toolBar = new JToolBar();
        toolBar.setName("toolBar");
        toolBar.setOrientation(toolBar.VERTICAL);

        vizualizacijaButton = new JRadioButton();
        vizualizacijaButton.setName("vizualizacijaButton");
        vizualizacijaButton.setText("Choose one for visualization");
        vizualizacijaButton.setMargin(new Insets(20, 5, 5, 5));
        vizualizacijaButton.setPreferredSize(new Dimension(190, 50));
        vizualizacijaButton.setSelected(true);
        vizualizacijaButton.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                visualizationButtonClicked(evt);
            }
        } );

        izvrednjavanjeSvihButton = new JRadioButton();
        izvrednjavanjeSvihButton.setName("izvrednjavanjeSvihButton");
        izvrednjavanjeSvihButton.setText("Run all without visualization");
        izvrednjavanjeSvihButton.setMargin(new Insets(0, 5, 30, 5));
        izvrednjavanjeSvihButton.setPreferredSize(new Dimension(190, 50));
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
        
        
        JPanel startPanel = new JPanel();
        startButton = new JButton();
        startButton.setName("startButton");
        startButton.setText("Start");
        startButton.setPreferredSize(new Dimension(130, 30));
        //startButton.addActionListener(grid.new startButtonPushed());
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonClicked(evt);
            }
        });
        startPanel.add(startButton);
        toolBar.add(startPanel);

        JPanel restartPanel = new JPanel();
        restartPanel.setLayout(new BorderLayout());
        newButton = new JButton();
        newButton.setName("newButton");
        newButton.setText("New");
        newButton.addActionListener(grid.new newButtonPushed());
        
        clearButton = new JButton();
        clearButton.setName("clearButton");
        clearButton.setText("Clear");
        clearButton.addActionListener(grid.new clearButtonPushed());
        restartPanel.add(newButton, BorderLayout.WEST);
        restartPanel.add(clearButton, BorderLayout.EAST);
        
        toolBar.add(restartPanel);
        toolBar.addSeparator(new Dimension(10, 60));
        
        JPanel dbpanel = new JPanel();
        GridLayout glayout = new GridLayout(4,1);
        glayout.setVgap(5);
        dbpanel.setLayout(glayout);
        
        JPanel imeGrafaUnosPanel = new JPanel();
        imeGrafaUnosPanel.setLayout(new BorderLayout());
        JLabel porukaSpremanja = new JLabel();
        porukaSpremanja.setText("Graph name: ");
        imeGrafaUnosPanel.add(porukaSpremanja, BorderLayout.WEST);
        
        imeGrafaField = new JTextField();
        imeGrafaField.setPreferredSize(new Dimension(120, 30));
        //imeGrafaField.setMargin(new Insets(40,0,0,0));
        imeGrafaUnosPanel.add(imeGrafaField, BorderLayout.EAST);
        dbpanel.add(imeGrafaUnosPanel);
        
        saveGraphButton = new JButton();
        saveGraphButton.setText("Save graph");
        saveGraphButton.setPreferredSize(new Dimension(130, 30));
        saveGraphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savegraphButtonClicked(evt);
            }
        });
        dbpanel.add(saveGraphButton);
        
        
        graphNames = new ArrayList<String>();
        graphNames = DbConnection.getAllGraphNames();
        graphComboBox = new JComboBox<>(graphNames.toArray());
        graphComboBox.setName("graphComboBox");
        dbpanel.add(graphComboBox);
        
        loadGraph = new JButton();
        loadGraph.setText("Load graph");
        dbpanel.add(loadGraph);
        
        toolBar.add(dbpanel);
        
        this.add(toolBar, BorderLayout.WEST);
        //pack();
    }
    
    private void savegraphButtonClicked(ActionEvent evt){
        String imeGrafa = imeGrafaField.getText();
        if(grid.checkInputCoordinates(grid.start.getText(), grid.end.getText()) && imeGrafa != null){
            StringTokenizer st = new StringTokenizer(grid.start.getText());
            int startX = Integer.parseInt(st.nextToken());
            int startY = Integer.parseInt(st.nextToken());
            
            StringTokenizer ste = new StringTokenizer(grid.end.getText());
            int endX = Integer.parseInt(ste.nextToken());
            int endY = Integer.parseInt(ste.nextToken());
            
            StringBuffer strb = new StringBuffer();
            strb.append(startX + " " + startY + " " + endX + " " + endY);
            for(Vertex v : grid.wallCells){
                strb.append(" " + v.getX() + " " + v.getY());
            }
            
            String strBridovi = strb.toString();
            DbConnection.upsertGraph(imeGrafa, strBridovi);
            graphNames = DbConnection.getAllGraphNames();
            graphComboBox = new JComboBox<>(graphNames.toArray());
        }
        else{
            JOptionPane.showMessageDialog(this, "Neispravan unos grafa!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            grid.start.setText("");
            grid.end.setText("");
            
            return;
        }
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
    protected JButton clearButton;
    protected JButton graphButton;
    protected JButton saveButton;
    protected JComboBox<String> algorithmComboBox;
    protected JToolBar toolBar;
    protected JRadioButton vizualizacijaButton;
    protected JRadioButton izvrednjavanjeSvihButton;
    protected ButtonGroup buttonGroup;
    protected JTextField imeGrafaField;
    protected JButton saveGraphButton;
    protected ArrayList<String> graphNames;
    protected JComboBox graphComboBox;
    protected JButton loadGraph;

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
                DbConnection.createTables();
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