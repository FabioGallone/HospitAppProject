import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class VisualizzaPrenotazione extends JFrame {

    private Utente utente;

    public VisualizzaPrenotazione(Utente utente) {
        this.utente=utente;
        Utils.leggiVisitedalFile("visita.txt");

        initializeUI();

    }

    private void initializeUI() {
        setTitle("Riepilogo Prenotazioni");
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = HospitApp.getInstance().visualizzaPrenotazioneUtente(utente);

        if (tableModel == null) {
            JLabel messageLabel = new JLabel("Nessuna prenotazione disponibile per l'utente. Per prenotare una visita selezionare presidio e reparto e mandare la richiesta.");
            messageLabel.setHorizontalAlignment(JLabel.CENTER);
            add(messageLabel, BorderLayout.CENTER);
        } else {
            JTable table = new JTable(tableModel);
            table.setRowHeight(30);
            JScrollPane scrollPane = new JScrollPane(table);


            String[] righeTabella = new String[tableModel.getRowCount()];
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                righeTabella[i] = tableModel.getValueAt(i, 0).toString();  // 0 perché mi prendo la visita
            }
            JComboBox<String> azioniComboBox = new JComboBox<>(righeTabella);

            JButton confermaButton = new JButton("Conferma");
            JButton rifiutaButton = new JButton("Rifiuta");

            JPanel bottomPanel = new JPanel(new FlowLayout());
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
    }

    private void rifiutaPrenotazione(JTable table, JComboBox<String> azioniComboBox) {
        String selectedValue = azioniComboBox.getSelectedItem().toString();
        System.out.println(selectedValue);
        String codiceFisc = utente.getCodiceFiscale();

        String[] parts = selectedValue.split(",");
        String ora = getValueAfterEquals(parts[0]);
        String giorno = getValueAfterEquals(parts[1]);
        String stato = getValueAfterEquals(parts[2]);

        Utils.rimuoviPrenotazioneDalFile(codiceFisc, giorno, ora, stato);

        System.out.println("Rifiuto prenotazione per il valore: " + selectedValue);
    }

    private String getValueAfterEquals(String input) {
        int index = input.indexOf('=');// restituisce la parte della stringa che segue =.
        if (index != -1) { //-1 se il carattere non è nella stringa, mi torna index=0 se lo trova e 0 sarà la posizione dell'=.
            return input.substring(index + 1); //mi sposto a destra dell'uguale per prendere tutti i valori dopo.
        }
        return input;
    }


}