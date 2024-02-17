package ui;

import domain.HospitApp;
import domain.Utente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RimuoviTicket extends JFrame {

    private Utente utente;
    private JScrollPane scrollPanePrenotata;
    private JButton backButton,searchButton, removeButton;
    private JLabel  messageLabel;
    private JTable tableTicket;
    private JTextField searchField;
    private HospitApp hospitapp;

    public RimuoviTicket(Utente utente, HospitApp h) {
        this.utente = utente;
        this.hospitapp=h;
        initialize();
    }

    public void initialize() {
        setTitle("Riepilogo Prenotazioni");
        setLayout(new BorderLayout());

        removeButton = new JButton("Rimuovi");
        JPanel removePanel = new JPanel(new FlowLayout());
        removePanel.add(removeButton);
        add(removePanel, BorderLayout.SOUTH);
        removeButton.addActionListener(e -> rimuoviTicketSelezionato());


        searchField = new JTextField();
        searchField.setColumns(20);
        searchButton = new JButton("Cerca");

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Codice Fiscale:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Inizializzazione della tabella
        DefaultTableModel tableModelTicket = hospitapp.VisualizzaTuttiTicketPresidio(utente.getNome());


        if (tableModelTicket == null || tableModelTicket.getRowCount()==0) {
            messageLabel = new JLabel("Nessun Ticket disponibile");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(messageLabel, BorderLayout.CENTER);
            searchField.setVisible(false);
            searchButton.setVisible(false);
            removeButton.setVisible(false);
            searchPanel.setVisible(false);
        } else {
            tableTicket = new JTable(tableModelTicket);
            tableTicket.setRowHeight(30);

            // Configurazione della tabella per la selezione
            tableTicket.setCellSelectionEnabled(true);
            ListSelectionModel cellSelectionModel = tableTicket.getSelectionModel();
            cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Disabilita la modifica delle celle
            tableTicket.setDefaultEditor(Object.class, null);

            scrollPanePrenotata = new JScrollPane(tableTicket);

            backButton = new JButton("Indietro");

            JPanel bottomPanelPrenotata = new JPanel(new FlowLayout());
            bottomPanelPrenotata.add(backButton);

            JPanel southPanelPrenotata = new JPanel(new BorderLayout());
            southPanelPrenotata.add(scrollPanePrenotata, BorderLayout.CENTER);
            southPanelPrenotata.add(bottomPanelPrenotata, BorderLayout.SOUTH);

            add(southPanelPrenotata, BorderLayout.CENTER);

            backButton.addActionListener(e -> Indietro(tableTicket));
            searchButton.addActionListener(e -> ricercaCodiceFiscale());
        }

        setSize(850, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void Indietro(JTable table) {
        dispose();
    }

    private void ricercaCodiceFiscale() {
        String codiceFiscaleCercato = searchField.getText().trim();

        if (!codiceFiscaleCercato.isEmpty()) {
            Utente utenteCercato=Utente.getUserFromCF(codiceFiscaleCercato);

            if (utenteCercato != null) {
                // Visualizza i ticket per l'utente trovato
                DefaultTableModel tableModelTicket = hospitapp.VisualizzaTicketUtenteCercato(utenteCercato,utente.getNome());


                if (tableModelTicket != null && tableModelTicket.getRowCount() > 0) {
                    tableTicket.setModel(tableModelTicket);
                } else {
                    mostraMessaggio("L'utente cercato non ha prenotato nessun Ticket");
                }

                // Restituisci l'utente trovato
            } else {
                mostraMessaggio("Utente non registrato su HospitApp");
            }
        } else {
            mostraMessaggio("Inserisci un codice fiscale da cercare.");
        }

    }


    private void rimuoviTicketSelezionato() {
        int selectedRow = tableTicket.getSelectedRow();

        if (selectedRow != -1) {
            String codiceFiscale = tableTicket.getValueAt(selectedRow, 0).toString().trim();
            String giornoVisita = tableTicket.getValueAt(selectedRow, 1).toString().trim();

            hospitapp.rimuoviTicketSelezionato(codiceFiscale, giornoVisita);

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un ticket prima di cliccare su Rimuovi.");
        }
    }

    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }
}
