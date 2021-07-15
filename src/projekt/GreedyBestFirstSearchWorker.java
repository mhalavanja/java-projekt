package projekt;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class GreedyBestFirstSearchWorker extends SwingWorker<Boolean, Vertex>{
    
    Grid proc;
    
    GreedyBestFirstSearchWorker(Grid proc){
        this.proc = proc;
    }
    
    private double calcEuclideanDistance(Vertex v, Vertex w){
        double tmp1 = Math.pow(v.getX() - w.getX(), 2);
        double tmp2 = Math.pow(v.getY() - w.getY(), 2);
        return Math.sqrt(tmp1 + tmp2);
    }
    
    @Override
    protected Boolean doInBackground() throws Exception{
        //Implementacija "Najboljeg prvog" - Greedy best first search algoritma.
        
        //Na pocetku je u listi otvorenih vrhova samo pocetni cvor
        Vertex endNode = new Vertex(proc.endX, proc.endY);
        Vertex current = proc.openedNodes.get(0);
        //racunamo h vrijednost (euklidska udaljenost) od pocetnog do krajnjeg vrha
        current.setH(calcEuclideanDistance(current, endNode));
        
        
        while(!proc.openedNodes.isEmpty()){
            
            //Trebamo izabrati otvoren vrh s najmanjom heuristickom vrijednosti procejene udaljenosti. 
            //Bolje korisiti prioritetni red.
            double minDist = Double.MAX_VALUE;
            //uzimamo src.main.java.Vertex s najmanjom  procjenom ostatka puta
            for(Vertex v : proc.openedNodes){
                double tmp = v.getH();
                if(tmp <= minDist){
                    minDist = tmp;
                    current = v;
                }
            }
            //ako smo pronasli rjesenje vrati true
            if(current.equals(endNode))
                return true;
            
            //oznaka closed ce sluziti u process funkciji koja ce ubaciti current u listu zatvorenih cvorova
            //current.setTag("closed");
            //publish(current);
            proc.visitedNodes.add(current);
            proc.visitedCell(current.getX(), current.getY());
            proc.removeOpenedCell(current);
            proc.openedNodes.remove(current);
            
            int[] pomakX = {1, -1, 0, 0};
            int[] pomakY = {0, 0, 1, -1};
            
            for(int i = 0; i < pomakX.length; ++i){
                int newX = current.getX() + pomakX[i];
                int newY = current.getY() + pomakY[i];
                
                if (newX >= 0 && newY >= 0 && newX < proc.numOfX && newY < proc.numOfY) {
                    
                    Vertex newPoint = new Vertex(newX, newY);
                    if(proc.wallCells.contains(newPoint))
                        continue;
                    if (!proc.visitedNodes.contains(newPoint) && !proc.openedNodes.contains(newPoint)) {
                        
                        //newPoint je na putu za 1 udaljeniji od prethodnika current (tezine pomaka u mrezi su 1)
                        newPoint.setG( current.getG() + 1 );
                        newPoint.setH( calcEuclideanDistance(newPoint, endNode) );
                    
                            
                        proc.openedNodes.add(newPoint);
                        //proc.openedCell(newPoint.getX(), newPoint.getY());
                        proc.addOpenedCell(newPoint);

                    }
                }
            }
        
            //dodajemo sleep radi vizualizacije
            Thread.sleep(30);
        }
        
        return false;
    }        
    
    @Override
    protected void done(){
        boolean pronadenPut;
        try{
            pronadenPut = get();
            if(pronadenPut) System.out.println("Pronaden put (src.main.java.GreedyBestFirstSearchWorker)");
            else System.out.println("Put nije pronaden (src.main.java.GreedyBestFirstSearchWorker)");
        }
        catch(InterruptedException e){}
        catch(ExecutionException e){}
    }
    
    @Override
    protected void process(List<Vertex> cvorovi){
    
    }
}
