package ui;

import domain.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static List<Paziente> populateUtentiListFromFile(String fileName) {
        List<Paziente> utentiList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length == 7) {
                    String nome = userDetails[0];
                    String cognome = userDetails[1];
                    String codiceFiscale = userDetails[2];
                    String email = userDetails[3];
                    String hashedPassword = userDetails[4];
                    boolean isAdministrator = Boolean.parseBoolean(userDetails[5]);
                    boolean isPresidio = Boolean.parseBoolean(userDetails[6]);

                    Paziente utente = new Paziente(nome, cognome, codiceFiscale, email, hashedPassword);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return utentiList;
    }



    public static boolean isEmailAlreadyUsed(String email) {
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


    public static void leggiPresidiDaFile(String filePath, HospitApp hospitapp) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    Presidio presidio = hospitapp.InserisciNuovoPresidio(data[0].trim(), data[1].trim(), data[2].trim());

                    for (int j = 3; j < data.length; j++) {
                        Reparto r = hospitapp.selezionaReparto(data[j].trim());
                        if (r != null) {
                            presidio.inserisciReparti(r.getNome(), r.getCodice());
                        }
                    }

                    hospitapp.confermaInserimento();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ScrivisuFileVisita(Reparto reparto, Presidio presidio, Paziente utente, Visita visita){

        String contentToWrite = presidio.getNome() + "," + reparto.getNome() +","+ utente.getCodiceFiscale() +"," + visita.getGiorno()+ "," + visita.getOra()+ "," + visita.isStato();
        Utils.writeOnFile("Visita.txt", contentToWrite);

    }

    public static Map<String, List<String>> leggiVisitedalFile(String filePath, HospitApp hospitapp) {

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
                     boolean isStato= Boolean.parseBoolean(data[5].trim());

                    Reparto reparto = hospitapp.selezionaReparto(nomeReparto);

                    Presidio presidio = hospitapp.selezionaPresidio(nomePresidio);

                    if (reparto != null && presidio != null) {
                    Paziente utente= Paziente.getUserFromCF(codiceFiscale);
                        Visita visita  = hospitapp.confermaPrenotazione(reparto, presidio, utente);
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
                            if (data.length >= 3) {
                                String presidio = data[0].trim();
                                String reparto = data[1].trim();
                                String utenteFromFile = data[2].trim();

                                if (presidio.equals(nomePresidio) && reparto.equals(nomeReparto) && utenteFromFile.equals(codiceFiscale)) {
                                    // Aggiorna la riga nel file con la nuova data e il nuovo orario
                                    line = presidio + "," + reparto + "," + utente + "," + nuovaData + "," + nuovoOrario + "," + isStato;
                                }
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



    public static void rimuoviRigaDaFile(String filePath, String lineToRemove) {
        try {

            List<String> lines = Files.readAllLines(Paths.get(filePath));

            lines.removeIf(line -> line.contains(lineToRemove));


            Files.write(Paths.get(filePath), lines);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    protected static void rimuoviVisitaDalFile(String codiceFiscale, String giornoVisita, String oraVisita) {
        try {
            //stringa della prenotazione da rimuovere
            List<String> lines = Files.readAllLines(Paths.get("Visita.txt"));

            lines.removeIf(line -> line.contains(codiceFiscale) && line.contains(giornoVisita) && line.contains(oraVisita));

            Files.write(Paths.get("Visita.txt"), lines);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rimuoviTicketDalFile(String codiceFiscale, String giornoVisita) {
        try {
            //stringa della prenotazione da rimuovere
            List<String> lines = Files.readAllLines(Paths.get("Ticket.txt"));

            lines.removeIf(line -> line.contains(codiceFiscale) && line.contains(giornoVisita));

            Files.write(Paths.get("Ticket.txt"), lines);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static boolean leggiTicketdalFile(String filePath, String riepilogo) {
        File file = new File(filePath);

        // Verifica se il file è vuoto
        if (file.length() == 0) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 1) {
                    String nome = data[0].trim();
                    String cognome = data[1].trim();
                    String codiceFiscale = data[2].trim();
                    String giornoVisita = data[3].trim();
                    String oraVisita = data[4].trim();
                    String nomePresidio = data[5].trim();
                    String nomeReparto = data[6].trim();


                    if (riepilogo.contains(nome) && riepilogo.contains(cognome) && riepilogo.contains(codiceFiscale)
                            && riepilogo.contains(giornoVisita) && riepilogo.contains(oraVisita)
                            && riepilogo.contains(nomePresidio) && riepilogo.contains(nomeReparto)) {
                        return true;
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }
    public static boolean leggiPresidiodalFile(String filePath, String riepilogo) {
        File file = new File(filePath);

        // Verifica se il file è vuoto
        if (file.length() == 0) {
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 1) {
                    String nome = data[0].trim();
                    String indirizzo = data[1].trim();

                    if (riepilogo.contains(nome) && riepilogo.contains(indirizzo)){
                        return true;
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }



    public static List<String> VisualizzaTuttiTicket(String filePath) {
        File file = new File(filePath);
        List<String> elencoVisite=new ArrayList<>();
        // Verifica se il file è vuoto
        if (file.length() == 0) {
            return elencoVisite;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 1) {

                    String nome = data[0].trim();
                    String cognome = data[1].trim();
                    String codiceFiscale = data[2].trim();
                    String giornoVisita = data[3].trim();
                    String oraVisita = data[4].trim();
                    String nomePresidio = data[5].trim();
                    String nomeReparto = data[6].trim();
                    String nazionalità=data[7].trim();
                    String residenza = data[8].trim();
                    String dataNascitaFormatted=data[9].trim();
                    String costo=data[10].trim();


                        String informazioni = nome + "," + cognome + "," + codiceFiscale + "," +
                                giornoVisita + "," + oraVisita + "," + nomePresidio + "," +
                                nomeReparto + "," + nazionalità + "," + residenza+ "," + dataNascitaFormatted + "," + costo;



                        elencoVisite.add(informazioni);



                }
            }
            return elencoVisite;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }



    public static String LeggiFileStatoVisita(String fileName, Paziente utente) {
        List<Paziente> utentiList = new ArrayList<>();
        String state="FALSE";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                //Ricordiamo che il file StatoVisitaCambiato.txt contiene "VERO"+codicefiscale
                if (userDetails.length == 2) {
                    if(utente.getCodiceFiscale().equals(userDetails[1])) {
                        state = userDetails[0]; //Questo sarà "VERO"


                        return state;
                    }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return state;
    }

}
