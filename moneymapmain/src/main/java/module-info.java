module com.moneymap {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.moneymap to javafx.fxml;
    exports com.moneymap;
}
