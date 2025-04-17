package edu.okcu.teamproject2;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Scanner;

public class studentLoginController {
    public PasswordField passwordPasswordField;
    public TextField emailTextField;
    public Button cancelButton;
    public Button loginButton;

    String email;
    String password;
    String salt = "2";
    String fileFirstName;
    String fileLastName;
    String fileEmail;
    String filePassword;
    String saltedPassword;
    static int lines = 0;
    static String number = "1";


    File file = new File("StudentData.txt");
    Scanner scan = new Scanner(file);

    public studentLoginController() throws FileNotFoundException {
    }

    public void onPasswordPasswordFieldEntered(ActionEvent actionEvent) {
    }

    public void onEmailTextFieldEntered(ActionEvent actionEvent) {
    }

    public void onCancelButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent login = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Stage window = (Stage) cancelButton.getScene().getWindow();
        window.setScene(new Scene(login, 300, 300));
        window.setTitle("Login");
    }

    public String onLoginButtonClicked(ActionEvent actionEvent) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        email = emailTextField.getText();
        password = passwordPasswordField.getText();
        number = "1";
        BufferedReader reader = new BufferedReader(new FileReader("StudentData.txt"));
        while (reader.readLine() != null) lines++;
        reader.close();
        lines = lines/6;
        for (int x = 1; lines >= x; x++) {
            number = scan.nextLine();
            fileFirstName = scan.nextLine();
            fileLastName = scan.nextLine();
            fileEmail = scan.nextLine();
            filePassword = scan.nextLine();
            salt = scan.nextLine();
            saltedPassword = EncryptionMethods.encrypt(password, salt);
            if ((email.equals(fileEmail)) && (saltedPassword.equals(filePassword))) {
                System.out.println(fileFirstName + "\n" + fileLastName + "\n" + fileEmail + "\n" + filePassword + "\n" + salt + "1");
                System.out.println(number);
                Parent studentTable = FXMLLoader.load(getClass().getResource("studentTable.fxml"));
                Stage window = (Stage) loginButton.getScene().getWindow();
                window.setScene(new Scene(studentTable, 600, 400));
                window.setTitle(number);
                return number;

            }
        }

        return number;
    }


}
