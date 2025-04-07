package edu.okcu.teamproject2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class signUpController {
    public TextField enterName;
    public TextField enterEmail;
    public PasswordField passwordBox;
    public Button signUpButton;
    public Button cancelButton;
    public ToggleGroup studentProfessor;
    public RadioButton studentRadioButton;
    public RadioButton professorRadioButton;
    public TextField enterLastName;
    public TextField enterFirstName;

    String firstName;
    String lastName;
    String email;
    String password;
    int studentOrProfessor = 0;
    String salt = EncryptionMethods.getSalt();



    public signUpController() throws NoSuchAlgorithmException {
    }


    public void nameEntered(ActionEvent actionEvent) {
    }

    public void emailEntered(ActionEvent actionEvent) {
    }

    public void passwordEntered(ActionEvent actionEvent) {
    }

    public void signUpClick(ActionEvent actionEvent) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        firstName = enterFirstName.getText();
        lastName = enterLastName.getText();
        email = enterEmail.getText();
        password = passwordBox.getText();
        String saltedPassword = EncryptionMethods.encrypt(password, salt);

        if (studentOrProfessor == 10) {
            try {
                FileWriter write = new FileWriter("StudentData.txt", true);
                write.write("\n" + firstName + "\n" + lastName + "\n" + email + "\n" + saltedPassword + "\n" + salt + "\n");
                write.close();

            } catch (IOException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        }

        if (studentOrProfessor == 11) {
            try {
                FileWriter write = new FileWriter("ProfessorData.txt", true);
                write.write("\n" + firstName + "\n" + lastName + "\n" + email + "\n" + saltedPassword + "\n" + salt + "\n");
                write.close();

            } catch (IOException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        }


    }

    public void cancelClicked(ActionEvent actionEvent) throws IOException {
        Parent signUp = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Stage window = (Stage) cancelButton.getScene().getWindow();
        window.setScene(new Scene(signUp, 400, 400));
        window.setTitle("Login");
    }

    public int onStudentRadioButtonClick(ActionEvent actionEvent) {
        studentOrProfessor = 10;
        return studentOrProfessor;

    }

    public int onProfessorRadioButtonClick(ActionEvent actionEvent) {
        studentOrProfessor = 11;
        return studentOrProfessor;
    }

    public void LastNameEntered(ActionEvent actionEvent) {
    }

    public void firstNameEntered(ActionEvent actionEvent) {
    }
}
