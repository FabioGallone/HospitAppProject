import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utente {
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;

    private String hashedPassword;
    private boolean isAdministrator;
    private List<Visita> visite;


    public Utente() {
        this.visite = new ArrayList<>();

    }

    public Utente(String nome, String cognome, String codiceFiscale, String email, String hashedPassword, boolean isAdministrator) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.isAdministrator=isAdministrator;
        this.visite = new ArrayList<>();

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



    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
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

    public boolean findUser(String email, String hashedPassword) {
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

    public Utente getUserFromEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length > 4 && userDetails[3].equals(email)) {
                    Utente utente = new Utente();
                    utente.setNome(userDetails[0]);
                    utente.setCognome(userDetails[1]);
                    utente.setCodiceFiscale(userDetails[2]);
                    utente.setEmail(userDetails[3]);
                    utente.setHashedPassword(userDetails[4]);
                    utente.setAdministrator(Boolean.parseBoolean(userDetails[5]));
                    return utente;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Restituisci null se l'utente non è stato trovato
    }




    public boolean isEmailAlreadyUsed(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length > 3 && userDetails[3].equals(email)) {
                    return true; // Email trovata nel file
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Email non trovata nel file
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
    public void prenotaVisita(Visita visita) {
        visite.add(visita);
    }

    public List<Visita> getVisite() {
        return visite;
    }

}
