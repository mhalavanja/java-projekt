
public class ComparableVertex implements Comparable<ComparableVertex> {
    private Vertex v;
    private int length;
    private boolean infinity;
    private final Vertex undefined = new Vertex(-1,-1);
    private Vertex previous;
    
    public ComparableVertex(Vertex v, int length, boolean infinity) {
        this.v = v;
        this.length = length;
        this.infinity = infinity;
        previous = undefined;
    }
    
    public void setLength(int length) {
        this.length = length;
    }
    
    public Vertex getVertex() {
        return v;
    }
    
    public void setPrevious(Vertex prev) {
        previous = prev;
    }
    
    public void setInfinity(boolean inf) {
        infinity = inf;
    }
    
    public boolean isInfinity() {
        return infinity;
    }
    
    public int getLength() {
        return length;
    }
    
    public int getX() {
        return v.getX();
    } 
    
    public int getY() {
        return v.getY();
    }
    
    @Override
    public int compareTo(ComparableVertex o) {
        if(infinity && o.infinity) {
            return 0;
        } 
        else if(o.infinity) {
            return -1;
        }
        else if(infinity) {
            return 1;
        }
        
        if(length < o.length) {
            return -1;
        }
        else if(length == o.length) {
            return 0;
        } else {
            return 1;
        }
    }
}
