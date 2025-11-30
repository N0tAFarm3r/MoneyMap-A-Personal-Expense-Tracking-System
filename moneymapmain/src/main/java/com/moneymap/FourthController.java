package com.moneymap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class FourthController {

    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpenseLabel;
    @FXML private Label netLabel;
    @FXML private Label avgIncomeLabel;
    @FXML private Label avgExpenseLabel;

    private static final String INCOME_CSV = "income_data.csv";
    private static final String EXPENSE_CSV = "expense_data.csv";

    private double lastTotalIncome = 0.0;
    private double lastTotalExpense = 0.0;
    private double lastNet = 0.0;
    private double lastAvgIncome = 0.0;
    private double lastAvgExpense = 0.0;

    @FXML
    public void initialize() {
        try {
            computeAndShowReports();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToFirst() throws IOException {
        App.setRoot("First");
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
            computeAndShowReports();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSettings(ActionEvent event) {
        // TODO: open settings
        System.out.println("Settings selected");
    }

    @FXML
    private void refreshReports(ActionEvent event) {
        try {
            computeAndShowReports();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exportReports(ActionEvent event) {
        try {
            computeAndShowReports();

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Export Reports");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            chooser.setInitialFileName("moneymap-report.csv");

            File dest = null;
            try {
                dest = chooser.showSaveDialog(null);
            } catch (Exception ex) {
                dest = null;
            }

            if (dest == null) {
                String ts = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                dest = new File(System.getProperty("user.dir"), "moneymap-report-" + ts + ".csv");
            }

            try (BufferedWriter writer = Files.newBufferedWriter(dest.toPath(), StandardCharsets.UTF_8)) {
                writer.write("Metric,Value\n");
                writer.write(String.format("Total Income,%.2f\n", lastTotalIncome));
                writer.write(String.format("Total Expenses,%.2f\n", lastTotalExpense));
                writer.write(String.format("Net,%.2f\n", lastNet));
                writer.write(String.format("Average Income,%.2f\n", lastAvgIncome));
                writer.write(String.format("Average Expense,%.2f\n", lastAvgExpense));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void computeAndShowReports() {
        Path incomePath = Paths.get(INCOME_CSV);
        Path expensePath = Paths.get(EXPENSE_CSV);

        double totalIncome = 0.0;
        int incomeCount = 0;

        double totalExpense = 0.0;
        int expenseCount = 0;

        if (Files.exists(incomePath)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(INCOME_CSV))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 3);
                    if (parts.length > 0 && !parts[0].isEmpty()) {
                        try {
                            double amt = Double.parseDouble(parts[0]);
                            totalIncome += amt;
                            incomeCount++;
                        } catch (NumberFormatException ex) {
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Files.exists(expensePath)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(EXPENSE_CSV))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", 3);
                    if (parts.length > 0 && !parts[0].isEmpty()) {
                        try {
                            double amt = Double.parseDouble(parts[0]);
                            totalExpense += amt;
                            expenseCount++;
                        } catch (NumberFormatException ex) {
                            
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    double net = totalIncome - totalExpense;
    double avgIncome = (incomeCount > 0) ? (totalIncome / incomeCount) : 0.0;
    double avgExpense = (expenseCount > 0) ? (totalExpense / expenseCount) : 0.0;

    lastTotalIncome = totalIncome;
    lastTotalExpense = totalExpense;
    lastNet = net;
    lastAvgIncome = avgIncome;
    lastAvgExpense = avgExpense;

    totalIncomeLabel.setText(String.format("Total Income: $%.2f", totalIncome));
    totalExpenseLabel.setText(String.format("Total Expenses: $%.2f", totalExpense));
    netLabel.setText(String.format("Net: $%.2f", net));
    avgIncomeLabel.setText(String.format("Average Income: $%.2f", avgIncome));
    avgExpenseLabel.setText(String.format("Average Expense: $%.2f", avgExpense));
    }
}
