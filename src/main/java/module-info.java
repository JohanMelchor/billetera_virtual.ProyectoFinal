module co.edu.uniquindio {

    requires javafx.fxml;
    requires javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;

    requires org.apache.pdfbox;
    requires java.desktop;
    requires java.logging;
    requires java.base;
    requires org.apache.fontbox;
    requires org.apache.pdfbox.io;

    requires org.apache.poi.poi;  

    requires org.apache.commons.collections4;
             
    requires org.apache.poi.ooxml;       
    requires org.apache.commons.compress;
    requires org.apache.logging.log4j;

    requires org.apache.poi.scratchpad;
    requires org.apache.xmlbeans;

    opens co.edu.uniquindio to javafx.fxml;
    exports co.edu.uniquindio;
    opens co.edu.uniquindio.model to javafx.fxml;
    exports co.edu.uniquindio.model;
    opens co.edu.uniquindio.model.Builder to javafx.fxml;
    exports co.edu.uniquindio.model.Builder;
    opens co.edu.uniquindio.controller to javafx.fxml;
    exports co.edu.uniquindio.controller;
    opens co.edu.uniquindio.viewcontroller to javafx.fxml;
    exports co.edu.uniquindio.viewcontroller;
    opens co.edu.uniquindio.mapping.dto to javafx.fxml;
    exports co.edu.uniquindio.mapping.dto;
    opens co.edu.uniquindio.state to javafx.fxml;
    exports co.edu.uniquindio.state;
    opens co.edu.uniquindio.factory to javafx.fxml;
    exports co.edu.uniquindio.factory;
    opens co.edu.uniquindio.service to javafx.fxml;
    exports co.edu.uniquindio.service;
    opens co.edu.uniquindio.decorator to javafx.fxml;
    exports co.edu.uniquindio.decorator;
    opens co.edu.uniquindio.strategy to javafx.fxml;
    exports co.edu.uniquindio.strategy;
    opens co.edu.uniquindio.mapping.mappers to javafx.fxml;
    exports co.edu.uniquindio.mapping.mappers;
    opens co.edu.uniquindio.Util to javafx.fxml;
    exports co.edu.uniquindio.Util;
    opens co.edu.uniquindio.service.Prototype to javafx.fxml;
    exports co.edu.uniquindio.service.Prototype;
    opens co.edu.uniquindio.facade to javafx.fxml;
    exports co.edu.uniquindio.facade;
    opens co.edu.uniquindio.adapter to javafx.fxml;
    exports co.edu.uniquindio.adapter;
}