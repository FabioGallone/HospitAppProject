package TestHospitApp;
import domain.*;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.jupiter.api.Assertions.*;

class ObservableVisitaTest {
    private Utente utente;
    private Presidio presidio;
    private Reparto reparto;
    private Visita observableVisita;
    private TestObserver testObserver;
    private HospitApp hospitApp;
    @BeforeEach
    public void setUp() {
        // Creiamo una nuova istanza di ObservableVisita per ogni test
        hospitApp = new HospitApp();
        this.observableVisita = new Visita("10:00", "2023-01-01", false, 100.0f);
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

        observableVisita.setVisitaStato(true);

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