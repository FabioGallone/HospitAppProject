package ui;

import com.toedter.calendar.JDateChooser;
import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreaTicket implements ActionListener {
    Utente utente;
    String oraVisita, giornoVisita, nomePresidio, nomeReparto;

    protected JFrame frame;
    private JPanel ticketPanel;
    private Date dataNascita;
    private String residenza, nazionalità;
    private JLabel title, annoLabel, residenzaLabel, nazionalitaLabel, riepilogoLabel1, riepilogoLabel2;
    private JDateChooser dateChooser; // Utilizza JDateChooser per la data
    HospitApp hospitapp = HospitApp.getInstance();
    private JTextField residenzaField, nazionalitaField;
    private JButton confermaButton, backButton;

    public CreaTicket(Utente utente, String oraVisita, String giornoVisita, String nomePresidio, String nomeReparto) {
        this.utente = utente;
        this.oraVisita = oraVisita;
        this.giornoVisita = giornoVisita;
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

        riepilogoLabel1 = new JLabel("Visita prenotata: Presidio: " + nomePresidio + " | Reparto : " + nomeReparto);
        riepilogoLabel1.setBounds(50, 190, 400, 35);
        frame.add(riepilogoLabel1);

        riepilogoLabel2 = new JLabel(" In data " + giornoVisita + " | alle ore " + oraVisita);
        riepilogoLabel2.setBounds(50, 215, 400, 35);
        frame.add(riepilogoLabel2);

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
        confermaButton.setBounds(90, 220, 100, 25);
        confermaButton.setFocusable(false);
        confermaButton.addActionListener(this);

        backButton = new JButton("Indietro");
        backButton.setBounds(200, 220, 100, 25);
        backButton.setFocusable(false);
        backButton.addActionListener(this);

        ticketPanel.add(annoLabel);
        ticketPanel.add(dateChooser);
        ticketPanel.add(residenzaLabel);
        ticketPanel.add(residenzaField);
        ticketPanel.add(nazionalitaLabel);
        ticketPanel.add(nazionalitaField);
        ticketPanel.add(confermaButton);
        ticketPanel.add(backButton);

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
            Visita visita= hospitapp.trovaVisita(nomeReparto,nomePresidio, utente.getCodiceFiscale());
            int eta=Utils.calcolaEtaDaDataNascita(dataNascitaFormatted);
            ScontoStrategyFactory fs = ScontoStrategyFactory.getInstance();
            ScontoStrategyInterface st = fs.getScontoStrategy();
            float costo = st.applicaSconto(visita.getCosto(), eta);

            String informazioni = utente.getNome() + "," + utente.getCognome() + "," + utente.getCodiceFiscale() +
                    "," + giornoVisita + "," + oraVisita + "," + nomePresidio + "," +
                    nomeReparto + "," + nazionalità + "," + residenza +","+ dataNascitaFormatted + "," + costo;

            new GestionePagamento(informazioni, utente);
            frame.dispose();

        }
        else {
            new VisualizzaPrenotazione(utente);
            frame.dispose();

        }


    }

    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }
}
