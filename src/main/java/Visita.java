import java.time.LocalDate;
import java.time.LocalTime;

public class Visita {

    private LocalTime ora;
    private LocalDate giorno;


    public Visita(LocalTime ora, LocalDate giorno) {
        this.ora = ora;
        this.giorno = giorno;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public LocalDate getGiorno() {
        return giorno;
    }

    public void setGiorno(LocalDate giorno) {
        this.giorno = giorno;
    }

    @Override
    public String toString() {
        return "PrenotaVisita{" +
                "ora=" + ora +
                ", giorno=" + giorno +
                '}';
    }
}
