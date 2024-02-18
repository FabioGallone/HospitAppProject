package TestHospitApp;

import static org.junit.Assert.*;

import domain.*;
import org.junit.Before;
import org.junit.Test;
import ui.CreaTicket;


public class CreaTicketTest {
    private HospitApp hospitApp;
    private Utente utente;


    @Before
    public void setUp() {
        hospitApp = new HospitApp();
        utente = new Utente("Fabio", "Gallone", "ABC", "ff@gg.com", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", false, false);
    }

    @Test
    public void testCreaTicketInitialization() {
        CreaTicket creaTicket = new CreaTicket(utente, "10:00", "2024-02-20", "Presidio A", "Reparto B", hospitApp);
        assertNotNull(creaTicket);
        assertEquals(utente, creaTicket.utente);
        assertEquals("10:00", creaTicket.oraVisita);
        assertEquals("2024-02-20", creaTicket.giornoVisita);
        assertEquals("Presidio A", creaTicket.nomePresidio);
        assertEquals("Reparto B", creaTicket.nomeReparto);
    }


}
