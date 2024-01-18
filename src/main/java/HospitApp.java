
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
public class HospitApp {

    private static HospitApp hospitapp;
    private Presidio presidioCorrente;

    private Map<String, Presidio> elencoPresidi;
    private Map<String, Reparto> elencoReparti; //tutti i reparti

    private HospitApp() {
        this.elencoPresidi = new HashMap<>();
        this.elencoReparti = new HashMap<>(); // Inizializza la mappa reparti
        loadReparti();
    }



    public static HospitApp getInstance() {
        if (hospitapp == null)
            hospitapp = new HospitApp();
        else
            System.out.println("Istanza già creata");

        return hospitapp;
    }

    public Presidio InserisciNuovoPresidio(String nome, String indirizzo, String orario){
        this.presidioCorrente=new Presidio(nome, indirizzo, orario);
        System.out.println("Presidio inserito");
        return presidioCorrente;
    }



    public void confermaInserimento() {
        if (presidioCorrente != null) {
            this.elencoPresidi.put(presidioCorrente.getNome(), presidioCorrente);
            System.out.println("Operazione Inserimento Presidio Conclusa");
        }

    }

    public List<Presidio> getElencoPresidi() {
        List<Presidio> listPresidi = new ArrayList<>();
        listPresidi.addAll(elencoPresidi.values());
        return listPresidi;
    }
    public List<Reparto> getElencoRepartiDelPresidio(Presidio presidio) {
        return presidio.getElencoRepartidelPresidio();
    }


    public void loadReparti(){
        Reparto r1 = new Reparto("Cardiologia", "1");
        Reparto r2 = new Reparto("Ortopedia", "2");
        Reparto r3 = new Reparto("Ginecologia e Ostetricia", "3");
        Reparto r4 = new Reparto("Neurologia", "4");
        Reparto r5 = new Reparto("Oculistica", "5");
        Reparto r6 = new Reparto("Urologia", "6");
        Reparto r7 = new Reparto("Oncologia", "7");
        this.elencoReparti.put("1", r1);
        this.elencoReparti.put("2", r2);
        this.elencoReparti.put("3", r3);
        this.elencoReparti.put("4", r4);
        this.elencoReparti.put("5", r5);
        this.elencoReparti.put("6", r6);
        this.elencoReparti.put("7", r7);
        System.out.println("Caricamento Reparti Completato");


    }

    public String[] getNomiReparti() {
        List<String> nomiReparti = new ArrayList<>();
        for (Reparto reparto : elencoReparti.values()) {
            nomiReparti.add(reparto.getNome());
        }
        for (Reparto reparto : elencoReparti.values()) {
            System.out.println(reparto.getNome() + " - " + reparto.getCodice());
        }
        return nomiReparti.toArray(new String[0]);
    }


    public Reparto inserisciNuovoReparto(String nome, String codiceReparto, Presidio p) {
        Reparto reparto = new Reparto(nome, codiceReparto, p);
        System.out.println("Reparto inserito");
        // Aggiungi il reparto all'elenco dei reparti del presidio corrente
        p.inserisciReparti(nome, codiceReparto, p);
        return reparto;
    }


    public Reparto getRepartoByNome(String nomeReparto) {
        for (Reparto reparto : elencoReparti.values()) {
            if (reparto.getNome().equals(nomeReparto)) {
                return reparto;
            }
        }

        return null;
    }


}
