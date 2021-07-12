package projekt;

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

public class XfsPathWorker extends SwingWorker<Boolean, Vertex>{
    
    Form.Grid proc;
    
    XfsPathWorker(Form.Grid proc){
        this.proc = proc;
    }
    
    @Override
    protected Boolean doInBackground() throws Exception{
        //Implementacija bsf, dfs
        
        while(!proc.found) {
            //bfs i dfs se razlikuju samo jel uzimamo s početka ili s kraja liste
            int ind = 0;
            if (proc.algorithm.equals("DFS")) ind = proc.openedCells.size() - 1;
            Vertex current = proc.openedCells.get(ind);
            proc.openedCells.remove(ind);
            if (proc.visitedCells.contains(current)) continue;
            proc.visitedCells.add(current);

            //proc.openedCell(current.getX(), current.getY());
            proc.currentCell = current;

            proc.visitedCell(current.getX(), current.getY());
            proc.removeOpenedCell(current);

            int newX = current.getX();
            int newY = current.getY();
            if (newX == proc.endX && newY == proc.endY) {
                System.out.println("Put pronađen.");
                proc.found = true;
                return true;
            }

            if (newX - 1 > -1) {
                Vertex v = new Vertex(newX - 1, newY);
                proc.openedCell(v.getX(), v.getY());
            }
            if (newY - 1 > -1) {
                Vertex v = new Vertex(newX, newY - 1);
                proc.openedCell(v.getX(), v.getY());
            }
            if (newX + 1 < 80) {
                Vertex v = new Vertex(newX + 1, newY);
                proc.openedCell(v.getX(), v.getY());
            }
            if (newY + 1 < 50) {
                Vertex v = new Vertex(newX, newY + 1);
                proc.openedCell(v.getX(), v.getY());
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
