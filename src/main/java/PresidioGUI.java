import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PresidioGUI implements ActionListener {
    protected JFrame frame;
    private JPanel addPanel;
    private JLabel nomeLabel, indirizzoLabel, orarioLabel, title, addMessageLabel;
    private JTextField nomeField, indirizzoField, orarioField;
    private JButton addButton, backToWelcomePage;
    private Utils utils;

    private String email;
//    private Presidio presidio;


    public PresidioGUI(String email) {
        this.email=email;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Hospital");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);

        title = new JLabel("Hospital");
        title.setBounds(160, 30, 200, 35);
        title.setFont(new Font(null, Font.ITALIC, 25));
        frame.add(title);

        addPanel = new JPanel();
        addPanel.setLayout(null);
        addPanel.setBounds(0, 60, 420, 280);
        addPanel.setVisible(true);

        addMessageLabel = new JLabel();
        addMessageLabel.setBounds(160, 250, 200, 35);
        addMessageLabel.setFont(new Font(null, Font.ITALIC, 15));
        addPanel.add(addMessageLabel);

        nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(50, 20, 75, 25);
        nomeField = new JTextField();
        nomeField.setBounds(125, 20, 200, 25);

        indirizzoLabel = new JLabel("Indirizzo:");
        indirizzoLabel.setBounds(50, 60, 75, 25);
        indirizzoField = new JTextField();
        indirizzoField.setBounds(125, 60, 200, 25);

        orarioLabel = new JLabel("Orario:");
        orarioLabel.setBounds(50, 100, 100, 25);
        orarioField = new JTextField();
        orarioField.setBounds(125, 100, 200, 25);

        addButton = new JButton("Aggiungi");
        addButton.setBounds(225, 150, 100, 25);
        addButton.setFocusable(false);
        addButton.addActionListener(this);


        backToWelcomePage = new JButton("Indietro");
        backToWelcomePage.setBounds(225, 220, 100, 25); // Modifica della posizione del pulsante
        backToWelcomePage.setFocusable(false);
        backToWelcomePage.addActionListener(this);
        addPanel.add(backToWelcomePage);

        addPanel.add(nomeLabel);
        addPanel.add(nomeField);
        addPanel.add(indirizzoField);
        addPanel.add(indirizzoLabel);
        addPanel.add(orarioLabel);
        addPanel.add(orarioField);
        addPanel.add(addButton);

        frame.add(addPanel);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String nome = nomeField.getText();
            String indirizzo = indirizzoField.getText();
            String orario = orarioField.getText();

            if (nome.isEmpty() || indirizzo.isEmpty() || orario.isEmpty()) {
                addMessageLabel.setForeground(Color.RED);
                addMessageLabel.setText("Compila tutti i campi!");
            } else {
                String hospital = nome + "," + indirizzo + "," + orario;
//                utils.writeOnFile("Presidio.txt", hospital);
                HospitApp hospitapp= HospitApp.getInstance();
                hospitapp.InserisciNuovoPresidio(nome, indirizzo, orario);
                hospitapp.confermaInserimento();
                addMessageLabel.setForeground(Color.GREEN);
                addMessageLabel.setText("Presidio registrato con successo!");

            }
        } else if (e.getSource() == backToWelcomePage) {
            frame.dispose();
            WelcomePage welcome = new WelcomePage(email);
            welcome.frame.setVisible(true);
        }


    }
}