import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Visita {

    private String ora;
    private Date giorno;


    public Visita(String ora, Date giorno) {
        this.ora = ora;
        this.giorno = giorno;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public Date getGiorno() {
        return giorno;
    }

    public void setGiorno(Date giorno) {
        this.giorno = giorno;
    }

    @Override
    public String toString() {
        return  "ora=" + ora +
                ", giorno=" + giorno +
                '}';
    }


}
