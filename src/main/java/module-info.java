module com.example.javafx_test2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.javafx_test2 to javafx.fxml;
    exports com.example.javafx_test2;
}