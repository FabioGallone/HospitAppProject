import javax.swing.*;

public class GestisciPrenotazione {
    private JFrame frame;
    private String nome,cognome, email, codicefiscale;

    private Utente utente;
    private JLabel welcomeLabel;
    private Medico medico;


    public GestisciPrenotazione(JFrame frame, Utente utente) {

        this.frame=frame;
        this.utente = utente;
        this.nome=utente.getNome();
        this.cognome=utente.getCognome();
        this.email=utente.getEmail();
        this.codicefiscale=utente.getCodiceFiscale();
        welcomeLabel = new JLabel();
        medico.loadMedico();
        showGestisciPrenotazioneUI();
    }

    public void showGestisciPrenotazioneUI(){

    }



}
