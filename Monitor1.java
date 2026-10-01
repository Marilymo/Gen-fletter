import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;

public class Monitor1 {
    private SubsekvensRegister register;
    private Lock laas = new ReentrantLock();
    private Condition ikkeTomt = laas.newCondition();
    
    //en condition for fletting?
    private boolean ferdigLest = false;

    public Monitor1(){
        //en delt ressurs? filene?
        register = new SubsekvensRegister();



    }


    public void settInn(HashMap<String,Subsekvens> map) { 
        laas.lock();
        try{
            register.settInn(map);
        }finally {
            laas.unlock();
        }
    }

    

    public HashMap<String,Subsekvens> taUt(){
        laas.lock();
        try{
            HashMap<String,Subsekvens> tilfeldigmap = register.taUt();
            return tilfeldigmap;
        }finally {
            laas.unlock();
        }
    }

    public HashMap<String,Subsekvens> sekvensFraFil(String filnavn) {
        laas.lock();
            try{
                return SubsekvensRegister.sekvensFraFil(filnavn);
            }finally {
                laas.unlock();
            }
        }


    public HashMap <String,Subsekvens> slaaSammenMap(HashMap<String,Subsekvens> map1, HashMap<String,Subsekvens> map2){
        laas.lock();
        try{
            HashMap<String,Subsekvens> slaatSammenMap =register.slaaSammenMap(map1,map2);
            return slaatSammenMap;
        }finally{
            laas.unlock();
        }
    }

    public int henteAntall(){
        laas.lock();
        try{
            return register.henteAntall();
        }finally {
            laas.unlock();
        }
    }

    public ArrayList<HashMap<String,Subsekvens>> hentBeholder(){
        laas.lock();
        try{
            return register.hentBeholder();
        }finally {
            laas.unlock();
        }
    }

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
} 

