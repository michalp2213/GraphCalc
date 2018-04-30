package com.github.michalp2213.GraphCalc;

import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;



/**
 * Hello world!
 *
 */
public class App extends Application {
    private final int resolutionX = 800;
    private final int resolutionY = 600;
    public static void main( String[] args ) {
    	launch(args);
    }

    private Button getSizedButton(String text, double wid, double hei){
        Button toReturn = new Button(text);
        toReturn.setMinSize(wid, hei);
        toReturn.setMaxSize(wid, hei);
        return toReturn;
    }

    public void start(Stage primaryStage){
        //menu with File options
        Button fileButton = getSizedButton("File", 70, 25);
        fileButton.setStyle("-fx-base: #CEECF5;");
        Button newButton = getSizedButton("New", 70, 25);
        Button openButton = getSizedButton("Open", 70, 25);
        Button saveButton = getSizedButton("Save", 70, 25);
        Button saveAsButton = getSizedButton("Save as...", 70, 25);
        VBox fileMenu = new VBox();
        fileMenu.setMaxSize(70, 100);
        fileMenu.setMinSize(70, 100);
        fileMenu.getChildren().add(newButton);
        fileMenu.getChildren().add(openButton);
        fileMenu.getChildren().add(saveButton);
        fileMenu.getChildren().add(saveAsButton);

        //menu shown after hovering over "New" button
        Button undirectedGraphButton = getSizedButton("Undirected Graph", 120, 25);
        Button directedGraphButton = getSizedButton("Directed Graph", 120, 25);
        Button posetButton = getSizedButton("Poset", 120, 25);
        VBox newMenu = new VBox();
        newMenu.setMaxSize(70, 75);
        newMenu.setMinSize(70, 75);
        newMenu.getChildren().add(undirectedGraphButton);
        newMenu.getChildren().add(directedGraphButton);
        newMenu.getChildren().add(posetButton);

        fileMenu.visibleProperty().bind(fileButton.hoverProperty()
                .or(fileMenu.hoverProperty())
                .or(newMenu.hoverProperty()));
        newMenu.visibleProperty().bind(newButton.hoverProperty()
                .or(newMenu.hoverProperty()));

        //group of graph operation buttons
        Button addVertexButton = getSizedButton("Add\nvertices", 70, 50);
        addVertexButton.setTextAlignment(TextAlignment.CENTER);
        Tooltip addVertexTooltip = new Tooltip();
        addVertexTooltip.setText("Click anywhere on the workspace to place a vertex");
        addVertexButton.setTooltip(addVertexTooltip);
        Button addEdgeButton = getSizedButton("Add\nedges", 70, 50);
        addEdgeButton.setTextAlignment(TextAlignment.CENTER);
        Tooltip addEdgeTooltip = new Tooltip();
        addEdgeTooltip.setText("Click on two vertices to place an edge from the first one to the other one");
        addEdgeButton.setTooltip(addEdgeTooltip);
        Button removeButton = getSizedButton("Remove\nobjects", 70, 50);
        removeButton.setTextAlignment(TextAlignment.CENTER);
        Tooltip removeTooltip = new Tooltip();
        removeTooltip.setText("Click on any vertex  or edge to remove it\nRemoving a vertex removes all adjacent edges");
        removeButton.setTooltip(removeTooltip);
        VBox graphOperations = new VBox();
        graphOperations.getChildren().add(addVertexButton);
        graphOperations.getChildren().add(addEdgeButton);
        graphOperations.getChildren().add(removeButton);

        //view control group
        Button zoomInButton = getSizedButton("Zoom in", 70, 25);
        Button zoomOutButton = getSizedButton("Zoom out", 70, 25);
        CheckBox lockViewCheckbox = new CheckBox("Lock");
        VBox viewControl = new VBox();
        viewControl.getChildren().add(zoomInButton);
        viewControl.getChildren().add(zoomOutButton);
        viewControl.getChildren().add(lockViewCheckbox);

        //nice alignment of left-side groups
        VBox leftSideButtons = new VBox();
        leftSideButtons.setSpacing(10);
        leftSideButtons.getChildren().add(fileButton);
        leftSideButtons.getChildren().add(graphOperations);
        leftSideButtons.getChildren().add(viewControl);

        //setting up workspace
        Canvas workspace = new Canvas(resolutionX - 70, resolutionY);

        //joining everything
        GridPane root = new GridPane();
        root.add(leftSideButtons, 0 ,0);
        GridPane.setValignment(fileButton, VPos.TOP);
        root.add(fileMenu, 1, 0);
        GridPane.setValignment(fileMenu, VPos.TOP);
        root.add(newMenu, 2, 0);
        GridPane.setValignment(newMenu, VPos.TOP);
        root.add(workspace, 1, 0, 3, 3);
        workspace.toBack();
        //root.setGridLinesVisible(true);
        primaryStage.setScene(new Scene(root, resolutionX, resolutionY));
        primaryStage.setTitle("GraphCalc");
        primaryStage.show();
    }
}
