package src.main.java;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import javax.swing.JOptionPane;

public class FindPathWorker extends SwingWorker<Boolean, Vertex>{
    
    Grid proc;
    
    FindPathWorker(Grid proc){
        this.proc = proc;
    }
    
    @Override
    protected Boolean doInBackground() throws Exception{
        //Implementacija findPath
        
        while(!proc.found) {
            if(proc.openedCells.isEmpty()) {
                System.out.println("Put nije pronađen!");
                JOptionPane.showMessageDialog(proc, "Put nije pronađen.", "Obavijest", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
            Vertex current = proc.openedCells.get(0);

            proc.visitedCell(current.getX(), current.getY());
            proc.removeOpenedCell(current);
            proc.currentCell = current;

            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {
                    int newX = current.getX() + i;
                    int newY = current.getY() + j;
                    if (newX == proc.endX && newY == proc.endY) {
                        System.out.println("Put pronađen.");
                        proc.found = true;
                        return true;
                    }
                    if (newX >= 0 && newY >= 0 && newX < proc.numOfX && newY < proc.numOfY) {
                        Vertex newPoint = new Vertex(newX, newY);
                        if (!proc.visitedCells.contains(newPoint) && !proc.openedCells.contains(newPoint)
                                && !proc.wallCells.contains(newPoint)) {
                            proc.openedCell(newPoint.getX(), newPoint.getY());
                        }
                    }
                }
            }
        
            //dodajemo sleep radi vizualizacije
            Thread.sleep(50);
        }
        return false;
    }        
    
    @Override
    protected void done(){
        boolean pronadenPut;
        try{
            pronadenPut = get();
            if(pronadenPut) System.out.println("Pronaden put (PathWorker)");
            else System.out.println("Put nije pronaden (PathWorker)");
        }
        catch(InterruptedException e){}
        catch(ExecutionException e){}
    }
    
    @Override
    protected void process(List<Vertex> cvorovi){
    
    }
}
