package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import domain.*;

public class GestisciPrenotazione implements ActionListener {
    private JFrame frame;
    private HospitApp hospitapp = HospitApp.getInstance();
    private String nome, cognome, email, codicefiscale, codicemedico;
    private Utente utente;
    private Reparto reparto;
    private JLabel welcomeLabel, label, orarioLabel,dataLabel,dataSelezionataLabel;
    private JFrame riepilogoFrame, nuovaFrame;
    private JPanel mainPanel, visiteDaPrenotarePanel,visitePrenotatePanel,orarioDataPanel;
    private  JButton visitaButton, confermaButton;
    private String nomeutente;
    private List<Presidio> ListaPresidi;
    private JComboBox<String> repartoComboBox;
    private JComboBox<?> comboBox;
    private JTextField orarioTextField;
    private JCalendar calendar;
    private JDateChooser dateChooser;
    JSpinner orarioSpinner;

    private JButton RimuoviTicket;

    private Medico medico = new Medico(nome, cognome, codicemedico);

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
        RimuoviTicket = new JButton();// Inizializzazione del pulsante del reparto selezionato
        showGestisciPrenotazioneUI();
    }



    public void showGestisciPrenotazioneUI() {
        frame.add(welcomeLabel);
        welcomeLabel.setBounds(30, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));
        welcomeLabel.setText("Ciao presidio " + nome);

        RimuoviTicket = new JButton("Visualizza Ticket Prenotati");
        RimuoviTicket.setBounds(75, 350, 200, 25);
        RimuoviTicket.setFocusable(false);
        frame.add(RimuoviTicket);

        RimuoviTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new RimuoviTicket(utente);
            }
        });



        Utils.leggiPresidiDaFile("Presidio.txt");
        utentiPerRepartoPresidio= Utils.leggiVisitedalFile("Visita.txt");
        ListaPresidi = hospitapp.getElencoPresidi();
        System.out.println(ListaPresidi);

        int comboBoxXPosition = 100;
        int comboBoxYPosition = 250;

        for (Presidio presidio : ListaPresidi) {

            if (this.nome.equals(presidio.getNome())) {
                String nomeStruttura = presidio.getNome();

              repartoComboBox = new JComboBox<>();

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
        comboBox = (JComboBox<?>) e.getSource();
        Presidio presidio = hospitapp.selezionaPresidio(utente.getNome());

        if (comboBox.getSelectedItem() != null) {
            String nomeRepartoSelezionato = comboBox.getSelectedItem().toString();
            this.reparto = hospitapp.selezionaReparto(nomeRepartoSelezionato);
            riepilogoFrame = new JFrame("Visite del reparto: " + reparto.getNome());
            riepilogoFrame.setSize(600, 400);
            riepilogoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            mainPanel = new JPanel(new GridLayout(1, 2));

            visiteDaPrenotarePanel = new JPanel();
            visitePrenotatePanel = new JPanel();

            visiteDaPrenotarePanel.setLayout(new BoxLayout(visiteDaPrenotarePanel, BoxLayout.Y_AXIS));
            visitePrenotatePanel.setLayout(new BoxLayout(visitePrenotatePanel, BoxLayout.Y_AXIS));


            List<String> utentiAssociati = hospitapp.visualizzaPrenotazioni(reparto, presidio, utentiPerRepartoPresidio);


            boolean titoloVisiteDaPrenotareAggiunto = false;
            boolean titoloVisitePrenotateAggiunto = false;

            if (utentiAssociati != null) {
                for (String codiceFiscale : utentiAssociati) {
                    visitaButton = new JButton("Prenotazione - CF: " + codiceFiscale);

                    Visita visita = hospitapp.trovaVisita(reparto.getNome(), presidio.getNome(), codiceFiscale);


                    if (!visita.isStato()) {
                        if (!titoloVisiteDaPrenotareAggiunto) {
                            label = new JLabel("Visite da prenotare: ");
                            visiteDaPrenotarePanel.add(label);
                            titoloVisiteDaPrenotareAggiunto = true;
                        }
                        visiteDaPrenotarePanel.add(visitaButton);
                    } else {
                        if (!titoloVisitePrenotateAggiunto) {
                            label = new JLabel("Visite prenotate: ");
                            visitePrenotatePanel.add(label);
                            titoloVisitePrenotateAggiunto = true;
                        }
                        visitePrenotatePanel.add(visitaButton);
                    }

                    visitaButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            nuovaFrame = new JFrame("Inserisci Orario e Data");
                            nuovaFrame.setSize(400, 300);
                            nuovaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            orarioDataPanel = new JPanel();
                            orarioDataPanel.setLayout(null);
                            orarioDataPanel.setBounds(0, 60, 420, 280);

                            orarioLabel = new JLabel("Orario:");
                            orarioLabel.setBounds(50, 20, 75, 25);

                            // Utilizza JSpinner per l'inserimento dell'orario
                            SpinnerDateModel orarioModel = new SpinnerDateModel();
                            orarioSpinner = new JSpinner(orarioModel);
                            JSpinner.DateEditor orarioEditor = new JSpinner.DateEditor(orarioSpinner, "HH:mm");
                            orarioSpinner.setEditor(orarioEditor);
                            orarioSpinner.setBounds(160, 20, 200, 25);

                            orarioDataPanel.add(orarioLabel);
                            orarioDataPanel.add(orarioSpinner);

                            dataLabel = new JLabel("Data Visita: ");
                            dataLabel.setBounds(50, 60, 75, 25);
                            dateChooser = new JDateChooser();
                            dateChooser.setBounds(160, 60, 200, 25);
                            orarioDataPanel.add(dateChooser);
                            orarioDataPanel.add(dataLabel);
                            riepilogoFrame.dispose();

                            confermaButton = new JButton("Conferma");
                            confermaButton.setBounds(160, 220, 100, 25);

                            confermaButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    Date orarioSelezionato = (Date) orarioSpinner.getValue();
                                    java.util.Date dataSelezionata = dateChooser.getDate();

                                    SimpleDateFormat sdfOrario = new SimpleDateFormat("HH:mm");
                                    String orarioInserito = sdfOrario.format(orarioSelezionato);

                                    SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
                                    String dataSelezionataFormattata = sdfData.format(dataSelezionata);

                                    System.out.println("Orario inserito: " + orarioInserito);
                                    System.out.println("Data selezionata: " + dataSelezionataFormattata);

                                    hospitapp.confermaGestione(visita, dataSelezionataFormattata, orarioInserito);
                                    Utils.aggiornaFileVisita("visita.txt", presidio.getNome(), reparto.getNome(), codiceFiscale, dataSelezionataFormattata, orarioInserito, utentiPerRepartoPresidio, visita.isStato());

                                    nuovaFrame.dispose();
                                }
                            });

                            nuovaFrame.add(confermaButton);
                            nuovaFrame.add(orarioDataPanel);
                            nuovaFrame.setVisible(true);

                        }
                    });
                }
            }

            if (titoloVisiteDaPrenotareAggiunto) {
                mainPanel.add(new JScrollPane(visiteDaPrenotarePanel));
            }
            if (titoloVisitePrenotateAggiunto) {
                mainPanel.add(new JScrollPane(visitePrenotatePanel));
            }

            riepilogoFrame.add(mainPanel);
            riepilogoFrame.setVisible(true);
        }
    }







}
