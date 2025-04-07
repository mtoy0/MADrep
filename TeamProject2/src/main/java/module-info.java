module edu.okcu.teamproject2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.okcu.teamproject2 to javafx.fxml;
    exports edu.okcu.teamproject2;
}