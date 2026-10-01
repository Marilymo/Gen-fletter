import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class Oblig2Del2B {
    final static int ANTALL_TRADER = 8;
    public static void main(String[] args) {

        if(args.length < 1) {
            System.out.println("Gi en mappesti som første argument");
            return; //avslutter hvis det ikke blir gitt mappesti
        }
        
        Monitor2 monitor2 = new Monitor2();
        String mappesti = args[0];
        String metaFilSti = mappesti + File.separator +"metadata.csv";
        File metaFil = new File(metaFilSti);
        int sekvensLengde = 3; 
        Subsekvens subsekvens;
        SubsekvensRegister register = new SubsekvensRegister();
        


        ArrayList<HashMap<String, Subsekvens>> hashBeholder = register.hentBeholder();
        ArrayList<Thread> trader = new ArrayList<>();
        ArrayList<Thread> fletteTrader = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(ANTALL_TRADER);


        
        try {
            Scanner sc = new Scanner(metaFil);
            while (sc.hasNextLine()) {
                String filnavn = sc.nextLine();
                String dataFilSti = mappesti + File.separator + filnavn;
               
                try {
                    LeseTrad oppgave = new LeseTrad(dataFilSti, monitor2);
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

        //System.out.print(register.henteAntall());

        for (Thread trad : trader){
            trad.start();
        }
        //System.out.print(register.henteAntall());
        System.out.println("Startet alle traader som leser filer.\n venter på at lesetråder skal bli ferdige");
        

        for (Thread trad : trader){
            try{
                trad.join();
            }catch (InterruptedException e) {
                System.out.println("join interrupted");
            }    
        }

        int antallFlettinger = monitor2.henteAntall() -1;
        //bruke metoden hente?
        System.out.println("Antall HashMaps i monitor: " + monitor2.henteAntall());

        System.out.println("Lesetraader er ferdige.\n Starter flettetraader\n");

        int antallFletteTrader = ANTALL_TRADER;
        for(int i = 0; i < antallFletteTrader; i++) {
            FletteTrad oppgave = new FletteTrad(monitor2,latch);
            Thread fletteTrad = new Thread(oppgave);
            fletteTrad.start();
            fletteTrader.add(fletteTrad);
            System.out.println("en tråd startet"); 
        }

        try{
            latch.await();
        }catch (InterruptedException e){
        System.out.println("Hovedtråd ble avbrutt under venting på flettetråder");

        }

        HashMap<String,Subsekvens> sisteMap = monitor2.taUt();
        //System.out.println(sisteMap);
        
        Subsekvens subsekvensMedHoyestForekomst = SubsekvensRegister.hoyestForekomst(sisteMap);
        System.out.println("Sekvens med høyest forekomst:" + subsekvensMedHoyestForekomst);

        return;
    }
}

        





/* 
//endre fra monitor2 til monitor2
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
}*/