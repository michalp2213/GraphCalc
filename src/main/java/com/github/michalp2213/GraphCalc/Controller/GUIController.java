package com.github.michalp2213.GraphCalc.Controller;

import com.github.michalp2213.GraphCalc.Model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


public class GUIController {
    private final int RADIUS = 5;
    public Button fileButton;
    public Button addVerticesButton;
    public Button addEdgesButton;
    public Button removeObjectsButton;
    public Button zoomInButton;
    public Button zoomOutButton;
    public CheckBox lockCheckBox;
    public VBox leftSideButtons;
    public VBox graphOperationButtons;
    public VBox viewOptionButtons;
    public VBox fileMenu;
    public Button newButton;
    public Button openButton;
    public Button saveButton;
    public Button saveAsButton;
    public Pane workspace;
    public GridPane mainFrame;
    public GridPane newMenu;
    public ComboBox<String> graphTypeBox;
    public ComboBox<String> sourceTypeBox;
    public TextField pathField;
    public Button newMenuExitButton;
    public Button newMenuAcceptButton;
    public Graph<Circle> graph = new UndirectedGraph<>();
    public Circle c1, c2;
    public Text pathFieldTitle;
    private boolean addVerticesMode = false;
    private boolean addEdgesMode = false;
    private boolean removeObjects = false;

    @FXML
    public void showFileMenu() {
        fileMenu.setVisible(true);
        fileMenu.toFront();
    }

    @FXML
    public void hideFileMenu() {
        fileMenu.setVisible(false);
    }

    @FXML
    public void showNewMenu(ActionEvent event) {
        newMenu.setVisible(true);
        if (graphTypeBox.getItems().isEmpty()) {
            graphTypeBox.getItems().addAll("Undirected",
                    "Directed",
                    "Poset");
        }
        if (sourceTypeBox.getItems().isEmpty()) {
            sourceTypeBox.getItems().addAll("Clear",
                    "Adjacency lists",
                    "Adjacency matrix");
        }
        graphTypeBox.getSelectionModel().selectFirst();
        sourceTypeBox.getSelectionModel().selectFirst();
        hideFileMenu();
    }

    @FXML
    public void newMenuExit(ActionEvent event) {
        //todo
        graphTypeBox.getSelectionModel().clearSelection();
        sourceTypeBox.getSelectionModel().clearSelection();
        hidePath(event);
        newMenu.setVisible(false);
    }

    @FXML
    public void newMenuAcceptAndExit(ActionEvent event) {
        switch (graphTypeBox.getSelectionModel().getSelectedItem()){
            case "Undirected":
                switch(sourceTypeBox.getSelectionModel().getSelectedItem()){
                    case "Clear":
                        System.out.println("mniam");
                        break;
                    case "Adjacency lists":
                        //todo
                        break;
                    case "Adjacency matrix":
                        //todo
                        break;
                }
                break;
            case "Directed":
                switch(sourceTypeBox.getSelectionModel().getSelectedItem()){
                    case "Clear":
                        //todo
                        break;
                    case "Adjacency lists":
                        //todo
                        break;
                    case "Adjacency matrix":
                        //todo
                        break;
                }
                break;
            case "Poset":
                switch(sourceTypeBox.getSelectionModel().getSelectedItem()){
                    case "Clear":
                        //todo
                        break;
                    case "Adjacency lists":
                        //todo
                        break;
                    case "Adjacency matrix":
                        //todo
                        break;
                }
                break;
        }
        newMenuExit(event);
    }

    @FXML
    public void showPath(ActionEvent event){
        pathField.setVisible(!(sourceTypeBox.getSelectionModel().getSelectedItem() != null &&
                sourceTypeBox.getSelectionModel().getSelectedItem().equals("Clear")));
        pathFieldTitle.setVisible(!(sourceTypeBox.getSelectionModel().getSelectedItem() != null &&
                sourceTypeBox.getSelectionModel().getSelectedItem().equals("Clear")));
    }

    @FXML
    public void hidePath(ActionEvent even){
        pathFieldTitle.setVisible(false);
        pathField.setVisible(false);
    }

    @FXML
    public void openClicked(ActionEvent event) {
        //todo
        hideFileMenu();
    }

    @FXML
    public void saveClicked(ActionEvent event) {
        //todo
        hideFileMenu();
    }

    @FXML
    public void saveAsClicked(ActionEvent event) {
        //todo
        hideFileMenu();
    }

    @FXML
    public void addVertices(MouseEvent mouseEvent) {
        addVerticesMode = !addVerticesMode;
    }

    public void addEdges(MouseEvent mouseEvent) {
        addEdgesMode = !addEdgesMode;
    }

    public void workspaceClicked(MouseEvent mouseEvent) {
        if (addVerticesMode) {
            Circle c = new Circle(mouseEvent.getX(), mouseEvent.getY(), RADIUS);
            workspace.getChildren().add(c);
            graph.addVertex(new CircleVertex(c, workspace));
            EventHandler<MouseEvent> vertexClicked = e -> {
                if (removeObjects) {
                    graph.removeVertex(new CircleVertex(c, workspace));
                } else if (addEdgesMode) {
                    if (c1 == null) c1 = c;
                    else if (c2 == null) {
                        c2 = c;
                        Line l = new Line(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY());
                        Circle a = c1, b = c2;
                        EventHandler<MouseEvent> edgeClicked = event -> {
                            if (removeObjects) {
                                graph.removeEdge(new LineEdge(new CircleVertex(a, workspace), new CircleVertex(b, workspace), l, workspace));
                            }
                        };
                        l.addEventFilter(MouseEvent.MOUSE_CLICKED, edgeClicked);
                        graph.addEdge(new LineEdge(new CircleVertex(c1, workspace), new CircleVertex(c2, workspace), l, workspace));
                        workspace.getChildren().add(l);
                        c1 = null;
                        c2 = null;
                    }
                }
            };
            c.addEventFilter(MouseEvent.MOUSE_CLICKED, vertexClicked);
        }
    }

    public void removeObjects(MouseEvent mouseEvent) {
        removeObjects = !removeObjects;
    }
}
