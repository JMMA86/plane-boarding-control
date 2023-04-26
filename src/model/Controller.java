package model;

import dataStructures.HashTable;
import dataStructures.PriorityQueue;
import exception.DuplicatedKeyException;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Controller {

    private Random rnd;
    private HashTable<String, Reservation> reservations;
    private PriorityQueue<Integer, Reservation> entryOrder;
    public Controller() {
        this.reservations = new HashTable<>();
        this.entryOrder = new PriorityQueue<>();
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
     * This method search a reservation by its id and shows the reservation information if found.
     * @param id the unique ID for the reservation
     * @return reservationString The String representation of the reservation
     */
    public String searchReservationById(String id) {
        Reservation foundReservation = reservations.search(id);
        return (foundReservation != null) ? foundReservation.toString() : "\nReservation not found";
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
        String id = info[0].replaceAll(" ", "");
        String name = info[1];
        int rowNumber = Integer.parseInt(info[2]);
        char columnChar = info[3].replaceAll(" ", "").charAt(0);
        boolean[] priority = new boolean[PriorityCriteria.values().length];
        for(int i=0; i< priority.length; i++) {
            priority[i] = Boolean.parseBoolean(info[i+4]);
        }
        return new Reservation(id, name, priority, rowNumber, columnChar);
    }

    public void setPassengerEnterPriority() {
        
    }
}
