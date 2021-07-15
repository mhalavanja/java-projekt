package projekt;

public class InfoAlgorithm {
    private int brojOtvorenihCvorova;
    private int brojZatvorenihCvorova;
    private String algorithmName;
    
    public InfoAlgorithm(){
        brojOtvorenihCvorova = brojZatvorenihCvorova = 0;
        algorithmName = "noName";
    }
    public InfoAlgorithm(int a, int b, String name){
        brojOtvorenihCvorova = a;
        brojZatvorenihCvorova = b;
        algorithmName = name;
    }
    
    //get metode
    public int getBrojOtvorenih(){
        return brojOtvorenihCvorova;
    }
    public int getBrojZatvorenih(){
        return brojZatvorenihCvorova;
    }
    public String getAlgorithmName(){
        return algorithmName;
    }
    
    //set metode
    public void setBrojOtvorenih(int a){
        brojOtvorenihCvorova = a;
    }
    public void setBrojZatvorenih(int a){
        brojZatvorenihCvorova = a;
    }
    public void setAlgorithmName(String s){
        algorithmName = s;
    }
    
}
