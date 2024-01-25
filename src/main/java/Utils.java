import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public static String generaCodiceCasuale(int lunghezza) {
        String caratteriPerCodice = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codiceCasuale = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < lunghezza; i++) {
            int index = random.nextInt(caratteriPerCodice.length());
            char carattereCasuale = caratteriPerCodice.charAt(index);
            codiceCasuale.append(carattereCasuale);
        }

        return codiceCasuale.toString();
    }

    public static void leggiPresidiDaFile(String filePath) {
        HospitApp hospitapp= HospitApp.getInstance();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    Presidio presidio = hospitapp.InserisciNuovoPresidio(data[0].trim(), data[1].trim(), data[2].trim());

                    for (int j = 3; j < data.length; j++) {
                        Reparto r = hospitapp.selezionaReparto(data[j].trim());
                        if (r != null) {
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

    public static void ScrivisuFileVisita(Reparto reparto, Presidio presidio,Utente utente, Visita visita){

        String contentToWrite = presidio.getNome() + "," + reparto.getNome() +","+ utente.getCodiceFiscale() +"," + visita.getGiorno()+ "," + visita.getOra()+ "," + visita.isStato();
        Utils.writeOnFile("Visita.txt", contentToWrite);

    }

    public static Map<String, List<String>> leggiVisitedalFile(String filePath) {
        HospitApp hospitapp= HospitApp.getInstance();
        Utente utente= new Utente();
        Map<String, List<String>> utentiPerRepartoPresidio=new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    String nomePresidio = data[0].trim();
                    String nomeReparto = data[1].trim();
                     String codiceFiscale = data[2].trim();
                     String date= data[3].trim();
                     String ora= data[4].trim();
                     boolean  isStato= Boolean.parseBoolean(data[5].trim());

                    Reparto reparto = hospitapp.selezionaReparto(nomeReparto);
                    Presidio presidio = hospitapp.selezionaPresidio(nomePresidio);

                    if (reparto != null && presidio != null) {
                        Visita visita  = hospitapp.confermaPrenotazione(reparto, presidio, utente.getUserFromCF(codiceFiscale));
                        visita.setStato(isStato);
                        visita.setOra(ora);
                        visita.setGiorno(date);

                        String chiave = nomePresidio + "_" + nomeReparto;
                        utentiPerRepartoPresidio.computeIfAbsent(chiave, k -> new ArrayList<>()).add(codiceFiscale);
                    } else {
                        System.out.println("Reparto o Presidio non trovato: " + nomeReparto + ", " + nomePresidio);
                        return null;
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return utentiPerRepartoPresidio;

    }

    public static void aggiornaFileVisita(String filePath, String nomePresidio, String nomeReparto, String codiceFiscale, String nuovaData, String nuovoOrario, Map<String, List<String>> utentiPerRepartoPresidio, boolean isStato) {
        String chiave = nomePresidio + "_" + nomeReparto;

        if (utentiPerRepartoPresidio.containsKey(chiave)) {
            List<String> utentiAssociati = utentiPerRepartoPresidio.get(chiave);

            for (String utente : utentiAssociati) {
                if (utente.equals(codiceFiscale)) {
                    try {
                        BufferedReader file = new BufferedReader(new FileReader(filePath));
                        String line;
                        StringBuilder inputBuffer = new StringBuilder();

                        while ((line = file.readLine()) != null) {
                            String[] data = line.split(",");
                            String presidio = data[0].trim();
                            String reparto = data[1].trim();
                            String utenteFromFile = data[2].trim();

                            if (presidio.equals(nomePresidio) && reparto.equals(nomeReparto) && utenteFromFile.equals(codiceFiscale)) {
                                // Aggiorna la riga nel file con la nuova data e il nuovo orario
                                line = presidio + "," + reparto + "," + utente + "," + nuovaData + "," + nuovoOrario +"," +isStato;
                            }

                            inputBuffer.append(line);
                            inputBuffer.append('\n');
                        }

                        file.close();

                        // Sovrascrivi il file con le nuove informazioni
                        FileOutputStream fileOut = new FileOutputStream(filePath);
                        fileOut.write(inputBuffer.toString().getBytes());
                        fileOut.close();

                        System.out.println("File visita.txt aggiornato con successo.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Prenotazione non trovata per la chiave: " + chiave);
        }
    }


}
