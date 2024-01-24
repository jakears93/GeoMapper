module dev.archtech.geomapper.geomapper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.opencsv;
    requires com.google.gson;
    requires google.maps.services;
    requires lombok;

    opens dev.archtech.geomapper to javafx.fxml;
    exports dev.archtech.geomapper;
    exports dev.archtech.geomapper.exception;
    opens dev.archtech.geomapper.exception to javafx.fxml;
    exports dev.archtech.geomapper.util;
    opens dev.archtech.geomapper.util to javafx.fxml;
    exports dev.archtech.geomapper.model;
    opens dev.archtech.geomapper.model to javafx.fxml;
    exports dev.archtech.geomapper.controller;
    opens dev.archtech.geomapper.controller to javafx.fxml;
    exports dev.archtech.geomapper.task;
    opens dev.archtech.geomapper.task to javafx.fxml;
    exports dev.archtech.geomapper.service;
    opens dev.archtech.geomapper.service to javafx.fxml;

    exports dev.archtech.geomapper.view;
    opens dev.archtech.geomapper.view to javafx.fxml;
    exports dev.archtech.geomapper.model.map;
    opens dev.archtech.geomapper.model.map to javafx.fxml;
    exports dev.archtech.geomapper.model.client;
    opens dev.archtech.geomapper.model.client to javafx.fxml;
}