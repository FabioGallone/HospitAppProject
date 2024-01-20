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

public class GestisciPrenotazione implements ActionListener {
    private JFrame frame;
    HospitApp hospitapp= HospitApp.getInstance();
    private String nome,cognome, email, codicefiscale,codicemedico;

    private Utente utente;
    private Reparto reparto;
    private JLabel welcomeLabel;
    private List<Presidio> ListaPresidi;
    private Medico medico = new Medico(nome,cognome,codicemedico);



    public GestisciPrenotazione(JFrame frame, Utente utente) {

        this.frame=frame;
        this.utente = utente;
        this.nome=utente.getNome();
        this.cognome=utente.getCognome();
        this.email=utente.getEmail();
        this.codicefiscale=utente.getCodiceFiscale();
        welcomeLabel = new JLabel();
        medico.loadMedico();
        showGestisciPrenotazioneUI();
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


    public void showGestisciPrenotazioneUI(){

        frame.add(welcomeLabel);
        welcomeLabel.setBounds(30, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));
        welcomeLabel.setText("Ciaoooooooooooo " + nome);


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
                String repartoSelezionato = comboBox.getSelectedItem().toString();
                this.reparto = hospitapp.selezionaReparto(repartoSelezionato);

                // Creazione della finestra del riepilogo delle visite
                JFrame riepilogoFrame = new JFrame("Visite del reparto: " + reparto.getNome());
                riepilogoFrame.setSize(400, 300);
                riepilogoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                riepilogoFrame.setLayout(new BorderLayout());

                // Ottieni l'elenco delle visite del reparto
                List<Visita> visiteReparto = reparto.getVisite();

                // Crea un componente (ad esempio, JTextArea) per visualizzare le visite
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);

                // Aggiungi le visite al componente
                for (Visita visita : visiteReparto) {
                    textArea.append(visita.toString() + "\n");
                }

                // Aggiungi il componente alla finestra
                JScrollPane scrollPane = new JScrollPane(textArea);
                riepilogoFrame.add(scrollPane, BorderLayout.CENTER);

                // Rendi la finestra visibile
                riepilogoFrame.setVisible(true);

                break;
            }
        }
    }


    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }








}
