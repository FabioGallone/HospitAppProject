import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void writeOnFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura su file: " + e.getMessage());
        }
    }

    public static void writeOnFileObject(String fileName, Presidio presidio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            // Converti l'oggetto Presidio in una stringa (ad esempio, usando il metodo toString())
            String presidioString = presidio.toString();

            // Scrivi la stringa nel file
            writer.write(presidioString);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura su file: " + e.getMessage());
        }
    }

    public boolean isEmailAlreadyUsed(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length > 3 && userDetails[3].equals(email)) {
                    return true; // Email trovata nel file
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Email non trovata nel file
    }

    public static List<Presidio> readFromFile(String fileName) {
        List<Presidio> listaPresidi = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            // Utilizziamo un delimitatore personalizzato per estrarre le informazioni
            scanner.useDelimiter("[=,{}\\s]+");

            while (scanner.hasNext()) {
                if (scanner.next().equals("Presidio")) {
                    // Leggi il nome
                    scanner.next(); // Ignora la chiave 'nome'
                    scanner.next(); // Ignora l'uguale
                    String nome = scanner.next();

                    // Leggi l'indirizzo
                    scanner.next(); // Ignora la chiave 'indirizzo'
                    scanner.next(); // Ignora l'uguale
                    String indirizzo = scanner.next();

                    // Leggi l'orario
                    scanner.next(); // Ignora la chiave 'orario'
                    scanner.next(); // Ignora l'uguale
                    String orario = scanner.next();

                    // Ignora il resto e costruisci l'oggetto Presidio
                    Presidio presidio = new Presidio(nome, indirizzo, orario);
                    listaPresidi.add(presidio);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Errore durante la lettura da file: " + e.getMessage());
        }

        return listaPresidi;
    }



}
