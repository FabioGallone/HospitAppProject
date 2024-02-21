package TestHospitApp;

import static org.junit.Assert.*;

import domain.*;
import org.junit.Before;
import org.junit.Test;
import ui.Utils;

import javax.swing.table.DefaultTableModel;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class HospitAppTest {

    private HospitApp hospitApp;
    private Presidio presidio;
    private Reparto reparto;
    private Paziente utente;


    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        hospitApp = new HospitApp();
        presidio = hospitApp.InserisciNuovoPresidio("Garibaldi", "TestIndirizzo", "TestOrario");
        hospitApp.confermaInserimento();
        reparto = hospitApp.inserisciNuovoReparto("Cardiologia", "1", presidio);
        utente = new Paziente("Fabio", "Gallone", "ABC", "ff@gg.com", "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
        // Assicurarsi che i reparti siano caricati prima di eseguire i test
        hospitApp.loadReparti();
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

        hospitApp.prenotaVisita("Garibaldi");

        //la stampa successiva vada di nuovo sulla console.
        System.setOut(originalOut);

        assertEquals("Prenotazione Visita - Presidio: Garibaldi", outContent.toString().trim());
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
        Visita foundVisita = hospitApp.trovaVisita("Cardiologia", "Garibaldi", "ABC");
        assertNotNull(foundVisita);
    }

    @Test
    public void testRimuoviVisitaAssociata() {
        hospitApp.confermaPrenotazione(reparto, presidio, utente);
        hospitApp.rimuoviVisita("Cardiologia", "Garibaldi", utente);
        assertNull(hospitApp.trovaVisita("Cardiologia", "Garibaldi", "TestCodiceFiscale"));
    }

    @Test
    public void testVisualizzaPrenotazioni() {
        // Assumendo che l'implementazione di visualizzaPrenotazioni ritorni una lista di stringhe
        // che rappresentano le prenotazioni per un dato reparto e presidio.
        Map<String, List<String>> utentiPerRepartoPresidio = new HashMap<>();
        utentiPerRepartoPresidio.put(presidio.getNome() + "_" + reparto.getNome(), Arrays.asList("Utente1", "Utente2"));
        List<String> prenotazioni = hospitApp.visualizzaPrenotazioni(reparto, presidio, utentiPerRepartoPresidio);

        assertNotNull(prenotazioni);
        assertFalse(prenotazioni.isEmpty());
        assertTrue(prenotazioni.contains("Utente1"));
        assertTrue(prenotazioni.contains("Utente2"));
    }

    @Test
    public void testConfermaGestione() {
        Visita visita = new Visita(null, null, false, 50);
        String data = "10/10/2024";
        String orario = "10:00";

        hospitApp.confermaGestione(visita, data, orario);

        assertEquals(data, visita.getGiorno());
        assertEquals(orario, visita.getOra());
        assertTrue(visita.isStato());
    }

    @Test
    public void testRimuoviTicketSelezionato() {
        // Prima, aggiungi un ticket
        String codiceFiscale = "ABC123";
        String giornoVisita = "10/10/2024";
        String ticketInfo = "Nome,Cognome," + codiceFiscale + ",Data," + giornoVisita + ",Presidio,Reparto,Nazionalita,Residenza,DataNascita,Costo";
        hospitApp.aggiungiTicket(ticketInfo);

        // Poi, rimuovi il ticket
        hospitApp.rimuoviTicketSelezionato(codiceFiscale, giornoVisita);

        // Verifica che il ticket sia stato rimosso controllando il modello della tabella
        DefaultTableModel model = hospitApp.VisualizzaTicketSpecificoUtente(utente);
        assertEquals(0, model.getRowCount());
    }

    @Test
    public void testLoadReparti() {
        hospitApp.loadReparti(); // Assumendo che questo metodo sia accessibile o chiamato implicitamente
        assertFalse(hospitApp.getNomiReparti()==null); // Verifica che la lista dei nomi dei reparti non sia vuota
        assertNotNull(hospitApp.selezionaReparto("Cardiologia")); // Verifica l'esistenza di un reparto specifico
    }
    @Test
    public void testSelezionaReparto() {
        Reparto repartoSelezionato = hospitApp.selezionaReparto("Cardiologia");
        assertNotNull(repartoSelezionato);
        assertEquals("Cardiologia", repartoSelezionato.getNome());
    }

    @Test
    public void testMostraReparti() {
        List<Reparto> repartiDelPresidio = hospitApp.mostraReparti(presidio);
        assertNotNull(repartiDelPresidio);
        // Assicurarsi che i reparti attesi siano presenti, ad esempio verificare dimensioni o elementi specifici
    }

    @Test
    public void testVisualizzaTuttiTicketPresidio() {
        // Aggiungi almeno un ticket per il presidio di test per rendere il test significativo
        hospitApp.aggiungiTicket("InfoTicket1");
        // Ora visualizza tutti i ticket per il presidio
        DefaultTableModel model = hospitApp.VisualizzaTuttiTicketPresidio("Garibaldi");
        assertNotNull(model);
        // Verifica che il modello di tabella contenga almeno un ticket
        assertTrue(model.getRowCount() > 0);
    }


    @Test
    public void testCreaInformazioniTicket() {
        // Assicurarsi che la visita sia prenotata prima di creare il ticket
        hospitApp.confermaPrenotazione(reparto, presidio, utente);

        Date dataNascita = new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime();
        String infoTicket = hospitApp.creaInformazioniTicket("Cardiologia", "Garibaldi", utente, dataNascita, "Catania", "10/10/2024", "10:00", "Italiana");

        assertNotNull(infoTicket);
        assertTrue(infoTicket.contains("Fabio"));
        assertTrue(infoTicket.contains("10/10/2024"));
    }

    @Test
    public void testVisualizzaVisitaDaPrenotareUtente() {
        hospitApp.confermaPrenotazione(reparto,presidio,utente);
        DefaultTableModel model = hospitApp.visualizzaVisitaDaPrenotareUtente(utente);
        assertNotNull(model);
        assertEquals(1, model.getRowCount());
    }

    @Test
    public void testVisualizzaTicketSpecificoUtente() {
        // Aggiungi un ticket per l'utente specifico

        String ticketInfo = "Fabio,Gallone,ABC,10/10/2024,12:30,Garibaldi,Cardiologia,Italiana,Catania,01/01/1990,50.0";
        Utils.writeOnFile("Ticket.txt",ticketInfo);
        hospitApp.aggiungiTicket(ticketInfo);
        // Ora visualizza i ticket per l'utente
        DefaultTableModel model = hospitApp.VisualizzaTicketSpecificoUtente(utente);
        Utils.rimuoviRigaDaFile("Ticket.txt",ticketInfo);
        assertNotNull(model);
        assertEquals(1, model.getRowCount()); // Verifica che il modello di tabella contenga esattamente un ticket


    }

    @Test
    public void testVisualizzaTuttiTicketPresidioAggiunto() {
        // Questo test assume che almeno un ticket sia stato aggiunto in un test precedente
        DefaultTableModel model = hospitApp.VisualizzaTuttiTicketPresidio("Garibaldi");
        assertNotNull(model);
        assertTrue(model.getRowCount() > 0); // Verifica che ci siano ticket per il presidio "Garibaldi"
    }

    @Test
    public void testRimuoviTicketDopoAggiunta() {
        // Aggiungi un altro ticket per poterlo rimuovere
        String nuovoTicketInfo = "Matteo,Galizia,DEF,11/11/2024,12:00,Garibaldi,Ortopedia,Italiana,Roma,01/01/1991,30";
        Utils.writeOnFile("Ticket.txt",nuovoTicketInfo);
        hospitApp.aggiungiTicket(nuovoTicketInfo);

        // Rimuovi il ticket appena aggiunto
        hospitApp.rimuoviTicketSelezionato("DEF", "01/01/1991");

        // Verifica che il ticket sia stato rimosso verificando il numero di ticket rimasti
        DefaultTableModel model = hospitApp.VisualizzaTicketSpecificoUtente(utente);
        // La riga seguente dipende da quanti ticket sono stati aggiunti e rimossi nei test precedenti
        assertTrue(model.getRowCount() == 0); // Assicurati che questo numero corrisponda al numero atteso di ticket rimasti
    }








}
