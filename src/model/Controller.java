package model;

import dataStructures.HashTable;
import dataStructures.PriorityQueue;
import exception.DuplicatedKeyException;
import exception.KeyIsSmallerException;

import java.io.*;
import java.util.Random;

public class Controller {
    private Random rnd;
    private static final int ROWS = 30;
    private HashTable<String, Reservation> reservations;
    private final PriorityQueue<Integer, Reservation> entryOrder;
    private final PriorityQueue<Integer, Reservation> exitOrder;
    public Controller() {
        this.reservations = new HashTable<>();
        this.entryOrder = new PriorityQueue<>();
        this.exitOrder = new PriorityQueue<>();
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
                    entryOrder.maxHeapInsert(calculateEntryPriority(newReservation), newReservation);
                    exitOrder.maxHeapInsert(calculateExitPriority(newReservation), newReservation);
                } catch(NumberFormatException | DuplicatedKeyException | KeyIsSmallerException e) {
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


    /**
     * This function generates the priority for a reservation based on a boolean array
     * where the first element represents a reservation for the first class. The sum of
     * all the true elements in the array will return the entrance priority for that
     * reservation.
     *
     * @param reservation the reservation recently added to the queue
     * @return The sum of all the trues in the boolean array calculated as 2 to the power
     *         of the position of that element.
     */
    private int calculateEntryPriority(Reservation reservation) {
        int ans = 0;
        if (reservation.getPriority()[reservation.getPriority().length - 1]) {
            for (int i = reservation.getPriority().length - 1; i >= 0; i--) {
                if (reservation.getPriority()[i]) {
                    ans += Math.pow(2, i);
                }
            }
        } else {
            ans = 1;
        }
        return ans;
    }

    /**
     * This function calculates the exit priority by the column of the passenger
     * the nearest he is to the hall and to the exit, the fastest he will leave the
     * airplane.
     *
     * @param reservation The reservation to be added
     * @return An int representing his order to leave the airplane
     */
    private int calculateExitPriority(Reservation reservation) {
        int ans = 0;
        switch (reservation.getColumnChar()) {
            case ('A'), ('F') -> ans = (ROWS - reservation.getRowNumber());
            case ('B'), ('E') -> ans = (ROWS - reservation.getRowNumber()) * 2;
            case ('C'), ('D') -> ans = (ROWS - reservation.getRowNumber()) * 3;
        }
        return ans;
    }

    public PriorityQueue<Integer, Reservation> getEntryOrder() {
        return entryOrder;
    }

    public PriorityQueue<Integer, Reservation> getExitOrder() {
        return exitOrder;
    }
}
