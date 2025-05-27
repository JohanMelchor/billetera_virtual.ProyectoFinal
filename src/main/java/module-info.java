module co.edu.uniquindio {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires transitive javafx.graphics;

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
    opens co.edu.uniquindio.controller to javafx.fxml;
    exports co.edu.uniquindio.controller;
    opens co.edu.uniquindio.viewcontroller to javafx.fxml;
    exports co.edu.uniquindio.viewcontroller;
    opens co.edu.uniquindio.mapping.dto to javafx.fxml;
    exports co.edu.uniquindio.mapping.dto;
    
}