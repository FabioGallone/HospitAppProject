package domain;

import ui.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utente implements Observer {
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    private String hashedPassword;
    private boolean isAdministrator;
    private String residenza;
    private String nazionalita;
    private int eta;

    private boolean isPresidio;
    String id;

    private Map<String, Visita> VisitaRepartoPresidioUtente;


    private static List<Utente> utentiList = new ArrayList<>();




    public Utente(String nome, String cognome, String codiceFiscale, String email, String hashedPassword, boolean isAdministrator, boolean isPresidio) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.isAdministrator=isAdministrator;
        this.isPresidio=isPresidio;
        this.VisitaRepartoPresidioUtente=new HashMap<>();
        utentiList.add(this);


    }

    public Utente(String nome, String cognome, String codiceFiscale, String email, String hashedPassword, boolean isAdministrator, String residenza, String nazionalita, int eta, boolean isPresidio) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.isAdministrator = isAdministrator;
        this.residenza = residenza;
        this.nazionalita = nazionalita;
        this.eta = eta;
        this.isPresidio = isPresidio;
    }

    public static List<Utente> getUtentiList() {
        return utentiList;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public String getCognome() {

        return cognome;
    }

    public void setCognome(String cognome) {

        this.cognome = cognome;
    }

    public String getCodiceFiscale() {

        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {

        this.codiceFiscale = codiceFiscale;
    }

    public String getEmail() {

        return email;
    }

    public String getResidenza() {
        return residenza;
    }

    public void setResidenza(String residenza) {
        this.residenza = residenza;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(String nazionalita) {
        this.nazionalita = nazionalita;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public boolean isPresidio() {
        return isPresidio;
    }

    public void setPresidio(boolean presidio) {
        isPresidio = presidio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", codiceFiscale='" + codiceFiscale + '\'' +
                ", email='" + email + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", isAdministrator=" + isAdministrator +
                '}';
    }

    public static boolean findUser(String email, String hashedPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length > 4 && userDetails[3].equals(email) && userDetails[4].equals(hashedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Utente getUserFromEmail(String email) {
        for (Utente utente : Utente.getUtentiList()) {
            if (utente.getEmail().equals(email)) {
                return utente;
            }
        }
        return null;
    }


    public static Utente getUserFromCF(String codiceFiscale) {
        for (Utente utente : Utente.getUtentiList()) {
            if (utente.getCodiceFiscale().equals(codiceFiscale)) {
                return utente;
            }
        }
        return null;
    }


    public  boolean isAdministrator(String email){
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length > 3 && userDetails[3].equals(email)) {
                    return Boolean.parseBoolean(userDetails[5]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Email non trovata nel file
    }


    public void prenotaVisita(Visita visita, String nomepresidio, String nomereparto) {

        String key= nomepresidio+"_"+nomereparto;
        this.VisitaRepartoPresidioUtente.put(key, visita); //tutte le visite di un utente

    }
    public Map<String, Visita> getVisitaRepartoPresidioUtente() {
        return this.VisitaRepartoPresidioUtente;
    }


    //la mappa VisiRepartoPresidioutente contiene come key nomePresidio+nomeReparto e come value la visita.
    public void rimuoviVisitaUtente(String nomeReparto, String nomePresidio) {
        String key = nomePresidio + "_" + nomeReparto;

        if (VisitaRepartoPresidioUtente.containsKey(key)) {
            VisitaRepartoPresidioUtente.remove(key);
            System.out.println("Visita rimossa con successo per il reparto " + nomeReparto + " e il presidio " + nomePresidio);
        } else {
            System.out.println("Nessuna visita trovata per il reparto " + nomeReparto + " e il presidio " + nomePresidio);
        }
    }

    public static int calcolaEtaDaDataNascita(String dataNascitaFormatted) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dataNascita = dateFormat.parse(dataNascitaFormatted);
            Calendar calNascita = Calendar.getInstance();
            Calendar oggi = Calendar.getInstance();
            calNascita.setTime(dataNascita);

            int anni = oggi.get(Calendar.YEAR) - calNascita.get(Calendar.YEAR);
            int meseOggi = oggi.get(Calendar.MONTH);
            int meseNascita = calNascita.get(Calendar.MONTH);

            // Se il mese di nascita è successivo al mese corrente, sottrai un anno
            if (meseNascita > meseOggi || (meseNascita == meseOggi && oggi.get(Calendar.DAY_OF_MONTH) < calNascita.get(Calendar.DAY_OF_MONTH))) {
                anni--;
            }

            return anni;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; //in caso di errore.
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("(observer-" + id + ") stato della visita: " + (boolean) arg);

        if ((boolean) arg ) {
            String state = "VERO" + "," + codiceFiscale;
            Utils.writeOnFile("StatoVisitaCambiato.txt", state);
        }
    }
}
