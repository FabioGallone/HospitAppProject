
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
public class HospitApp {

    private static HospitApp hospitapp;
    private Presidio presidioCorrente;
    private Reparto repartocorrente;
    private Utente utente;
    private Sala salaCorrente;
    private Map<String, Presidio> elencoPresidi;
    List<Reparto> listReparto = new ArrayList<>(); //lista di tutti i reparti selezionati nelle checkbox

    private Map<String, Sala> elencoSale ;

    private Map<String,List<Reparto>> elencoreparti; //mappa che associa ad un presidio i reparti


    private Map<String, Reparto> reparti; //tutti i reparti

    private HospitApp() {
        this.elencoPresidi = new HashMap<>();
        this.elencoreparti=new HashMap<>();
        this.elencoSale = new HashMap<>();
        this.reparti = new HashMap<>(); // Inizializza la mappa reparti
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

//    public void inserisciSale(String codiceSala, String codiceReparto){
//        if(presidioCorrente!=null){
//            Reparto r = reparti.get(codiceReparto);
//            if(r!=null){
//                this.salaCorrente=new Sala(codiceSala,r);
//                this.presidioCorrente.inserisciSale(codiceSala, r);//inserisco nella sala il suo codice e il reparto di appartenenza
//                System.out.println("Sala inserita");
//            }else
//                System.out.println("Sala esistente");
//        }
//    }



    public void confermaInserimento() {
        if (presidioCorrente != null) {
            this.elencoPresidi.put(presidioCorrente.getNome(), presidioCorrente);
            System.out.println("Operazione Inserimento Presidio Conclusa");
        }

        elencoreparti.put(presidioCorrente.getNome(), this.getElencoReparti());


        System.out.println(elencoreparti);

    }

//    public void confermaInserimentoSala() {
//        if (salaCorrente != null) {
//            this.elencoSale.put(salaCorrente.getCodice(), salaCorrente);
//            System.out.println("Operazione Inserimento Sala Conclusa");
//        }
//
//    }

    public List<Presidio> getElencoPresidi() {
        List<Presidio> listPresidi = new ArrayList<>();
        listPresidi.addAll(elencoPresidi.values());
        return listPresidi;
    }
    public List<Reparto> getElencoRepartiDelPresidio(Presidio presidio) {
        return presidio.getElencoRepartidelPresidio();
    }


//    public List<Sala> getElencoSale() {
//        List<Sala> listSale = new ArrayList<>();
//        listSale.addAll(elencoSale.values());
//        return listSale;
//    }

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


    public Reparto inserisciReparto(String nome, String codice, Presidio p) {
        Reparto reparto = new Reparto(nome, codice, p);
        System.out.println("Reparto inserito");
        // Aggiungi il reparto all'elenco dei reparti del presidio corrente
        p.inserisciReparti(nome, codice, p);
        return reparto;
    }



    public List<Reparto> getElencoReparti() {

        return listReparto;
    }

    public Reparto getRepartoByNome(String nomeReparto) {
        for (Reparto reparto : reparti.values()) {
            if (reparto.getNome().equals(nomeReparto)) {
                return reparto;
            }
        }

        return null;
    }


}
