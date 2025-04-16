package com.example.teamproject2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class LoginController {


    @FXML
    public TextField EmailField;
    @FXML
    public TextField PasswordField;
    @FXML
    public Label statusLabel;

    @FXML
    public void onLoginButtonClick(ActionEvent event) throws IOException {
        String email = EmailField.getText();
        String password = PasswordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter email and password.");
            return;
        }

        if (checkCredentials("student.txt", email, password)) {
            Files.write(Paths.get("current_user.txt"), email.getBytes());
            loadScene("StudentLogin.fxml",event);
        } else if(checkCredentials("professor.txt", email, password)){
            Files.write(Paths.get("current_user.txt"), email.getBytes());
            loadScene("ProfessorLogin.fxml",event);
        }
        else
            statusLabel.setText("Invalid email or password");
    }

    private boolean checkCredentials(String fileName, String email, String password) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                if (parts.length >= 5) {
                    String fileEmail = parts[2].trim();  // Email in file
                    String fileEncryptedPassword = parts[3].trim();  // Encrypted password in file
                    String salt = parts[4].trim();  // Salt in file

                    // Decrypt the stored password with the salt from the file
                    String decryptedPassword = EncryptionMethods.decrypt(fileEncryptedPassword, salt);

                    // Compare the decrypted password with the entered password
                    if (fileEmail.equalsIgnoreCase(email) && decryptedPassword.equals(password)) {
                        return true;  // Match found
                    }
                }
            }
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return false;  // No match found
    }

    // Method to switch scene based on login
    private void loadScene(String fxmlFile, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle(fxmlFile.split("\\.")[0]);  // Set title dynamically based on FXML
        stage.show();
    }

    @FXML
    private void onSignupButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Signup-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Sign Up");
        stage.show();
    }
}