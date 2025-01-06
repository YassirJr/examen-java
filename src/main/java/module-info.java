module com.javaoop.examen {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires static lombok;

    opens com.javaoop.examen.controllers to javafx.fxml;
    opens com.javaoop.examen to javafx.fxml;
    opens com.javaoop.examen.models to javafx.base;

    exports com.javaoop.examen.controllers;
    exports com.javaoop.examen;
}