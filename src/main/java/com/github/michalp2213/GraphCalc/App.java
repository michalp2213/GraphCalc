package com.github.michalp2213.GraphCalc;

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
        Parent root = FXMLLoader.load(getClass().getResource("View/GUI.fxml"));
        Scene mainScene = new Scene(root, resolutionX, resolutionY);
        mainScene.getStylesheets().add(App.class.getResource("css/Style.css").toExternalForm());
        primaryStage.setTitle("GraphCalc");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
