package domain;

import ui.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Paziente extends Utente implements Observer  {


    private String residenza;
    private String nazionalita;
    private int eta;


    String id;

    private Map<String, Visita> VisitaRepartoPresidioUtente;


    private static List<Paziente> utentiList = new ArrayList<>();

    public Paziente(String nome, String cognome, String codiceFiscale, String email, String hashedPassword) {
        super(nome, cognome, codiceFiscale, email, hashedPassword);
        utentiList.add(this);
        this.VisitaRepartoPresidioUtente=new HashMap<>();
    }

    public Paziente(String nome, String cognome, String codiceFiscale, String email, String hashedPassword, String residenza, String nazionalita, int eta) {
        super(nome, cognome, codiceFiscale, email, hashedPassword);
        this.residenza = residenza;
        this.nazionalita = nazionalita;
        this.eta = eta;
    }

    @Override
    public String getNome() {
        return super.getNome();
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
    }

    @Override
    public String getCognome() {
        return super.getCognome();
    }

    @Override
    public void setCognome(String cognome) {
        super.setCognome(cognome);
    }

    @Override
    public String getCodiceFiscale() {
        return super.getCodiceFiscale();
    }

    @Override
    public void setCodiceFiscale(String codiceFiscale) {
        super.setCodiceFiscale(codiceFiscale);
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    public void setEmail(String email) {
        super.setEmail(email);
    }

    @Override
    public String getHashedPassword() {
        return super.getHashedPassword();
    }

    @Override
    public void setHashedPassword(String hashedPassword) {
        super.setHashedPassword(hashedPassword);
    }

    public static List<Paziente> getUtentiList() {
        return utentiList;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


        @Override
    public String toString() {
        return "Paziente{" +
                "nome='" + this.getNome() + '\'' +
                ", cognome='" + this.getCognome() + '\'' +
                ", codice fiscale=" + this.getCodiceFiscale() +
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

    public static Paziente getUserFromEmail(String email) {
        for (Paziente utente : Paziente.getUtentiList()) {
            if (utente.getEmail().equals(email)) {
                return utente;
            }
        }
        return null;
    }


    public static Paziente getUserFromCF(String codiceFiscale) {
        for (Paziente utente : Paziente.getUtentiList()) {
            if (utente.getCodiceFiscale().equals(codiceFiscale)) {
                return utente;
            }
        }
        return null;
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
            String state = "VERO" + "," + this.getCodiceFiscale();
            Utils.writeOnFile("StatoVisitaCambiato.txt", state);
        }
    }


}
