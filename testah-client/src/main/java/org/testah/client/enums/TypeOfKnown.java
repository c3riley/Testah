package org.testah.client.enums;

public enum TypeOfKnown {

    FLAKY("Test has issues causing it to fail sometimes requires investigation"),
    
    REFACTOR_REQUIRED("Automation related issues, requires code to be refactored"),
    
    DEFECT("Application Under Test Defect results in fail"),
    
    APP_CHANGED("Application Under Test has changed causes test to fail and need refactor"),
    
    ENVIR("Issue with the Testing enviroment"),

    OTHER("Other type of issue see related linked ids");
    
    final String description;
    
    TypeOfKnown(final String description) {
        this.description = description;
    }
    
}
