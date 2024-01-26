
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.HashMap;

public class HospitApp {

    private static HospitApp hospitapp;
    private Map<String, Visita> visiteAssociations;


    private Presidio presidioCorrente;
    private Visita visitacorrente;

    private Map<String, Presidio> elencoPresidi;
    private Map<String, Reparto> reparti; //tutti i reparti

    private HospitApp() {
        this.elencoPresidi = new HashMap<>();
        this.reparti = new HashMap<>(); // Inizializza la mappa reparti
        this.visiteAssociations = new HashMap<>();

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




    public void loadReparti(){
        Reparto r1 = new Reparto("Cardiologia", "1");
        Reparto r2 = new Reparto("Ortopedia", "2");
        Reparto r3 = new Reparto("Ginecologia e Ostetricia", "3");
        Reparto r4 = new Reparto("Neurologia", "4");
        Reparto r5 = new Reparto("Oculistica", "5");
        Reparto r6 = new Reparto("Urologia", "6");
        Reparto r7 = new Reparto("Oncologia", "7");
        this.reparti.put("1", r1);
        this.reparti.put("2", r2);
        this.reparti.put("3", r3);
        this.reparti.put("4", r4);
        this.reparti.put("5", r5);
        this.reparti.put("6", r6);
        this.reparti.put("7", r7);
        System.out.println("Caricamento Reparti Completato");


    }

    public String[] getNomiReparti() {
        List<String> nomiReparti = new ArrayList<>();
        for (Reparto reparto : reparti.values()) {
            nomiReparti.add(reparto.getNome());
        }
        for (Reparto reparto : reparti.values()) {
            System.out.println(reparto.getNome() + " - " + reparto.getCodice());
        }
        return nomiReparti.toArray(new String[0]);
    }


    public Reparto inserisciNuovoReparto(String nome, String codiceReparto, Presidio p) {
        Reparto reparto = new Reparto(nome, codiceReparto);
        System.out.println("Reparto inserito");
        p.inserisciReparti(nome, codiceReparto, p);
        return reparto;
    }



    //prima chiamata getRepartoByNome
    public Reparto selezionaReparto(String nomeReparto) {
        for (Reparto reparto : reparti.values()) {


            if (reparto.getNome().equals(nomeReparto)) {
                return reparto;
            }
        }

        return null;
    }

    public Presidio selezionaPresidio(String nomePresidio) {
        for (Presidio presidio : elencoPresidi.values()) {

            if (presidio.getNome().equalsIgnoreCase(nomePresidio)){
                return presidio;
            }
        }

        return null;
    }


    public void prenotaVisita(String nomePresidio) {
        Presidio presidio = elencoPresidi.get(nomePresidio);

        if (presidio != null) {
            System.out.println("Prenotazione Visita - Presidio: " + presidio.getNome());
        } else {
            System.out.println("Presidio non trovato: " + nomePresidio);
        }



    }

    //prima si chiamava getElencoRepartiDelPresidio
    public List<Reparto> mostraReparti(Presidio presidio) {

        return presidio.getElencoRepartidelPresidio();
    }

    public Visita confermaPrenotazione(Reparto reparto, Presidio presidio, Utente utente) {
        if (reparto != null && presidio != null && utente != null) {


            Visita visita = new Visita(null, null, false);

            presidio.aggiungiVisita(visita, reparto.getNome(), utente.getNome());

            reparto.aggiungiVisita(visita, presidio.getNome(), utente.getNome());

            utente.prenotaVisita(visita, presidio.getNome(), reparto.getNome());


            String value=reparto.getNome()+"_"+presidio.getNome()+"_"+utente.getCodiceFiscale();
            visiteAssociations.put(value, visita);


            return visita;
        } else {
            System.out.println("Reparto, Presidio o Utente non validi.");
            return null;
        }
    }
    public Visita trovaVisita(String nomeReparto, String nomePresidio, String codiceFiscale) {
        String key = nomeReparto + "_" + nomePresidio + "_" + codiceFiscale;

        if (visiteAssociations.containsKey(key)) {
            return visiteAssociations.get(key);
        } else {
            System.out.println("Visita non trovata per la chiave: " + key);
            return null;
        }
    }


    public List<String> visualizzaPrenotazioni(Reparto reparto, Presidio presidio, Utente utente, Map<String, List<String>> utentiPerRepartoPresidio) {

        String chiaveMappa = presidio.getNome() + "_" + reparto.getNome();
        List<String> utentiAssociati = utentiPerRepartoPresidio.get(chiaveMappa);
        System.out.println("Utenti associati a " + chiaveMappa + ": " + utentiAssociati);
        return utentiAssociati;
    }


    public void confermaGestione(Visita visita, String data, String orario) {

        visita.setGiorno(data);
        visita.setOra(orario);
        visita.setStato(true);
    }













}
