import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreaTicket implements ActionListener {
    Utente utente;
    String oraVisita, giornoVisita, stato, nomePresidio, nomeReparto;

    protected JFrame frame;
    private JPanel ticketPanel;
    private Date dataNascita;
    private String residenza, nazionalità;
    private JLabel title, annoLabel, residenzaLabel, nazionalitaLabel, riepilogoLabel, utenteLabel, scelteLabel;
    private JDateChooser dateChooser; // Utilizza JDateChooser per la data
    private JTextField residenzaField, nazionalitaField;
    private JButton confermaButton;

    public CreaTicket(Utente utente, String oraVisita, String giornoVisita, String stato, String nomePresidio, String nomeReparto) {
        this.utente = utente;
        this.oraVisita = oraVisita;
        this.giornoVisita = giornoVisita;
        this.stato = stato;
        this.nomePresidio = nomePresidio;
        this.nomeReparto = nomeReparto;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Ticket");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);

        title = new JLabel("Riepilogo Ticket");
        title.setBounds(130, 30, 200, 35);
        title.setFont(new Font(null, Font.ITALIC, 25));
        frame.add(title);

        ticketPanel = new JPanel();
        ticketPanel.setLayout(null);
        ticketPanel.setBounds(0, 60, 420, 280);

        utenteLabel = new JLabel("Visita prenotata: Presidio " + nomePresidio + " | Reparto : " + nomeReparto);
        utenteLabel.setBounds(50, 190, 400, 35);
        frame.add(utenteLabel);

        scelteLabel = new JLabel(" In data " + giornoVisita + " | alle ore " + oraVisita);
        scelteLabel.setBounds(50, 215, 400, 35);
        frame.add(scelteLabel);

        // Componenti per la registrazione
        annoLabel = new JLabel("Data di nascita:");
        annoLabel.setBounds(50, 20, 100, 25);

        // Utilizza JDateChooser per la data di nascita
        dateChooser = new JDateChooser();
        dateChooser.setBounds(160, 20, 200, 25);

        residenzaLabel = new JLabel("Residenza:");
        residenzaLabel.setBounds(50, 60, 75, 25);
        residenzaField = new JTextField();
        residenzaField.setBounds(160, 60, 200, 25);

        nazionalitaLabel = new JLabel("Nazionalità:");
        nazionalitaLabel.setBounds(50, 100, 100, 25);
        nazionalitaField = new JTextField();
        nazionalitaField.setBounds(160, 100, 200, 25);

        confermaButton = new JButton("Conferma");
        confermaButton.setBounds(160, 220, 100, 25);
        confermaButton.setFocusable(false);
        confermaButton.addActionListener(this);

        ticketPanel.add(annoLabel);
        ticketPanel.add(dateChooser);
        ticketPanel.add(residenzaLabel);
        ticketPanel.add(residenzaField);
        ticketPanel.add(nazionalitaLabel);
        ticketPanel.add(nazionalitaField);
        ticketPanel.add(confermaButton);

        frame.add(ticketPanel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confermaButton) {
            dataNascita = dateChooser.getDate();
            residenza = residenzaField.getText();
            nazionalità = nazionalitaField.getText();

            if (dataNascita == null || residenza.trim().isEmpty() || nazionalità.trim().isEmpty()) {
                mostraMessaggio("Si prega di compilare tutti i campi obbligatori.");
                return;
            }

            // Formattazione della data in dd/MM/yyyy
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dataNascitaFormatted = dateFormat.format(dataNascita);

            String informazioni = utente.getNome() + "," + utente.getCognome() + "," + utente.getCodiceFiscale() + "," +
                    residenza + "," + giornoVisita + "," + oraVisita + "," + nomePresidio + "," +
                    nomeReparto + "," + nazionalità + "," + dataNascitaFormatted;

            if (!Utils.leggiTicketdalFile("Ticket.txt", informazioni)) {
                Utils.writeOnFile("Ticket.txt", informazioni);
                Utils.rimuoviPrenotazioneDalFile(utente.getCodiceFiscale(), giornoVisita, oraVisita);
                HospitApp.getInstance().rimuoviVisitaAssociata(nomeReparto,nomePresidio,utente);

            }
            else
                mostraMessaggio("Hai già richiesto questo ticket.");

            frame.dispose();


        }
    }

    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }
}
