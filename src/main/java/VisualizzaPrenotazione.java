import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizzaPrenotazione extends JFrame {
    private Map<String, Visita> RiepilogoVisiteUtente ;
    private Utente utente;


    public VisualizzaPrenotazione(Utente utente) {
        this.utente = utente;
        Utils.leggiPresidiDaFile("Presidio.txt");
        Utils.leggiVisitedalFile("visita.txt");
        RiepilogoVisiteUtente=new HashMap<>();
        initializeUI();
    }

    private void initializeUI() {
        RiepilogoVisiteUtente=utente.getVisite();
        System.out.println(RiepilogoVisiteUtente);
    }


}
