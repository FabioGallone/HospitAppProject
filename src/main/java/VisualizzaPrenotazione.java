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
            add(scrollPane, BorderLayout.CENTER);
        }

        setSize(900, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
