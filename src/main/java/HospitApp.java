
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
public class HospitApp {

    private static HospitApp hospitapp;
    private Presidio presidioCorrente;
    private Map<String, Presidio> elencoPresidi;

    private Map<String, Reparto> reparti;

    private HospitApp() {
        this.elencoPresidi = new HashMap<>();
        this.reparti = new HashMap<>();
        loadReparti();
    }

    public static HospitApp getInstance() {
        if (hospitapp == null)
            hospitapp = new HospitApp();
        else
            System.out.println("Istanza già creata");

        return hospitapp;
    }

    public void InserisciNuovoPresidio(String nome, String indirizzo, LocalTime orario){
        this.presidioCorrente=new Presidio(nome, indirizzo, orario);
        System.out.println("Presidio inserito");
    }

    public void inserisciSale(String codiceSala, String codiceReparto){
        if(presidioCorrente!=null){
            Reparto r = reparti.get(codiceReparto);
            if(r!=null){
                this.presidioCorrente.inserisciSale(codiceSala);
                System.out.println("Sala inserita");
            }else
                System.out.println("Sala esistente");
        }
    }

    public void loadReparti(){
        Reparto r1 = new Reparto("1", "Cardiologia");
        Reparto r2 = new Reparto("2", "Ortopedia");
        Reparto r3 = new Reparto("3", "Ginecologia e Ostetricia");
        Reparto r4 = new Reparto("1", "Neurologia");
        Reparto r5 = new Reparto("2", "Oculistica");
        Reparto r6 = new Reparto("3", "Urologia");
        Reparto r7 = new Reparto("3", "Oncologia");
        this.reparti.put("1", r1);
        this.reparti.put("2", r2);
        this.reparti.put("3", r3);
        this.reparti.put("4", r4);
        this.reparti.put("5", r5);
        this.reparti.put("6", r6);
        this.reparti.put("7", r7);
        System.out.println("Caricamento Reparti Completato");
    }


}
