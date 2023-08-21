module dev.archtech.geomapper.geomapper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.opencsv;
    requires com.google.gson;
    requires google.maps.services;

    opens dev.archtech.geomapper.geomapper to javafx.fxml;
    exports dev.archtech.geomapper.geomapper;
}