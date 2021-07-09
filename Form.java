import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.util.*;
import javax.swing.JLabel;
/**
 *
 * @author Korisnik
 */
public class Form extends javax.swing.JFrame {

    /**
     * Creates new form Form
     */
    public Form() {
        initComponents();
    }
    
    // mreža proširuje objekt jPanel, u nju stavljamo sve potrebne elemente za rad 
    // algoritma pretrage
    
    public static class Grid extends JPanel implements ActionListener {

        private List<Point> startEndCells;
        private List<Point> openedCells;
        private List<Point> currentCells;
        int startX = 0, startY = 0, endX = 0, endY = 0;
        Timer clock = new Timer(20,this);
        ArrayList<Point> openedNodes = new ArrayList<>(80*80);
        ArrayList<Point> visitedNodes = new ArrayList<>(80*80);
        
        boolean found = false;
        
        //
        JTextArea start = new JTextArea();
        JTextArea end = new JTextArea();

        

        public Grid() {
            startEndCells = new ArrayList<>(25);
            openedCells = new ArrayList<>(80*80);
            currentCells = new ArrayList<>();
            //clock.start();
        }
        
        // otkuvaj sata omogućuje iscrtavanje mreže (treba neki događaj da bi 
        // se mreža ponovno istrtala)
        @Override
        public void actionPerformed(ActionEvent arg0) {
            // moze elseif-ovi u ovisnosti koji algoritam koristimo
            repaint();
            if(found){
                clock.stop();
            }
            else {
                findPath();
            }
        }
        // funkcije koja crza mrežu (linije, boja kvadratiće)
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Point fillCell : startEndCells) {
                int cellX = 10 + (fillCell.x * 10);
                int cellY = 10 + (fillCell.y * 10);
                g.setColor(Color.RED);
                g.fillRect(cellX, cellY, 10, 10);
            }
            // bojenje otvorenih
            for (Point fillCell : openedCells) {
                int cellX = 10 + (fillCell.x * 10);
                int cellY = 10 + (fillCell.y * 10);
                g.setColor(Color.BLUE);
                g.fillRect(cellX, cellY, 10, 10);
            }
            //bojanje trenutno otvorenih
             for (Point fillCell : currentCells) {
                int cellX = 10 + (fillCell.x * 10);
                int cellY = 10 + (fillCell.y * 10);
                g.setColor(Color.GREEN);
                g.fillRect(cellX, cellY, 10, 10);
            }
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 800, 500);

            for (int i = 10; i <= 800; i += 10) {
                g.drawLine(i, 10, i, 510);
            }

            for (int i = 10; i <= 500; i += 10) {
                g.drawLine(10, i, 810, i);
            }
        }
        // dodavanje početnog i završnog čvora, potrebno za iscrtavanje
        // (početni i završni su crvene boje)
        public void startEndCell(int x, int y) {
            startEndCells.add(new Point(x, y));
            repaint();
        }
        // dodavanje otvorenih čvorova
        public void openedCell(int x, int y) {
            openedCells.add(new Point(x,y));
            repaint();
        }
        // algoritam za pretragu, poziva se na otkucaj sata i refresha otvorene čvorove...
        public void findPath() {
            currentCells.clear();
            final int minCellIndex = 0;
            final int maxCellIndex = 80;
        
            Point current = openedNodes.get(0);
            visitedNodes.add(current);
            openedNodes.remove(0);
        

            openedCell(current.x, current.y);
            currentCells.add(current);
            
            for(int i = -1; i < 2; ++i) {
                for(int j = -1; j < 2; ++j) {
                    int newX = current.x + i;
                    int newY = current.y + j;
                        if(newX == endX && newY == endY) {
                        System.out.println("Put pronađen.");
                        found = true;
                        return;
                    }
                    if(newX >= minCellIndex && newX < maxCellIndex &&
                      newY >= minCellIndex && newX < maxCellIndex) {
                        Point newPoint = new Point(newX, newY);
                        if(!visitedNodes.contains(newPoint) && !openedNodes.contains(newPoint)) {
                            openedNodes.add(newPoint);
                        }
                    }
                }
            }
        }
        
        // kad kliknemo start čitamo početni i završni čvor i počinje pretraga(sat start)
        public class startButtonPushed implements ActionListener {
        
            @Override
            public void actionPerformed ( ActionEvent event ) {
                
                StringTokenizer st = new StringTokenizer(start.getText());
                Integer stX = Integer.parseInt(st.nextToken());
                Integer stY = Integer.parseInt(st.nextToken());
                
                StringTokenizer ste = new StringTokenizer(end.getText());
                Integer enX = Integer.parseInt(ste.nextToken());
                Integer enY = Integer.parseInt(ste.nextToken());
                
                startX = stX; startY = stY; endX = enX; endY = enY;
                openedNodes.add(new Point(startX,startY));
                
                startEndCell(stX, stY);
                startEndCell(enX,enY);
                clock.start();
            }
        } 
        
        // nova pretraga -- čistimo sva potrebna polja i iscrtavamo ponovno mrežu
        public class newButtonPushed implements ActionListener {
        
            @Override
            public void actionPerformed ( ActionEvent event ) {
                
                startEndCells.clear();
                openedCells.clear();
                openedNodes.clear();
                visitedNodes.clear();
                found = false;
                repaint();
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
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                // inicijalizacija prozora i ubacivanje svih potrebnih elemenata
                Grid grid = new Grid();
                JFrame window = new JFrame();
                window.setSize(1000, 600); //560
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

                window.add(menu, BorderLayout.SOUTH);
                window.setVisible(true);
            }
        });
    }

    public static class graphButtonPushed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public static class saveButtonPushed implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
