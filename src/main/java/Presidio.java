import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Presidio {

    private String nome;
    private String indirizzo;
    private LocalTime orario;

    private Map<String, Sala> elencoSale;

    public Presidio(String nome, String indirizzo, LocalTime orario) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.elencoSale = new HashMap<>();
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

    public LocalTime getOrario() {
        return orario;
    }

    public void setOrario(LocalTime orario) {
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

    public List<Sala> getElencoSale(){
        List<Sala> listSale=new ArrayList<>();
        listSale.addAll(elencoSale.values());
        return listSale;
    }
    public Sala getSala(String codiceSala) {
        return elencoSale.get(elencoSale);
    }



}
