package com.planeboarding.planeboardingcontrol.controller;

import com.planeboarding.planeboardingcontrol.MainApplication;
import com.planeboarding.planeboardingcontrol.exception.*;
import com.planeboarding.planeboardingcontrol.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class BoardingController {
    @FXML
    public Label passengerListLbl;
    @FXML
    public TextArea passengerListTA;
    @FXML
    public Label entryQueueLbl;
    @FXML
    public TextArea entryQueueTA;
    @FXML
    public Label exitQueueLbl;
    @FXML
    public TextArea exitQueueTA;
    @FXML
    public TextField passengerIdField;
    @FXML
    public Button searchPassengerBtn;
    @FXML
    private Button loadPassengersBtn;
    @FXML
    private Label airlineNameLbl;

    BoardingManager manager = new BoardingManager();

    int registeredCount = 0;

    FileChooser fileChooser = new FileChooser();

    public void onLoadPassengers() {
        Stage stage = (Stage)loadPassengersBtn.getScene().getWindow();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data"));
        File file = fileChooser.showOpenDialog(stage);
        if(file == null) {
            return;
        }

        String path = file.getAbsolutePath();

        try {
            manager.readDataFromFile(path);
            passengerListTA.setText(manager.showPassengerList().substring(1));
            entryQueueTA.setText("The entry queue will appear here");
            exitQueueTA.setText("The exit queue will appear here");
        } catch (IncorrectFormatException | IOException e) {
            e.getStackTrace();
            MainApplication.showAlert("Error adding passenger", "Text file is not in the correct format", Alert.AlertType.ERROR);
        }

    }

    public void onAddPassengerClick() {
        String targetId = passengerIdField.getText().toUpperCase();
        if (registeredCount == 180) {
            MainApplication.showAlert("Error registering passenger", "Registered passengers limit reached (30)", Alert.AlertType.ERROR);
            return;
        }
        try {
            Reservation foundReservation = manager.registerReservation(targetId);
            MainApplication.showAlert("Passenger registered successfully", foundReservation.showReservationInfo(), Alert.AlertType.INFORMATION);
            if (entryQueueTA.getText().equals("The entry queue will appear here")) {
                String text = manager.getEntryOrder().getArray().get(0).toString();
                int firstBracketIndex = text.indexOf("[");
                int lastBracketIndex = text.lastIndexOf(")");
                entryQueueTA.setText("1. " + text.substring(firstBracketIndex, lastBracketIndex + 1));
            } else {
                String textToAdd = "";
                for (int i = 0; i < manager.getEntryOrder().getArray().toArray().length; i++) {
                    String text = manager.getEntryOrder().getArray().get(i).toString();
                    int firstBracketIndex = text.indexOf("[");
                    int lastBracketIndex = text.lastIndexOf(")");
                    textToAdd += "\n" + (i+1) + ". " + text.substring(firstBracketIndex, lastBracketIndex + 1);
                }
                entryQueueTA.setText(textToAdd.substring(1));
            }
            if (exitQueueTA.getText().equals("The exit queue will appear here")) {
                String text = manager.getExitOrder().getArray().get(0).toString();
                int firstBracketIndex = text.indexOf("[");
                int lastBracketIndex = text.lastIndexOf(")");
                exitQueueTA.setText(text.substring(firstBracketIndex, lastBracketIndex + 1));
            } else {
                String textToAdd = "";
                for (int i = 0; i < manager.getExitOrder().getArray().toArray().length; i++) {
                    String text = manager.getExitOrder().getArray().get(i).toString();
                    int firstBracketIndex = text.indexOf("[");
                    int lastBracketIndex = text.lastIndexOf(")");
                    textToAdd += "\n" + (i+1) + ". " + text.substring(firstBracketIndex, lastBracketIndex + 1);
                }
                exitQueueTA.setText(textToAdd.substring(1));
            }
            registeredCount++;
        } catch (ReservationNotFoundException e) {
            MainApplication.showAlert("Error registering passenger", "Reservation was not found, check the id and try again", Alert.AlertType.ERROR);
        } catch (KeyIsSmallerException e) {
            MainApplication.showAlert("Error registering passenger", "Passenger could not be added to the list", Alert.AlertType.ERROR);
        } catch (RepeatedPassengerException e) {
            MainApplication.showAlert("Error registering passenger", "Passenger is already added to the list", Alert.AlertType.ERROR);
        }
    }
}
