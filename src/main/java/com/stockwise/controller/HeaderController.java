package com.stockwise.controller;

import com.stockwise.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HeaderController {

    @FXML
    private Label titleLabel;

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void handleBack(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/fxml/dashboard.fxml");
    }
}
