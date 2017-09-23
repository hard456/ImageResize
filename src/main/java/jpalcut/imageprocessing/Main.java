package jpalcut.imageprocessing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static FXMLLoader FXMLresizeStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ImageResize.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Image resize application");
        primaryStage.setScene(new Scene(root, 430, 324));
        primaryStage.setResizable(false);
        primaryStage.show();
        FXMLresizeStage = loader;
    }

    public static FXMLLoader getFXMLresizeStage() {
        return FXMLresizeStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
