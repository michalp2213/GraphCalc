package com.github.michalp2213.GraphCalc.Controller;

import java.io.File;
import java.io.IOException;

import com.github.michalp2213.GraphCalc.Model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


public class GUIController {
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
    public AnchorPane mainFrame;
    public GridPane newMenu;
    public ComboBox<String> graphTypeBox;
    public ComboBox<String> sourceTypeBox;
    public TextField pathField;
    public Button newMenuExitButton;
    public Text pathFieldTitle;
    public Button newMenuAcceptButton;
    private Graph<Circle> graph = new UndirectedGraph<>();
    private File file = null;
    private FileChooser fileChooser = new FileChooser();
    private Circle c1, c2;
    private Boolean addVerticesMode = false;
    private Boolean addEdgesMode = false;
    private Boolean removeObjectsMode = false;
    private static final int RADIUS = 5;

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
        switch (graphTypeBox.getSelectionModel().getSelectedItem()) {
            case "Undirected":
                graph = new UndirectedGraph<>();
                break;
            case "Directed":
                graph = new DirectedGraph<>();
                break;
            case "Poset":
                graph = new Poset<>();
                break;
        }
        workspace.getChildren().clear();
        switch (sourceTypeBox.getSelectionModel().getSelectedItem()) {
            case "Clear":
                break;
            case "Adjacency lists":
                //todo
                break;
            case "Adjacency matrix":
                //todo
                break;
        }
        if (graph.getAdjacencyList().keySet().size() > 0) {
            spreadVerticesEvenly();
        }
        newMenuExit(event);
    }

    @FXML
    public void showPath(ActionEvent event) {
        pathField.setVisible(!(sourceTypeBox.getSelectionModel().getSelectedItem() != null &&
                sourceTypeBox.getSelectionModel().getSelectedItem().equals("Clear")));
        pathFieldTitle.setVisible(!(sourceTypeBox.getSelectionModel().getSelectedItem() != null &&
                sourceTypeBox.getSelectionModel().getSelectedItem().equals("Clear")));
    }

    @FXML
    public void hidePath(ActionEvent even) {
        pathFieldTitle.setVisible(false);
        pathField.setVisible(false);
    }

    @FXML
    public void openClicked(ActionEvent event) {
    	file = fileChooser.showOpenDialog(((Node)event.getTarget()).getScene().getWindow());
    	try {
			graph = FileIO.readFromFile(file);
		} catch (IOException e) {
			showAlert("ERROR", "Could not open file: " + e.toString());
		}
        hideFileMenu();
    }

    @FXML
    public void saveClicked(ActionEvent event) {
    	if (file == null) {
    		saveAsClicked(event);
    	} else {
    		try {
    		FileIO.writeToFile(file, graph);
    		} catch (IOException e) {
    			showAlert("ERROR", "Could not save file: " + e.toString());
    		}
    	}
        hideFileMenu();
    }

    @FXML
    public void saveAsClicked(ActionEvent event) {
    	file = fileChooser.showSaveDialog(((Node)event.getTarget()).getScene().getWindow());
    	saveClicked(event);
        hideFileMenu();
    }

    @FXML
    public void addVertices(MouseEvent mouseEvent) {
        changeMode(addVerticesMode, addVerticesButton);
        addVerticesMode = !addVerticesMode;
        if (addVerticesMode) {
            changeMode(false, true, true);
        }
    }

    @FXML
    public void addEdges(MouseEvent mouseEvent) {
        changeMode(addEdgesMode, addEdgesButton);
        addEdgesMode = !addEdgesMode;
        if (addEdgesMode) {
            changeMode(true, false, true);
        }
    }

    @FXML
    public void removeObjects(MouseEvent mouseEvent) {
        changeMode(removeObjectsMode, removeObjectsButton);
        removeObjectsMode = !removeObjectsMode;
        if (removeObjectsMode) {
            changeMode(true, true, false);
        }
    }

    @FXML
    public void workspaceClicked(MouseEvent mouseEvent) {
        if (addVerticesMode) {
            Circle c = new Circle(mouseEvent.getX(), mouseEvent.getY(), RADIUS);
            workspace.getChildren().add(c);
            graph.addVertex(new CircleVertex(c, workspace));
            EventHandler<MouseEvent> vertexClicked = e -> {
                if (removeObjectsMode) {
                    graph.removeVertex(new CircleVertex(c, workspace));
                } else if (addEdgesMode) {
                    if (c1 == null) {
                        c1 = c;
                        c.setFill(Color.RED);
                    } else if (c2 == null) {
                        c2 = c;
                        Node l = getLine(c1, c2);
                        Circle a = c1, b = c2;
                        EventHandler<MouseEvent> edgeClicked = event -> {
                            if (removeObjectsMode) {
                                graph.removeEdge(new LineEdge(new CircleVertex(a, workspace),
                                        new CircleVertex(b, workspace), l, workspace));
                            }
                        };
                        l.addEventFilter(MouseEvent.MOUSE_CLICKED, edgeClicked);
                        try {
                            if (!graph.containsEdge(new LineEdge(new CircleVertex(c1, workspace),
                                    new CircleVertex(c2, workspace), l, workspace))) {
                                graph.addEdge(new LineEdge(new CircleVertex(c1, workspace),
                                        new CircleVertex(c2, workspace), l, workspace));
                                workspace.getChildren().add(l);
                            }
                        } catch (IllegalArgumentException exception) {
                            showAlert("Wrong edge", "This edge cannot be inserted into poset.");
                        }
                        c1.setFill(Color.BLACK);
                        c1.toFront();
                        c2.toFront();
                        c1 = null;
                        c2 = null;
                    }
                }
            };
            c.addEventFilter(MouseEvent.MOUSE_CLICKED, vertexClicked);
        }
    }

    private void changeMode(Boolean mode, Button button) {
        if (mode) {
            button.getStyleClass().removeAll("actionButton");
        } else {
            button.getStyleClass().add("actionButton");
        }
    }

    private void changeMode(Boolean b1, Boolean b2, Boolean b3) {
        if (b1) {
            changeMode(true, addVerticesButton);
            addVerticesMode = false;
        }
        if (b2) {
            changeMode(true, addEdgesButton);
            addEdgesMode = false;
        }
        if (b3) {
            changeMode(true, removeObjectsButton);
            removeObjectsMode = false;
        }
    }

    private Node getLine(Circle a, Circle b) {
        Node line;
        double startX = a.getCenterX(), startY = a.getCenterY(), endX = b.getCenterX(), endY = b.getCenterY();
        double vecLength = Math.sqrt((a.getCenterX() + b.getCenterX())*(a.getCenterX() + b.getCenterX())+
                (a.getCenterY() + b.getCenterY())*(a.getCenterY() + b.getCenterY()));
        if (vecLength != 0) {
            double unitVecX = (b.getCenterX() - a.getCenterX()) / vecLength;
            double unitVecY = (b.getCenterY() - a.getCenterY()) / vecLength;
            double midX = (a.getCenterX() + b.getCenterX()) / 2;
            double midY = (a.getCenterY() + b.getCenterY()) / 2;
            double distFromMid = vecLength / 2 - RADIUS;
            startX = midX - distFromMid * unitVecX;
            startY = midY - distFromMid * unitVecY;
            endX = midX + distFromMid * unitVecX;
            endY = midY + distFromMid * unitVecY;
        }
        if (graph.getClass().equals(UndirectedGraph.class)) {
            line = new Line(startX, startY, endX, endY);
        } else {
            line = new DirectedLine(startX, startY, endX, endY);
        }
        return line;
    }

    private void showAlert(String message, String message2) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(message);
        alert.setContentText(message2);
        alert.showAndWait();
    }

    private void moveVertex(CircleVertex v, double toX, double toY){
        Circle c = new Circle(toX, toY, RADIUS);
        for (Edge<Circle> e : graph.getAdjacencyList().get(v)){
            LineEdge le = (LineEdge) e;
            if(graph.getClass().equals(UndirectedGraph.class)){
                Line l = (Line) le.line;
                Line temp;
                if(le.to.equals(v)){
                    temp =(Line) getLine(le.from.v, c);
                }else {
                    temp =(Line) getLine(c, le.to.v);
                }
                l.setStartX(temp.getStartX());
                l.setStartY(temp.getStartY());
                l.setEndX(temp.getEndX());
                l.setEndY(temp.getEndY());
            } else{
                DirectedLine l = (DirectedLine) le.line;
                DirectedLine temp;
                if(le.to.equals(v)){
                    temp =(DirectedLine) getLine(le.from.v, c);
                }else {
                    temp =(DirectedLine) getLine(c, le.to.v);
                }
                l.setStartX(temp.getStartX());
                l.setStartY(temp.getStartY());
                l.setEndX(temp.getEndX());
                l.setEndY(temp.getEndY());
            }
        }
        v.v.setCenterX(toX);
        v.v.setCenterY(toY);
    }

    private void spreadVerticesEvenly(){
        double midX = workspace.getWidth()/2;
        double midY = workspace.getHeight()/2;
        double polygonRadius = Math.min(midX/2, midY/2);
        int k = graph.getAdjacencyList().keySet().size();
        int j = 0;
        for (Vertex<Circle> vT : graph.getAdjacencyList().keySet()){
            CircleVertex v = (CircleVertex) vT;
            double arg = (2*j*Math.PI)/k;
            double toX = Math.cos(arg)*polygonRadius + midX;
            double toY = Math.sin(arg)*polygonRadius + midY;
            moveVertex(v, toX, toY);
            j++;
        }
    }
}
