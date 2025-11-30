package com.moneymap;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.nio.file.*;

public class SecondController {

    @FXML
    private void switchToFirst() throws IOException {
        App.setRoot("First");
    }

        private static final ObservableList<String> savedIncomes = FXCollections.observableArrayList();
        private static final String CSV_FILE = "income_data.csv";
        private static boolean dataLoaded = false;

    @FXML private HBox addIncomeBar;
    @FXML private TextField incomeAmountField;
    @FXML private TextField incomeDescField;
    @FXML private TextField incomeDateField;
    @FXML private javafx.scene.control.DatePicker incomeDatePicker;
    @FXML private ListView<String> incomeListView;
    @FXML private javafx.scene.control.Label totalIncomeLabel;


    @FXML
    public void initialize() {
        incomeListView.setItems(savedIncomes);

        if (!dataLoaded) {
            loadIncomesFromCSV();
            dataLoaded = true;
        }

        updateTotalIncome();
        ContextMenu ctx = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> deleteSelectedIncome(null));
        ctx.getItems().add(delete);
        incomeListView.setContextMenu(ctx);
    }

    @FXML
    private void onAddIncome(ActionEvent event) {
        addIncomeBar.setVisible(true);
        addIncomeBar.setManaged(true);
        incomeAmountField.requestFocus();
    }

    @FXML
    private void cancelAddIncome(ActionEvent event) {
        incomeAmountField.clear();
        incomeDescField.clear();
        addIncomeBar.setVisible(false);
        addIncomeBar.setManaged(false);
    }

@FXML
private void saveIncome(ActionEvent event) {
    String amountText = incomeAmountField.getText().trim();
    String desc = incomeDescField.getText().trim();

    if (amountText.isEmpty()) {
        Alert a = new Alert(AlertType.WARNING, "Enter an amount");
        a.showAndWait();
        return;
    }

    if (incomeDatePicker.getValue() == null) {
        Alert a = new Alert(Alert.AlertType.WARNING, "Please select a date");
        a.showAndWait();
        return;
    }

    String dateText = incomeDatePicker.getValue().toString();
    String entry = "$" + amountText + " on " + dateText;
    if (!desc.isEmpty()) {
        entry += " - " + desc;
    }

    savedIncomes.add(entry);
    saveIncomeToCSV(amountText, desc, dateText);
    System.out.println("Saved income: " + entry);

    // clear input
    incomeAmountField.clear();
    incomeDescField.clear();
    incomeDatePicker.setValue(null);

    updateTotalIncome();
}
    @FXML
    private void saveIncomeToCSV(String amount, String desc, String date) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            writer.write(amount + "," + desc + "," + date);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadIncomesFromCSV() {
        Path filePath = Paths.get(CSV_FILE);
        if (!Files.exists(filePath)) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                String amount = parts.length > 0 ? parts[0] : "";
                String desc = parts.length > 1 ? parts[1] : "";
                String date = parts.length > 2 ? parts[2] : "";
                String entry = "$" + amount;
                if (!desc.isEmpty()) entry += " - " + desc;
                if (!date.isEmpty()) entry += " on " + date;
                savedIncomes.add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateTotalIncome() {
        double total = 0.0;
        Pattern p = Pattern.compile("\\$(\\d+(?:\\.\\d+)?)");
        for (String entry : savedIncomes) {
            if (entry == null) continue;
            Matcher m = p.matcher(entry);
            if (m.find()) {
                try {
                    total += Double.parseDouble(m.group(1));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        totalIncomeLabel.setText(String.format("Total Income: $%.2f", total));
}

    @FXML
    private void deleteSelectedIncome(ActionEvent event) {
        String selected = incomeListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert a = new Alert(AlertType.INFORMATION, "No income selected to delete.");
            a.showAndWait();
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION, "Delete selected income? This will remove it from the list and the CSV.");
        Optional<javafx.scene.control.ButtonType> res = confirm.showAndWait();
        if (!res.isPresent() || res.get() != javafx.scene.control.ButtonType.OK) return;

            String entry = selected;
            String desc = "";
            String mainPart = entry;
            if (entry.contains(" - ")) {
                String[] parts = entry.split(" - ", 2);
                mainPart = parts[0];
                desc = parts[1];
            }

            if (desc.contains(" on ")) {
                int lastOn = desc.lastIndexOf(" on ");
                desc = desc.substring(0, lastOn).trim();
            }

            String amount = "";
            if (mainPart.startsWith("$")) {
                String after = mainPart.substring(1).trim();
                String[] toks = after.split("\\s+", 2);
                amount = toks[0];
            }

            String date = "";
            int lastOnInEntry = entry.lastIndexOf(" on ");
            if (lastOnInEntry >= 0) {
                date = entry.substring(lastOnInEntry + 4).trim();
                if (date.startsWith("on")) date = date.substring(2).trim();
            }

        savedIncomes.remove(selected);

        Path filePath = Paths.get(CSV_FILE);
        if (Files.exists(filePath)) {
            try {
                List<String> lines = Files.readAllLines(filePath);
                List<String> keep = new ArrayList<>();
                for (String line : lines) {
                    String[] parts = line.split(",", 3);
                    String a = parts.length > 0 ? parts[0] : "";
                    String d = parts.length > 1 ? parts[1] : "";
                    String dt = parts.length > 2 ? parts[2] : "";

                    boolean matchAmount = a.equals(amount);
                    boolean matchDesc = d.equals(desc);
                    boolean matchDate = false;
                        if (date.isEmpty()) {
                            matchDate = true;
                        } else {
                            String dtNorm = dt != null ? dt : "";
                            String dtCandidate = dtNorm.startsWith("on") ? dtNorm.substring(2).trim() : dtNorm;
                            matchDate = dtCandidate.contains(date) || dtNorm.contains(date) || dtCandidate.equals(date);
                        }

                        if (matchAmount && (desc.isEmpty() || matchDesc) && matchDate) {
                            continue;
                    }
                    keep.add(line);
                }
                Files.write(filePath, keep);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        updateTotalIncome();
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