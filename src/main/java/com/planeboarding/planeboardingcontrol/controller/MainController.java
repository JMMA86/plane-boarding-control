package com.planeboarding.planeboardingcontrol.controller;

import com.planeboarding.planeboardingcontrol.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Button openBoardingBtn;

    @FXML
    void onOpenBoarding() {
        Stage stage = (Stage) openBoardingBtn.getScene().getWindow();
        stage.close();
        MainApplication.renderView("boarding-view.fxml", 1200, 300);
    }
}