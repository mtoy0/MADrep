module com.example.teamproject2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.teamproject2 to javafx.fxml;
    exports com.example.teamproject2;
}