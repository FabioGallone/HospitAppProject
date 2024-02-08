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

    private JScrollPane scrollPanePrenotata;
    private JButton backButton;
    private JLabel  messageLabel;
    private JTable tableTicket;


    public VisualizzaTicket(Utente utente) {
        this.utente = utente;
        initialize();
    }
    public void initialize() {
        setTitle("Riepilogo Prenotazioni");
        setLayout(new BorderLayout());
        DefaultTableModel tableModelTicket = HospitApp.getInstance().visualizzaPrenotazioneTicket(utente);


        if (tableModelTicket == null) {
            messageLabel = new JLabel("Nessun Ticket disponibile per l'utente.");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(messageLabel, BorderLayout.CENTER);
        } else {
            tableTicket = new JTable(tableModelTicket);
            tableTicket.setRowHeight(30);
            scrollPanePrenotata = new JScrollPane(tableTicket);

//            int righePrenotate = tableModelTicket.getRowCount();


//            codFisc = new String[righePrenotate];
//            OraGiornoVisita = new String[righePrenotate];
//            nomiPresidi = new String[righePrenotate];
//            nomiReparti = new String[righePrenotate];
//            StringaCompleta= new String[righePrenotate];
//
//            for (int i = 0; i < righePrenotate; i++) {
//                codFisc[i] = tableModelTicket.getValueAt(i, 0).toString();
//                OraGiornoVisita[i] = tableModelTicket.getValueAt(i, 1).toString();
//                nomiPresidi[i] = tableModelTicket.getValueAt(i, 2).toString();
//                nomiReparti[i] = tableModelTicket.getValueAt(i, 3).toString();
//                StringaCompleta[i]= OraGiornoVisita[i]+","+  nomiPresidi[i]+","+nomiReparti[i] ;
//
//
//            }

//            JComboBox<String> azioniComboBox = new JComboBox<>(StringaCompleta);
            backButton = new JButton("indietro");

            JPanel bottomPanelPrenotata = new JPanel(new FlowLayout());
//            bottomPanelPrenotata.add(azioniComboBox);
            bottomPanelPrenotata.add(backButton);


            JPanel southPanelPrenotata = new JPanel(new BorderLayout());
            southPanelPrenotata.add(scrollPanePrenotata, BorderLayout.CENTER);
            southPanelPrenotata.add(bottomPanelPrenotata, BorderLayout.SOUTH);

            add(southPanelPrenotata, BorderLayout.CENTER);

            backButton.addActionListener(e -> Indietro(tableTicket));
        }


        setSize(850,400);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void Indietro(JTable table) {
        dispose();
    }


}
