/**
 * Klasa Vertex ce sluziti kao klasa Point u algoritmima pronalaska puta, samo sto jos
 * treba imati informacije o prethodnom cvoru / roditelju te podatke o ukupnoj duzini
 * puta pronadenoj u odredenom trenutku.
 */
public class Vertex implements Comparable<Vertex>{
    
    //Koordinate tocke Vertex u prostoru.
    private int x, y;
    //g-stvarna cijena puta od pocetnog cvora do trenutnog
    //h-heuristicka (optimisticna) procjena cijene puta od trenutnog do ciljnog vrha
    private double g, h;
    //Prethodni vrh u pronalasku puta - parent.
    private Vertex parent; 
    //Pomocna varijabla koja ce se koristiti u SwingWorkeru kako bi se znalo je li
    //vrh otvoren ili obraden.
    private String tag;
    
    public Vertex(int x, int y){
        this.x = x;
        this.y = y;
        g = h = 0.0;
        parent = null;
    }
    
    //Get metode
    public int getX(){ 
        return x;
    }
    public int getY(){ 
        return y;
    }
    public double getG(){ 
        return g;
    }
    public double getH(){ 
        return h;
    }
    public Vertex getparent(){ 
        return parent;
    }
    public String getTag(){
        return tag;
    }

    //Set metode
    public void setXY(int x, int y){ 
        this.x = x; this.y = y;
    }
    public void setG(double g){
        this.g = g;
    }
    public void setH(double h){
        this.h = h;
    }
    public void setParent(Vertex p){
        this.parent = p;
    }
    public void setTag(String s){
        this.tag = s;
    }
    
    @Override
    public int compareTo(Vertex v){
        if(g + h > v.getG() + v.getH()){
            //trenutni objekt je veci, tj ukupna procjena puta je veca
            return 1;
        }
        else if(g + h < v.getG() + v.getH()){
            //trenutna ukupna procejna puta je manja kod pozivnog objekta Vertex
            return -1;
        }
        else{
            //oba objekta imaju istu procjenu vrijednosti
            if(x > v.getX()) return 1;
            else if(x < v.getX()) return -1;
            else if(y > v.getY()) return 1;
            else if(y < v.getY()) return -1;
            else return 0; //iste su tocke
        }
    }
    
    @Override
    public boolean equals(Object b){
        if(b == this){
            return true;
        }
        if(!(b instanceof Vertex)){
            return false;
        }
        Vertex bVertex = (Vertex)b;
        
        return (x == bVertex.getX() && y == bVertex.getY());
    }
}
