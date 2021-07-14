
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class AStarWorker extends SwingWorker<Boolean, Vertex>{
    
    Grid proc;
    
    AStarWorker(Grid proc){
        this.proc = proc;
    }
    
    private double calcEuclideanDistance(Vertex v, Vertex w){
        double tmp1 = Math.pow(v.getX() - w.getX(), 2);
        double tmp2 = Math.pow(v.getY() - w.getY(), 2);
        return Math.sqrt(tmp1 + tmp2);
    }
    
    @Override
    protected Boolean doInBackground() throws Exception{
        //Implementacija A* algoritma.
        
        //Na pocetku je u listi otvorenih vrhova samo pocetni cvor
        Vertex endNode = new Vertex(proc.endX, proc.endY);
        Vertex current = proc.openedNodes.get(0);
        //racunamo h vrijednost (euklidska udaljenost) od pocetnog do krajnjeg vrha
        current.setH(calcEuclideanDistance(current, endNode));
        
        
        while(!proc.openedNodes.isEmpty()){
            
            //Trebamo izabrati otvoren vrh s najmanjom g+h vrijednosti. 
            //Bolje korisiti prioritetni red.
            double minDist = Double.MAX_VALUE;
            //uzimamo Vertex s najmanjom sumom dosadasnjeg puta i procjene ostatka
            for(Vertex v : proc.openedNodes){
                double tmp = v.getG() + v.getH();
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
                    if (!proc.visitedNodes.contains(newPoint)) {
                        
                        //newPoint je na putu za 1 udaljeniji od prethodnika current (tezine pomaka u mrezi su 1)
                        newPoint.setG( current.getG() + 1 );
                        newPoint.setH( calcEuclideanDistance(newPoint, endNode) );
                    
                        if(proc.openedNodes.contains(newPoint)){
                            int indeks = proc.openedNodes.indexOf(newPoint);
                            Vertex point = proc.openedNodes.get(indeks);
                            
                            if(point.getG() > newPoint.getG()){
                                point.setG( newPoint.getG() );
                                point.setH( newPoint.getH() );
                            }                        }
                        else{
                            //newPoint.setTag("opened");
                            //publish(newPoint);
                            
                            proc.openedNodes.add(newPoint);
                            //proc.openedCell(newPoint.getX(), newPoint.getY());
                            proc.addOpenedCell(newPoint);
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
            if(pronadenPut) System.out.println("Pronaden put (AStarWorker)");
            else System.out.println("Put nije pronaden (AStarWorker)");
        }
        catch(InterruptedException e){}
        catch(ExecutionException e){}
    }
    
    @Override
    protected void process(List<Vertex> cvorovi){
    
    }
}
