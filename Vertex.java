/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

/**
 * Klasa Vertex ce sluziti kao klasa Point u algoritmima pronalaska puta, samo sto jos
 * treba imati informacije o prethodnom cvoru / roditelju te podatke o ukupnoj duzini
 * puta pronadenoj u odredenom trenutku.
 */
public class Vertex {
    
    //Koordinate tocke Vertex u prostoru.
    private int x, y;
    //g-stvarna cijena puta od pocetnog cvora do trenutnog
    //h-heuristicka (optimisticna) procjena cijene puta od trenutnog do ciljnog vrha
    private double g, h;
    //Prethodni vrh u pronalasku puta - parent.
    private Vertex parent; 
    
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
