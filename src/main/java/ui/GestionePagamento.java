package ui;

import domain.HospitApp;
import domain.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionePagamento {

    private String informazioni;
    private Utente utente;
    private JFrame frame;
    private JPanel titlePanel,upperPanel,infoPanel,bottomPanel,buttonsPanel,imagePanel;
    private JLabel titleLabel,logoLabel,importoLabel,valueLabel;
    HospitApp hospitapp = HospitApp.getInstance();
    private ImageIcon logoIcon,resizedLogoIcon;
    private Image logoImage;
    private JButton prenotaButton,indietroButton;
    public GestionePagamento(String informazioni, Utente utente) {
        this.informazioni = informazioni;
        this.utente = utente;
        initialize();
    }

    public void initialize() {
        frame = new JFrame("HospitApp - Gestione Pagamento");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());



        // Pannello con il titolo
        titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);

        titleLabel = new JLabel("RIEPILOGO TICKET");
        titleLabel.setFont(new Font(null, Font.BOLD, 20));
        titlePanel.add(titleLabel);

        frame.add(titlePanel, BorderLayout.NORTH);

        upperPanel = new JPanel(new BorderLayout());
        upperPanel.setPreferredSize(new Dimension(300, 120));
        upperPanel.setBackground(Color.WHITE);

        infoPanel = new JPanel(new GridLayout(10, 2));
        infoPanel.setBackground(Color.WHITE);

        String[] dettagli = informazioni.split(",");
        aggiungiInformazione(infoPanel, "Nome", dettagli[0].trim());
        aggiungiInformazione(infoPanel, "Cognome", dettagli[1].trim());
        aggiungiInformazione(infoPanel, "Codice Fiscale", dettagli[2].trim());
        aggiungiInformazione(infoPanel, "Giorno Visita", dettagli[3].trim());
        aggiungiInformazione(infoPanel, "Ora Visita", dettagli[4].trim());
        aggiungiInformazione(infoPanel, "Nome Presidio", dettagli[5].trim());
        aggiungiInformazione(infoPanel, "Nome Reparto", dettagli[6].trim());
        aggiungiInformazione(infoPanel, "Nazionalità", dettagli[7].trim());
        aggiungiInformazione(infoPanel, "Residenza", dettagli[8].trim());
        aggiungiInformazione(infoPanel, "Data di Nascita", dettagli[9].trim());

        upperPanel.add(infoPanel, BorderLayout.CENTER);
        frame.add(upperPanel, BorderLayout.WEST);

        logoIcon = new ImageIcon("sfondo.png");
        logoImage = logoIcon.getImage().getScaledInstance(350, 250, Image.SCALE_SMOOTH);
        resizedLogoIcon = new ImageIcon(logoImage);
        logoLabel = new JLabel(resizedLogoIcon);
        imagePanel = new JPanel(new FlowLayout());
        imagePanel.setPreferredSize(new Dimension(500, 300));
        imagePanel.setBackground(Color.WHITE);  // Imposta il colore di sfondo
        imagePanel.add(logoLabel);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        frame.add(imagePanel, BorderLayout.CENTER);

        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);

        int eta = Utils.calcolaEtaDaDataNascita(dettagli[9].trim());

        String importo = dettagli[10].trim();

        importoLabel = new JLabel("Importo: €" + importo);
        importoLabel.setFont(new Font(null, Font.BOLD, 24));
        bottomPanel.add(importoLabel, BorderLayout.CENTER);

        buttonsPanel = new JPanel(new FlowLayout());
        prenotaButton = new JButton("Prenota Ora");
        prenotaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //prenota ora
                if (!Utils.leggiTicketdalFile("Ticket.txt", informazioni)) {
                    Utils.writeOnFile("Ticket.txt", informazioni);
                    hospitapp.aggiungiTicket(informazioni);
                    Utils.rimuoviVisitaDalFile(utente.getCodiceFiscale(), dettagli[3].trim(), dettagli[4].trim());
                    hospitapp.rimuoviVisitaAssociata(dettagli[6].trim(), dettagli[5].trim(), utente);
                    frame.dispose();
                }
                else
                    mostraMessaggio("Hai già richiesto questo ticket.");

            }
        });
        buttonsPanel.add(prenotaButton);

        indietroButton = new JButton("Indietro");
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreaTicket(utente,dettagli[5].trim(),dettagli[4].trim(),dettagli[6].trim(),dettagli[7].trim());
                frame.dispose();
            }
        });
        buttonsPanel.add(indietroButton);

        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void aggiungiInformazione(JPanel panel, String titolo, String valore) {
        titleLabel = new JLabel(titolo + ":");
        titleLabel.setFont(new Font(null, Font.BOLD, 12));
        valueLabel = new JLabel(valore);

        panel.add(titleLabel);
        panel.add(valueLabel);
    }

    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }
}