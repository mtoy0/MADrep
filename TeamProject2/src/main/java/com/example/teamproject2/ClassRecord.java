package com.example.teamproject2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.Scanner;

public class ClassRecord {
    private String className;
    private String classId;

    // Constructor
    public ClassRecord(String className, String classId) {
        this.className = className;
        this.classId = classId;
    }

    public ClassRecord() {

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public static void saveClassesToFile(String studentId, ObservableList<ClassRecord> classList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentId + "_classes.txt"))) {
            for (ClassRecord record : classList) {
                writer.write(record.getClassName() + "," + record.getClassId());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<ClassRecord> loadClassesFromFile(String studentId) {
        ObservableList<ClassRecord> list = FXCollections.observableArrayList();
        File file = new File(studentId + "_classes.txt");
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    list.add(new ClassRecord(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ObservableList<String> getStudentsForClass(String classId) {
        ObservableList<String> studentsInClass = FXCollections.observableArrayList();

        // Assuming the file student_classes.txt stores student name, ID, and class ID
        try (BufferedReader reader = new BufferedReader(new FileReader("student_classes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[2].equals(classId)) {
                    studentsInClass.add(parts[0] + " (" + parts[1] + ")");  // Adds student's name and ID
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return studentsInClass;
    }


}

