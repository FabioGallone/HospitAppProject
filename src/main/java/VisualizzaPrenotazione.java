import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VisualizzaPrenotazione extends JFrame {

    private Utente utente;
    private String[] nomiPresidi;
    private String[] nomiReparti;
    private JLabel messageLabel;
    private JPanel bottomPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton confermaButton, rifiutaButton;

    public VisualizzaPrenotazione(Utente utente) {
        this.utente = utente;
        Utils.leggiVisitedalFile("visita.txt");
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Riepilogo Prenotazioni");
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = HospitApp.getInstance().visualizzaPrenotazioneUtente(utente);

        if (tableModel == null) {
            messageLabel = new JLabel("Nessuna prenotazione disponibile per l'utente. Per prenotare una visita selezionare presidio e reparto e mandare la richiesta.");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(messageLabel, BorderLayout.CENTER);
        } else {
            table = new JTable(tableModel);
            table.setRowHeight(30);
            scrollPane = new JScrollPane(table);

            int righe=tableModel.getRowCount();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if(tableModel.getValueAt(i, 0).toString().equals("ora=null,giorno=null,stato=false")) {
                  righe--;
                }
            }


            String[] righeTabella = new String[righe];
            nomiPresidi = new String[righe];
            nomiReparti = new String[righe];


            for (int i = 0; i < righe; i++) {
                if(!tableModel.getValueAt(i, 0).toString().equals("ora=null,giorno=null,stato=false")) {
                    righeTabella[i] = tableModel.getValueAt(i, 0).toString();
                    nomiPresidi[i] = tableModel.getValueAt(i, 1).toString();
                    nomiReparti[i] = tableModel.getValueAt(i, 2).toString();
                }
            }

            JComboBox<String> azioniComboBox = new JComboBox<>(righeTabella);
            confermaButton = new JButton("Conferma");
            rifiutaButton = new JButton("Rifiuta");

            bottomPanel = new JPanel(new FlowLayout());
            bottomPanel.add(azioniComboBox);
            bottomPanel.add(confermaButton);
            bottomPanel.add(rifiutaButton);

            add(scrollPane, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

            confermaButton.addActionListener(e -> confermaPrenotazione(table, azioniComboBox));
            rifiutaButton.addActionListener(e -> rifiutaPrenotazione(table, azioniComboBox));
        }

        setSize(900, 400);
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
        String stato = getValueAfterEquals(parts[2]);
        String nomePresidio = getValueAfterEquals(nomiPresidi[selectedIndex]);
        String nomeReparto = getValueAfterEquals(nomiReparti[selectedIndex]);
        CreaTicket creaTicket= new CreaTicket(utente,ora,giorno,stato,nomePresidio,nomeReparto);

    }




    private void rifiutaPrenotazione(JTable table, JComboBox<String> azioniComboBox) {
        String selectedValue = azioniComboBox.getSelectedItem().toString();
        int selectedIndex = azioniComboBox.getSelectedIndex();

        String codiceFisc = utente.getCodiceFiscale();

        String[] parts = selectedValue.split(",");
        String ora = getValueAfterEquals(parts[0]);
        String giorno = getValueAfterEquals(parts[1]);
        String stato = getValueAfterEquals(parts[2]);
        String nomePresidio = getValueAfterEquals(nomiPresidi[selectedIndex]);
        String nomeReparto = getValueAfterEquals(nomiReparti[selectedIndex]);


        Utils.rimuoviPrenotazioneDalFile(codiceFisc, giorno, ora, stato);
        HospitApp.getInstance().rimuoviVisitaAssociata(nomeReparto,nomePresidio,utente);


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
