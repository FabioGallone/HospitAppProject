


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI implements ActionListener {

    protected JFrame frame;
    private JPanel loginPanel, registerPanel;
    private JLabel title, emailLabel, userPasswordLabel, nameLabel, surnameLabel, fiscalCodeLabel, regEmailLabel, regPasswordLabel, loginMessageLabel, registerMessageLabel;
    private JTextField emailField, nameField, surnameField, fiscalCodeField, regEmailField;
    private JPasswordField userPasswordField, regPasswordField;
    private JButton loginButton, registerButton, backToLoginButton, signUpButton;

       private Utente utente;
//    private FileManager fileManager;

    public LoginGUI() {
        initialize();
          utente= new Utente();
//        fileManager = new FileManager();
    }

    private void initialize() {
        frame = new JFrame("HospitApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);

        title = new JLabel("HospitApp");
        title.setBounds(160, 30, 200, 35);
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

//        registerButton = new JButton("Registrati");
//        registerButton.setBounds(225, 150, 100, 25);
//        registerButton.setFocusable(false);
//        registerButton.addActionListener(this);

        loginPanel.add(emailLabel);

        loginPanel.add(emailField);
        loginPanel.add(userPasswordLabel);
        loginPanel.add(userPasswordField);
        loginPanel.add(loginButton);
//        loginPanel.add(registerButton);
        frame.add(loginPanel);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
          if (e.getSource() == loginButton) {
            // Esegui la logica per il login
            String email = emailField.getText();
            String password = String.valueOf(userPasswordField.getPassword());

                if (utente.findUser(email, Utils.hashPassword(password))) {

                loginMessageLabel.setForeground(Color.GREEN);
                loginMessageLabel.setText("Login avvenuto con successo!");

                frame.dispose();

            } else {
                loginMessageLabel.setForeground(Color.RED);
                loginMessageLabel.setText("Password o email non corrispondono");
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