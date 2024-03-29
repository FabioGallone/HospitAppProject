package ui;

import domain.HospitApp;
import domain.Paziente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class VisualizzaTicket extends JFrame{

    private HospitApp hospitapp;

    private Paziente paziente;

    private JScrollPane scrollPanePrenotata;
    private JButton backButton;
    private JLabel  messageLabel;
    private JTable tableTicket;


    public VisualizzaTicket(Paziente paziente, HospitApp h) {
        this.paziente = paziente;
        this.hospitapp=h;
        initialize();
    }
    public void initialize() {
        setTitle("Riepilogo Ticket");
        setLayout(new BorderLayout());

        DefaultTableModel tableModelTicket = hospitapp.visualizzaTicketUtente(paziente);


        if (tableModelTicket == null) {
            messageLabel = new JLabel("Nessun Ticket disponibile per l'utente.");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(messageLabel, BorderLayout.CENTER);
        } else {
            tableTicket = new JTable(tableModelTicket);
            tableTicket.setRowHeight(30);
            scrollPanePrenotata = new JScrollPane(tableTicket);


            backButton = new JButton("indietro");

            JPanel bottomPanelPrenotata = new JPanel(new FlowLayout());

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
