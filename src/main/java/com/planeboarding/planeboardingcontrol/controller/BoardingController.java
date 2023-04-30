package com.planeboarding.planeboardingcontrol.controller;

import com.planeboarding.planeboardingcontrol.MainApplication;
import com.planeboarding.planeboardingcontrol.exception.*;
import com.planeboarding.planeboardingcontrol.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class BoardingController {
    @FXML
    public Label passengerListLbl;
    public TextArea passengerListTA;
    public Button addPassengerBtn;
    public Label entryQueueLbl;
    public TextArea entryQueueTA;
    public Label exitQueueLbl;
    public TextArea exitQueueTA;
    public TextField passengerIdField;
    public Button searchPassengerBtn;
    @FXML
    private Button loadPassengersBtn;

    @FXML
    private Label airlineNameLbl;

    BoardingManager manager = new BoardingManager();


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
        } catch (IncorrectFormatException | IOException e) {
            e.getStackTrace();
            MainApplication.showAlert("Error adding passenger", "Text file is not in the correct format", Alert.AlertType.ERROR);
        }

    }

    public void onAddPassengerClick(ActionEvent actionEvent) {
        String targetId = passengerIdField.getText().toUpperCase();
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
            };
        } catch (ReservationNotFoundException e) {
            MainApplication.showAlert("Error registering passenger", "Reservation was not found, check the id and try again", Alert.AlertType.ERROR);
        } catch (KeyIsSmallerException e) {
            MainApplication.showAlert("Error registering passenger", "Passenger could not be added to the list", Alert.AlertType.ERROR);
        } catch (RepeatedPassengerException e) {
            MainApplication.showAlert("Error registering passenger", "Passenger is already added to the list", Alert.AlertType.ERROR);
        }
    }
}
