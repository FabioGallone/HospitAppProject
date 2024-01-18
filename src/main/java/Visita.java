import java.time.LocalDate;
import java.time.LocalTime;

public class Visita {

    private LocalTime ora;
    private LocalDate giorno;
    private boolean disponibilitàmedico;
    private final String codicemedico;

    public Visita(LocalTime ora, LocalDate giorno, boolean disponibilitàmedico, String codicemedico) {
        this.ora = ora;
        this.giorno = giorno;
        this.disponibilitàmedico = disponibilitàmedico;
        this.codicemedico = codicemedico;
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

    public boolean isDisponibilitàmedico() {
        return disponibilitàmedico;
    }

    public void setDisponibilitàmedico(boolean disponibilitàmedico) {
        this.disponibilitàmedico = disponibilitàmedico;
    }

    public String getCodicemedico() {
        return codicemedico;
    }

    @Override
    public String toString() {
        return "PrenotaVisita{" +
                "ora=" + ora +
                ", giorno=" + giorno +
                ", disponibilitàmedico=" + disponibilitàmedico +
                ", codicemedico='" + codicemedico + '\'' +
                '}';
    }
}
