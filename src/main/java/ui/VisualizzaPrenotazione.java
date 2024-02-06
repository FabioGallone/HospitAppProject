package ui;

import domain.HospitApp;
import domain.Utente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VisualizzaPrenotazione extends JFrame {

    private Utente utente;
    private String[] nomiPresidi;
    private String[] nomiReparti;
    private JLabel messageLabel, messageLabel2;
    private JTable tablePrenotata, tableDaPrenotare;
    private JScrollPane scrollPanePrenotata, scrollPaneDaPrenotare;
    private JButton confermaButton, rifiutaButton;

    public VisualizzaPrenotazione(Utente utente) {
        this.utente = utente;
        Utils.leggiVisitedalFile("visita.txt");
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Riepilogo Prenotazioni");
        setLayout(new BorderLayout());

        DefaultTableModel tableModelPrenotata = HospitApp.getInstance().visualizzaVisitaPrenotata(utente);
        DefaultTableModel tableModelDaPrenotare = HospitApp.getInstance().visualizzaVisitaDaPrenotare(utente);

        if (tableModelPrenotata == null) {
            messageLabel = new JLabel("Nessuna prenotazione disponibile per l'utente. Per prenotare una visita selezionare presidio e reparto e mandare la richiesta.");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(messageLabel, BorderLayout.CENTER);
        } else {
            tablePrenotata = new JTable(tableModelPrenotata);
            tablePrenotata.setRowHeight(30);
            scrollPanePrenotata = new JScrollPane(tablePrenotata);

            int righePrenotate = tableModelPrenotata.getRowCount();

            String[] righeTabellaPrenotate = new String[righePrenotate];
            nomiPresidi = new String[righePrenotate];
            nomiReparti = new String[righePrenotate];

            for (int i = 0; i < righePrenotate; i++) {
                righeTabellaPrenotate[i] = tableModelPrenotata.getValueAt(i, 0).toString();
                nomiPresidi[i] = tableModelPrenotata.getValueAt(i, 1).toString();
                nomiReparti[i] = tableModelPrenotata.getValueAt(i, 2).toString();
            }

            JComboBox<String> azioniComboBox = new JComboBox<>(righeTabellaPrenotate);
            confermaButton = new JButton("Conferma");
            rifiutaButton = new JButton("Rifiuta");

            JPanel bottomPanelPrenotata = new JPanel(new FlowLayout());
            bottomPanelPrenotata.add(azioniComboBox);
            bottomPanelPrenotata.add(confermaButton);
            bottomPanelPrenotata.add(rifiutaButton);

            JPanel southPanelPrenotata = new JPanel(new BorderLayout());
            southPanelPrenotata.add(scrollPanePrenotata, BorderLayout.CENTER);
            southPanelPrenotata.add(bottomPanelPrenotata, BorderLayout.SOUTH);

            add(southPanelPrenotata, BorderLayout.CENTER);

            confermaButton.addActionListener(e -> confermaPrenotazione(tablePrenotata, azioniComboBox));
            rifiutaButton.addActionListener(e -> rifiutaPrenotazione(tablePrenotata, azioniComboBox));
        }

            tableDaPrenotare = new JTable(tableModelDaPrenotare);
            tableDaPrenotare.setRowHeight(30);
            scrollPaneDaPrenotare = new JScrollPane(tableDaPrenotare);
            add(scrollPaneDaPrenotare, BorderLayout.SOUTH);


        setSize(850,700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void confermaPrenotazione(JTable table, JComboBox<String> azioniComboBox) {
        String selectedValue = azioniComboBox.getSelectedItem().toString();
        System.out.println("Conferma prenotazione per il valore: " + selectedValue);
        int selectedIndex = azioniComboBox.getSelectedIndex();
        String[] parts = selectedValue.split(",");
        String ora = getValueAfterEquals(parts[0]);
        String giorno = getValueAfterEquals(parts[1]);
        String nomePresidio = getValueAfterEquals(nomiPresidi[selectedIndex]);
        String nomeReparto = getValueAfterEquals(nomiReparti[selectedIndex]);
        new CreaTicket(utente, ora, giorno, nomePresidio, nomeReparto);
        dispose();
    }

    private void rifiutaPrenotazione(JTable table, JComboBox<String> azioniComboBox) {
        String selectedValue = azioniComboBox.getSelectedItem().toString();
        int selectedIndex = azioniComboBox.getSelectedIndex();

        String codiceFisc = utente.getCodiceFiscale();

        String[] parts = selectedValue.split(",");
        String ora = getValueAfterEquals(parts[0]);
        String giorno = getValueAfterEquals(parts[1]);
        String nomePresidio = getValueAfterEquals(nomiPresidi[selectedIndex]);
        String nomeReparto = getValueAfterEquals(nomiReparti[selectedIndex]);

        Utils.rimuoviPrenotazioneDalFile(codiceFisc, giorno, ora);
        HospitApp.getInstance().rimuoviVisitaAssociata(nomeReparto, nomePresidio, utente);

        System.out.println("Rifiuto prenotazione per il valore: " + selectedValue);
        mostraMessaggio("Prenotazione cancellata con successo");
        dispose();
    }

    private String getValueAfterEquals(String input) {
        int index = input.indexOf('=');
        if (index != -1) {
            return input.substring(index + 1);
        }
        return input;
    }

    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }
}
