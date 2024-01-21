import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

public class GestisciPrenotazione implements ActionListener {
    private JFrame frame;
    private HospitApp hospitapp = HospitApp.getInstance();
    private String nome, cognome, email, codicefiscale, codicemedico;

    private Utente utente;
    private Reparto reparto;
    private JLabel welcomeLabel;
    private String nomeutente;
    private List<Presidio> ListaPresidi;
    private Utente u;
    private Medico medico = new Medico(nome, cognome, codicemedico);
    private List<Visita> listVisita = new ArrayList<>();
    private Map<String, List<String>> utentiPerRepartoPresidio;

    public GestisciPrenotazione(JFrame frame, Utente utente) {
        this.frame = frame;
        this.utente = utente;
        this.nome = utente.getNome();
        this.cognome = utente.getCognome();
        this.email = utente.getEmail();
        this.codicefiscale = utente.getCodiceFiscale();
        welcomeLabel = new JLabel();
        medico.loadMedico();
        utentiPerRepartoPresidio = new HashMap<>();
        showGestisciPrenotazioneUI();
    }

    private void leggiVisitedalFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String nomePresidio = data[0].trim();
                    String nomeReparto = data[1].trim();
                    nomeutente = data[2].trim();

                    Reparto reparto = hospitapp.selezionaReparto(nomeReparto);
                    Presidio presidio = hospitapp.selezionaPresidio(nomePresidio);

                    if (reparto != null && presidio != null) {
                        Visita visita = hospitapp.confermaPrenotazione(reparto, presidio, utente.getUserFromName(nomeutente));
                        String chiave = nomePresidio + "_" + nomeReparto;
                        utentiPerRepartoPresidio.computeIfAbsent(chiave, k -> new ArrayList<>()).add(nomeutente);
                    } else {
                        System.out.println("Reparto o Presidio non trovato: " + nomeReparto + ", " + nomePresidio);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGestisciPrenotazioneUI() {
        frame.add(welcomeLabel);
        welcomeLabel.setBounds(30, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));
        welcomeLabel.setText("Ciaoooooooooooo " + nome);

        this.leggiPresidiDaFile("Presidio.txt");
        this.leggiVisitedalFile("Visita.txt");
        ListaPresidi = hospitapp.getElencoPresidi();

        int comboBoxXPosition = 100;
        int comboBoxYPosition = 250;

        for (Presidio presidio : ListaPresidi) {
            if (this.nome.equals(presidio.getNome())) {
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
                break;
            }

        }

        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<?> comboBox = (JComboBox<?>) e.getSource();
        Presidio presidio = hospitapp.selezionaPresidio(utente.getNome());
        List<Reparto> elencoReparti = presidio.getElencoRepartidelPresidio();

        if (comboBox.getSelectedItem() != null) {
            String nomeRepartoSelezionato = comboBox.getSelectedItem().toString();
            this.reparto = hospitapp.selezionaReparto(nomeRepartoSelezionato);

            JFrame riepilogoFrame = new JFrame("Visite del reparto: " + reparto.getNome());
            riepilogoFrame.setSize(300, 200);
            riepilogoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel buttonPanel = new JPanel();
//            buttonPanel.setLayout(new GridLayout(3, 1));

            String chiaveMappa = presidio.getNome() + "_" + reparto.getNome();
            List<String> utentiAssociati = utentiPerRepartoPresidio.get(chiaveMappa);

            System.out.println("Utenti associati a " + chiaveMappa + ": " + utentiAssociati);

            if (utentiAssociati != null) {
                for (String utente : utentiAssociati) {
                    JButton visitaButton = new JButton("Prenotazione del/la Signor/a: " + utente);
                    visitaButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFrame nuovaFrame = new JFrame("Inserisci Orario e Data");
                            nuovaFrame.setSize(400, 300);
                            nuovaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            JPanel orarioDataPanel = new JPanel();
                            orarioDataPanel.setLayout(new GridLayout(2, 2));

                            JLabel orarioLabel = new JLabel("Inserisci Orario:");
                            JTextField orarioTextField = new JTextField();
                            orarioDataPanel.add(orarioLabel);
                            orarioDataPanel.add(orarioTextField);

                            JLabel dataLabel = new JLabel("Data selezionata:");
                            JLabel dataSelezionataLabel = new JLabel();
                            orarioDataPanel.add(dataLabel);
                            orarioDataPanel.add(dataSelezionataLabel);

                            nuovaFrame.add(orarioDataPanel, BorderLayout.NORTH);

                            // Rendi il campo di inserimento della data più piccolo
                            JCalendar calendar = new JCalendar();
                            JDateChooser dateChooser = new JDateChooser(calendar.getDate());
                            nuovaFrame.add(dateChooser, BorderLayout.CENTER);

                            dateChooser.addPropertyChangeListener("date", new PropertyChangeListener() {
                                @Override
                                public void propertyChange(PropertyChangeEvent evt) {
                                    java.util.Date dataSelezionata = dateChooser.getDate();
                                    System.out.println("Data selezionata: " + dataSelezionata);

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    dataSelezionataLabel.setText(sdf.format(dataSelezionata));
                                }
                            });

                            JButton confermaButton = new JButton("Conferma");
                            confermaButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String orarioInserito = orarioTextField.getText();
                                    java.util.Date dataSelezionata = dateChooser.getDate();

                                    System.out.println("Orario inserito: " + orarioInserito);
                                    System.out.println("Data selezionata: " + dataSelezionata);

                                    nuovaFrame.dispose();
                                }
                            });

                            nuovaFrame.add(confermaButton, BorderLayout.SOUTH);
                            nuovaFrame.setVisible(true);
                        }
                    });
                    buttonPanel.add(visitaButton);
                }
            }

            riepilogoFrame.add(buttonPanel, BorderLayout.CENTER);

            riepilogoFrame.setVisible(true);
        }
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

    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }
}
