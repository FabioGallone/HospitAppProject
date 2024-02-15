package TestHospitApp;
import domain.HospitApp;
import domain.ObservableVisita;
import domain.Visita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.*;

class ObservableVisitaTest {

    private ObservableVisita observableVisita;
    private TestObserver testObserver;

    @BeforeEach
    void setUp() {
        // Creiamo una nuova istanza di ObservableVisita per ogni test
        Visita visita = new Visita("10:00", "2023-01-01", false, 100.0f);
        observableVisita = new ObservableVisita(visita);

        // Creiamo un nuovo TestObserver per ogni test
        testObserver = new TestObserver();
    }

    @Test
    void testAddObserver() {
        observableVisita.addObserver(testObserver);
        assertEquals(1, observableVisita.countMyObservers());
    }

    @Test
    void testRemoveObserver() {
        observableVisita.addObserver(testObserver);
        observableVisita.removeObserver(testObserver);
        assertEquals(0, observableVisita.countMyObservers());
    }

    @Test
    void testSetVisitaStato() {
        observableVisita.addObserver(testObserver);
        Visita visita=HospitApp.getInstance().trovaVisita("Neurologia","Garibaldi","codfiscLello");
        // Chiamiamo il metodo setVisitaStato con newState = true
        observableVisita.setVisitaStato(true, visita);

        // Verifichiamo che l'Observer sia stato notificato con il nuovo stato true
        assertTrue(testObserver.isUpdated());
        assertEquals(true, testObserver.getStato());
    }

    // Un Observer di test per verificare le notifiche
    private static class TestObserver implements Observer {
        private boolean updated;
        private boolean stato;

        @Override
        public void update(Observable o, Object arg) {
            updated = true;
            stato = (boolean) arg;
        }

        public boolean isUpdated() {
            return updated;
        }

        public boolean getStato() {
            return stato;
        }
    }
}