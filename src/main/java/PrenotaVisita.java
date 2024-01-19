import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PrenotaVisita implements ActionListener  {
    private JFrame frame;
    private String nome,cognome, email, codicefiscale;
    private Presidio presidio;
    private Reparto reparto;
    private List<Presidio> ListaPresidi;
    private Utente utente;
    private JLabel welcomeLabel;
    private JButton backtoInserisciPresidio;

    HospitApp hospitapp = HospitApp.getInstance();

    public PrenotaVisita(JFrame frame, Utente utente) {

        this.frame=frame;
        this.utente = utente;
        this.nome=utente.getNome();
        this.cognome=utente.getCognome();
        this.email=utente.getEmail();
        this.codicefiscale=utente.getCodiceFiscale();
        welcomeLabel = new JLabel();
        showPrenotaVisitaUI();
    }

    protected void leggiPresidiDaFile(String filePath) {
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

    public void showPrenotaVisitaUI() {

        frame.add(welcomeLabel);
        welcomeLabel.setBounds(30, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));
        welcomeLabel.setText("Ciao " + nome);


        this.leggiPresidiDaFile("Presidio.txt");
        ListaPresidi = hospitapp.getElencoPresidi();

        int comboBoxXPosition = 100;
        int comboBoxYPosition = 250;

        for (Presidio presidio : ListaPresidi) {
            String nomeStruttura = presidio.getNome();

            JComboBox<String> repartoComboBox = new JComboBox<>();

            for (Reparto reparto : hospitapp.mostraReparti(presidio)) {
                repartoComboBox.addItem(reparto.getNome());
            }

            repartoComboBox.setBounds(comboBoxXPosition, comboBoxYPosition, 150, 40);
            repartoComboBox.setFocusable(false);
            repartoComboBox.addActionListener(this);

            repartoComboBox.setBorder(BorderFactory.createTitledBorder(nomeStruttura));

            frame.add(repartoComboBox);

            comboBoxXPosition += 160;
            if (comboBoxXPosition + 150 > frame.getWidth()) {
                comboBoxXPosition = 100;
                comboBoxYPosition += 45;
            }
        }



        frame.revalidate();
        frame.repaint();


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Presidio presidio : ListaPresidi) {
            JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
            if (comboBox.getSelectedItem() != null) {
                hospitapp.prenotaVisita((presidio.getNome()));
                String repartoSelezionato = comboBox.getSelectedItem().toString();
                mostraMessaggio("Hai selezionato il reparto: " + repartoSelezionato);
                this.reparto = hospitapp.selezionaReparto(repartoSelezionato);

                // Rimuovi tutti i componenti dal frame corrente
                frame.getContentPane().removeAll();

                JLabel riepilogoLabel = new JLabel("Riepilogo:");
                riepilogoLabel.setBounds(100, 150, 200, 45);
                frame.add(riepilogoLabel);

                JLabel utenteLabel = new JLabel("Nome: " + nome + " | Email: " + email + " | CF: " + codicefiscale);
                utenteLabel.setBounds(100, 175, 400, 35);
                utenteLabel.setFont(new Font("Arial", Font.BOLD, 17));
                frame.add(utenteLabel);

                JLabel scelteLabel = new JLabel("Presidio: " + presidio.getNome() + " | Reparto: " + repartoSelezionato);
                scelteLabel.setBounds(100, 200, 400, 35);
                frame.add(scelteLabel);

                JButton bookVisitButton = new JButton("Prenota Visita");
                bookVisitButton.setBounds(100, 300, 150, 25);
                bookVisitButton.setFocusable(false);
                bookVisitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        hospitapp.confermaPrenotazione(reparto, presidio, utente);
                        mostraMessaggio("Visita prenotata con successo!");
                        InserisciPresidio inserisciPresidio = new InserisciPresidio(utente);
                        frame.dispose();
                    }
                });
                frame.add(bookVisitButton);

                JButton backtoInserisciPresidio = new JButton("Indietro");
                backtoInserisciPresidio.setBounds(170, 410, 100, 25);
                backtoInserisciPresidio.setFocusable(false);
                backtoInserisciPresidio.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        InserisciPresidio inserisciPresidio = new InserisciPresidio(utente);
                        frame.dispose();
                    }
                });
                frame.add(backtoInserisciPresidio);

                frame.revalidate();
                frame.repaint();

                break;
            }
        }
    }


    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }
}