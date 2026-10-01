import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;

public class FletteTrad implements Runnable{
    
    private Monitor2 monitor2;
    private CountDownLatch latch;

    public FletteTrad(Monitor2 monitor2, CountDownLatch latch){
        this.monitor2 = monitor2;
        this.latch = latch;
    }


    @Override
    public void run(){

        //ved å putte true i while så kjører den helt til den blir bedt om å breake
        while(true) {
            try{
                //henter ut to hashmaps til fletting
                ArrayList<HashMap<String,Subsekvens>> toMaps = monitor2.hentUtTo();
                
                //hvis en av Hashmapsene er null eller det bare er 1 i toMaps, og
                if(toMaps == null || toMaps.size() < 2) {
                    //hvis den ikke er null og ArrayListen ikke er tom, så
                    if(toMaps != null && !toMaps.isEmpty()){
                        //legges den tilbake i monitor
                        monitor2.settInnFlettet(toMaps.get(0));
                    }
                    latch.countDown(); //her er det klart for utskrift og må derfor ha siste latch her
                    break; //breaker while-loopen
                }

                //henter ut de to HashMapsene i toMaps
                HashMap<String,Subsekvens> map1 = toMaps.get(0);
                HashMap<String,Subsekvens> map2 = toMaps.get(1);
                //sålenge ingen av de er null skal de slås sammen
                if(map1 != null && map2 != null) {
                    HashMap<String,Subsekvens> flettaMap = monitor2.slaaSammenMap(map1,map2);
                    //sjekker enda en gang at det ikke er null før den settes tilbake i monitor
                    if(flettaMap != null) {
                        monitor2.settInnFlettet(flettaMap);
                    }
                }

            }catch (Exception e) {
                e.printStackTrace();
            }   
        }
        latch.countDown(); //countdown utenfor while løkken for å unngå at det skjer for mange ganger
    }
    
}
