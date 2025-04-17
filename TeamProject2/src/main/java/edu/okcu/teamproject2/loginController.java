package edu.okcu.teamproject2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class loginController {
    public Button studentLoginButton;
    public Button professorLoginButton;
    public Button signUpButton;
    @FXML

    public void onStudentLoginButtonClick(ActionEvent actionEvent) throws IOException {
        Parent studentLogin = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("studentLogin.fxml")));
        Stage window = (Stage) studentLoginButton.getScene().getWindow();
        window.setScene(new Scene(studentLogin, 300, 300));
        window.setTitle("Student Login");

    }

    public void onProfessorLoginButtonClick(ActionEvent actionEvent) {
    }

    public void onSignUpButtonClick(ActionEvent actionEvent) throws IOException {
        Parent signUp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("signUp.fxml")));
        Stage window = (Stage) signUpButton.getScene().getWindow();
        window.setScene(new Scene(signUp, 400, 400));
        window.setTitle("Sign Up");
    }
}