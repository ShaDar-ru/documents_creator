module ru.shadar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens ru.shadar to javafx.fxml;
    opens ru.shadar.controllers to javafx.fxml;
    opens ru.shadar.views to javafx.fxml;

    exports ru.shadar;
    exports ru.shadar.controllers;
    exports ru.shadar.objects;
    exports ru.shadar.views;
    exports ru.shadar.elementTypes;
    exports ru.shadar.modules;
}