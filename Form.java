
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class Form extends javax.swing.JFrame {

    String algorithm = "findPath";

    public Form() {
        initComponents();
    }

    // mreža proširuje objekt jPanel, u nju stavljamo sve potrebne elemente za rad 
    // algoritma pretrage

    public static class Grid extends JPanel implements ActionListener {

        protected List<Vertex> startEndCells;
        protected List<Vertex> openedCells;
        protected List<Vertex> visitedCells;
        protected Vertex currentCell;
        protected int numOfX = 80;
        protected int numOfY = 50;
        protected int cellSize = 10;
        protected int startX = 0, startY = 0, endX = 0, endY = 0;
        Timer clock = new Timer(20, this);
        protected ArrayList<Vertex> openedNodes = new ArrayList<>(numOfX * numOfY);
        protected ArrayList<Vertex> visitedNodes = new ArrayList<>(numOfX * numOfY);
        protected boolean found = false;
        protected String algorithm = "";

        JTextArea start = new JTextArea();
        JTextArea end = new JTextArea();

        public Grid(String algorithm) {
            startEndCells = new ArrayList<>(2);
            openedCells = new ArrayList<>(numOfX * numOfY);
            visitedCells = new ArrayList<>(numOfX * numOfY);
            Vertex currentCell = null;
            this.algorithm = algorithm;
        }

        // otkuvaj sata omogućuje iscrtavanje mreže (treba neki događaj da bi 
        // se mreža ponovno istrtala)
        @Override
        public void actionPerformed(ActionEvent arg0) {
            // moze elseif-ovi u ovisnosti koji algoritam koristimo
            repaint();
            if (found) {
                clock.stop();
            } else if (algorithm.equals("findPath")) {
                findPath();
            } else if (algorithm.equals("BFS") || algorithm.equals("DFS")) {
                xfs();
            }
        }

        private void paintCell(Vertex cell, Graphics g, Color color){
            if (cell != null) {
                int cellX = cellSize + (cell.getX() * cellSize);
                int cellY = cellSize + (cell.getY() * cellSize);
                g.setColor(color);
                g.fillRect(cellX, cellY, cellSize, cellSize);
            }
        }

        private void paintCells(List<Vertex> cells, Graphics g, Color color){
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

            //bojanje pocetne i zavrsne celije
            paintCells(startEndCells, g, Color.RED);

            // bojenje otvorenih
            paintCells(openedCells, g, Color.BLUE);
            
            //bojanje zatvorenih cvorova
            paintCells(visitedCells, g, Color.MAGENTA);

            //bojanje trenutno otvorenog
            paintCell(currentCell, g, Color.GREEN);

            g.setColor(Color.BLACK);
            g.drawRect(cellSize, cellSize, width, height);

            for (int i = cellSize; i <= width; i += cellSize) {
                g.drawLine(i, cellSize, i, height + cellSize);
            }

            for (int i = cellSize; i <= height; i += cellSize) {
                g.drawLine(cellSize, i, width + cellSize, i);
            }
        }

        // dodavanje početnog i završnog čvora, potrebno za iscrtavanje
        // (početni i završni su crvene boje)
        public void startEndCell(int x, int y) {
            startEndCells.add(new Vertex(x, y));
            repaint();
        }

        // dodavanje otvorenih čvorova
        public void openedCell(int x, int y) {
            openedCells.add(new Vertex(x, y));
            repaint();
        }
        
        public void visitedCell(int x, int y) {
            visitedCells.add(new Vertex(x ,y));
            repaint();
        }
        public void removeOpenedCell( Vertex v ) {
            openedCells.remove(v);
            repaint();
        }

        // algoritam za pretragu, poziva se na otkucaj sata i refresha otvorene čvorove...
        public void findPath() {
            Vertex current = openedNodes.get(0);
            visitedNodes.add(current);
            openedNodes.remove(0);


            visitedCell(current.getX(), current.getY());
            removeOpenedCell(current);
            currentCell = current;

            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {
                    int newX = current.getX() + i;
                    int newY = current.getY() + j;
                    if (newX == endX && newY == endY) {
                        System.out.println("Put pronađen.");
                        found = true;
                        return;
                    }
                    if (newX >= 0 && newY >= 0 && newX < numOfX && newY < numOfY) {
                        Vertex newPoint = new Vertex(newX, newY);
                        if (!visitedNodes.contains(newPoint) && !openedNodes.contains(newPoint)) {
                            openedNodes.add(newPoint);
                            openedCell(newPoint.getX(), newPoint.getY());
                        }
                    }
                }
            }
        }

//      bfs i dfs se razlikuju samo jel uzimamo s početka ili s kraja liste
        public void xfs() {
            int ind = 0;
            if(algorithm.equals("DFS")) ind = openedNodes.size()-1;
            Vertex current = openedNodes.get(ind);
            openedNodes.remove(ind);
            if (visitedNodes.contains(current)) return;
            visitedNodes.add(current);

            openedCell(current.getX(), current.getY());
            currentCell = current;

            int newX = current.getX();
            int newY = current.getY();
            if (newX == endX && newY == endY) {
                System.out.println("Put pronađen.");
                found = true;
                return;
            }

            if(newX - 1 > -1) openedNodes.add(new Vertex(newX - 1, newY));
            if(newY - 1 > -1) openedNodes.add(new Vertex(newX, newY - 1));
            if(newX + 1 < 80) openedNodes.add(new Vertex(newX + 1, newY));
            if(newY + 1 < 50) openedNodes.add(new Vertex(newX, newY + 1));
        }


        // kad kliknemo start čitamo početni i završni čvor i počinje pretraga(sat start)
        public class startButtonPushed implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {

                StringTokenizer st = new StringTokenizer(start.getText());
                int stX = Integer.parseInt(st.nextToken());
                int stY = Integer.parseInt(st.nextToken());

                StringTokenizer ste = new StringTokenizer(end.getText());
                int enX = Integer.parseInt(ste.nextToken());
                int enY = Integer.parseInt(ste.nextToken());

                startX = stX;
                startY = stY;
                endX = enX;
                endY = enY;
                openedNodes.add(new Vertex(startX, startY));

                startEndCell(stX, stY);
                startEndCell(enX, enY);
                clock.start();
                
                
            }
        }
        
        private void pokretanjeWorkera(java.awt.event.ActionEvent evt) {                                         
            if(algorithm.equals("Dijkstra")){
                    //Dio koji poziva SwingWorkera koji pretrazuje graf u zasebnoj dretvi.
                    SwingWorker<Boolean, Vertex> dijsktraSearch = new PathWorker(this); 
                    dijsktraSearch.execute();
            }
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
                startButton.addActionListener(grid.new startButtonPushed());
                startButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    grid.pokretanjeWorkera(evt);
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

                String[] algorithmList = {"findPath", "BFS", "DFS", "A*", "Dijkstra"};
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