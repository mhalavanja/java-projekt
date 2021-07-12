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

    public Form() {
        initComponents();
    }

    // mreža proširuje objekt jPanel, u nju stavljamo sve potrebne elemente za rad 
    // algoritma pretrage

    public static class Grid extends JPanel implements ActionListener{

        protected List<Vertex> startEndCells;
        protected List<Vertex> openedCells;
        protected List<Vertex> visitedCells;
        protected List<Vertex> wallCells;
        protected Vertex currentCell;
        protected int numOfX = 80;
        protected int numOfY = 50;
        protected int cellSize = 10;
        protected int startX = 0, startY = 0, endX = 0, endY = 0;
        //Timer clock = new Timer(20, this);
        protected ArrayList<Vertex> openedNodes = new ArrayList<>(numOfX * numOfY); // mislim da su još u a* ostali s nodes
        protected ArrayList<Vertex> visitedNodes = new ArrayList<>(numOfX * numOfY);
        protected boolean found = false;
        protected String algorithm = "";
        
        //Varijabla pressedKey ce sluziti kod postavljanja Start i End vrha pretrage. Mozemo 
        //postaviti i da pretraga krene ukoliko se stisne space.
        protected char pressedKey = '-';

        JTextArea start = new JTextArea();
        JTextArea end = new JTextArea();
        

        public Grid(String algorithm) {
            startEndCells = new ArrayList<>(2);
            openedCells = new ArrayList<>(numOfX * numOfY);
            visitedCells = new ArrayList<>(numOfX * numOfY);
            wallCells = new ArrayList<>(numOfX * numOfY);
            Vertex currentCell = null;
            this.algorithm = algorithm;
          
            // klikom miša crtamo zid, trebalo bi dodat da samo funkcionira
            // od pokretanja aplikacije do start i od new do start (neki boolean)
            // ali to cemo valjda kad budemo sve gumbe regulirali kad mogu radit a kad ne
            MouseAdapter handler = new MouseAdapter() { 
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)){
                        if(e.getX()/cellSize -1 >= 0 && e.getX()/cellSize -1 < numOfX &&
                                e.getY()/cellSize -1 >= 0 && e.getY()/cellSize -1 < numOfY) {
                            
                            int positionX = e.getX()/cellSize -1;
                            int positionY = e.getY()/cellSize -1;
                            //ako kliknemo lijevim klikom misa i drzimo tipku 'S' -> oznacavamo startni vrh
                            if(pressedKey == 's'){
                                System.out.println("S stisnut uz lijevi klik misa.");
                                startX = positionX;
                                startY = positionY;
                            }
                            else if(pressedKey == 'e'){
                                endX = positionX;
                                endY = positionY;
                            }
                            else{
                                //Oznacili smo novi zid/prepreku.
                                wallCell(positionX, positionY);
                            }
                        }
                    }
                    //Ukoliko stisnemo desni klik misa, micemo oznaceni zid.
                    else if(SwingUtilities.isRightMouseButton(e)){
                        if(e.getX()/cellSize -1 >= 0 && e.getX()/cellSize -1 < numOfX &&
                                e.getY()/cellSize -1 >= 0 && e.getY()/cellSize -1 < numOfY) {
                            removeWallCell(e.getX()/cellSize -1,e.getY()/cellSize -1);
                        }
                    }
                }
                @Override
                public void mouseDragged(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)){
                        if(e.getX()/cellSize -1 >= 0 && e.getX()/cellSize -1 < numOfX &&
                                e.getY()/cellSize -1 >= 0 && e.getY()/cellSize -1 < numOfY) {
                            wallCell(e.getX()/cellSize -1,e.getY()/cellSize -1);
                        }
                    }
                    //Drzanjem desnog klika misa i prelaskom preko zida, uklanjamo isti iz liste zidova.
                    else if(SwingUtilities.isRightMouseButton(e)){
                        if(e.getX()/cellSize -1 >= 0 && e.getX()/cellSize -1 < numOfX &&
                                e.getY()/cellSize -1 >= 0 && e.getY()/cellSize -1 < numOfY) {
                            removeWallCell(e.getX()/cellSize -1,e.getY()/cellSize -1);
                        }
                    }
                }
            };
            this.addMouseListener(handler);
            this.addMouseMotionListener(handler);
            
            KeyAdapter keyHandler = new KeyAdapter(){
                //Pristiskom/drzanjem nekog gumba s tipkovnice pamtimo njegovu vrijednost - ukoliko se ne drzi 
                //tipka defaultna vrijednost je '-' (taj nam znak nece sluziti za nikakvu akciju).
                //Primjene su kod postavljanja stratnog i krajnjeg vrha, pokretanja algoritma sa space.
                @Override
                public void keyPressed(KeyEvent e){
                    pressedKey = e.getKeyChar();
                    System.out.println("Pritisnuta tipka " + pressedKey);
                }
                @Override
                public void keyReleased(KeyEvent e){
                    pressedKey = '-';
                }
                @Override
                public void keyTyped(KeyEvent e){

                    System.out.println("Pritisnuta tipka " + pressedKey);
                }
            };
            this.addKeyListener(keyHandler);
        }
        
        // možda za klik miša kasnije bude trebalo...
        @Override
        public void actionPerformed(ActionEvent arg0) {
            // moze elseif-ovi u ovisnosti koji algoritam koristimo
            /*
            repaint();
            
            if (found) {
                clock.stop();
            } 
            //ovo maknuti kad se prebaci na workera
    
            else if (algorithm.equals("BFS") || algorithm.equals("DFS")) {
                xfs();
            }*/
        }
        

        

        private void paintCell(Vertex cell, Graphics g, Color color) {
            if (cell != null) {
                int cellX = cellSize + (cell.getX() * cellSize);
                int cellY = cellSize + (cell.getY() * cellSize);
                g.setColor(color);
                g.fillRect(cellX, cellY, cellSize, cellSize);
            }
        }

        private void paintCells(List<Vertex> cells, Graphics g, Color color) {
            for (Vertex fillCell : cells) {
                int cellX = cellSize + (fillCell.getX() * cellSize);
                int cellY = cellSize + (fillCell.getY() * cellSize);
                g.setColor(color);
                g.fillRect(cellX, cellY, cellSize, cellSize);
            }
        }

        // funkcije koja crza mrežu (linije, boja kvadratiće)
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = numOfX * cellSize;
            int height = numOfY * cellSize;

            // bojenje otvorenih
            paintCells(openedCells, g, Color.BLUE);

            //bojanje zatvorenih cvorova
            paintCells(visitedCells, g, Color.cyan);

            //bojanje trenutno otvorenog
            paintCell(currentCell, g, Color.GREEN);
            
            //bojenje zidova
            paintCells(wallCells, g, Color.BLACK);
            
            //bojanje pocetne i zavrsne celije
            paintCells(startEndCells, g, Color.ORANGE);

            g.setColor(Color.BLACK);
            g.drawRect(cellSize, cellSize, width, height);

            for (int i = cellSize; i <= width; i += cellSize) {
                g.drawLine(i, cellSize, i, height + cellSize);
            }

            for (int i = cellSize; i <= height; i += cellSize) {
                g.drawLine(cellSize, i, width + cellSize, i);
            }
        }
        // nisam siguran je li treba dodati da ne ubacuje element ako ga već sadrži
        // i je li 
        
        // dodavanje početnog i završnog čvora, potrebno za iscrtavanje
        // (početni i završni su crvene boje)
        public void startEndCell(int x, int y) {
            if(!startEndCells.contains(new Vertex(x, y))) {
                startEndCells.add(new Vertex(x, y));
            }
            repaint();
        }

        // dodavanje otvorenih čvorova
        public void openedCell(int x, int y) {
            if(!openedCells.contains(new Vertex(x, y))) {
                openedCells.add(new Vertex(x, y));
            }
            repaint();
        }
        
        public void addOpenedCell(Vertex v){
            if(!openedCells.contains(v)){
                openedCells.add(v);
            }
            repaint();
        }
        
        public void wallCell(int x, int y) {
            if(!wallCells.contains(new Vertex(x, y))) {
                wallCells.add(new Vertex(x, y));
            }
            repaint();
        }
        
        public void removeWallCell(int x, int y){
            if(wallCells.contains(new Vertex(x, y))) {
                wallCells.remove(new Vertex(x, y));
                repaint();
            }
        }

        public void visitedCell(int x, int y) {
            if(!visitedCells.contains(new Vertex(x, y))) {
                visitedCells.add(new Vertex(x, y));
            }
            repaint();
        }

        public void removeOpenedCell(Vertex v) {
            if(openedCells.contains(v)) {
                openedCells.remove(v);
            }
            repaint();
        }

        //Implementiramo realizaciju pritiska gumba start.
        private void startButtonPushed(java.awt.event.ActionEvent evt) {
            
            // dodati kontrolu unosa //////////////////////////////////////////////!!!
            
            //ako ne unesemo nista u tekstualni dio, mogli smo oznaciti s misom i tiupkom 's' start
            if(start.getText() != null){
                StringTokenizer st = new StringTokenizer(start.getText());
                startX = Integer.parseInt(st.nextToken());
                startY = Integer.parseInt(st.nextToken());
            }
            //ako nismo unijeli koordinate cilja, mogli smo ga oznaciti s klikom misa i tipkom 'e'
            if(start.getText() != null){
                StringTokenizer ste = new StringTokenizer(end.getText());
                endX = Integer.parseInt(ste.nextToken());
                endY = Integer.parseInt(ste.nextToken());
            }
            
            openedNodes.add(new Vertex(startX, startY)); // još u a* je vjj koristeno
            openedCells.add(new Vertex(startX, startY));

            startEndCell(startX, startY);
            startEndCell(endX, endY);
            //clock.start();

            if (algorithm.equals("A*")) {
                //Dio koji poziva SwingWorkera koji pretrazuje graf u zasebnoj dretvi.
                SwingWorker<Boolean, Vertex> dijsktraSearch = new AStarWorker(this);
                dijsktraSearch.execute();
            }
            else if(algorithm.equals("findPath")) {
                SwingWorker<Boolean, Vertex> findPathSearch = new FindPathWorker(this);
                findPathSearch.execute();
            }
            else if(algorithm.equals("DFS") || algorithm.equals("BFS")) {
                SwingWorker<Boolean, Vertex> xfsSearch = new XfsPathWorker(this);
                xfsSearch.execute();
            }
            else if(algorithm.equals("Greedy best first search")){
                SwingWorker<Boolean, Vertex> greedySearch = new GreedyBestFirstSearchWorker(this);
                greedySearch.execute();
            }
            //dodati ovdje pokretanje workera za ostale algoritme
            //kada se dodaju, onda maknuti pozivanje koraka algoritma kod otkucaja sata
        }

        // nova pretraga -- čistimo sva potrebna polja i iscrtavamo ponovno mrežu
        public class newButtonPushed implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {

                startEndCells.clear();
                openedCells.clear();
                openedNodes.clear();
                visitedNodes.clear();
                visitedCells.clear();
                wallCells.clear();
                found = false;
                currentCell = null;
                repaint();
            }
        }

        public class algorithmComboBoxSelected implements ActionListener {

            private final JComboBox<String> algorithmComboBox;

            public algorithmComboBoxSelected(JComboBox<String> algorithmComboBox) {
                this.algorithmComboBox = algorithmComboBox;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                algorithm = Objects.requireNonNull(algorithmComboBox.getSelectedItem()).toString();
            }
        }
    }


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

                // inicijalizacija prozora i ubacivanje svih potrebnih elemenata
                Form window = new Form();
                Grid grid = new Grid(window.algorithm);
                window.setSize(1200, 600); //560
                window.setLayout(new BorderLayout());
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.add(grid, BorderLayout.CENTER);

                JPanel menu = new JPanel();
                JLabel startCoordinetesLabel = new JLabel();
                startCoordinetesLabel.setText("Start node coordinates(numbers with space between):");
                JLabel endCoordinetesLabel = new JLabel();
                endCoordinetesLabel.setText("End node coordinates(numbers with space between):");

                grid.start.setColumns(2);
                menu.add(startCoordinetesLabel);
                menu.add(grid.start);
                grid.end.setColumns(2);
                menu.add(endCoordinetesLabel);
                menu.add(grid.end);

                JButton startButton = new JButton();
                startButton.setText("Start");
                //startButton.addActionListener(grid.new startButtonPushed());
                startButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        grid.startButtonPushed(evt);
                    }
                });
                menu.add(startButton);

                JButton newButton = new JButton();
                newButton.setText("New");
                newButton.addActionListener(grid.new newButtonPushed());
                menu.add(newButton);

                JButton graphButton = new JButton();
                graphButton.setText("Graph");
                graphButton.addActionListener(new graphButtonPushed());
                menu.add(graphButton);

                JButton saveButton = new JButton();
                saveButton.setText("Save");
                saveButton.addActionListener(new saveButtonPushed());
                menu.add(saveButton);

                String[] algorithmList = {"findPath", "BFS", "DFS", "A*", "Dijkstra", "Greedy best first search"};
                JComboBox<String> algorithmComboBox = new JComboBox<>(algorithmList);
                algorithmComboBox.addActionListener(grid.new algorithmComboBoxSelected(algorithmComboBox));
                menu.add(algorithmComboBox);

                window.add(menu, BorderLayout.SOUTH);
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