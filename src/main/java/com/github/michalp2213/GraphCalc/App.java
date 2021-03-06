package com.github.michalp2213.GraphCalc;

import com.github.michalp2213.GraphCalc.Controller.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class App extends Application {
    private final int resolutionX = 800;
    private final int resolutionY = 600;

    public static void main( String[] args ) {
    	launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/GUI.fxml"));
        Parent root = loader.load();
        Scene mainScene = new Scene(root, resolutionX, resolutionY);
        mainScene.getStylesheets().add(App.class.getResource("css/Style.css").toExternalForm());
        primaryStage.setTitle("GraphCalc");
        ((GUIController) loader.getController()).setupShortcuts(mainScene);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
