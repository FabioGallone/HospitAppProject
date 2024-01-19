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
    private JButton addButton, backtoInserisciPresidio;
    private String nome;
    private Presidio presidioSelezionato;

    private String codicefiscale;
    HospitApp hospitapp = HospitApp.getInstance();
    private String email;
    private boolean isAdministrator;
    private List<Presidio> ListaPresidi;

    public InserisciPresidio(Utente utente) {
        this.utente = utente;
        this.nome = utente.getNome();
        this.email = utente.getEmail();
        this.codicefiscale=utente.getCodiceFiscale();
        this.isAdministrator = utente.isAdministrator(email);

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

        }else {
            welcomeLabel.setText("Ciao " + nome);

            // Aggiungi menù a tendina basati sul file di testo Presidio.txt
            leggiPresidiDaFile("presidio.txt");
            ListaPresidi = hospitapp.getElencoPresidi();

            int comboBoxXPosition = 100;
            int comboBoxYPosition = 250;

            for (Presidio presidio : ListaPresidi) {
                String nomeStruttura = presidio.getNome();

                // Creare un JComboBox per ogni presidio
                JComboBox<String> repartoComboBox = new JComboBox<>();

                // Aggiungi i reparti associati al presidio corrente al menu a tendina
                for (Reparto reparto : hospitapp.mostraReparti(presidio)) {
                    repartoComboBox.addItem(reparto.getNome());
                }

                repartoComboBox.setBounds(comboBoxXPosition, comboBoxYPosition, 150, 40);
                repartoComboBox.setFocusable(false);
                repartoComboBox.addActionListener(this);

                // Imposta il titolo del JComboBox con il nome del presidio
                repartoComboBox.setBorder(BorderFactory.createTitledBorder(nomeStruttura));

                frame.add(repartoComboBox);

                // Aumenta la posizione X e Y per il prossimo JComboBox
                comboBoxXPosition += 160;
                if (comboBoxXPosition + 150 > frame.getWidth()) {
                    comboBoxXPosition = 100;
                    comboBoxYPosition += 45;
                }
            }
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


    private void mostraReparti(Presidio presidio) {
        List<Reparto> reparti = hospitapp.mostraReparti(presidio);

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
            inserisciReparto inserisciReparto = new inserisciReparto(utente);
            inserisciReparto.frame.setVisible(true);
        }      else if (e.getSource() == backtoInserisciPresidio) {
            frame.dispose();

            InserisciPresidio inseriscipresidio = new InserisciPresidio(utente);
            inseriscipresidio.frame.setVisible(true);
        } else if (this.isAdministrator) {
            // Se il pulsante cliccato è uno dei pulsanti dei presidi
            for (Presidio presidio : ListaPresidi) {
                if (e.getActionCommand().equals(presidio.getNome())) {
                    // Chiamare il nuovo metodo per mostrare i reparti e prenotare la visita
                    hospitapp.mostraReparti(presidio);
                    break;
                }
            }
        }
        else if (!this.isAdministrator) {
            for (Presidio presidio : ListaPresidi) {
                JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
                if (comboBox.getSelectedItem() != null) {
                    String nomeRepartoSelezionato =   comboBox.getSelectedItem().toString();;


                    hospitapp.selezionaReparto(nomeRepartoSelezionato);


                    frame.getContentPane().removeAll();


                    JLabel riepilogoLabel = new JLabel("Riepilogo:");
                    riepilogoLabel.setBounds(100, 150, 200, 45);
                    frame.add(riepilogoLabel);

                    // Aggiungi le informazioni dell'utente alla JLabel
                    JLabel utenteLabel = new JLabel("Nome: " + nome + " | Email: " + email + " | CF: " + codicefiscale);
                    utenteLabel.setBounds(100, 175, 400, 35);
                    riepilogoLabel.setFont(new Font("Arial", Font.BOLD, 17));
                    frame.add(utenteLabel);

                    JLabel scelteLabel = new JLabel("Presidio: " + presidio.getNome() + " | Reparto: " + nomeRepartoSelezionato);
                    scelteLabel.setBounds(100, 200, 400, 35);
                    frame.add(scelteLabel);


                    JButton bookVisitButton = new JButton("Prenota Visita");
                    bookVisitButton.setBounds(100, 300, 150, 25);
                    bookVisitButton.setFocusable(false);
                    bookVisitButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frame.dispose();

                        }
                    });
                    frame.add(bookVisitButton);

                    backtoInserisciPresidio = new JButton("Indietro");
                    backtoInserisciPresidio.setBounds(170, 410, 100, 25);
                    backtoInserisciPresidio.setFocusable(false);
                    backtoInserisciPresidio.addActionListener(this);
                    frame.add(backtoInserisciPresidio);

                    // Rendi nuovamente visibili i componenti
                    frame.revalidate();
                    frame.repaint();

                    break;
                }
            }
        }


    }

    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio, "Selezione Reparto", JOptionPane.INFORMATION_MESSAGE);
    }
}