package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Presidio extends Utente{


    private String indirizzo;
    private String orario;

    private Map<String, Reparto> elencoReparti;

    private Map<String, Visita> visiteperutenterepartopresidio;

    public Presidio(String nome, String indirizzo, String orario) {
        super(nome);
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.elencoReparti = new HashMap<>();
        this.visiteperutenterepartopresidio=new HashMap<>();
    }

    public Presidio(String nome, String cognome, String codiceFiscale, String email, String hashedPassword, String indirizzo, String orario) {
        super(nome, cognome, codiceFiscale, email, hashedPassword);
        this.indirizzo = indirizzo;
        this.orario = orario;
        this.elencoReparti = new HashMap<>();
        this.visiteperutenterepartopresidio=new HashMap<>();
    }

    public Presidio(String nome, String cognome, String codiceFiscale, String email, String hashedPassword) {
        super(nome, cognome, codiceFiscale, email, hashedPassword);
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
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
                "nome='" + this.getNome() + "" +
                ", indirizzo='" + indirizzo + "" +
                ", orario=" + orario +
                '}';
    }

    public Reparto inserisciReparti(String nome, String codiceReparto) {
        Reparto r = new Reparto(nome, codiceReparto);
        elencoReparti.put(codiceReparto, r);
        return r;
    }

    public Map<String, Reparto> getElencoReparti() {
        return elencoReparti;
    }

    public void setElencoReparti(Map<String, Reparto> elencoReparti) {
        this.elencoReparti = elencoReparti;
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




