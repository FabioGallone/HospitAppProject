package domain;

import java.util.*;

public class Visita extends Observable {

    private String ora;
    private String giorno;

    private boolean stato;
    private float costo;


    public Visita(String ora, String giorno, boolean stato, float costo) {
        this.ora = ora;
        this.giorno = giorno;
        this.stato=stato;
        this.costo=costo;
    }



    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public boolean isStato() {
        return stato;
    }

    public void setStato(boolean stato) {
        this.stato = stato;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    @Override
    public String toString() {
        return   "ora=" + ora + ",giorno=" + giorno;
    }


    public float calcolaCosto(int eta) {
        ScontoStrategyFactory fs = ScontoStrategyFactory.getInstance();
        ScontoStrategyInterface st = fs.getScontoStrategy();

        float costoVisita = st.applicaSconto(this.costo, eta);
        return costoVisita;
    }

    private List<Observer> ListaOsservatori = new ArrayList<>();


    // Questo metodo serve ad aggiungere un observer. Observer è una interfaccia java già esistente.
    public void addObserver(Observer observer) {

        observer.update(this, this.isStato());

        this.ListaOsservatori.add(observer);
    }

    //Rimuovo l'observer specifico.
    public void removeObserver(Observer observer) {
        this.ListaOsservatori.remove(observer);
    }


    //Mi setto lo stato della visita ad un nuovo stato newState, ad esempio true
    public void setVisitaStato(boolean nuovoStato) {
        this.setStato(nuovoStato);
        for (Observer observer : this.ListaOsservatori) {
            observer.update(this, this.isStato());

        }
    }

    public int countMyObservers() {

        return ListaOsservatori.size();
    }



}
