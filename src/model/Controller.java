package model;

import dataStructures.HashTable;

public class Controller {
    private HashTable<String, Reservation> reservations;
    public Controller() {
        this.reservations = new HashTable<>();
    }
}
