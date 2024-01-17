
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Presidio {

    private String nome;
    private String indirizzo;
    private String orario;

    private Map<String, Sala> elencoSale;
    private Map<String, Reparto> elencoReparti;

    public Presidio(String nome, String indirizzo, String orario) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.elencoSale = new HashMap<>();
        this.elencoReparti=new HashMap<>();
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


    public void setElencoSale(Map<String, Sala> elencoSale) {
        this.elencoSale = elencoSale;
    }

    @Override
    public String toString() {
        return "Presidio{" +
                "nome='" + nome + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", orario=" + orario +
                '}';
    }

    public void inserisciSale(String codiceSala, Reparto r) {
        Sala s = new Sala(codiceSala, r);
        elencoSale.put(codiceSala, s);

    }
    public void inserisciReparti(String nome, String codiceReparto, Presidio p){
        Reparto r = new Reparto(nome, codiceReparto, p);
        elencoReparti.put(codiceReparto, r);
    }
    public List<Reparto> getElencoRepartidelPresidio() {
        return new ArrayList<>(elencoReparti.values());
    }


    public List<Sala> getElencoSale(){
        List<Sala> listSale=new ArrayList<>();
        listSale.addAll(elencoSale.values());
        return listSale;
    }
    public Sala getSala(String codiceSala) {
        return elencoSale.get(elencoSale);
    }



}
