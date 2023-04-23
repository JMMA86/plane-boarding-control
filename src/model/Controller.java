package model;

import dataStructures.HashTable;
import exception.DuplicatedKeyException;

import java.io.*;
import java.util.Random;

public class Controller {

    private Random rnd;
    private HashTable<String, Reservation> reservations;
    public Controller() {
        this.reservations = new HashTable<>();
        this.rnd = new Random();
    }

    /**
     * This method reads data from a text file called located in data folder, parses its information and registers a new reservation based on that information.
     * @throws NumberFormatException if the input file contains a non-numeric character where a number is expected
     */
    public void readDataFromFile() {
        File file = new File("data/sampleText.txt");
        String line;
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            while( (line = br.readLine()) != null ) {
                try {
                    Reservation newReservation = parseLineIntoReservation(line);
                    reservations.insert(newReservation.getId(), newReservation);
                } catch(NumberFormatException | DuplicatedKeyException e) {
                    e.getStackTrace();
                }
            }
        } catch(IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * This method extracts the attributes of the reservation from the plain text file and returns a new Reservation with that information.
     * @param line The initial text line, it is separated by ' | ' character
     * @return newReservation The new reservation created from the text line
     * @throws NumberFormatException if the rowNumber is not a valid integer
     * @throws DuplicatedKeyException if the generated id for the reservation already exists in the reservations hash table
     */
    private Reservation parseLineIntoReservation(String line) throws NumberFormatException, DuplicatedKeyException {
        String[] info = line.split(" \\| ");
        String name = info[0];
        int rowNumber = Integer.parseInt(info[1]);
        char columnChar = info[2].replaceAll(" ", "").charAt(0);
        boolean[] priority = new boolean[PriorityCriteria.values().length];
        for(int i=0; i< priority.length; i++) {
            priority[i] = Boolean.parseBoolean(info[i+3]);
        }
        return new Reservation(generateRandomId(), name, priority, rowNumber, columnChar);
    }

    /**
     * This method generates random length 6 String IDs ensuring that they are not repeated in the hash table.
     * @return id A String representing the random ID
     */
    private String generateRandomId() {
        char[] charId = new char[6];
        for(int i=0; i<charId.length; i++) {
            charId[i] = (char)rnd.nextInt(65,91);
        }
        String id = new String(charId);
        return (reservations.search(id) == null) ? id : generateRandomId();
    }
}
