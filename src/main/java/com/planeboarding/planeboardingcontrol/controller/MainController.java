package com.planeboarding.planeboardingcontrol.controller;
import com.planeboarding.planeboardingcontrol.model.*;
import com.planeboarding.planeboardingcontrol.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private Label airlineNameLbl;

    @FXML
    private Label airlineDescriptionLbl;

    @FXML
    private Button openBoardingBtn;

    @FXML
    private FileManager fileManager = FileManager.getInstance();

    @FXML
    void onOpenBoarding(ActionEvent actionEvent) {
        Stage stage = (Stage) openBoardingBtn.getScene().getWindow();
        stage.close();
        MainApplication.renderView("boarding-view.fxml", 1280, 720);
    }
}