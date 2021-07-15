package projekt;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import java.util.PriorityQueue;
import java.util.ArrayList;

public class DijkstraWorker extends SwingWorker<Boolean, Vertex>{
    
    Grid proc;
    
    DijkstraWorker(Grid proc){
        this.proc = proc;
    }
    
    @Override
    protected Boolean doInBackground() throws Exception{
        //Implementacija Dijkstra
   
        int numOfX = 80;
        int numOfY = 50;
        PriorityQueue<ComparableVertex> Q = new PriorityQueue<>(numOfX * numOfY);
        Vertex start = new Vertex(proc.startX, proc.startY);
        ComparableVertex compStart = new ComparableVertex(start, 0, false);
        
        for(int i = 0; i < numOfX; ++i) {
            for(int j = 0; j < numOfY; ++j) {
                if(new Vertex(i,j) != start) {
                    Q.add(new ComparableVertex(new Vertex(i,j), 0, true));
                }
            }
        }
        Q.add(compStart);
        
        boolean isStartNode = true;
        
        while(!Q.isEmpty()) {
            ComparableVertex min = Q.poll();
            
            if(min.isInfinity()) {
                System.out.println("Put nije pronađen!");
                JOptionPane.showMessageDialog(proc, "Put nije pronađen.", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            
            if(proc.wallCells.contains(min.getVertex())) continue;
            
            if(min.getX() == proc.endX && min.getY() == proc.endY) {
                System.out.println("Put pronađen.");
                proc.found = true;
                return true;
            }
            
            if(!isStartNode) {
                proc.visitedCell(min.getX(), min.getY());
                proc.removeOpenedCell(min.getVertex());
            }
            isStartNode = false;
            
            ArrayList<ComparableVertex> forRemove = new ArrayList<ComparableVertex>();
            ArrayList<ComparableVertex> forAdd = new ArrayList<ComparableVertex>();
            
            
            for(ComparableVertex v : Q) {
                // tražimo sve susjede od min
                if(( v.getX() == min.getX() - 1 && v.getY() == min.getY() ) || 
                    ( v.getX() == min.getX() + 1 && v.getY() == min.getY() ) ||
                     ( v.getX() == min.getX() && v.getY() == min.getY() - 1 ) ||
                      (v.getX() == min.getX() && v.getY() == min.getY() + 1)) {
                    int alt = min.getLength() + 1;
                            
                    if(v.isInfinity() || alt < v.getLength()) {
                        ComparableVertex n = new ComparableVertex(v.getVertex(), alt, false);                     
                        n.setPrevious(min.getVertex());
                        forRemove.add(v);
                        forAdd.add(n);
                    }
                }
            }
            for(ComparableVertex v : forRemove) {
                Q.remove(v);
            }
            for(ComparableVertex v : forAdd) {
                proc.openedCell(v.getX(),v.getY());
                Q.add(v);
            }
            
            Thread.sleep(30);
        }
        return false;
    }        
    
    @Override
    protected void done(){
        boolean pronadenPut;
        try{
            pronadenPut = get();
            if(pronadenPut) System.out.println("Pronaden put (DijkstraWorker)");
            else System.out.println("Put nije pronaden (DijkstraWorker)");
        }
        catch(InterruptedException e){}
        catch(ExecutionException e){}
    }
    
    @Override
    protected void process(List<Vertex> cvorovi){
    
    }
}
