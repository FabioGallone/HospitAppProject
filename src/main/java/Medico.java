import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Medico {

    private String nome;
    private String cognome;
    private String codiceMedico;
    private boolean disponibilità;

    private Reparto reparto;
    private Map<String, List<Medico>> medici; //tutti i medici

    public Medico(String nome, String cognome, String codiceMedico, boolean disponibilità) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceMedico = codiceMedico;
        this.disponibilità = disponibilità;

    }


    public Medico(String nome, String cognome, String codiceMedico) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceMedico = codiceMedico;
        this.disponibilità=false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceMedico() {
        return codiceMedico;
    }

    public void setCodiceMedico(String codiceMedico) {
        this.codiceMedico = codiceMedico;
    }

    public boolean isDisponibilità() {
        return disponibilità;
    }

    public void setDisponibilità(boolean disponibilità) {
        this.disponibilità = disponibilità;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", codiceMedico='" + codiceMedico + '\'' +
                ", disponibilità=" + disponibilità +
                '}';
    }

    public void loadMedico(){
        // Cardiologia
        Medico m1 = new Medico("Francesca", "Rossi", "M12345");
        Medico m2 = new Medico("Marco", "Bianchi", "M67890");
        Medico m3 = new Medico("Anna", "Verdi", "M98765");
        List<Medico> mediciCardiologia=new ArrayList<>();
        mediciCardiologia.add(m1);
        mediciCardiologia.add(m2);
        mediciCardiologia.add(m3);
        // Neurologia
        Medico m4 = new Medico("Luigi", "Neri", "M54321");
        Medico m5 = new Medico("Elena", "Rizzo", "M23456");
        Medico m6 = new Medico("Giuseppe", "De Luca", "M87654");
        List<Medico> mediciNeurologia=new ArrayList<>();
        mediciNeurologia.add(m4);
        mediciNeurologia.add(m5);
        mediciNeurologia.add(m6);

        // Oncologia
        Medico m7 = new Medico("Maria", "Gallo", "M34567");
        Medico m8 = new Medico("Paolo", "Ferrari", "M65432");
        Medico m9 = new Medico("Silvia", "Martini", "M78901");
        List<Medico> mediciOncologia=new ArrayList<>();
        mediciOncologia.add(m7);
        mediciOncologia.add(m8);
        mediciOncologia.add(m9);

        // Oculistica
        Medico m10 = new Medico("Riccardo", "Moretti", "M21098");
        Medico m11 = new Medico("Alessia", "Poli", "M76543");
        Medico m12 = new Medico("Davide", "Ricci", "M54321");
        List<Medico> mediciOculistica=new ArrayList<>();
        mediciOculistica.add(m10);
        mediciOculistica.add(m11);
        mediciOculistica.add(m12);


        // Ortopedia
        Medico m13 = new Medico("Martina", "Colombo", "M87654");
        Medico m14 = new Medico("Simone", "Fabbri", "M98765");
        Medico m15 = new Medico("Roberta", "Longo", "M12345");

        List<Medico> mediciOrtopedia=new ArrayList<>();
        mediciOrtopedia.add(m13);
        mediciOrtopedia.add(m14);
        mediciOrtopedia.add(m15);

        // Ginecologia e Ostetricia
        Medico m16 = new Medico("Luca", "Marini", "M67890");
        Medico m17 = new Medico("Giorgia", "Santoro", "M23456");
        Medico m18 = new Medico("Antonio", "Leone", "M54321");
        List<Medico> mediciGinecologiaOstetricia=new ArrayList<>();
        mediciGinecologiaOstetricia.add(m16);
        mediciGinecologiaOstetricia.add(m17);
        mediciGinecologiaOstetricia.add(m18);
        // Urologia
        Medico m19 = new Medico("Valentina", "Piazza", "M78901");
        Medico m20 = new Medico("Gabriele", "Ruggiero", "M34567");
        Medico m21 = new Medico("Francesco", "Costa", "M21098");
        List<Medico> mediciUrologia=new ArrayList<>();
        mediciUrologia.add(m19);
        mediciUrologia.add(m20);
        mediciUrologia.add(m21);

        this.medici.put("Cardiologia", mediciCardiologia);
        this.medici.put("Neurologia", mediciNeurologia);
        this.medici.put("Oncologia", mediciOncologia);
        this.medici.put("Oculistica", mediciOculistica);
        this.medici.put("Ortopedia", mediciOrtopedia);
        this.medici.put("Ginecologia e Ostetricia", mediciGinecologiaOstetricia);
        this.medici.put("Urologia", mediciUrologia);



        System.out.println("Caricamento Medici Completato");


    }
}
