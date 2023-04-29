module com.planeboarding.planeboardingcontrol {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.planeboarding.planeboardingcontrol to javafx.fxml;
    exports com.planeboarding.planeboardingcontrol;
    exports com.planeboarding.planeboardingcontrol.model;
    opens com.planeboarding.planeboardingcontrol.model to javafx.fxml;
    exports com.planeboarding.planeboardingcontrol.controller;
    opens com.planeboarding.planeboardingcontrol.controller to javafx.fxml;
}