package domain;

public class App {

    public static void main(String[] args) {
        HospitApp hospitapp= HospitApp.getInstance();
        hospitapp.loadReparti();

    }
}

