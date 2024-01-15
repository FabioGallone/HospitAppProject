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

public class WelcomePage implements ActionListener {
    private Utente utente;
    JFrame frame;
    private JLabel welcomeLabel;
    private JButton quitButton;
    private JButton addButton;
    private String email;




    public WelcomePage(String email) {
        this.email = email;
        utente = new Utente();
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


    private void initializeUI() {
        welcomeLabel.setBounds(0, 0, 400, 200);
        welcomeLabel.setFont(new Font(null, Font.PLAIN, 25));


        if (utente.isAdministrator(email)){
            welcomeLabel.setText("Ciao amministratore " + email);
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
            PresidioGUI presidioGUI = new PresidioGUI(this.email);
            presidioGUI.frame.setVisible(true);
        }
    }


}