import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Visita {

    private String ora;
    private String giorno;

    private boolean stato;
    private double costo;


    public Visita(String ora, String giorno, boolean stato, double costo) {
        this.ora = ora;
        this.giorno = giorno;
        this.stato=stato;
        this.costo=costo;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
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
        return   "ora=" + ora + ",giorno=" + giorno +
                ",stato=" + stato;
    }
}
