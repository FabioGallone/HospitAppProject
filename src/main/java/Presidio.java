import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Presidio {

    private String nome;
    private String indirizzo;
    private String orario;
    private List<Visita> elencoVisite;
    private Map<String, Reparto> elencoReparti;

    private Map<String, Visita> visiteperutenterepartopresidio;

    public Presidio(String nome, String indirizzo, String orario) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.elencoReparti = new HashMap<>();
        this.elencoVisite = new ArrayList<>();
        this.visiteperutenterepartopresidio=new HashMap<>();
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }


    @Override
    public String toString() {
        return "Presidio{" +
                "nome='" + nome + "" +
                ", indirizzo='" + indirizzo + "" +
                ", orario=" + orario +
                '}';
    }

    public void inserisciReparti(String nome, String codiceReparto, Presidio p) {
        Reparto r = new Reparto(nome, codiceReparto);
        elencoReparti.put(codiceReparto, r);
    }

    public List<Reparto> getElencoRepartidelPresidio() {
        return new ArrayList<>(elencoReparti.values());
    }

    public void aggiungiVisita(Visita visita, String nomereparto, String nomeutente) {

        String key = nomereparto + "_" + nomeutente;
        visiteperutenterepartopresidio.put(key, visita);

    }


    public Map<String, Visita> getVisiteUtenteRepartoPresidio() {
        return visiteperutenterepartopresidio;
    }

    public void rimuoviVisita(String nomeReparto, String nomeUtente) {
        String key = nomeReparto + "_" + nomeUtente;
        visiteperutenterepartopresidio.remove(key);
    }








}




