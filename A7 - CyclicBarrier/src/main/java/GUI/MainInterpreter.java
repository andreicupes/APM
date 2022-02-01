package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainInterpreter extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage mainStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
        System.out.println(getClass().getResource("MainLayout.fxml"));
        GridPane root = new GridPane();
        Scene scene = new Scene(fxmlLoader.load(), 1292, 632, Color.GRAY);
        mainStage.setTitle("Toy Language - Please select a program");
        mainStage.setScene(scene);
        mainStage.show();
    }

}