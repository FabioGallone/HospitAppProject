package domain;

public class Sala {

    private String codice;

    private Reparto reparto;

    public Sala(String codice, Reparto reparto) {
        this.codice = codice;
        this.reparto = reparto;

    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Reparto getReparto() {
        return reparto;
    }

    public void setReparto(Reparto reparto) {
        this.reparto = reparto;
    }

    @Override
    public String toString() {
        return "domain.Sala{" +
                "codice='" + codice + '\'' +
                ", reparto=" + reparto +
                '}';
    }
}
