package projekt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

// mreža proširuje objekt jPanel, u nju stavljamo sve potrebne elemente za rad 
// algoritma pretrage
public class Grid extends JPanel{

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
        
        JTextArea start = new JTextArea();
        JTextArea end = new JTextArea();
        
        

        public Grid(String algorithm) {
            startEndCells = new ArrayList<>(2);
            openedCells = new ArrayList<>(numOfX * numOfY);
            visitedCells = new ArrayList<>(numOfX * numOfY);
            wallCells = new ArrayList<>(numOfX * numOfY);
            Vertex currentCell = null;
            this.algorithm = algorithm;
          
            start.setName("start");
            end.setName("end");
            
            
            MouseAdapter handler = new MouseAdapter() { 
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)){
                        if(e.getX()/cellSize -1 >= 0 && e.getX()/cellSize -1 < numOfX &&
                                e.getY()/cellSize -1 >= 0 && e.getY()/cellSize -1 < numOfY) {
                            
                            int positionX = e.getX()/cellSize -1;
                            int positionY = e.getY()/cellSize -1;
                            
                            //Oznacili smo novi zid/prepreku.
                            wallCell(positionX, positionY);
                            
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
            
        }
       
        
        public boolean isFound(){
            return found;
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

        //Implementiramo realizaciju pritiska gumba start kada zelimo prikazati jedan od algoritama.
        protected void startButtonPushedVisualization(java.awt.event.ActionEvent evt) {
            
            if(checkInputCoordinates(start.getText(), end.getText())){
                StringTokenizer st = new StringTokenizer(start.getText());
                startX = Integer.parseInt(st.nextToken());
                startY = Integer.parseInt(st.nextToken());
            
                StringTokenizer ste = new StringTokenizer(end.getText());
                endX = Integer.parseInt(ste.nextToken());
                endY = Integer.parseInt(ste.nextToken());
            }
            else{
                JOptionPane.showMessageDialog(this, "Neispravan unos koordinata početka i kraja pretrage!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                start.setText("");
                end.setText("");
                return;
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
            else if(algorithm.equals("Dijkstra")){
                SwingWorker<Boolean, Vertex> dijkstraSearch = new DijkstraWorker(this);
                dijkstraSearch.execute(); 
            }
            //dodati ovdje pokretanje workera za ostale algoritme
        }
        
        //Implementacija pokretanja svih algoritama i spremanja u bazu podataka.
        protected void startButtonPushedRunAll(ActionEvent evt){
             //ako ne unesemo nista u tekstualni dio, mogli smo oznaciti s misom i tiupkom 's' start
            if(checkInputCoordinates(start.getText(), end.getText())){
                StringTokenizer st = new StringTokenizer(start.getText());
                startX = Integer.parseInt(st.nextToken());
                startY = Integer.parseInt(st.nextToken());
            
                StringTokenizer ste = new StringTokenizer(end.getText());
                endX = Integer.parseInt(ste.nextToken());
                endY = Integer.parseInt(ste.nextToken());
            }
            else{
                JOptionPane.showMessageDialog(this, "Neispravan unos koordinata početka i kraja pretrage!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                start.setText("");
                end.setText("");
                return;
            }
            
            openedNodes.add(new Vertex(startX, startY)); // još u a* je vjj koristeno
            openedCells.add(new Vertex(startX, startY));
            
            ArrayList<Integer> otvoreni = new ArrayList<>(); 
            ArrayList<Integer> zatvoreni = new ArrayList<>(); 
            ArrayList<String> algoritmi = new ArrayList<>(); 
            
            InfoAlgorithm info = AllAlgorithms.findPathAlgorithm(this);
            otvoreni.add(info.getBrojOtvorenih());
            zatvoreni.add(info.getBrojZatvorenih());
            algoritmi.add(info.getAlgorithmName());
            System.out.println(info.getAlgorithmName());
            
            info = AllAlgorithms.bfsAlgorithm(this);
            otvoreni.add(info.getBrojOtvorenih());
            zatvoreni.add(info.getBrojZatvorenih());
            algoritmi.add(info.getAlgorithmName());
            System.out.println(info.getAlgorithmName());
            
            info = AllAlgorithms.dfsAlgorithm(this);
            otvoreni.add(info.getBrojOtvorenih());
            zatvoreni.add(info.getBrojZatvorenih());
            algoritmi.add(info.getAlgorithmName());
            System.out.println(info.getAlgorithmName());
            
            info = AllAlgorithms.aStarAlgorithm(this);
            otvoreni.add(info.getBrojOtvorenih());
            zatvoreni.add(info.getBrojZatvorenih());
            algoritmi.add(info.getAlgorithmName());
            System.out.println(info.getAlgorithmName());
            
            info = AllAlgorithms.dijkstraAlgorithm(this);
            otvoreni.add(info.getBrojOtvorenih());
            zatvoreni.add(info.getBrojZatvorenih());
            algoritmi.add(info.getAlgorithmName());
            System.out.println(info.getAlgorithmName());
            
            info = AllAlgorithms.greedyBestFirstSearchAlgorithm(this);
            otvoreni.add(info.getBrojOtvorenih());
            zatvoreni.add(info.getBrojZatvorenih());
            algoritmi.add(info.getAlgorithmName());
            System.out.println(info.getAlgorithmName());
            
            GraphForm gf = new GraphForm(otvoreni, zatvoreni, algoritmi);
            gf.setVisible(true);
        }
        
        //Provjerava unos podataka/koordinata za pocetak i kraj pretrage.
        protected boolean checkInputCoordinates(String startString, String endString){
            if(startString == null || endString == null)
                return false;
            
            //startString i endString moraju biti  oblika "integer integer"
            //ukoliko nije takav oblik dobit cemo iznimku koju hvatamo u catch dijelu
            try{
                StringTokenizer st1 = new StringTokenizer(startString);
                StringTokenizer st2 = new StringTokenizer(endString);
                int[] granice = {numOfX, numOfY};
                for(int i = 0; i < 2; ++i){
                    if(Integer.parseInt(st1.nextToken()) >= granice[i])
                        return false;
                    if(Integer.parseInt(st2.nextToken()) >= granice[i]);
                }
            }
            catch(NumberFormatException e){
                return false;
            }
            catch(Exception e){
                return false;
            }
            
            return true;
        }

        // nova pretraga -- čistimo sva potrebna polja i iscrtavamo ponovno mrežu
        public class newButtonPushed implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {

                startEndCells = new ArrayList<>();
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

        //Za razliku od new, clear metoda ostavlja oznacene zidove, te pocetak i kraj.
        public class clearButtonPushed implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent event){
                openedCells.clear();
                openedNodes.clear();
                visitedNodes.clear();
                visitedCells.clear();
                found = false;
                currentCell = null;
                repaint();
            }
        }
        
        
    }
