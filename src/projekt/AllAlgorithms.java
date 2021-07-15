package projekt;

//Ova klasa sadrzi sve implementacije algoritama.

import java.util.ArrayList;

//Koristimo ju iskljucivo kada hocemo usporediti algoritme, nikad kod vizualizacije u grafickom sucelju.
//Ukoliko ne pronademo put mozemo vratiti nesto primjerice new InfoAlgorithm(0,0,"NOtfounded");
public class AllAlgorithms {
    
    public static InfoAlgorithm findPathAlgorithm(Grid proc){
        //implementacija
        //probna random povratna vrijednost
        return new InfoAlgorithm(3, 5, "findPath");
    }
    
    public static InfoAlgorithm greedyBestFirstSearchAlgorithm(Grid proc){
        //Implementacija "Najboljeg prvog" - Greedy best first search algoritma.
        
        //Koristit cemo lokalne varijeble koje trebamo mijenjati, posto ne radimo vise
        //vizualizaciju, te ih svaki algoritam treba imati zasebno.
        ArrayList<Vertex> mOpenedNodes = new ArrayList<>();
        ArrayList<Vertex> mVisitedNodes = new ArrayList<>();
        
        for(int i = 0; i < proc.openedNodes.size(); ++i)
            mOpenedNodes.add( new Vertex(proc.openedNodes.get(i).getX(), proc.openedNodes.get(i).getY()) );
        for(int i = 0; i < proc.visitedNodes.size(); ++i)
            mVisitedNodes.add( new Vertex(proc.visitedNodes.get(i).getX(), proc.visitedNodes.get(i).getY()) );
        
        
        //Na pocetku je u listi otvorenih vrhova samo pocetni cvor
        Vertex endNode = new Vertex(proc.endX, proc.endY);
        Vertex current = mOpenedNodes.get(0);
        //racunamo h vrijednost (euklidska udaljenost) od pocetnog do krajnjeg vrha
        current.setH(calcEuclideanDistance(current, endNode));
        
        
        while(!mOpenedNodes.isEmpty()){
            
            //Trebamo izabrati otvoren vrh s najmanjom heuristickom vrijednosti procejene udaljenosti. 
            //Bolje korisiti prioritetni red.
            double minDist = Double.MAX_VALUE;
            //uzimamo src.main.java.Vertex s najmanjom  procjenom ostatka puta
            for(Vertex v : mOpenedNodes){
                double tmp = v.getH();
                if(tmp <= minDist){
                    minDist = tmp;
                    current = v;
                }
            }
            //ako smo pronasli rjesenje vrati true
            if(current.equals(endNode))
                return new InfoAlgorithm(mOpenedNodes.size(), mVisitedNodes.size(), "GreedySearch");
            
            //oznaka closed ce sluziti u process funkciji koja ce ubaciti current u listu zatvorenih cvorova
            mVisitedNodes.add(current);
            mOpenedNodes.remove(current);
            
            int[] pomakX = {1, -1, 0, 0};
            int[] pomakY = {0, 0, 1, -1};
            
            for(int i = 0; i < pomakX.length; ++i){
                int newX = current.getX() + pomakX[i];
                int newY = current.getY() + pomakY[i];
                
                if (newX >= 0 && newY >= 0 && newX < proc.numOfX && newY < proc.numOfY) {
                    
                    Vertex newPoint = new Vertex(newX, newY);
                    if(proc.wallCells.contains(newPoint))
                        continue;
                    if (!mVisitedNodes.contains(newPoint) && !mOpenedNodes.contains(newPoint)) {
                        
                        //newPoint je na putu za 1 udaljeniji od prethodnika current (tezine pomaka u mrezi su 1)
                        newPoint.setG( current.getG() + 1 );
                        newPoint.setH( calcEuclideanDistance(newPoint, endNode) );
                    
                            
                        mOpenedNodes.add(newPoint);
                    }
                }
            }
        
        }
        
        return new InfoAlgorithm(10, 10, "GNOT");
    }
    
    public static InfoAlgorithm dijkstraAlgorithm(Grid proc){
        //implementacija
        //probna random povratna vrijednost
        return new InfoAlgorithm(5, 12, "Dijkstra");
    }
    
    public static InfoAlgorithm aStarAlgorithm(Grid proc){
        //Implementacija A* algoritma.
        
        //Koristit cemo lokalne varijeble koje trebamo mijenjati, posto ne radimo vise
        //vizualizaciju, te ih svaki algoritam treba imati zasebno.
        ArrayList<Vertex> mOpenedNodes = new ArrayList<>();
        ArrayList<Vertex> mVisitedNodes = new ArrayList<>();
        
        for(int i = 0; i < proc.openedNodes.size(); ++i)
            mOpenedNodes.add( new Vertex(proc.openedNodes.get(i).getX(), proc.openedNodes.get(i).getY()) );
        for(int i = 0; i < proc.visitedNodes.size(); ++i)
            mVisitedNodes.add( new Vertex(proc.visitedNodes.get(i).getX(), proc.visitedNodes.get(i).getY()) );
        
       
        //Na pocetku je u listi otvorenih vrhova samo pocetni cvor
        Vertex endNode = new Vertex(proc.endX, proc.endY);
        Vertex current = proc.openedNodes.get(0);
        //racunamo h vrijednost (euklidska udaljenost) od pocetnog do krajnjeg vrha
        current.setH(calcEuclideanDistance(current, endNode));
        
        
        while(!mOpenedNodes.isEmpty()){
            
            //Trebamo izabrati otvoren vrh s najmanjom g+h vrijednosti. 
            //Bolje korisiti prioritetni red.
            double minDist = Double.MAX_VALUE;
            //uzimamo src.main.java.Vertex s najmanjom sumom dosadasnjeg puta i procjene ostatka
            for(Vertex v : mOpenedNodes){
                double tmp = v.getG() + v.getH();
                if(tmp <= minDist){
                    minDist = tmp;
                    current = v;
                }
            }
            //ako smo pronasli rjesenje vrati true
            if(current.equals(endNode))
                return new InfoAlgorithm(mOpenedNodes.size(), mVisitedNodes.size(), "AStar");
            
            //oznaka closed ce sluziti u process funkciji koja ce ubaciti current u listu zatvorenih cvorova
            //current.setTag("closed");
            //publish(current);
            mVisitedNodes.add(current);
            mOpenedNodes.remove(current);
            
            int[] pomakX = {1, -1, 0, 0};
            int[] pomakY = {0, 0, 1, -1};
            
            for(int i = 0; i < pomakX.length; ++i){
                int newX = current.getX() + pomakX[i];
                int newY = current.getY() + pomakY[i];
                
                if (newX >= 0 && newY >= 0 && newX < proc.numOfX && newY < proc.numOfY) {
                    
                    Vertex newPoint = new Vertex(newX, newY);
                    if(proc.wallCells.contains(newPoint))
                        continue;
                    if (!mVisitedNodes.contains(newPoint)) {
                        
                        //newPoint je na putu za 1 udaljeniji od prethodnika current (tezine pomaka u mrezi su 1)
                        newPoint.setG( current.getG() + 1 );
                        newPoint.setH( calcEuclideanDistance(newPoint, endNode) );
                    
                        if(mOpenedNodes.contains(newPoint)){
                            int indeks = mOpenedNodes.indexOf(newPoint);
                            Vertex point = mOpenedNodes.get(indeks);
                            
                            if(point.getG() > newPoint.getG()){
                                point.setG( newPoint.getG() );
                                point.setH( newPoint.getH() );
                            }                        }
                        else{
                            
                            mOpenedNodes.add(newPoint);
                        }
                    }
                }
            }
        
        }
        return new InfoAlgorithm(10, 10, "NOT");
    }
    
    public static InfoAlgorithm bfsAlgorithm(Grid proc){
        //implementacija
        //probna random povratna vrijednost
        return new InfoAlgorithm(13, 12, "BFS");
    }
    
    public static InfoAlgorithm dfsAlgorithm(Grid proc){
        //implementacija
        //probna random povratna vrijednost
        return new InfoAlgorithm(10, 14, "DFS");
    }
    
    
    //pomocne funkcije
    private static double calcEuclideanDistance(Vertex v, Vertex w){
        double tmp1 = Math.pow(v.getX() - w.getX(), 2);
        double tmp2 = Math.pow(v.getY() - w.getY(), 2);
        return Math.sqrt(tmp1 + tmp2);
    }
}
