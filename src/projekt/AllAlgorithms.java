package projekt;

//Ova klasa sadrzi sve implementacije algoritama.

import java.util.ArrayList;
import java.util.PriorityQueue;

//Koristimo ju iskljucivo kada hocemo usporediti algoritme, nikad kod vizualizacije u grafickom sucelju.
//Ukoliko ne pronademo put mozemo vratiti nesto primjerice new InfoAlgorithm(0,0,"NOtfounded");
public class AllAlgorithms {
    
    public static InfoAlgorithm findPathAlgorithm(Grid proc){
        // Implementacija findPath algoritma.
        ArrayList<Vertex> mOpenedNodes = new ArrayList<>();
        ArrayList<Vertex> mVisitedNodes = new ArrayList<>();
        
        mOpenedNodes.add(new Vertex(proc.startX,proc.startY));
        
        while(true) {
            if(mOpenedNodes.isEmpty()) {
                return new InfoAlgorithm(0, 0, "notFoundFP");
            }
            Vertex current = mOpenedNodes.get(0);

            mVisitedNodes.add(current);
            mOpenedNodes.remove(current);

            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {
                    int newX = current.getX() + i;
                    int newY = current.getY() + j;
                    if (newX == proc.endX && newY == proc.endY) {
                        return new InfoAlgorithm(mOpenedNodes.size(), mVisitedNodes.size(), "FindPath");
                    }
                    if (newX >= 0 && newY >= 0 && newX < proc.numOfX && newY < proc.numOfY) {
                        Vertex newPoint = new Vertex(newX, newY);
                        if (!mVisitedNodes.contains(newPoint) && !mOpenedNodes.contains(newPoint)
                                && !proc.wallCells.contains(newPoint)) {
                            mOpenedNodes.add(newPoint);
                        }
                    }
                }
            }
        }
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
        
        return new InfoAlgorithm(0, 0, "notFoundGreedy");
    }
    
    public static InfoAlgorithm dijkstraAlgorithm(Grid proc){
        // Implementacija Dijkstrinog algoritma.
        ArrayList<Vertex> mOpenedNodes = new ArrayList<>();
        ArrayList<Vertex> mVisitedNodes = new ArrayList<>();
        
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
                return new InfoAlgorithm(0, 0, "notFoundDijkstra");
            }
            
            if(proc.wallCells.contains(min.getVertex())) continue;
            
            if(min.getX() == proc.endX && min.getY() == proc.endY) {
                System.out.println("Put pronađen.");
                return new InfoAlgorithm(mOpenedNodes.size(), mVisitedNodes.size(), "Dijkstra");
            }
            
            if(!isStartNode) {
                mVisitedNodes.add(min.getVertex());
                mOpenedNodes.remove(min.getVertex());
            }
            isStartNode = false;
            
            ArrayList<ComparableVertex> forRemove = new ArrayList<>();
            ArrayList<ComparableVertex> forAdd = new ArrayList<>();
            
            
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
                mOpenedNodes.add(v.getVertex());
                Q.add(v);
            }
        }
        return new InfoAlgorithm(0, 0, "notFoundDijkstra");
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
        return new InfoAlgorithm(0, 0, "notFoundAStar");
    }
    
    public static InfoAlgorithm bfsAlgorithm(Grid proc){
        // Implementacija BFS algoritma.
        ArrayList<Vertex> mOpenedNodes = new ArrayList<>();
        ArrayList<Vertex> mVisitedNodes = new ArrayList<>();
        
        mOpenedNodes.add(new Vertex(proc.startX,proc.startY));
        
        while(true) {
            if(mOpenedNodes.isEmpty()) {
                return new InfoAlgorithm(0, 0, "notFoundBFS");
            }
            //bfs i dfs se razlikuju samo jel uzimamo s početka ili s kraja liste
            Vertex current = mOpenedNodes.get(0);
            
            mVisitedNodes.add(current);
            mOpenedNodes.remove(current);

            int newX = current.getX();
            int newY = current.getY();
            if (newX == proc.endX && newY == proc.endY) {
                return new InfoAlgorithm(mOpenedNodes.size(), mVisitedNodes.size(), "BFS");
            }

            if (newX - 1 > -1) {
                Vertex v = new Vertex(newX - 1, newY);
                if(!proc.wallCells.contains(v) && !mVisitedNodes.contains(v) 
                        && !mOpenedNodes.contains(v)){
                    mOpenedNodes.add(v);
                }
            }
            if (newY - 1 > -1) {
                Vertex v = new Vertex(newX, newY - 1);
                if(!proc.wallCells.contains(v) && !mVisitedNodes.contains(v) 
                        && !mOpenedNodes.contains(v)) {
                    mOpenedNodes.add(v);
                }
            }
            if (newX + 1 < 80) {
                Vertex v = new Vertex(newX + 1, newY);
                if(!proc.wallCells.contains(v) && !mVisitedNodes.contains(v) 
                        && !mOpenedNodes.contains(v)) {
                    mOpenedNodes.add(v);
                }
            }
            if (newY + 1 < 50) {
                Vertex v = new Vertex(newX, newY + 1);
                if(!proc.wallCells.contains(v) && !mVisitedNodes.contains(v) 
                        && !mOpenedNodes.contains(v)) {
                    mOpenedNodes.add(v);
                }
            }
        }
    }
    
    public static InfoAlgorithm dfsAlgorithm(Grid proc){
        // Implementacija DFS algoritma.
        ArrayList<Vertex> mOpenedNodes = new ArrayList<>();
        ArrayList<Vertex> mVisitedNodes = new ArrayList<>();
        
        mOpenedNodes.add(new Vertex(proc.startX,proc.startY));
        
        while(true) {
            if(mOpenedNodes.isEmpty()) {
                return new InfoAlgorithm(0, 0, "notFoundDFS");
            }
            //bfs i dfs se razlikuju samo jel uzimamo s početka ili s kraja liste
            int ind = mOpenedNodes.size() - 1;
            Vertex current = mOpenedNodes.get(ind);
            
            mVisitedNodes.add(current);
            mOpenedNodes.remove(current);

            int newX = current.getX();
            int newY = current.getY();
            if (newX == proc.endX && newY == proc.endY) {
                return new InfoAlgorithm(mOpenedNodes.size(), mVisitedNodes.size(), "DFS");
            }

            if (newX - 1 > -1) {
                Vertex v = new Vertex(newX - 1, newY);
                if(!proc.wallCells.contains(v) && !mVisitedNodes.contains(v) 
                        && !mOpenedNodes.contains(v)){
                    mOpenedNodes.add(v);
                }
            }
            if (newY - 1 > -1) {
                Vertex v = new Vertex(newX, newY - 1);
                if(!proc.wallCells.contains(v) && !mVisitedNodes.contains(v) 
                        && !mOpenedNodes.contains(v)) {
                    mOpenedNodes.add(v);
                }
            }
            if (newX + 1 < 80) {
                Vertex v = new Vertex(newX + 1, newY);
                if(!proc.wallCells.contains(v) && !mVisitedNodes.contains(v) 
                        && !mOpenedNodes.contains(v)) {
                    mOpenedNodes.add(v);
                }
            }
            if (newY + 1 < 50) {
                Vertex v = new Vertex(newX, newY + 1);
                if(!proc.wallCells.contains(v) && !mVisitedNodes.contains(v)
                        && !mOpenedNodes.contains(v)) {
                    mOpenedNodes.add(v);
                }
            }
        }
    }
    
    
    //pomocne funkcije
    private static double calcEuclideanDistance(Vertex v, Vertex w){
        double tmp1 = Math.pow(v.getX() - w.getX(), 2);
        double tmp2 = Math.pow(v.getY() - w.getY(), 2);
        return Math.sqrt(tmp1 + tmp2);
    }
}
