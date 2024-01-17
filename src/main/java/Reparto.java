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



    public String getCodice() {
        return codice;
    }


    @Override
    public String toString() {
        return "Reparto{" +
                "nome='" + nome + '\'' +
                ", codice='" + codice + '\'' +
                '}';
    }
}

