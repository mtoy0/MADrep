package com.example.teamproject2;

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
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class StudentLoginController {
    @FXML
    TableView<ClassRecord> tableView;
    @FXML
    TableColumn<ClassRecord, String> classIdColumn;
    @FXML
    TableColumn<ClassRecord, String> classNameColumn;
    @FXML
    TextField classField;
    @FXML
    TextField classIdField;
    @FXML
    Button addButton;
    @FXML
    Button clearButton;
    @FXML
    Button updateButton;
    @FXML
    Button deleteButton;
    @FXML
    Button logoutButton;
    @FXML
    Label studentIdLabel;
    @FXML
    Label studentNameLabel;

    private ClassRecord selectedClass;

    ObservableList<ClassRecord> classRecord;

    public StudentLoginController() throws NoSuchAlgorithmException {
    }

    public void initialize() {
        loadStudentInfo();

        classNameColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        classIdColumn.setCellValueFactory(new PropertyValueFactory<>("classId"));



        // Load saved classes for the current student
        String studentId = studentIdLabel.getText();
        classRecord = ClassRecord.loadClassesFromFile(studentId);
        tableView.setItems(classRecord);


        addButton.setDisable(false);
        deleteButton.setDisable(true);
        updateButton.setDisable(true);

        TableView.TableViewSelectionModel<ClassRecord> selectionModel = tableView.getSelectionModel();
        ObservableList<ClassRecord> selectedItems = selectionModel.getSelectedItems();

        selectedItems.addListener(new ListChangeListener<ClassRecord>() {
            @Override
            public void onChanged(Change<? extends ClassRecord> change) {
                addButton.setDisable(true);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                if (!change.getList().isEmpty()) {
                    selectedClass = change.getList().get(0);
                    classField.setText(selectedClass.getClassName());
                    classIdField.setText(selectedClass.getClassId());
                }

            }
        });

    }

    public void selectRow(MouseEvent mouseEvent) {
        var event = mouseEvent;
    }
    private String getLoggedInEmail() {
        try (BufferedReader reader = new BufferedReader(new FileReader("current_user.txt"))) {
            //System.out.println(reader.readLine());
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void loadStudentInfo() {
        String loggedInEmail = getLoggedInEmail();
        File studentFile = new File("student.txt");

        try (Scanner scanner = new Scanner(studentFile)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                if (line.length >=7  && line[2].equalsIgnoreCase(loggedInEmail)) {
                    String firstName = line[0];
                    String lastName = line[1];
                    String id = line[6];


                    studentNameLabel.setText("Name: " + firstName + " " + lastName);
                    studentIdLabel.setText("ID: " + id);


                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onAddButtonClick(ActionEvent actionEvent) {
        var newClassRecord = new ClassRecord();
        // Set the class data (this would usually come from a form or dialog)
        newClassRecord.setClassName(classField.getText());
        newClassRecord.setClassId(classIdField.getText());
        classRecord.add(newClassRecord);
        ClassRecord.saveClassesToFile(studentIdLabel.getText(), classRecord);


    }

    public void onUpdateButtonClick(ActionEvent actionEvent) {
        if (selectedClass != null) {
            selectedClass.setClassName(classField.getText());
            selectedClass.setClassId(classIdField.getText());

            tableView.refresh();
            ClassRecord.saveClassesToFile(studentIdLabel.getText(), classRecord);

        }
    }

    public void onDeleteButtonClick(ActionEvent actionEvent) {
        if (selectedClass != null) {
            classRecord.remove(selectedClass);
            ClassRecord.saveClassesToFile(studentIdLabel.getText(), classRecord);

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
}