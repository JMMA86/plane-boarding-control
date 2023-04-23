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

    private String generateRandomId() {
        char[] charId = new char[6];
        for(int i=0; i<charId.length; i++) {
            charId[i] = (char)rnd.nextInt(65,91);
        }
        String id = new String(charId);
        return (reservations.search(id) == null) ? id : generateRandomId();
    }
}
