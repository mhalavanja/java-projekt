/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Korisnik
 */
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class FindPathWorker extends SwingWorker<Boolean, Vertex>{
    
    Form.Grid proc;
    
    FindPathWorker(Form.Grid proc){
        this.proc = proc;
    }
    
    @Override
    protected Boolean doInBackground() throws Exception{
        //Implementacija findPath
        
        while(!proc.found) {
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
                        if (!proc.visitedCells.contains(newPoint) && !proc.openedCells.contains(newPoint)) {
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
