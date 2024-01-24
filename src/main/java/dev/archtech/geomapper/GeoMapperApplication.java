package dev.archtech.geomapper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GeoMapperApplication extends Application {

    private static String WINDOW_TITLE = "GeoMapper";
    private static String VIEW_FILE = "main-view.fxml";
    private static int DEFAULT_WIDTH = 700;
    private static int DEFAULT_HEIGHT = 350;
    private static boolean IS_RESIZABLE = false;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GeoMapperApplication.class.getResource(VIEW_FILE));
        Scene scene = new Scene(fxmlLoader.load(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        stage.setTitle(WINDOW_TITLE);
        stage.setScene(scene);
        stage.setResizable(IS_RESIZABLE);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}