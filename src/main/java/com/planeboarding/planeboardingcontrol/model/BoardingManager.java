package com.planeboarding.planeboardingcontrol.model;

import com.planeboarding.planeboardingcontrol.dataStructures.HashTable;
import com.planeboarding.planeboardingcontrol.dataStructures.PriorityQueue;
import com.planeboarding.planeboardingcontrol.exception.*;

import java.io.*;

public class BoardingManager {
    private static final int ROWS = 30;
    private static final int COLUMNS = 6;
    private final HashTable<String, Reservation> reservations;
    private final PriorityQueue<Integer, Reservation> entryOrder;
    private final PriorityQueue<Integer, Reservation> exitOrder;
    public BoardingManager() {
        this.reservations = new HashTable<>();
        this.entryOrder = new PriorityQueue<>();
        this.exitOrder = new PriorityQueue<>();
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
     * @return The priority according to the level of priority of the reservation
     */
    private int calculateEntryPriority(Reservation reservation) {
        int ans = 0;
        if (reservation.getPriority()[0]) {
            for (int i = reservation.getPriority().length - 1; i >= 1; i--) {
                if (reservation.getPriority()[i]) {
                    ans += Math.pow(2, reservation.getPriority().length - i);
                }
            }
            ans += reservation.getRowNumber() * Math.pow(2, reservation.getPriority().length) + ROWS;
        } else  {
            ans += reservation.getRowNumber();
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
    public Reservation registerReservation(String id) throws ReservationNotFoundException, KeyIsSmallerException, RepeatedPassengerException {
        Reservation reservation = searchReservationById(id);
        if(reservation == null) throw new ReservationNotFoundException("Reservation not found");
        if(reservation.isRegistered()) throw new RepeatedPassengerException("Passenger is already registered");
        entryOrder.maxHeapInsert(calculateEntryPriority(reservation), reservation);
        exitOrder.maxHeapInsert(calculateExitPriority(reservation), reservation);
        reservation.setRegistered(true);
        return reservation;
    }


    // GETTERS

    public PriorityQueue<Integer, Reservation> getExitOrder() {
        return exitOrder;
    }

    public PriorityQueue<Integer, Reservation> getEntryOrder() {
        return entryOrder;
    }
}
