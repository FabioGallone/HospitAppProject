package ui;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private String email;
    private Presidio presidio;

    private Amministratore amministratore;
    private List<Presidio> ListaPresidi;
    private HospitApp hospitapp;

    public InserisciPresidio(Utente utente, HospitApp h) {
        this.utente = utente;
        this.nome = utente.getNome();
        this.email = utente.getEmail();
        this.hospitapp = h;


        if (utente.isAdministrator(email)) {
            amministratore=new Amministratore(utente.getNome(), utente.getCognome(), utente.getCodiceFiscale(), email, utente.getHashedPassword());
        }
        else if (utente.isPresidio(email)){
            presidio= new Presidio(utente.getNome(), utente.getCognome(), utente.getCodiceFiscale(), email, utente.getHashedPassword());
        }


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

        if (amministratore!=null) {
            System.out.println("Amministratore");
            welcomeLabel.setText("Ciao amministratore " + nome);
            Utils.leggiPresidiDaFile("presidio.txt",hospitapp);
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

        } else if(presidio!=null) {
            System.out.println("Presidio");

            new GestisciPrenotazione(frame, presidio, hospitapp);


        }else{
            System.out.println("Utente");
            new PrenotaVisita(frame, (Paziente)utente ,hospitapp);
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

            LoginGUI login = new LoginGUI(hospitapp);
            login.frame.setVisible(true);
        } else if (e.getSource() == addButton) {
            frame.dispose();
            InserisciReparto inseriscireparto = new InserisciReparto(amministratore,hospitapp);
            inseriscireparto.frame.setVisible(true);
        } else if(amministratore!=null) {
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
        System.out.println("Reparti del presidio " +presidio.getNome()+":"+reparti);
        StringBuilder repartiText = new StringBuilder("Reparti del presidio " + presidio.getNome() + ":\n");
        for (Reparto reparto : reparti) {
            repartiText.append(reparto.getNome()).append("\n");
        }

        JOptionPane.showMessageDialog(frame, repartiText.toString(), "Reparti", JOptionPane.INFORMATION_MESSAGE);
    }




}