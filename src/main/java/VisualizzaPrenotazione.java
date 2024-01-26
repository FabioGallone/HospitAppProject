import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class VisualizzaPrenotazione extends JFrame {
    private Map<String, Visita> RiepilogoVisiteUtente;

    public VisualizzaPrenotazione(Utente utente) {
        Utils.leggiVisitedalFile("visita.txt");
        RiepilogoVisiteUtente = utente.getVisitaRepartoPresidioUtente();
        initializeUI();
    }

    private void initializeUI() {

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Visita");
        tableModel.addColumn("Presidio");
        tableModel.addColumn("Reparto");


        for (Map.Entry<String, Visita> entry : RiepilogoVisiteUtente.entrySet()) {
            String key = entry.getKey();
            Visita visita = entry.getValue();

            // Separo la chiave in Reparto e Presidio
            String[] repartoPresidio = key.split("_");

            // Aggiungo una riga al modello di tabella
            tableModel.addRow(new Object[]{visita.toString(), repartoPresidio[0], repartoPresidio[1]});
        }

        //JTable con il modello di tabella
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
