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

    public Presidio(String nome, String indirizzo, String orario) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.elencoReparti=new HashMap<>();
        this.elencoVisite = new ArrayList<>();
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

    public void inserisciReparti(String nome, String codiceReparto, Presidio p){
        Reparto r = new Reparto(nome, codiceReparto, p);
        elencoReparti.put(codiceReparto, r);
    }
    public List<Reparto> getElencoRepartidelPresidio() {
        return new ArrayList<>(elencoReparti.values());
    }

    public void aggiungiVisita(Visita visita) {
        elencoVisite.add(visita);
    }




}