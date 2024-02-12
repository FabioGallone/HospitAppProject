package ui;

import domain.HospitApp;
import domain.Presidio;
import domain.Reparto;
import domain.Utente;

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
    public JFrame frame;
    private JLabel welcomeLabel;
    private JButton quitButton;
    private JButton addButton;
    private String nome;
    HospitApp hospitapp = HospitApp.getInstance();
    private String email;
    private boolean isAdministrator, isPresidio;
    private List<Presidio> ListaPresidi;

    public InserisciPresidio(Utente utente) {
        this.utente = utente;
        this.nome = utente.getNome();
        this.email = utente.getEmail();

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


    private void initializeUI() {

        welcomeLabel.setBounds(30, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));

        if (this.isAdministrator) {
            welcomeLabel.setText("Ciao amministratore " + nome);
            Utils.leggiPresidiDaFile("presidio.txt");
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
          new PrenotaVisita(frame, utente);

        }else{

           new GestisciPrenotazione(frame, utente);
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