package domain;

import java.util.*;

public class ObservableVisita extends Observable {
    private Visita visita;
    private List<Observer> ListaOsservatori = new ArrayList<>();


    // Questo metodo serve ad aggiungere un observer. Observer è una interfaccia java già esistente.
    public void addObserver(Observer observer) {
        if (visita != null) {
            observer.update(this, visita.isStato());
        }
        this.ListaOsservatori.add(observer);
    }

    //Rimuovo l'observer specifico.
    public void removeObserver(Observer observer) {
        this.ListaOsservatori.remove(observer);
    }


    //Costruttore
    public ObservableVisita(Visita visita) {
        //Posso anche non mettere queste righe, le metto solo per essere sicuro che la visita è false.
        this.visita = Objects.requireNonNull(visita, "La visita non può essere null");
        this.visita.setStato(false);
    }


    //Mi setto lo stato della visita ad un nuovo stato newState, ad esempio true
    public void setVisitaStato(boolean newState, Visita visita) {
        if (this.visita != null) {
            this.visita.setStato(newState);
            for (Observer observer : this.ListaOsservatori) {
                observer.update(this, this.visita.isStato());

            }
        }
    }



}