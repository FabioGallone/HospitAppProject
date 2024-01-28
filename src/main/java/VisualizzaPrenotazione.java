import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class VisualizzaPrenotazione extends JFrame {
    private Map<String, Visita> RiepilogoVisiteUtente;
    private Utente utente;

    public VisualizzaPrenotazione(Utente utente) {
        this.utente=utente;
        Utils.leggiVisitedalFile("visita.txt");
        RiepilogoVisiteUtente = utente.getVisitaRepartoPresidioUtente();
        initializeUI();

    }

    private void initializeUI() {
        DefaultTableModel tableModel = HospitApp.getInstance().visualizzaPrenotazioneUtente(utente);

        // Crea la JTable utilizzando il modello di tabella
        JTable table = new JTable(tableModel);

        table.setRowHeight(30);

        setTitle("Riepilogo Prenotazioni");
        setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setSize(900, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
