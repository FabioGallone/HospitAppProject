import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class WelcomePage implements ActionListener {
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




    public WelcomePage(Utente utente) {
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

    private List<Presidio> leggiPresidiDaFile(String filePath) {
        List<Presidio> presidi = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Supponendo che ogni riga contenga i dati separati da virgole
                String[] data = line.split(",");
                if (data.length == 3) {
                    Presidio presidio = new Presidio(data[0].trim(), data[1].trim(), data[2].trim());
                    presidi.add(presidio);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return presidi;
    }



    private void initializeUI() {
        welcomeLabel.setBounds(0, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));


        if (this.isAdministrator) {
            welcomeLabel.setText("Ciao amministratore " + email);
            HospitApp hospitapp = HospitApp.getInstance();
            ListaPresidi = leggiPresidiDaFile("presidio.txt");

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

        quitButton.setBounds(125, 250, 100, 25);
        quitButton.setFocusable(false);
        quitButton.addActionListener(this);

        frame.add(welcomeLabel);
        frame.add(quitButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitButton) {
            frame.dispose();
            this.LogOut();

            LoginGUI login = new LoginGUI();
            login.frame.setVisible(true);
        } else if (e.getSource()==addButton) {
            frame.dispose();
            PresidioGUI presidioGUI = new PresidioGUI(utente);
            presidioGUI.frame.setVisible(true);
        }
    }


}