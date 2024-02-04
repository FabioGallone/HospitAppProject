import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionePagamento {

    private String informazioni;
    private Utente utente;

    public GestionePagamento(String informazioni, Utente utente) {
        this.informazioni = informazioni;
        this.utente = utente;
        initialize();
    }

    public void initialize() {
        JFrame frame = new JFrame("HospitApp - Gestione Pagamento");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        // Pannello con il titolo
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("RIEPILOGO TICKET");
        titleLabel.setFont(new Font(null, Font.BOLD, 20));
        titlePanel.add(titleLabel);

        frame.add(titlePanel, BorderLayout.NORTH);

        JPanel upperPanel = new JPanel(new BorderLayout());
        upperPanel.setPreferredSize(new Dimension(300, 120));
        upperPanel.setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel(new GridLayout(9, 2));
        infoPanel.setBackground(Color.WHITE);

        String[] dettagli = informazioni.split(",");
        aggiungiInformazione(infoPanel, "Nome", dettagli[0].trim());
        aggiungiInformazione(infoPanel, "Cognome", dettagli[1].trim());
        aggiungiInformazione(infoPanel, "Codice Fiscale", dettagli[2].trim());
        aggiungiInformazione(infoPanel, "Residenza", dettagli[3].trim());
        aggiungiInformazione(infoPanel, "Giorno Visita", dettagli[4].trim());
        aggiungiInformazione(infoPanel, "Ora Visita", dettagli[5].trim());
        aggiungiInformazione(infoPanel, "Nome Presidio", dettagli[6].trim());
        aggiungiInformazione(infoPanel, "Nome Reparto", dettagli[7].trim());
        aggiungiInformazione(infoPanel, "Data di Nascita", dettagli[9].trim());

        upperPanel.add(infoPanel, BorderLayout.CENTER);
        frame.add(upperPanel, BorderLayout.WEST);

        ImageIcon logoIcon = new ImageIcon("sfondo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedLogoIcon = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(resizedLogoIcon);
        frame.add(logoLabel, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);

        int eta = Utils.calcolaEtaDaDataNascita(dettagli[9].trim());
        double importo = HospitApp.getInstance().calcolaCostoTotalePaziente(HospitApp.getInstance().trovaVisita(dettagli[7].trim(), dettagli[6].trim(), dettagli[2].trim()), eta);
        HospitApp.getInstance().rimuoviVisitaAssociata(dettagli[7].trim(), dettagli[6].trim(), utente);
        JLabel importoLabel = new JLabel("Importo: €" + importo);
        importoLabel.setFont(new Font(null, Font.BOLD, 24));
        bottomPanel.add(importoLabel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton prenotaButton = new JButton("Prenota Ora");
        prenotaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    //prenota ora
            }
        });
        buttonsPanel.add(prenotaButton);

        JButton indietroButton = new JButton("Indietro");
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //indietro
            }
        });
        buttonsPanel.add(indietroButton);

        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void aggiungiInformazione(JPanel panel, String titolo, String valore) {
        JLabel titleLabel = new JLabel(titolo + ":");
        titleLabel.setFont(new Font(null, Font.BOLD, 12));
        JLabel valueLabel = new JLabel(valore);

        panel.add(titleLabel);
        panel.add(valueLabel);
    }
}
