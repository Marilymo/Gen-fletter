import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Scanner;
import java.io.File;
import java.util.Map;

public class SubsekvensRegister {
    private HashMap <String,Subsekvens> subBeholder = new HashMap<>();
    private ArrayList <HashMap<String, Subsekvens>> hashBeholder = new ArrayList<>();
    private int hashTeller;
    private int sekvensLengde = 3;

    public SubsekvensRegister() {
        subBeholder = new HashMap<>(); //initialiserer
        hashBeholder = new ArrayList<>();
        hashTeller = 0;
    }

    public void settInn(HashMap<String, Subsekvens> subBeholder){
        hashBeholder.add(subBeholder);
    }

    public HashMap<String,Subsekvens> taUt(){
        if (hashBeholder.isEmpty()){
            return null;
        }
        HashMap <String, Subsekvens> tilfeldigMap = hashBeholder.remove(0);
        return tilfeldigMap;
    }

    public int henteAntall(){
        return hashBeholder.size();
    }

    public ArrayList<HashMap<String, Subsekvens>> hentBeholder(){
        return hashBeholder;
    }




    public static HashMap<String, Subsekvens> sekvensFraFil(String filnavn) throws FileNotFoundException, IOException {

        File fil = new File(filnavn);
        int sekvensLengde = 3;
        HashMap<String, Subsekvens> subBeholder = new HashMap<>();
        try(Scanner sc = new Scanner(fil)){
            while (sc.hasNextLine()) {
                String linje = sc.nextLine();
                if (linje.length() < sekvensLengde){
                    System.out.println("For kort sekvens i " +filnavn + ", linjen ignoreres");
                    continue;
                }

                for (int i = 0; i<= linje.length()-sekvensLengde; i++){
                    String sekvens = linje.substring(i, i+sekvensLengde);
                    if(!subBeholder.containsKey(sekvens)) {
                        subBeholder.put(sekvens, new Subsekvens(sekvens, 1));

                    }
                }
            }
            
        }catch (FileNotFoundException e){
            System.out.println("Fant ikke filen " + filnavn);
        }
        return subBeholder;
    }


    public static HashMap<String,Subsekvens> slaaSammenMap(HashMap<String, Subsekvens> map1, HashMap<String,Subsekvens> map2 ) {

        HashMap<String,Subsekvens> flettaMap = new HashMap<>(map1); // Kopierer alle elementer fra map1 inn i flettaMap

        for(Map.Entry<String,Subsekvens> entry : map2.entrySet()) {
            String key = entry.getKey();
            Subsekvens subsekvens = entry.getValue();

            if(flettaMap.containsKey(key)) {
                //henter det eksisterende antallet fra flettaMap og legger til antallet fra subsekvens
                int nyttAntall = flettaMap.get(key).hentAntall() + subsekvens.hentAntall();
                //oppretter ny subsekvens.instans
                flettaMap.put(key, new Subsekvens(key, nyttAntall));

            } else {
                flettaMap.put(key,new Subsekvens(key,subsekvens.hentAntall()));
            }
        }
        return flettaMap;



    }

    public static Subsekvens hoyestForekomst(HashMap<String,Subsekvens> flettaMap){
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
    }
}



