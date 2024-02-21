package ui;

import domain.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VisualizzaPrenotazione extends JFrame {

    private Paziente paziente;
    private String[] nomiPresidi;
    private String[] nomiReparti;
    private JLabel messageLabel;
    private HospitApp hospitapp;
    private JTable tablePrenotata, tableDaPrenotare;
    private JScrollPane scrollPanePrenotata, scrollPaneDaPrenotare;
    private JButton confermaButton, rifiutaButton;
    JComboBox<String> azioniComboBox;
    private String id;


    public VisualizzaPrenotazione(Paziente paziente, HospitApp h) {
        this.paziente = paziente;
        this.hospitapp = h;
        Utils.leggiVisitedalFile("visita.txt", hospitapp);

        setTitle("Riepilogo Prenotazioni");
        setLayout(new BorderLayout());

        DefaultTableModel tableModelPrenotata = hospitapp.visualizzaVisitaPrenotataUtente(paziente);
        DefaultTableModel tableModelDaPrenotare = hospitapp.visualizzaVisitaDaPrenotareUtente(paziente);

        if (tableModelPrenotata == null || tableModelPrenotata.getRowCount() == 0) {
            messageLabel = new JLabel("Nessuna prenotazione disponibile per l'utente. Per prenotare una visita selezionare presidio e reparto e mandare la richiesta.");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(messageLabel, BorderLayout.CENTER);

        } else {

            if (Utils.LeggiFileStatoVisita("StatoVisitaCambiato.txt", paziente).equals("VERO")) {
                mostraMessaggio("Complimenti, una o più visite sono state aggiornate dall'amministratore!");
                Utils.rimuoviRigaDaFile("StatoVisitaCambiato.txt", paziente.getCodiceFiscale());
            }

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

            azioniComboBox = new JComboBox<>(righeTabellaPrenotate);
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

        visualizzaTutteLeVisiteUtente(paziente);

        setSize(850, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void visualizzaTutteLeVisiteUtente(Paziente utente) {
        List<String> tutteLeVisite = hospitapp.visualizzaVisiteUtente(utente);

        if (!tutteLeVisite.isEmpty()) {
            System.out.println("Tutte le visite dell'utente:");
            for (String visitaInfo : tutteLeVisite) {
                System.out.println(visitaInfo);
            }
        } else {
            System.out.println("Nessuna visita trovata per l'utente.");
        }
    }

    private void confermaPrenotazione(JTable table, JComboBox<String> azioniComboBox) {
        String selectedValue = azioniComboBox.getSelectedItem().toString();
        hospitapp.confermaVisita();
        int selectedIndex = azioniComboBox.getSelectedIndex();
        String[] parts = selectedValue.split(",");
        String ora = getValueAfterEquals(parts[0]);
        String giorno = getValueAfterEquals(parts[1]);
        String nomePresidio = getValueAfterEquals(nomiPresidi[selectedIndex]);
        String nomeReparto = getValueAfterEquals(nomiReparti[selectedIndex]);
        new CreaTicket(paziente, ora, giorno, nomePresidio, nomeReparto, hospitapp);
        dispose();
    }

    private void rifiutaPrenotazione(JTable table, JComboBox<String> azioniComboBox) {
        String selectedValue = azioniComboBox.getSelectedItem().toString();
        int selectedIndex = azioniComboBox.getSelectedIndex();
        String codiceFisc = paziente.getCodiceFiscale();

        String[] parts = selectedValue.split(",");
        String ora = getValueAfterEquals(parts[0]);
        String giorno = getValueAfterEquals(parts[1]);
        String nomePresidio = getValueAfterEquals(nomiPresidi[selectedIndex]);
        String nomeReparto = getValueAfterEquals(nomiReparti[selectedIndex]);

        Utils.rimuoviVisitaDalFile(codiceFisc, giorno, ora);
        hospitapp.rimuoviVisita(nomeReparto, nomePresidio, paziente);

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
