package edu.okcu.teamproject2;

import javafx.beans.property.SimpleStringProperty;

public class ClassRecord {
    private String className;
    private String classID;


    public ClassRecord(String className, String classID) {
        this.className = className;
        this.classID = classID;

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String cName) {
        className = cName;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String cID) {
        classID = cID;
    }
}