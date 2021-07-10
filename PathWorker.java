import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;

public class PathWorker extends SwingWorker<Boolean, Vertex>{
    
    Form.Grid proc;
    
    PathWorker(Form.Grid proc){
        this.proc = proc;
    }
    
    @Override
    protected Boolean doInBackground() throws Exception{
    
        return true;
    }        
    
    @Override
    protected void done(){
        boolean pronadenPut;
        try{
            pronadenPut = get();
            if(pronadenPut) System.out.println("Pronaden put (PathWorker)");
        }
        catch(InterruptedException e){}
        catch(ExecutionException e){}
    }
    
    @Override
    protected void process(List<Vertex> cvorovi){
    
    }
}
