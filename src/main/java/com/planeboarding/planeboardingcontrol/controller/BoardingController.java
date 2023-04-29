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
        File file = fileChooser.showOpenDialog(stage);
        if(file == null) {
            return;
        }

        String path = file.getAbsolutePath();

        try {
            manager.readDataFromFile(path);
            passengerListTA.setText(manager.showPassengerList());
        } catch (IncorrectFormatException | IOException e) {
            e.getStackTrace();
            MainApplication.showAlert("Error adding passenger", "Text file is not in the correct format", Alert.AlertType.ERROR);
        }

    }

    public void onAddPassengerClick(ActionEvent actionEvent) {
        String targetId = passengerIdField.getText();
        try {
            Reservation foundReservation = manager.registerReservation(targetId);
            MainApplication.showAlert("Passenger registered successfully", foundReservation.showReservationInfo(), Alert.AlertType.INFORMATION);
            entryQueueTA.setText(manager.getEntryOrder().getArray().toString());
            exitQueueTA.setText(manager.getExitOrder().getArray().toString());
        } catch (ReservationNotFoundException e) {
            MainApplication.showAlert("Error registering passenger", "Reservation was not found, check the id and try again", Alert.AlertType.ERROR);
        } catch (KeyIsSmallerException e) {
            MainApplication.showAlert("Error registering passenger", "Passenger could not be added to the list", Alert.AlertType.ERROR);
        }
    }
}
