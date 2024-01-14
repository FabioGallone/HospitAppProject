
public class App {

    public static void main(String[] args) {
        HospitApp hospitapp= HospitApp.getInstance();
        hospitapp.loadReparti();
        System.out.println("Inserimento nuovo Presidio");
        Presidio p1 = hospitapp.InserisciNuovoPresidio("cannizzaro","via messina 893", "8-22");
        hospitapp.inserisciSale("3", "1"); //sala 3 cardiologia
        hospitapp.inserisciSale("5", "5");
        hospitapp.confermaInserimento();
        System.out.println(hospitapp.getElencoPresidi());
    }
}

