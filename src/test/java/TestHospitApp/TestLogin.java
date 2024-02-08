package TestHospitApp;


import java.awt.event.ActionEvent;
import ui.LoginGUI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TestLogin {

    @Test
    void testRegistration() {
        LoginGUI loginGUI = new LoginGUI();


        String existingEmail = "c@m.com"; //email già esistente (utente già registrato)
        String invalidEmailFormat = "invalidemail"; // Un formato di email non valido

        loginGUI.nameField.setText("Giorgio");
        loginGUI.surnameField.setText("Di Bartolo");
        loginGUI.fiscalCodeField.setText("ABC123");
        loginGUI.regEmailField.setText(existingEmail);
        loginGUI.regPasswordField.setText("testPassword");
        loginGUI.actionPerformed(new ActionEvent(loginGUI.signUpButton, ActionEvent.ACTION_PERFORMED, null));
        assertEquals("Email gia in uso", loginGUI.registerMessageLabel.getText());

        //email non valida
        loginGUI.regEmailField.setText(invalidEmailFormat);
        loginGUI.actionPerformed(new ActionEvent(loginGUI.signUpButton, ActionEvent.ACTION_PERFORMED, null));
        assertEquals("Email nel formato non corretto", loginGUI.registerMessageLabel.getText());

        // Test di registrazione valida
        String validName = "Giorgio";
        String validSurname = "Di Bartolo";
        String validFiscalCode = "ABC123";
        String validEmail = "giorgio@gmail.com";
        String validPassword = "testPassword";

        loginGUI.nameField.setText(validName);
        loginGUI.surnameField.setText(validSurname);
        loginGUI.fiscalCodeField.setText(validFiscalCode);
        loginGUI.regEmailField.setText(validEmail);
        loginGUI.regPasswordField.setText(validPassword);
        loginGUI.actionPerformed(new ActionEvent(loginGUI.signUpButton, ActionEvent.ACTION_PERFORMED, null));
        assertEquals("Utente registrato con successo!", loginGUI.registerMessageLabel.getText());
    }

    @Test
    void testLogin() {
        LoginGUI loginGUI = new LoginGUI();


        String validEmail = "m@c.com";
        String validPassword = "123";
        String invalidEmail = "invalid@example.com";
        String invalidPassword = "invalidPassword";

        // Test login valido
        loginGUI.emailField.setText(validEmail);
        loginGUI.userPasswordField.setText(validPassword);
        loginGUI.actionPerformed(new ActionEvent(loginGUI.loginButton, ActionEvent.ACTION_PERFORMED, null));
        assertEquals("Login avvenuto con successo!", loginGUI.loginMessageLabel.getText());

        // test login non valido.
        loginGUI.emailField.setText(invalidEmail);
        loginGUI.userPasswordField.setText(invalidPassword);
        loginGUI.actionPerformed(new ActionEvent(loginGUI.loginButton, ActionEvent.ACTION_PERFORMED, null));
        assertEquals("Password o email non corrispondono", loginGUI.loginMessageLabel.getText());
    }


}
