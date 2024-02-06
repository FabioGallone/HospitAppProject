package ui;

import domain.HospitApp;
import domain.Presidio;
import domain.Reparto;
import domain.Utente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VisualizzaTicket extends JFrame{

    private JFrame frame;
    private Utente utente;
    private JPanel titlePanel;
    private String[] nomiPresidi, nomiReparti, codFisc, OraGiornoVisita, StringaCompleta;
    private JScrollPane scrollPanePrenotata;
    private JButton confermaButton, rifiutaButton;
    private JLabel titleLabel,title, messageLabel;
    private JTable tableTicket;

    HospitApp hospitapp = HospitApp.getInstance();


    public VisualizzaTicket(Utente utente) {
        this.utente = utente;
        initialize();
    }
    public void initialize() {
        setTitle("Riepilogo Prenotazioni");
        setLayout(new BorderLayout());
        DefaultTableModel tableModelTicket = HospitApp.getInstance().visualizzaPrenotazioneTicket(utente);


        if (tableModelTicket == null) {
            messageLabel = new JLabel("Nessuna prenotazione disponibile per l'utente. Per prenotare una visita selezionare presidio e reparto e mandare la richiesta.");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(messageLabel, BorderLayout.CENTER);
        } else {
            tableTicket = new JTable(tableModelTicket);
            tableTicket.setRowHeight(30);
            scrollPanePrenotata = new JScrollPane(tableTicket);

            int righePrenotate = tableModelTicket.getRowCount();


            codFisc = new String[righePrenotate];
            OraGiornoVisita = new String[righePrenotate];
            nomiPresidi = new String[righePrenotate];
            nomiReparti = new String[righePrenotate];
            StringaCompleta= new String[righePrenotate];

            for (int i = 0; i < righePrenotate; i++) {
                codFisc[i] = tableModelTicket.getValueAt(i, 0).toString();
                OraGiornoVisita[i] = tableModelTicket.getValueAt(i, 1).toString();
                nomiPresidi[i] = tableModelTicket.getValueAt(i, 2).toString();
                nomiReparti[i] = tableModelTicket.getValueAt(i, 3).toString();
                StringaCompleta[i]= OraGiornoVisita[i]+","+  nomiPresidi[i]+","+nomiReparti[i] ;


            }

            JComboBox<String> azioniComboBox = new JComboBox<>(StringaCompleta);
            confermaButton = new JButton("Conferma");

            JPanel bottomPanelPrenotata = new JPanel(new FlowLayout());
            bottomPanelPrenotata.add(azioniComboBox);
            bottomPanelPrenotata.add(confermaButton);


            JPanel southPanelPrenotata = new JPanel(new BorderLayout());
            southPanelPrenotata.add(scrollPanePrenotata, BorderLayout.CENTER);
            southPanelPrenotata.add(bottomPanelPrenotata, BorderLayout.SOUTH);

            add(southPanelPrenotata, BorderLayout.CENTER);

            confermaButton.addActionListener(e -> confermaPrenotazione(tableTicket, azioniComboBox));
        }


        setSize(850,400);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void confermaPrenotazione(JTable table, JComboBox<String> azioniComboBox) {
        System.out.println("VediTikcert:");
    }


}
