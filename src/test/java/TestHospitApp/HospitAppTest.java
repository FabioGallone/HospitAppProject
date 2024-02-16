package TestHospitApp;

import static org.junit.Assert.*;

import domain.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HospitAppTest {

    private HospitApp hospitApp;
    private Presidio presidio;
    private Reparto reparto;
    private Utente utente;


    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        hospitApp = new HospitApp();
        presidio = hospitApp.InserisciNuovoPresidio("TestPresidio", "TestIndirizzo", "TestOrario");
        hospitApp.confermaInserimento();
        reparto = hospitApp.inserisciNuovoReparto("Cardiologia", "1", presidio);
        utente = new Utente("Fabio", "Gallone", "ABC123", "ff@gg.com", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", false, false);
    }

    @Test
    public void testGetInstance() {
        assertNotNull(HospitApp.getInstance());
    }

    @Test
    public void testInserisciNuovoPresidio() {
        assertNotNull(presidio);
        assertTrue(hospitApp.getElencoPresidi().contains(presidio));
    }

    @Test
    public void testInserisciNuovoReparto() {
        assertNotNull(reparto);

        // Verifica se la mappa di reparti del presidio contiene il reparto con la chiave corretta (codice reparto)
        assertTrue(presidio.getElencoRepartidelPresidio().stream()
                .anyMatch(r -> presidio.getElencoReparti().containsKey(r.getCodice())));

        // Verifica specifiche del reparto aggiunto
        Reparto repartoAggiunto = presidio.getElencoReparti().get("1");
        assertNotNull(repartoAggiunto);
        assertEquals("Cardiologia", repartoAggiunto.getNome());
        assertEquals("1", repartoAggiunto.getCodice());
    }

    @Test
    public void testPrenotaVisita() {

        //ByteArrayOutputStream serve per catturare l'output che viene normalmente stampato su System.out
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        //tutto ciò che viene stampato su System.out verrà ora catturato da outContent
        System.setOut(new PrintStream(outContent));

        hospitApp.prenotaVisita("TestPresidio");

        //la stampa successiva vada di nuovo sulla console.
        System.setOut(originalOut);

        assertEquals("Prenotazione Visita - Presidio: TestPresidio", outContent.toString().trim());
    }

    @Test
    public void testConfermaPrenotazione() {
        Visita visita = hospitApp.confermaPrenotazione(reparto, presidio, utente);
        assertNotNull(visita);

        assertTrue(utente.getVisitaRepartoPresidioUtente().containsValue(visita));
    }

    @Test
    public void testTrovaVisita() {
        hospitApp.confermaPrenotazione(reparto, presidio, utente);
        Visita foundVisita = hospitApp.trovaVisita("Cardiologia", "TestPresidio", "ABC123");
        assertNotNull(foundVisita);
    }

    @Test
    public void testRimuoviVisitaAssociata() {
        hospitApp.confermaPrenotazione(reparto, presidio, utente);
        hospitApp.rimuoviVisita("Cardiologia", "TestPresidio", utente);
        assertNull(hospitApp.trovaVisita("Cardiologia", "TestPresidio", "TestCodiceFiscale"));
    }




}
