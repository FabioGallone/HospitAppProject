import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InserisciPresidio implements ActionListener {
    private Utente utente;
    JFrame frame;
    private JLabel welcomeLabel;
    private JButton quitButton;
    private JButton addButton;
    private String nome;
    private String cognome;
    private String fiscalCode;
    private String email;
    private boolean isAdministrator;
    private List<Presidio> ListaPresidi;




    public InserisciPresidio(Utente utente) {
        this.utente=utente;
        this.nome=utente.getNome();
        this.email = utente.getEmail();
        this.cognome=utente.getCognome();
        this.fiscalCode=utente.getCodiceFiscale();
        this.isAdministrator =utente.isAdministrator(email);

        frame = new JFrame();
        welcomeLabel = new JLabel();
        quitButton = new JButton("LogOut");
        addButton = new JButton("Aggiungi Presidio");
        initializeUI();
    }

    public String getEmail() {
        return email;
    }
    public void LogOut(){
        email=null;
    }

    private void leggiPresidiDaFile(String filePath) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Supponendo che ogni riga contenga i dati separati da virgole
                String[] data = line.split(",");
                if (data.length == 3) {
                    HospitApp hospitapp= HospitApp.getInstance();
                    hospitapp.InserisciNuovoPresidio(data[0].trim(), data[1].trim(), data[2].trim());
                    hospitapp.confermaInserimento();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private void initializeUI() {
        welcomeLabel.setBounds(0, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));


        if (this.isAdministrator) {
            welcomeLabel.setText("Ciao amministratore " + nome);
            leggiPresidiDaFile("presidio.txt");
            ListaPresidi= HospitApp.getInstance().getElencoPresidi();

            int buttonYPosition = 250; // Imposta la posizione iniziale dei pulsanti

            Set<String> nomiUnici = new HashSet<>();

            for (Presidio presidio : ListaPresidi) {
                String nomeStruttura = presidio.getNome();
                if (nomiUnici.add(nomeStruttura)) {
                    // Creare un pulsante per ogni nome diverso nella lista
                    JButton presidioButton = new JButton(presidio.getNome());
                    presidioButton.setBounds(100, buttonYPosition, 150, 25);
                    presidioButton.setFocusable(false);
                    presidioButton.addActionListener(this);
                    frame.add(presidioButton);

                    buttonYPosition += 30; // Aumenta la posizione Y per il prossimo pulsante
                }
            }


            addButton.setBounds(100, 200, 150, 25);
            addButton.setFocusable(false);
            addButton.addActionListener(this);
            frame.add(addButton);

        } else {
            welcomeLabel.setText("Ciao " + email);
        }

        quitButton.setBounds(125, 165, 100, 25);
        quitButton.setFocusable(false);
        quitButton.addActionListener(this);

        frame.add(welcomeLabel);
        frame.add(quitButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
    }



    private void mostraReparti(Presidio presidio) {

        List<Reparto> reparti = presidio.getElencoRepartidelPresidio();

        StringBuilder repartiText = new StringBuilder("Reparti del presidio " + presidio.getNome() + ":\n");
        for (Reparto reparto : reparti) {
            repartiText.append(reparto.getNome()).append("\n");
        }

        JOptionPane.showMessageDialog(frame, repartiText.toString(), "Reparti", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitButton) {
            frame.dispose();
            this.LogOut();

            LoginGUI login = new LoginGUI();
            login.frame.setVisible(true);
        } else if (e.getSource() == addButton) {
            frame.dispose();
            PresidioGUI presidioGUI = new PresidioGUI(utente);
            presidioGUI.frame.setVisible(true);
        } else {
            // Se il pulsante cliccato è uno dei pulsanti dei presidi
            for (Presidio presidio : ListaPresidi) {
                if (e.getActionCommand().equals(presidio.getNome())) {
                    // Mostra i reparti associati a questo presidio
                    mostraReparti(presidio);
                    break;
                }
            }
        }
    }


}