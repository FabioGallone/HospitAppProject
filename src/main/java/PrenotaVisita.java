import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class PrenotaVisita implements ActionListener {
    private JFrame frame;
    private String nome, cognome, email, codicefiscale;
    private Reparto reparto;
    private List<Presidio> ListaPresidi;
    private Utente utente;
    private JLabel welcomeLabel, titoloLabel;
    private JComboBox<String> presidioComboBox;
    private JComboBox<String> repartoComboBox;
    private JButton repartoButton;  // Aggiunta dichiarazione del pulsante del reparto selezionato
    private Presidio  presidioCorrente;
    private String nomepresidioSelezionato, nomeRepartoSelezionato;
    HospitApp hospitapp = HospitApp.getInstance();

    public PrenotaVisita(JFrame frame, Utente utente) {
        this.frame = frame;
        this.utente = utente;
        this.nome = utente.getNome();
        this.cognome = utente.getCognome();
        this.email = utente.getEmail();
        this.codicefiscale = utente.getCodiceFiscale();
        welcomeLabel = new JLabel();
        titoloLabel = new JLabel();
        presidioComboBox = new JComboBox<>();
        repartoComboBox = new JComboBox<>();
        repartoButton = new JButton();  // Inizializzazione del pulsante del reparto selezionato


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
        frame.add(titoloLabel);
        titoloLabel.setBounds(65, 120, 400, 200);
        titoloLabel.setFont(new Font(null, Font.ITALIC, 15));
        titoloLabel.setText("SELEZIONA PRESIDIO E REPARTO ");


        this.leggiPresidiDaFile("Presidio.txt");
        ListaPresidi = hospitapp.getElencoPresidi();

        //aggiunge il menù a tendina con la lsita dei presidi
        for (Presidio presidio : ListaPresidi) {
            String nomeStruttura = presidio.getNome();
            presidioComboBox.addItem(nomeStruttura);
        }



        presidioComboBox.setBounds(100, 250, 150, 40);
        presidioComboBox.setFocusable(false);
        presidioComboBox.addActionListener(this);

        frame.add(presidioComboBox);

        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == presidioComboBox) {
            nomepresidioSelezionato = presidioComboBox.getSelectedItem().toString();

            // Rimuovi il componente repartoComboBox se è già presente
            if (repartoComboBox.getParent() != null) {
                frame.remove(repartoComboBox);
            }

            // Rimuovi gli ascoltatori associati a repartoComboBox
            for (ActionListener al : repartoComboBox.getActionListeners()) {
                repartoComboBox.removeActionListener(al);
            }

            // Aggiorna la lista dei reparti in base al presidio selezionato
            repartoComboBox.removeAllItems();
            presidioCorrente = hospitapp.selezionaPresidio(nomepresidioSelezionato);
            if (presidioCorrente != null) {
                for (Reparto reparto : hospitapp.mostraReparti(presidioCorrente)) {
                    repartoComboBox.addItem(reparto.getNome());
                }
            }

            repartoComboBox.setBounds(100, 300, 150, 40);
            repartoComboBox.setFocusable(false);
            repartoComboBox.addActionListener(this);

            frame.add(repartoComboBox);

            frame.revalidate();
            frame.repaint();
        } else if (e.getSource() == repartoComboBox) {

             nomeRepartoSelezionato = repartoComboBox.getSelectedItem().toString();

            hospitapp.prenotaVisita(presidioCorrente.getNome());
            this.reparto = hospitapp.selezionaReparto(nomeRepartoSelezionato);
            mostraMessaggio("Hai selezionato il presidio: " + nomepresidioSelezionato +  " e il reparto: " + nomeRepartoSelezionato);


            mostraRiepilogo();
            frame.revalidate();
            frame.repaint();
        }
    }

    private void mostraRiepilogo() {

        frame.getContentPane().removeAll();

        JLabel riepilogoLabel = new JLabel("Riepilogo:");
        riepilogoLabel.setBounds(100, 150, 200, 45);
        riepilogoLabel.setFont(new Font("Arial", Font.BOLD, 17));
        frame.add(riepilogoLabel);

        JLabel utenteLabel = new JLabel("Nome: " + nome + " | Email: " + email);
        utenteLabel.setBounds(100, 175, 400, 35);
        frame.add(utenteLabel);

        JLabel scelteLabel = new JLabel("Presidio: " + presidioComboBox.getSelectedItem() + " | Reparto: " + nomeRepartoSelezionato);
        scelteLabel.setBounds(100, 200, 400, 35);
        frame.add(scelteLabel);

        JButton bookVisitButton = new JButton("Prenota Visita");
        bookVisitButton.setBounds(100, 250, 150, 25);
        bookVisitButton.setFocusable(false);
        bookVisitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Visita visita = hospitapp.confermaPrenotazione(reparto, presidioCorrente, utente);
                Utils.ScrivisuFileVisita(reparto, presidioCorrente, utente, visita);
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
    }

    private void mostraMessaggio(String messaggio) {
        JOptionPane.showMessageDialog(frame, messaggio, "Messaggio", JOptionPane.INFORMATION_MESSAGE);
    }
}
