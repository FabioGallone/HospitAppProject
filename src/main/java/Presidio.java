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




    public double calcolaCostoTotalePaziente(int etaPaziente) {
        double costoTotale = 0;

        for (Map.Entry<String, Visita> entry : visiteperutenterepartopresidio.entrySet()) {
            Visita visita = entry.getValue();


            costoTotale += visita.getCosto();
        }

        double sconto = calcolaScontoInBaseAllEta(etaPaziente);
        costoTotale -= costoTotale * sconto;

        return costoTotale;
    }

    private double calcolaScontoInBaseAllEta(int eta) {
        if (eta >= 0 && eta <= 6) {
            // Bambini (0-6 anni): gratis
            return 1.0;
        } else if (eta >= 7 && eta <= 17) {
            // Ragazzi (7-17 anni): Sconto del 50%
            return 0.5;
        } else if (eta >= 65) {
            // Anziani (65 anni o più): Sconto del 70%
            return 0.7;
        } else {
            //adulti: costo massimo.
            return 0.0;
        }
    }



}




