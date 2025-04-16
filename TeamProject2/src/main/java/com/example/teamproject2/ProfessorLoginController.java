package com.example.teamproject2;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class ProfessorLoginController {
    @FXML
    private TableView<ClassRecord> classTableView;
    @FXML
    private TableColumn<ClassRecord, String> classIdColumn;
    @FXML
    private TableColumn<ClassRecord, String> classNameColumn;
    @FXML
    private TextField classField;
    @FXML
    private TextField classIdField;
    @FXML
    private Button addButton;
    @FXML
    Button clearButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    Button logoutButton;
    @FXML
    public Label professorIdLabel;
    @FXML
    public Label professorNameLabel;

    private ClassRecord selectedClass;
    ObservableList<ClassRecord> classRecord;


    public void initialize() {
        loadProfessorInfo();

        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        classIdColumn.setCellValueFactory(new PropertyValueFactory<>("classId"));


        // Load saved classes
        String professorId = professorIdLabel.getText();
        classRecord = ClassRecord.loadClassesFromFile(professorId);
        classTableView.setItems(classRecord);

        // Disable buttons initially
        addButton.setDisable(false);
        deleteButton.setDisable(true);
        updateButton.setDisable(true);

        classTableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<ClassRecord>) change -> {
            addButton.setDisable(true);
            updateButton.setDisable(false);
            deleteButton.setDisable(false);
            if (!change.getList().isEmpty()) {
                selectedClass = change.getList().get(0);
                classField.setText(selectedClass.getClassName());
                classIdField.setText(selectedClass.getClassId());
            }
        });
    }

    private String getLoggedInEmail() {
        try (BufferedReader reader = new BufferedReader(new FileReader("current_user.txt"))) {
            return reader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void loadProfessorInfo() {
        String loggedInEmail = getLoggedInEmail();
        File professorFile = new File("professor.txt");

        try (Scanner scanner = new Scanner(professorFile)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                if (line.length >= 7 && line[2].equalsIgnoreCase(loggedInEmail)) {
                    String firstName = line[0];
                    String lastName = line[1];
                    String id = line[6];

                    professorNameLabel.setText("Name: " + firstName + " " + lastName);
                    professorIdLabel.setText("ID: " +id);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddButtonClick(ActionEvent actionEvent) {
        ClassRecord newRecord = new ClassRecord();
        newRecord.setClassName(classField.getText());
        newRecord.setClassId(classIdField.getText());
        classRecord.add(newRecord);
        ClassRecord.saveClassesToFile(professorIdLabel.getText(), classRecord);
    }

    public void onUpdateButtonClick(ActionEvent actionEvent) {
        if (selectedClass != null) {
            selectedClass.setClassName(classField.getText());
            selectedClass.setClassId(classIdField.getText());
            classTableView.refresh();
            ClassRecord.saveClassesToFile(professorIdLabel.getText(), classRecord);
        }
    }

    public void onDeleteButtonClick(ActionEvent actionEvent) {
        if (selectedClass != null) {
            classRecord.remove(selectedClass);
            ClassRecord.saveClassesToFile(professorIdLabel.getText(), classRecord);
        }
    }

    public void onClearButtonClick(ActionEvent actionEvent) {
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        classField.setText("");
        classIdField.setText("");
        selectedClass = null;
    }

    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Login");
        stage.show();
    }

    public void selectRow(MouseEvent mouseEvent) {
    }
}
