public class Reparto {

    private String nome;
    private String codice;
    private Presidio presidio;

    public Reparto(String nome, String codice) {
        this.nome = nome;
        this.codice = codice;
    }
    public Reparto(String nome, String codice, Presidio presidio) {
        this.presidio =presidio;
        this.nome = nome;
        this.codice = codice;
    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    @Override
    public String toString() {
        return "Reparto{" +
                "nome='" + nome + '\'' +
                ", codice='" + codice + '\'' +
                '}';
    }
}

