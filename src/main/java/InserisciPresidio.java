import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InserisciPresidio implements ActionListener {
    private Utente utente;
    JFrame frame;
    private JLabel welcomeLabel;
    private JButton quitButton;
    private JButton addButton;
    private String nome;
    private String codicefiscale;

    HospitApp hospitapp = HospitApp.getInstance();
    private String email;
    private boolean isAdministrator, isPresidio;
    private List<Presidio> ListaPresidi;

    public InserisciPresidio(Utente utente) {
        this.utente = utente;
        this.nome = utente.getNome();
        this.email = utente.getEmail();
        this.codicefiscale = utente.getCodiceFiscale();
        this.isAdministrator = utente.isAdministrator(email);
        this.isPresidio= utente.isPresidio();

        frame = new JFrame();
        welcomeLabel = new JLabel();
        quitButton = new JButton("LogOut");
        addButton = new JButton("Aggiungi Presidio");
        initializeUI();
    }

    public void LogOut() {
        email = null;
    }

    private void leggiPresidiDaFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    Presidio presidio = hospitapp.InserisciNuovoPresidio(data[0].trim(), data[1].trim(), data[2].trim());

                    for (int j = 3; j < data.length; j++) {
                        Reparto r = hospitapp.selezionaReparto(data[j].trim());
                        if (r != null) {
                            // Associa il reparto al presidio corrente
                            presidio.inserisciReparti(r.getNome(), r.getCodice(), presidio);
                        }
                    }

                    hospitapp.confermaInserimento();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        welcomeLabel.setBounds(30, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));

        if (this.isAdministrator) {
            welcomeLabel.setText("Ciao amministratore " + nome);
            leggiPresidiDaFile("presidio.txt");
            ListaPresidi = hospitapp.getElencoPresidi();

            int buttonYPosition = 250;

            Set<String> nomiUnici = new HashSet<>();

            for (Presidio presidio : ListaPresidi) {
                String nomeStruttura = presidio.getNome();
                if (nomiUnici.add(nomeStruttura)) {

                    JButton presidioButton = new JButton(presidio.getNome());
                    presidioButton.setBounds(100, buttonYPosition, 150, 25);
                    presidioButton.setFocusable(false);
                    presidioButton.addActionListener(this);
                    frame.add(presidioButton);

                    buttonYPosition += 30;
                }
            }


            addButton.setBounds(100, 200, 150, 25);
            addButton.setFocusable(false);
            addButton.addActionListener(this);
            frame.add(addButton);

        } else if(!this.isAdministrator && !this.isPresidio) {
            PrenotaVisita prenotaVisita = new PrenotaVisita(frame, utente);

        }else{
            GestisciPrenotazione medicoclass= new GestisciPrenotazione(frame, utente);
        }

        quitButton.setBounds(125, 165, 100, 25);
        quitButton.setFocusable(false);
        quitButton.addActionListener(this);

        frame.add(welcomeLabel);
        frame.add(quitButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 550);
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
        } else if (e.getSource() == addButton) {
            frame.dispose();
            InserisciReparto inseriscireparto = new InserisciReparto(utente);
            inseriscireparto.frame.setVisible(true);
        } else if(this.isAdministrator) {
            for (Presidio presidio : ListaPresidi) {
                if (e.getActionCommand().equals(presidio.getNome())) {
                    mostraReparti(presidio);
                    break;
                }
            }
        }
    }


    private void mostraReparti(Presidio presidio) {
        List<Reparto> reparti = hospitapp.mostraReparti(presidio);

        StringBuilder repartiText = new StringBuilder("Reparti del presidio " + presidio.getNome() + ":\n");
        for (Reparto reparto : reparti) {
            repartiText.append(reparto.getNome()).append("\n");
        }

        JOptionPane.showMessageDialog(frame, repartiText.toString(), "Reparti", JOptionPane.INFORMATION_MESSAGE);
    }


}