package com.planeboarding.planeboardingcontrol.model;

public class Reservation {
    private String id;
    private String passengerName;
    private boolean[] priority;
    private int rowNumber;
    private char columnChar;
    private boolean registered;

    public Reservation(String id, String passengerName, boolean[] priority, int rowNumber, char columnChar) {
        this.id = id;
        this.passengerName = passengerName;
        this.priority = priority;
        this.rowNumber = rowNumber;
        this.columnChar = columnChar;
        this.registered = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public boolean[] getPriority() {
        return priority;
    }

    public void setPriority(boolean[] priority) {
        this.priority = priority;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public char getColumnChar() {
        return columnChar;
    }

    public void setColumnChar(char columnChar) {
        this.columnChar = columnChar;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    /** this function shows a readable description of the current reservation
     * @return id The String representation of the reservation
     */
    public String showReservationInfo() {
        String id = String.format("%nReservation: %s%n - Name: %s%n - Seat: %d%c", this.id, this.passengerName, this.rowNumber, this.columnChar);
        for(int i=0; i<priority.length; i++) {
            String priorityCriteria = PriorityCriteria.values()[i].toString();
            id += String.format("%n - %s: %s", priorityCriteria.substring(0,1).toUpperCase() + priorityCriteria.substring(1).toLowerCase().replaceAll("_", " "), ( priority[i] ? "Yes" : "No") );
        }
        return id;
    }

    /** this function shows a readable description of the current reservation
     * @return id The String representation of the reservation in one line
     */
    public String showReservationInfoOneLine(boolean showPriorities) {
        String id = String.format("[ %s ] %s (%d%c) ", this.id, this.passengerName, this.rowNumber, this.columnChar);
        if (showPriorities) {
            id += "(";
            for(int i=0; i<priority.length; i++) {
                if(priority[i]) id += ", " + PriorityCriteria.values()[i].toString().toLowerCase().replaceAll("_", " ");
            }
            id += ")";
        }
        return id.replace("(,", "(");
    }

    @Override
    public String toString() {
        return showReservationInfoOneLine(false);
    }

}
