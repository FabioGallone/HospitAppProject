package domain;

public class Amministratore extends Utente{

    public Amministratore(String nome, String cognome, String codiceFiscale, String email, String hashedPassword) {
        super(nome, cognome, codiceFiscale, email, hashedPassword);
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
    public boolean isAdministrator(String email) {
        return super.isAdministrator(email);
    }

    @Override
    public boolean isPresidio(String email) {
        return super.isPresidio(email);
    }


}
