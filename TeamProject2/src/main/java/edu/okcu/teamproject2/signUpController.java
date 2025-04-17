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
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class signUpController {
    public TextField enterEmail;
    public PasswordField passwordBox;
    public Button signUpButton;
    public Button cancelButton;
    public ToggleGroup studentProfessor;
    public RadioButton studentRadioButton;
    public RadioButton professorRadioButton;
    public TextField enterLastName;
    public TextField enterFirstName;
    public Label signUpLabel;

    String firstName;
    String lastName;
    String email;
    String password;
    int studentOrProfessor = 0;
    String salt = EncryptionMethods.getSalt();
    int signUpNumber = 0;
    String saltedPassword;




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
        saltedPassword = EncryptionMethods.encrypt(password, salt);

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
        ) {
            studentOrProfessor = 0;
            signUpLabel.setText("Please fill all fields");
            studentProfessor.selectToggle(null);
        }

        if (studentOrProfessor == 10) {
            String studentID = generateStudentId(firstName, lastName);
            try {
                FileWriter write = new FileWriter("StudentData.txt", true);
                write.write(studentID + "\n" + firstName + "\n" + lastName + "\n" + email + "\n" + saltedPassword + "\n" + salt + "\n");
                write.close();
                signUpLabel.setText("Sign Up Registered");

            } catch (IOException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        }

        if (studentOrProfessor == 11) {
            try {
                String studentID = generateStudentId(firstName, lastName);
                FileWriter write = new FileWriter("ProfessorData.txt", true);
                write.write(studentID + "\n" + firstName + "\n" + lastName + "\n" + email + "\n" + saltedPassword + "\n" + salt + "\n");
                write.close();
                signUpLabel.setText("Sign Up Registered");

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

    public String generateStudentId(String firstName, String lastName) {
        // Use the first letter of first name, the first letter of last name, and a random number to generate a unique ID
        String initials = firstName.substring(0,1).toUpperCase() + lastName.substring(0,1).toUpperCase();
        Random rand = new Random();
        int randomNum = rand.nextInt(10000)+1;
        return initials + randomNum;
    }
}
