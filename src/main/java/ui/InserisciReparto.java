package ui;

import domain.HospitApp;
import domain.Presidio;
import domain.Reparto;
import domain.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;

public class InserisciReparto implements ActionListener {
    protected JFrame frame;
    private Utente utente;
    private JPanel addPanel;
    private JLabel nomeLabel, indirizzoLabel, orarioLabel, title, addMessageLabel, repartiLabel;
    private JTextField nomeField, indirizzoField, orarioField;
    private JButton addButton, backtoInserisciPresidio;
    private String email;
    private final String nome;
    private final String cognome;
    private final String fiscalCode;
    private boolean isAdministrator;
    private String[] nomiReparti;
    private JCheckBox[] repartoCheckBoxes;




    public InserisciReparto(Utente utente) {
        this.utente=utente;
        this.nome=utente.getNome();
        this.email = utente.getEmail();
        this.cognome=utente.getCognome();
        this.fiscalCode=utente.getCodiceFiscale();
        this.isAdministrator =utente.isAdministrator(email);
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Aggiungi Presidio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(470, 600);
        frame.setLayout(null);

        title = new JLabel("Aggiungi Presidio");
        title.setBounds(124, 30, 200, 35);
        title.setFont(new Font(null, Font.ITALIC, 25));
        frame.add(title);

        addPanel = new JPanel();
        addPanel.setLayout(null);
        addPanel.setBounds(0, 60, 470, 600);
        addPanel.setVisible(true);

        addMessageLabel = new JLabel();
        addMessageLabel.setBounds(160, 430, 200, 35);
        addMessageLabel.setFont(new Font(null, Font.ITALIC, 15));
        addPanel.add(addMessageLabel);

        nomiReparti = HospitApp.getInstance().getNomiReparti();
        repartoCheckBoxes = new JCheckBox[nomiReparti.length];

        for (int i = 0; i < nomiReparti.length; i++) {
            repartoCheckBoxes[i] = new JCheckBox(nomiReparti[i]);
            repartoCheckBoxes[i].setBounds(125, 140 + i * 30, 200, 25);
            addPanel.add(repartoCheckBoxes[i]);
        }

        nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(50, 20, 75, 25);
        nomeField = new JTextField();
        nomeField.setBounds(125, 20, 200, 25);

        indirizzoLabel = new JLabel("Indirizzo:");
        indirizzoLabel.setBounds(50, 60, 75, 25);
        indirizzoField = new JTextField();
        indirizzoField.setBounds(125, 60, 200, 25);

        orarioLabel = new JLabel("Orario:");
        orarioLabel.setBounds(50, 100, 100, 25);
        orarioField = new JTextField();
        orarioField.setBounds(125, 100, 200, 25);

        repartiLabel = new JLabel("Reparti:");
        repartiLabel.setBounds(50, 140, 100, 25);


        addButton = new JButton("Aggiungi");
        addButton.setBounds(170, 380, 100, 25);
        addButton.setFocusable(false);
        addButton.addActionListener(this);


        backtoInserisciPresidio = new JButton("Indietro");
        backtoInserisciPresidio.setBounds(170, 410, 100, 25); // Modifica della posizione del pulsante
        backtoInserisciPresidio.setFocusable(false);
        backtoInserisciPresidio.addActionListener(this);
        addPanel.add(backtoInserisciPresidio);

        addPanel.add(nomeLabel);
        addPanel.add(nomeField);
        addPanel.add(indirizzoField);
        addPanel.add(indirizzoLabel);
        addPanel.add(orarioLabel);
        addPanel.add(orarioField);
        addPanel.add(repartiLabel);

        addPanel.add(addButton);

        frame.add(addPanel);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String nome = nomeField.getText();
            String indirizzo = indirizzoField.getText();
            String orario = orarioField.getText();


            HospitApp hospitapp= HospitApp.getInstance();
            if (nome.isEmpty() || indirizzo.isEmpty() || orario.isEmpty()) {
                addMessageLabel.setForeground(Color.RED);
                addMessageLabel.setText("Compila tutti i campi!");
            } else {

                String hospital = nome + "," + indirizzo + "," + orario;
                Presidio p=hospitapp.InserisciNuovoPresidio(nome, indirizzo, orario);

                for (int i = 0; i < repartoCheckBoxes.length; i++) {
                    if (repartoCheckBoxes[i].isSelected()) {
                       Reparto r=hospitapp.selezionaReparto(nomiReparti[i]); //ritorna un oggetto reparto corrispondente a quel nome
                       hospitapp.inserisciNuovoReparto(r.getNome(), r.getCodice(), p); //lo mette dentro listReparti e crea l'oggetto

                    }
                }

                hospitapp.confermaInserimento();

                //stampo nel file gli ospedali con i propri reparti

                String repartiData = p.getElencoRepartidelPresidio().stream()
                        .map(reparto -> reparto.getNome())
                        .collect(Collectors.joining(","));
                String contentToWrite = hospital + "," + repartiData;
                Utils.writeOnFile("Presidio.txt", contentToWrite);



                addMessageLabel.setForeground(Color.GREEN);
                addMessageLabel.setText("Presidio registrato con successo!");
                frame.dispose();

                InserisciPresidio inseriscipresidio = new InserisciPresidio(utente);
                inseriscipresidio.frame.setVisible(true);


            }
        } else if (e.getSource() == backtoInserisciPresidio) {
            frame.dispose();

            InserisciPresidio inseriscipresidio = new InserisciPresidio(utente);
            inseriscipresidio.frame.setVisible(true);
        }


    }
}