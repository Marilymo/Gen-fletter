//import java.io.File;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.ArrayList;

public class Oblig2Del2A {

    static String mappesti;

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Gi en mappesti som første argument");
            return; //avslutter hvis det ikke blir gitt mappesti
        }
        Monitor1 monitor1 = new Monitor1();
        String mappesti = args[0];
        String metaFilSti = mappesti + File.separator +"metadata.csv";
        File metaFil = new File(metaFilSti);
        int sekvensLengde = 3; 
        Subsekvens subsekvens;
        SubsekvensRegister reg = new SubsekvensRegister();

        ArrayList<HashMap<String, Subsekvens>> hashBeholder = reg.hentBeholder();
        ArrayList<Thread> trader = new ArrayList<>();

        
        try {
            Scanner sc = new Scanner(metaFil);
            while (sc.hasNextLine()) {
                String filnavn = sc.nextLine();
                String dataFilSti = mappesti + File.separator + filnavn;
               
                try {
                    LeseTrad oppgave = new LeseTrad(dataFilSti, monitor1);
                    Thread trad = new Thread(oppgave);
                    trader.add(trad);
                } finally{
                
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fant ikke metafil");
            return;
        }

        for (Thread trad : trader){
            trad.start();
        }

        for (Thread trad : trader){
            try{
                trad.join();
            }catch (InterruptedException e) {
                System.out.println("join interrupted");
            }    
        }
            
        while(monitor1.henteAntall() > 1){
            HashMap <String, Subsekvens> map1 = monitor1.taUt();
            HashMap <String, Subsekvens> map2 = monitor1.taUt();
            HashMap<String, Subsekvens> flettaMap = monitor1.slaaSammenMap(map1,map2);

            monitor1.settInn(flettaMap);
        }

        HashMap<String,Subsekvens> sisteMap = monitor1.taUt();
        
        Subsekvens subsekvensMedHoyestForekomst = SubsekvensRegister.hoyestForekomst(sisteMap);
        System.out.println("Sekvens med høyest forekomst:" + subsekvensMedHoyestForekomst);
        
    }
}





    
