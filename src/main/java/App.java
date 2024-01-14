
public class App {

    public static void main(String[] args) {
        HospitApp hospitapp= HospitApp.getInstance();
        hospitapp.loadReparti();
        System.out.println("Inserimento nuovo Presidio");
        Presidio p1 = hospitapp.InserisciNuovoPresidio("cannizzaro","via messina 893", "8-22");
        hospitapp.confermaInserimento();
        hospitapp.inserisciSale("3", "1"); //sala 3 cardiologia
        hospitapp.confermaInserimentoSala();
        Presidio p2 = hospitapp.InserisciNuovoPresidio("garibaldi","circonvallazione", "8-22");
        hospitapp.confermaInserimento();
        hospitapp.inserisciSale("5", "5");
        hospitapp.confermaInserimentoSala();

        System.out.println(hospitapp.getElencoSale());
        System.out.println(hospitapp.getElencoPresidi());
    }
}

