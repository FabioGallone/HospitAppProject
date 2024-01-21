import java.util.ArrayList;
import java.util.List;

public class Reparto {

    private String nome;
    private String codice;
    private Presidio presidio;
    private List<Visita> elencoVisite;
    private List<Reparto> elencoReparto;
    public Reparto(String nome, String codice) {
        this.nome = nome;
        this.codice = codice;
        this.elencoVisite = new ArrayList<>();
    }


    public String getNome() {
        return nome;
    }



    public String getCodice() {
        return codice;
    }


    @Override
    public String toString() {
        return "Reparto{" +
                "nome='" + nome + "" +
        ", codice='" + codice + "" +
        '}';
    }

    public void aggiungiVisita(Visita visita) {
        elencoVisite.add(visita);
    }

    public List<Visita> getElencoVisite() {
        return elencoVisite;
    }
}