module co.edu.uniquindio {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.uniquindio to javafx.fxml;
    exports co.edu.uniquindio;

    opens co.edu.uniquindio.viewcontroller;
    exports co.edu.uniquindio.viewcontroller;
    opens co.edu.uniquindio.controller;
    exports co.edu.uniquindio.controller;
}
