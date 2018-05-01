package com.github.michalp2213.GraphCalc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;*/



public class App extends Application {
    private final int resolutionX = 800;
    private final int resolutionY = 600;

    public static void main( String[] args ) {
    	launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/GUI.fxml"));
        Scene mainScene = new Scene(root, resolutionX, resolutionY);
        primaryStage.setTitle("GraphCalc");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
