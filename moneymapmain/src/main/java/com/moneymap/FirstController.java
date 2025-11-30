package com.moneymap;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class FirstController {

    public void setStageTitle(javafx.stage.Stage primaryStage) {
        primaryStage.setTitle("MoneyMap: A Personal Expense Tracking System");
    }

    @FXML
    private void switchToSecond() throws IOException {
        App.setRoot("Second");
    }

        @FXML
    private void onAddIncome(ActionEvent event) {
        try {
            App.setRoot("Second");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddExpense(ActionEvent event) {
        try {
            App.setRoot("Third");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onViewReports(ActionEvent event) {
        try {
            App.setRoot("Fourth");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSettings(ActionEvent event) {
        System.out.println("Settings selected");
    }
}