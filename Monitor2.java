import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;



public class Monitor2 {

    private SubsekvensRegister register = new SubsekvensRegister();
    private Lock laas = new ReentrantLock();
    private Condition forFaa = laas.newCondition();
    private Condition tilgjengeligForFletting = laas.newCondition();
    private ArrayList<HashMap<String,Subsekvens>> beholder = new ArrayList<>();
    
    private boolean ferdigLest = false; //fletting skal slippe å vente
    private boolean ferdigFletta = false;
    private boolean erFerdig = false;

    public Monitor2(){
        register = new SubsekvensRegister(); //initialiserer
    }

    //Bygger en monitor rundt metodene i SubsekvensRegister med lock/unlock, for at bare en tråd skal kunne bruke metoden om gangen

    //metode for å sette HashMaps inn i registeret
    public void settInn(HashMap<String,Subsekvens> map) { 
        laas.lock();
        try{
            register.settInn(map);

            //hvis det er 2 eller flere HashMaps så skal det signaliserer at fletting er mulig
            if(register.henteAntall()>= 2){
                tilgjengeligForFletting.signalAll();
            }
        }finally {
            laas.unlock();
        }   
    }


    //tar ut HashMap med metoden fra Subsekvensregister/Subsekvens
    
    public HashMap<String,Subsekvens> taUt(){
        laas.lock();
        try{
            HashMap<String,Subsekvens> tilfeldigMap = register.taUt();
            return tilfeldigMap;
        }finally {
            laas.unlock();
        }
    }


    //samme som i SubsekvensRegister
    public HashMap<String,Subsekvens> sekvensFraFil(String filnavn) {
        laas.lock();
            try{
                return register.sekvensFraFil(filnavn);
            }finally {
                laas.unlock();
            }
        }


    public HashMap <String,Subsekvens> slaaSammenMap(HashMap<String,Subsekvens> map1, HashMap<String,Subsekvens> map2){
        laas.lock();
        try{
            HashMap<String,Subsekvens> slaatSammenMap = register.slaaSammenMap(map1,map2);
            return slaatSammenMap;
        }finally{
            laas.unlock();
        }
    }
//metode for å hente ut antallet hashMaps som befinner seg i registeret
    public int henteAntall(){
        laas.lock();
        try{
            return register.henteAntall();
        }finally {
            laas.unlock();
        }
    }

        //metode for å hente hele ArrayListen, skal brukes i hovedprogrammet
    public ArrayList<HashMap<String,Subsekvens>> hentBeholder(){
        laas.lock();
        try{
            return register.hentBeholder();
        }finally {
            laas.unlock();
        }
    }

    //metoden for å finne høyestforekomst, samme som i subsekvensRegister
    public Subsekvens hoyestForekomst(HashMap<String,Subsekvens> flettaMap){
        laas.lock();
        try{
            int storsteVerdi= 0;
            Subsekvens subsekvensHoyestVerdi = null;

            for(Map.Entry<String,Subsekvens> entry : flettaMap.entrySet()) {
                Subsekvens subsekvens = entry.getValue();
                int verdi = subsekvens.hentAntall();
                if (verdi > storsteVerdi) {
                    storsteVerdi = verdi;
                    subsekvensHoyestVerdi = subsekvens;
            }
            }
            return subsekvensHoyestVerdi;
        }finally{
            laas.unlock();

        }
    }

    // en metode for å hente ut to hashMaps for fletting, kommer ut i en arraylist
    public ArrayList<HashMap<String,Subsekvens>> hentUtTo(){

        //deklarerer en ArrayList toMaps utenfor lock fordi det kan gjøres uansett
        ArrayList<HashMap<String,Subsekvens>> toMaps = new ArrayList<>(); 
        
        laas.lock();
        try{
            //når det er mindre enn to i registeret, hvis det ikke er 1, så må man vente på at det skal komme flere
            while(register.henteAntall() < 2){        
                if(register.henteAntall() != 1) {
                    tilgjengeligForFletting.await(); //vente på signal
                }else{
                    break; //breaker hvis det kun finnes ett element tilgjengelig for fletting 
                }
                
            }
            //hvis det er mer enn to så kan man ta ut to
            if(register.henteAntall() >= 2) {
                toMaps = new ArrayList<>(); //initilaiserer toMaps
                toMaps.add(register.taUt());
                toMaps.add(register.taUt());
            }
            return toMaps;

        }catch (InterruptedException e) {
            System.out.println("avbrutt");
            Thread.currentThread().interrupt();
            return null;
        }finally{
            laas.unlock();
        
        }
    }

    //settInn metode for å bruke på de fletta hashmapsene, brukes i FletteTrad-klassen
    public void settInnFlettet(HashMap<String,Subsekvens> fletta){
        laas.lock();
        try{
            register.settInn(fletta);

        }finally{
            laas.unlock();
        }
    }
}
    
