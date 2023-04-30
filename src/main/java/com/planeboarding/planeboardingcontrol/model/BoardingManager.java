package com.planeboarding.planeboardingcontrol.model;

import com.planeboarding.planeboardingcontrol.dataStructures.HashTable;
import com.planeboarding.planeboardingcontrol.dataStructures.PriorityQueue;
import com.planeboarding.planeboardingcontrol.exception.DuplicatedKeyException;
import com.planeboarding.planeboardingcontrol.exception.IncorrectFormatException;
import com.planeboarding.planeboardingcontrol.exception.KeyIsSmallerException;
import com.planeboarding.planeboardingcontrol.exception.ReservationNotFoundException;

import java.io.*;
import java.util.Random;

public class BoardingManager {

    private Random rnd;
    private static final int ROWS = 30;
    private static final int COLUMNS = 6;
    private HashTable<String, Reservation> reservations;
    private final PriorityQueue<Integer, Reservation> entryOrder;
    private final PriorityQueue<Integer, Reservation> exitOrder;
    public BoardingManager() {
        this.reservations = new HashTable<>();
        this.entryOrder = new PriorityQueue<>();
        this.exitOrder = new PriorityQueue<>();
        this.rnd = new Random();
    }

    /**
     * This method reads data from a text file called located in data folder, parses its information and registers a new reservation based on that information.
     * @throws NumberFormatException if the input file contains a non-numeric character where a number is expected
     */
    public void readDataFromFile(String path) throws IOException, IncorrectFormatException {
        File file = new File(path);
        String line;
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        while( (line = br.readLine()) != null ) {
            Reservation newReservation = parseLineIntoReservation(line);
            try {
                reservations.insert(newReservation.getId(), newReservation);
            } catch (DuplicatedKeyException e) {
                e.getStackTrace();
            }
        }
    }

    /**
     * This method search a reservation by its id and shows the reservation information if found.
     * @param id the unique ID for the reservation
     * @return reservationString The String representation of the reservation
     */
    public Reservation searchReservationById(String id) {
        return reservations.search(id.replaceAll(" ", ""));
    }

    /**
     * This method extracts the attributes of the reservation from the plain text file and returns a new Reservation with that information.
     * @param line The initial text line, it is separated by ' | ' character
     * @return newReservation The new reservation created from the text line
     * @throws NumberFormatException if the rowNumber is not a valid integer
     */
    private Reservation parseLineIntoReservation(String line) throws IncorrectFormatException, NumberFormatException {
        String[] info = line.split(" \\| ");
        if(info.length != 4 + PriorityCriteria.values().length) throw new IncorrectFormatException("Data is not formatted correctly");
        String id = info[0].replaceAll(" ", "");
        if(id.length() != 6) throw new IncorrectFormatException("IDs must have 6 characters");
        String name = info[1];
        int rowNumber = Integer.parseInt(info[2]);
        char columnChar = info[3].replaceAll(" ", "").charAt(0);
        if((int)columnChar < 65 || (int)columnChar > 70) throw new IncorrectFormatException("Characters must be between A and C");
        boolean[] priority = new boolean[PriorityCriteria.values().length];
        for(int i=0; i< priority.length; i++) {
            String currentPriority = info[i+4].replaceAll(" ", "");
            if( !(currentPriority.equalsIgnoreCase("TRUE") || currentPriority.equalsIgnoreCase("FALSE")) ) {
                throw new IncorrectFormatException("Data must be written in boolean type");
            }
            priority[i] = Boolean.parseBoolean(info[i+4]);
        }
        return new Reservation(id, name, priority, rowNumber, columnChar);
    }

    public String showPassengerList() {
        return reservations.toString();
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
        ans += calculateEntryRow(reservation);
        return ans;
    }

    /**
     * This function calculates the entry order for all the passengers according to its
     * proximity to the end of the plane
     *
     * @param reservation The reservation to be added to the plane
     * @return The priority according to the row of that belongs to the reservation
     */
    private int calculateEntryRow(Reservation reservation) {
        int ans = 0;
        switch (reservation.getColumnChar()) {
            case ('A'), ('F') -> ans = reservation.getRowNumber() * (COLUMNS / 2) - 3;
            case ('B'), ('E') -> ans = reservation.getRowNumber() * (COLUMNS / 2) - 2;
            case ('C'), ('D') -> ans = reservation.getRowNumber() * (COLUMNS / 2) - 1;
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
            case ('A'), ('F') -> ans = (ROWS - reservation.getRowNumber()) * (COLUMNS / 2) - 3;
            case ('B'), ('E') -> ans = (ROWS - reservation.getRowNumber()) * (COLUMNS / 2) - 2;
            case ('C'), ('D') -> ans = (ROWS - reservation.getRowNumber()) * (COLUMNS / 2) - 1;
        }
        return ans;
    }

    /** this method search a reservation by its id and adds it to the entry and exit queue.
     * @param id the reservation unique identifier
     * @return the added reservation if the process is successful
     * @throws ReservationNotFoundException when the reservation is not in the hash table
     * @throws KeyIsSmallerException when there is a problem with the key in the priority queue
     */
    public Reservation registerReservation(String id) throws ReservationNotFoundException, KeyIsSmallerException {
        Reservation reservation = searchReservationById(id);
        if(reservation == null) throw new ReservationNotFoundException("Reservation not found");
        entryOrder.maxHeapInsert(calculateEntryPriority(reservation), reservation);
        exitOrder.maxHeapInsert(calculateExitPriority(reservation), reservation);
        return reservation;
    }


    // GETTERS

    public PriorityQueue<Integer, Reservation> getExitOrder() {
        return exitOrder;
    }

    public PriorityQueue<Integer, Reservation> getEntryOrder() {
        return entryOrder;
    }

    public HashTable<String, Reservation> getReservations() {
        return reservations;
    }
}
