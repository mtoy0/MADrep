package edu.okcu.teamproject2;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class StudentTable extends studentLoginController {

    public TableColumn<ClassRecord, String> classColumn = new TableColumn<ClassRecord, String>("Class");
    public TableColumn<ClassRecord, String> classIDColumn = new TableColumn<ClassRecord, String>("ClassID");
    public TextField classTextBox;
    public TextField classIDTextBox;
    public Button logoutButton;
    BufferedReader reader = new BufferedReader(new FileReader("StudentData.txt"));
    BufferedReader reader1 = new BufferedReader(new FileReader("StudentData.txt"));
    BufferedReader reader2 = new BufferedReader(new FileReader("StudentData.txt"));


    public TextField firstNameBox;
    public TextField lastNameBox;
    public TextField emailBox;
    public TextField ageBox;
    public Label idLabel;
    public Button updateButton;
    public Button clearButton;
    public Button addButton;
    public Button deleteButton;
    public Button clearTextFieldButton;
    static String studentID = "";
    static String firstName = "";
    static String lastName = "";
    static String email = "";
    static String password = "";
    static String salt = "";
    public TableView<ClassRecord> tableView;
    public TextField passwordBox;
    public Button updateInfoButton;
    public Label nameLabel;
    public Label emailLabel;
    public Label passwordLabel;

    public void initialize() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        clearTextFieldButton.fire();
        for (int x = 1; lines >= x; x++) {
            String studentIDChecker = reader.readLine();
            firstName = reader.readLine();
            lastName = reader.readLine();
            email = reader.readLine();
            password = reader.readLine();
            salt = reader.readLine();

            if (Objects.equals(studentIDChecker, number)) {
                nameLabel.setText("Name: " + firstName + " " + lastName);
                emailLabel.setText("Email: " + email);
                password = EncryptionMethods.decrypt(password, salt);
                passwordLabel.setText("Password: " + password);
                reader.close();
                x = lines;
                tableView.setEditable(true);
                ObservableList<ClassRecord> data = loadClassData(number);
                tableView.setItems(data);
                classColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
                classIDColumn.setCellValueFactory(new PropertyValueFactory<>("classID"));
                tableView.setItems(data);
                tableView.getColumns().add(classColumn);
                tableView.getColumns().add(classIDColumn);
                tableView.setItems(data);
                tableView.refresh();
                clearTextFieldButton.setVisible(false);

            }
        }
    }

    public StudentTable() throws FileNotFoundException {
    }

    public void firstNameEntered(ActionEvent actionEvent) {
    }

    public void lastNamedEntered(ActionEvent actionEvent) {
    }

    public void emailEntered(ActionEvent actionEvent) {
    }

    public void updateButtonClicked(ActionEvent actionEvent) {
        tableView.refresh();
    }

    public void clearButtonClicked(ActionEvent actionEvent) {
        classTextBox.clear();
        classIDTextBox.clear();
    }

    public void deleteButtonClicked(ActionEvent actionEvent) {
        ClassRecord selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tableView.getItems().remove(selected);
            tableView.refresh();
            saveClassData(number, tableView.getItems());
        }
    }

    public void onClearTextFieldButton(ActionEvent actionEvent) throws FileNotFoundException {
        idLabel.setText("ID: " + studentLoginController.number);
        studentID = idLabel.getText();
    }

    public void selectRow(MouseEvent mouseEvent) {
        var event = mouseEvent;
    }

    public void passwordEntered(ActionEvent actionEvent) {
    }

    public void onUpdateInfoButtonClicked(ActionEvent actionEvent) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        firstName = firstNameBox.getText();
        lastName = lastNameBox.getText();
        email = emailBox.getText();
        password = passwordBox.getText();
        nameLabel.setText("Name: " + firstName + " " + lastName);
        emailLabel.setText("Email: " + email);
        passwordLabel.setText("Password: " + password);
        firstNameBox.clear();
        lastNameBox.clear();
        emailBox.clear();
        passwordBox.clear();
        lines = lines * 6;
        password = EncryptionMethods.encrypt(password, salt);
        for (int x = 1; lines >= x; x++) {
            String studentIDChecker = reader1.readLine();
            if (Objects.equals(studentIDChecker, number)) {
                try {
                    int y = 1;
                    for (y = 1; lines >= y; y++) {
                        studentIDChecker = reader2.readLine();
                        if (Objects.equals(studentIDChecker, number)) {
                            String filePath = ("StudentData.txt");
                            replaceLine(filePath, y, number);
                            replaceLine(filePath, y+1, firstName);
                            replaceLine(filePath, y+2, lastName);
                            replaceLine(filePath, y+3, email);
                            replaceLine(filePath, y+4, password);
                            replaceLine(filePath, y+5, salt);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void writeLine(String filePath, int lineNumber, String newLine) throws IOException {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        if (lineNumber > 0 && lineNumber <= lines.size()) {
            lines.set(lineNumber - 1, newLine);
            Files.write(path, lines, StandardCharsets.UTF_8);
        } else {
            System.err.println("Invalid line number.");
        }
    }

    public static void replaceLine(String filePath, int lineNumber, String newContent) throws IOException {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

        if (lineNumber >= 1 && lineNumber <= lines.size()) {
            lines.set(lineNumber - 1, newContent);
            Files.write(path, lines, StandardCharsets.UTF_8);
        } else {
            throw new IndexOutOfBoundsException("Invalid line number.");
        }
    }

    private ObservableList<ClassRecord> loadClassData(String studentID) {
        ObservableList<ClassRecord> records = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader("StudentClassData.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3 && parts[0].equals(studentID)) {
                    records.add(new ClassRecord(parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private void saveClassData(String studentID, ObservableList<ClassRecord> data) {
        List<String> allLines = new ArrayList<>();

        // Load existing lines that don't match this student
        try (BufferedReader br = new BufferedReader(new FileReader("StudentClassData.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(studentID + ";")) {
                    allLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add current records for this student
        for (ClassRecord record : data) {
            allLines.add(studentID + ";" + record.getClassName() + ";" + record.getClassID());
        }

        // Write back
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("StudentClassData.txt"))) {
            for (String line : allLines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void classTextBoxEntered(ActionEvent actionEvent) {
    }

    public void classIDTextBoxEntered(ActionEvent actionEvent) {

    }

    public void onAddButtonClicked(ActionEvent actionEvent) {
        String newClass = classTextBox.getText();
        String newClassID = classIDTextBox.getText();
        tableView.getItems().add(new ClassRecord(newClass, newClassID));
        tableView.refresh();
        saveClassData(number, tableView.getItems());
    }

    public void onLogoutButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent login = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Stage window = (Stage) logoutButton.getScene().getWindow();
        window.setScene(new Scene(login, 300, 300));
        window.setTitle("Login");
    }
}
