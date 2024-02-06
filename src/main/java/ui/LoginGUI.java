package ui;



import domain.Utente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI implements ActionListener {

    protected JFrame frame;
    private JPanel loginPanel, registerPanel;
    private JLabel title;
    private JLabel emailLabel;
    private JLabel userPasswordLabel;
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel fiscalCodeLabel;
    private JLabel regEmailLabel;
    private JLabel regPasswordLabel;
    public JLabel loginMessageLabel;
    public JLabel registerMessageLabel;
    public JTextField emailField;
    public JTextField nameField;
    public JTextField surnameField;
    public JTextField fiscalCodeField;
    public JTextField regEmailField;
    public JPasswordField userPasswordField;
    public JPasswordField regPasswordField;
    public JButton loginButton;
    private JButton registerButton;
    private JButton backToLoginButton;
    public JButton signUpButton;





    public LoginGUI() {
        Utils.populateUtentiListFromFile("Users.txt");


        initialize();
    }

    private void initialize() {
        frame = new JFrame("HospitApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);

        title = new JLabel("HospitApp");
        title.setBounds(150, 30, 200, 35);
        title.setFont(new Font(null, Font.ITALIC, 25));
        frame.add(title);


        // Pannello di login
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBounds(0, 100, 420, 250);


        loginMessageLabel = new JLabel();
        loginMessageLabel.setBounds(125, 200, 200, 35);
        loginMessageLabel.setFont(new Font(null, Font.ITALIC, 15));
        loginPanel.add(loginMessageLabel);

        // Componenti per il login
        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 50, 75, 25);
        emailField = new JTextField();
        emailField.setBounds(125, 50, 200, 25);

        userPasswordLabel = new JLabel("Password:");
        userPasswordLabel.setBounds(50, 90, 75, 25);
        userPasswordField = new JPasswordField();
        userPasswordField.setBounds(125, 90, 200, 25);

        loginButton = new JButton("Login");
        loginButton.setBounds(125, 150, 100, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);

        registerButton = new JButton("Registrati");
        registerButton.setBounds(225, 150, 100, 25);
        registerButton.setFocusable(false);
        registerButton.addActionListener(this);

        loginPanel.add(emailLabel);

        loginPanel.add(emailField);
        loginPanel.add(userPasswordLabel);
        loginPanel.add(userPasswordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        frame.add(loginPanel);

       //Registrazione
        registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBounds(0, 60, 420, 280);
        registerPanel.setVisible(false);

        registerMessageLabel = new JLabel();
        registerMessageLabel.setBounds(160, 250, 200, 35);
        registerMessageLabel.setFont(new Font(null, Font.ITALIC, 15));
        registerPanel.add(registerMessageLabel);

        // Componenti per la registrazione
        nameLabel = new JLabel("Nome:");
        nameLabel.setBounds(50, 20, 75, 25);
        nameField = new JTextField();
        nameField.setBounds(125, 20, 200, 25);

        // Altri campi per la registrazione
        surnameLabel = new JLabel("Cognome:");
        surnameLabel.setBounds(50, 60, 75, 25);
        surnameField = new JTextField();
        surnameField.setBounds(125, 60, 200, 25);

        fiscalCodeLabel = new JLabel("CF:");
        fiscalCodeLabel.setBounds(50, 100, 100, 25);
        fiscalCodeField = new JTextField();
        fiscalCodeField.setBounds(125, 100, 200, 25);

        regEmailLabel = new JLabel("Email:");
        regEmailLabel.setBounds(50, 140, 75, 25);
        regEmailField = new JTextField();
        regEmailField.setBounds(125, 140, 200, 25);

        regPasswordLabel = new JLabel("Password:");
        regPasswordLabel.setBounds(50, 180, 75, 25);
        regPasswordField = new JPasswordField();
        regPasswordField.setBounds(125, 180, 200, 25);

        signUpButton = new JButton("Registrati");
        signUpButton.setBounds(125, 220, 100, 25);
        signUpButton.setFocusable(false);
        signUpButton.addActionListener(this);

        backToLoginButton = new JButton("Indietro");
        backToLoginButton.setBounds(225, 220, 100, 25); // Modifica della posizione del pulsante
        backToLoginButton.setFocusable(false);
        backToLoginButton.addActionListener(this);


        registerPanel.add(nameLabel);
        registerPanel.add(nameField);
        registerPanel.add(surnameLabel);
        registerPanel.add(surnameField);
        registerPanel.add(fiscalCodeLabel);
        registerPanel.add(fiscalCodeField);
        registerPanel.add(regEmailLabel);
        registerPanel.add(regEmailField);
        registerPanel.add(regPasswordLabel);
        registerPanel.add(regPasswordField);
        registerPanel.add(signUpButton);
        registerPanel.add(backToLoginButton);
        frame.add(registerPanel);

        frame.setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            registerPanel.setVisible(true);
            loginPanel.setVisible(false);
            registerMessageLabel.setText(""); // Reset del messaggio di errore

        } else if (e.getSource() == backToLoginButton) {
            loginPanel.setVisible(true);
            registerPanel.setVisible(false);
            registerMessageLabel.setText(""); // Reset del messaggio di errore

        } else if (e.getSource() == loginButton) {
            // Esegui la logica per il login
            String email = emailField.getText();
            String password = String.valueOf(userPasswordField.getPassword());

            if (Utente.findUser(email, Utils.hashPassword(password))) {
                loginMessageLabel.setForeground(Color.GREEN);
                loginMessageLabel.setText("Login avvenuto con successo!");

                frame.dispose();
                InserisciPresidio inseriscipresidio = new InserisciPresidio(Utente.getUserFromEmail(email));
            } else {
                loginMessageLabel.setForeground(Color.RED);
                loginMessageLabel.setText("Password o email non corrispondono");
            }

        } else if (e.getSource() == signUpButton) {
            // Esegui la logica per la registrazione
            String name = nameField.getText();
            String surname = surnameField.getText();
            String fiscalCode = fiscalCodeField.getText();
            String email = regEmailField.getText();
            String password = String.valueOf(regPasswordField.getPassword());
            Boolean isAdministrator = false;
            Boolean isPresidio = false;

            if (name.isEmpty() || surname.isEmpty() || fiscalCode.isEmpty() || email.isEmpty() || password.isEmpty()) {
                registerMessageLabel.setForeground(Color.RED);
                registerMessageLabel.setText("Compila tutti i campi!");
            } else {
                String hashedPassword = Utils.hashPassword(password);

                String user = name + "," + surname + "," + fiscalCode + "," + email + "," + hashedPassword + "," + isAdministrator +","+ isPresidio;

                Utente utente=new Utente(name, surname, fiscalCode, email, hashedPassword, isAdministrator, isPresidio);

                if (!utente.isEmailAlreadyUsed(email) && Utils.isValidEmail(email)) {
                    Utils.writeOnFile("Users.txt", user);
                    registerMessageLabel.setForeground(Color.GREEN);
                    registerMessageLabel.setText("Utente registrato con successo!");
                    frame.dispose();
                    InserisciPresidio inseriscipresidio = new InserisciPresidio(utente);
                } else if (Utils.isEmailAlreadyUsed(email)) {
                    registerMessageLabel.setForeground(Color.RED);
                    registerMessageLabel.setText("Email gia in uso");
                } else if (!Utils.isValidEmail(email)) {
                    registerMessageLabel.setForeground(Color.RED);
                    registerMessageLabel.setText("Email nel formato non corretto");
                }
            }
        }
    }


    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginGUI appGUI = new LoginGUI();
                appGUI.frame.setVisible(true);
            }
        });
    }


}