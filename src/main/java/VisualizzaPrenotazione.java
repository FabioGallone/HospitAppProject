import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizzaPrenotazione extends JFrame {
    private Map<String, Visita> RiepilogoVisiteUtente ;


    public VisualizzaPrenotazione(Utente utente) {

        Utils.leggiVisitedalFile("visita.txt");
        RiepilogoVisiteUtente=utente.getVisitaRepartoPresidioUtente();

        initializeUI();
    }

    private void initializeUI() {

        System.out.println(RiepilogoVisiteUtente);
    }


}
