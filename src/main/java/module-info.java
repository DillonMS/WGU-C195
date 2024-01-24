module smith.c195v2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens smith.c195v2 to javafx.fxml;
    exports smith.c195v2;
}