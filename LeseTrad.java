import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class LeseTrad implements Runnable{

    private String filnavn;
    private Monitor2 monitor2;
    private SubsekvensRegister register;
    //private CountDownLatch latch;

    //liste med plass for filer filer?

    public LeseTrad(String filnavn, Monitor2 monitor2){
        this.filnavn = filnavn;
        this.monitor2 = monitor2;
        //this.latch = latch;
        this.register = new SubsekvensRegister();

    }

    @Override
    public void run(){
        //boolean ferdigLest = false;
        //while (!ferdigLest) {
            try{
                HashMap<String, Subsekvens> map = monitor2.sekvensFraFil(filnavn);
                monitor2.settInn(map);
                //if(register.henteAntall() == monitor2.henteAntall()){
                    //ferdigLest = true;
                
            }catch (Exception e) {
                    e.printStackTrace();
            }finally{
                //latch.countDown(); 
                }
            }
    }

