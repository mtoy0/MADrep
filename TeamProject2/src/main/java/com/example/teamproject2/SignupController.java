package com.example.teamproject2;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;


public class SignupController {

    @FXML
    public TextField FirstNameField;
    @FXML
    public TextField LastNameField;
    @FXML
    public PasswordField PasswordField;
    @FXML
    public TextField EmailField;
    @FXML
    public Label statusLabel;
    @FXML
    private ComboBox<String> roleComboBox;

    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String role;
    public String salt = EncryptionMethods.getSalt();
    private String ID;

    public SignupController() throws NoSuchAlgorithmException {
    }


    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("student", "professor");

    }

    public void onSignupButtonClick () throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        firstName = FirstNameField.getText();
        lastName = LastNameField.getText();
        email = EmailField.getText();
        password = PasswordField.getText();
        role = roleComboBox.getValue();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()
            ) {
                statusLabel.setText("Please fill all fields");
            }

            ID = generateStudentId(firstName, lastName);


        // Ensure the student and professor files exist, creates them if they don't
        File studentFile = new File("student.txt");
        if (!studentFile.exists()) {
            if (!studentFile.createNewFile()) {
                System.out.println("Failed to create student.txt file.");
                statusLabel.setText("Error creating student file.");
                return;
            }
        }

        File professorFile = new File("professor.txt");
        if (!professorFile.exists()) {
            if (!professorFile.createNewFile()) {
                System.out.println("Failed to create professor.txt file.");
                statusLabel.setText("Error creating professor file.");
                return;
            }
        }

        // Check if the email already exists in student.txt
        try (Scanner studentScanner = new Scanner(studentFile)) {
            while (studentScanner.hasNextLine()) {
                String[] line = studentScanner.nextLine().split(",");
                if (line.length > 2 && line[2].equalsIgnoreCase(email)) {
                    statusLabel.setText("Email already in use.");
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            statusLabel.setText("Error reading student file.");
            e.printStackTrace();
        }

        // Check if the email already exists in professor.txt
        try (Scanner profScanner = new Scanner(professorFile)) {
            while (profScanner.hasNextLine()) {
                String[] line = profScanner.nextLine().split(",");
                if (line.length > 2 && line[2].equalsIgnoreCase(email)) {
                    statusLabel.setText("Email already in use.");
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            statusLabel.setText("Error reading professor file.");
            e.printStackTrace();
        }

            String encryptedPassword = EncryptionMethods.encrypt(password, salt);

            if (role.equals("student")) {
                saveToFile("student.txt", encryptedPassword);
            }
            else if (role.equals("professor")) {
                saveToFile("professor.txt", encryptedPassword);
            }
            statusLabel.setText("Signup Successful");
        }

    public String generateStudentId(String firstName, String lastName) {
        // Use the first letter of first name, the first letter of last name, and a random number to generate a unique ID
        String initials = firstName.substring(0,1).toUpperCase() + lastName.substring(0,1).toUpperCase();
        Random rand = new Random();
        int randomNum = rand.nextInt(10000)+1;
        return initials + randomNum;
    }



    private void saveToFile (String fileName, String encryptedPassword) {
            String line = firstName + "," + lastName + "," + email + "," + encryptedPassword + "," + salt + "," + role + "," + ID + "\n";
            try (FileWriter writer = new FileWriter(fileName, true)) {
                writer.write(line);
                System.out.println("User saved to " + fileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void onReturnButtonClick (ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Login");
            stage.show();
        }
    }