package model;

public class Reservation {
    private String id;
    private String passengerName;
    private boolean[] priority;
    private int rowNumber;
    private char columnChar;

    public Reservation(String id, String passengerName, boolean[] priority, int rowNumber, char columnChar) {
        this.id = id;
        this.passengerName = passengerName;
        this.priority = priority;
        this.rowNumber = rowNumber;
        this.columnChar = columnChar;
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

    @Override
    public String toString() {
        String id = String.format("%nReservation: %s%n - name: %s%n - seat: %d%c", this.id, this.passengerName, this.rowNumber, this.columnChar);
        for(int i=0; i<priority.length; i++) {
            id += String.format("%n - %s: %s", PriorityCriteria.values()[i].toString().toLowerCase().replaceAll("_", " "), ( priority[i] ? "yes" : "no") );
        }
        return id;
    }

}
