package com.github.michalp2213.GraphCalc.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

import com.github.michalp2213.GraphCalc.Model.*;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    public Button newMenuAcceptButton;
    private SavableCircleGraph graph = new SavableCircleGraph(SavableCircleGraph.Type.UNDIRECTED);
    public Button spreadVerticesButton;
    public HBox newMenuPath;
    public Label graphLabel;
    public Label sourceLabel;
    public Label pathLabel;
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
        changeMode(true, true, true);
        graphTypeBox.getSelectionModel().clearSelection();
        sourceTypeBox.getSelectionModel().clearSelection();
        hidePath(event);
        newMenu.setVisible(false);
    }

    @FXML
    public void newMenuAcceptAndExit(ActionEvent event) {
        switch (graphTypeBox.getSelectionModel().getSelectedItem()) {
            case "Undirected":
                graph = new SavableCircleGraph(SavableCircleGraph.Type.UNDIRECTED);
                break;
            case "Directed":
                graph = new SavableCircleGraph(SavableCircleGraph.Type.DIRECTED);
                break;
            case "Poset":
                graph = new SavableCircleGraph(SavableCircleGraph.Type.POSET);
                break;
        }
        workspace.getChildren().clear();
        switch (sourceTypeBox.getSelectionModel().getSelectedItem()) {
            case "Clear":
                break;
            case "Adjacency lists":
                graphFromList(pathField.getText());
                break;
            case "Adjacency matrix":
                graphFromMatrix(pathField.getText());
                break;
        }
        spreadVerticesEvenly();
        newMenuExit(event);
    }

    @FXML
    public void showPath(ActionEvent event) {
        newMenuPath.setVisible(!(sourceTypeBox.getSelectionModel().getSelectedItem() != null &&
                sourceTypeBox.getSelectionModel().getSelectedItem().equals("Clear")));
    }

    @FXML
    public void hidePath(ActionEvent even) {
        newMenuPath.setVisible(false);
    }

    @FXML
    public void openClicked(ActionEvent event) {
    	file = fileChooser.showOpenDialog(mainFrame.getScene().getWindow());
    	try {
			Graph <SerializableCircle> tmp = FileIO.readFromFile(file, workspace);

            for (Object v : ((Graph) graph).getAdjacencyList().keySet()) {
                ((CircleVertex) v).finishIt();
            }

            for (Object neigh : ((Graph) graph).getAdjacencyList().values()) {
                for (Object e : (HashSet) neigh) {
                    ((LineEdge) e).finishIt();
                }
            }

			if (tmp instanceof UndirectedGraph) {
				graph = new SavableCircleGraph(SavableCircleGraph.Type.UNDIRECTED);
			} else if (tmp instanceof Poset) {
				graph = new SavableCircleGraph(SavableCircleGraph.Type.POSET);
			} else if (tmp instanceof DirectedGraph) {
				graph = new SavableCircleGraph(SavableCircleGraph.Type.DIRECTED);
			}
			
			for (Object v : tmp.getAdjacencyList().keySet()) {
				Circle c = ((SerializableCircleVertex) v).getCircleVertex(workspace).getLabel();
                graph.addVertex(new CircleVertex(c, workspace));
            	c.addEventFilter(MouseEvent.MOUSE_CLICKED, getCircleEventHandler(c));
			}

			for (Object neigh : tmp.getAdjacencyList().values()) {
				for (Object x : (HashSet) neigh) {
					Edge <SerializableCircle> e = (Edge<SerializableCircle>) x;
					
					SerializableCircleVertex v = ((SerializableCircleVertex) e.from);
					SerializableCircleVertex u = ((SerializableCircleVertex) e.to);
					
					c1 = v.getCircleVertex(workspace).getLabel();
					c2 = u.getCircleVertex(workspace).getLabel();
					
					graph.addEdge(new LineEdge(v.getCircleVertex(workspace), u.getCircleVertex(workspace), getLine(c1, c2), workspace));

					c1 = null;
					c2 = null;
				}
			}
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
    	file = fileChooser.showSaveDialog(mainFrame.getScene().getWindow());
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
    public void spreadVerticesPressed(MouseEvent event) {
        spreadVerticesEvenly();
    }

    @FXML
    public void workspaceClicked(MouseEvent mouseEvent) {
        if (addVerticesMode) {
            Circle c = new Circle(mouseEvent.getX(), mouseEvent.getY(), RADIUS);
            graph.addVertex(new CircleVertex(c, workspace));
            c.addEventFilter(MouseEvent.MOUSE_CLICKED, getCircleEventHandler(c));
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
        double vecLength = Math.sqrt((a.getCenterX() + b.getCenterX()) * (a.getCenterX() + b.getCenterX()) +
                (a.getCenterY() + b.getCenterY()) * (a.getCenterY() + b.getCenterY()));
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
        if (graph.getType() == SavableCircleGraph.Type.UNDIRECTED) {
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

    private void moveVertex(CircleVertex v, double toX, double toY) {
        Circle c = new Circle(toX, toY, RADIUS);
        for (Edge<Circle> e : graph.getAdjacencyList().get(v)) {
            LineEdge le = (LineEdge) e;
            if (graph.getType() == SavableCircleGraph.Type.UNDIRECTED) {
                Line l = (Line) le.line;
                Line temp;
                if (le.to.equals(v)) {
                    if (le.from.equals(v)){
                        temp = (Line) getLine(c, c);
                    }
                    else {
                        temp = (Line) getLine(le.from.getLabel(), c);
                    }
                } else {
                    temp = (Line) getLine(c, le.to.getLabel());
                }
                l.setStartX(temp.getStartX());
                l.setStartY(temp.getStartY());
                l.setEndX(temp.getEndX());
                l.setEndY(temp.getEndY());
            } else {
                DirectedLine l = (DirectedLine) le.line;
                DirectedLine temp;
                if (le.to.equals(v)) {
                    if (le.from.equals(v)){
                        temp = (DirectedLine) getLine(c, c);
                    }
                    else {
                        temp = (DirectedLine) getLine(le.from.getLabel(), c);
                    }
                } else {
                    temp = (DirectedLine) getLine(c, le.to.getLabel());
                }
                l.setStartX(temp.getStartX());
                l.setStartY(temp.getStartY());
                l.setEndX(temp.getEndX());
                l.setEndY(temp.getEndY());
            }
        }
        if (graph.getType() == SavableCircleGraph.Type.DIRECTED) {
            for (Edge<Circle> e : graph.getTransposedAdjacencyList().get(v)) {
                LineEdge le = (LineEdge) e;
                DirectedLine l = (DirectedLine) le.line;
                DirectedLine temp;
                if (le.to.equals(v)) {
                    if (le.from.equals(v)){
                        temp = (DirectedLine) getLine(c, c);
                    }
                    else {
                        temp = (DirectedLine) getLine(c, le.from.getLabel());
                    }
                } else {
                    temp = (DirectedLine) getLine(le.to.getLabel(), c);
                }
                l.setStartX(temp.getStartX());
                l.setStartY(temp.getStartY());
                l.setEndX(temp.getEndX());
                l.setEndY(temp.getEndY());
            }
        }
        v.getLabel().setCenterX(toX);
        v.getLabel().setCenterY(toY);
    }

    private void spreadVerticesEvenly() {
        if (graph.getAdjacencyList().keySet().size() != 0) {
            double midX = workspace.getWidth() / 2;
            double midY = workspace.getHeight() / 2;
            if (graph.getAdjacencyList().keySet().size() == 1) {
                CircleVertex v = (CircleVertex) graph.getAdjacencyList().keySet().iterator().next();
                moveVertex(v, midX, midY);
            } else {
                double polygonRadius = Math.min(2 * midX / 3, 2 * midY / 3);
                int k = graph.getAdjacencyList().keySet().size();
                int j = 0;
                for (Vertex<Circle> vT : graph.getAdjacencyList().keySet()) {
                    CircleVertex v = (CircleVertex) vT;
                    double arg = (2 * j * Math.PI) / k;
                    double toX = Math.cos(arg) * polygonRadius + midX;
                    double toY = Math.sin(arg) * polygonRadius + midY;
                    moveVertex(v, toX, toY);
                    j++;
                }
            }
        }
    }
    private EventHandler<MouseEvent> getLineEventHandler(Node l, Circle a, Circle b){
        return event -> {
            if (removeObjectsMode) {
                graph.removeEdge(new LineEdge(new CircleVertex(a, workspace),
                        new CircleVertex(b, workspace), l, workspace));
            }
        };
    }

    private EventHandler<MouseEvent> getCircleEventHandler(Circle c){
        return e -> {
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
                    l.addEventFilter(MouseEvent.MOUSE_CLICKED, getLineEventHandler(l, c1, c2));
                    try {
                        if (!graph.containsEdge(new LineEdge(new CircleVertex(c1, workspace),
                                new CircleVertex(c2, workspace), l, workspace))) {
                            graph.addEdge(new LineEdge(new CircleVertex(c1, workspace),
                                    new CircleVertex(c2, workspace), l, workspace));
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
    }

    private void graphFromMatrix(String s){
        try(Scanner sc = new Scanner(new File(s))) {
            int n = sc.nextInt();
            Circle arr[] = new Circle[n];
            for(int i = 0; i < n; i++){
                arr[i] = new Circle(0, 0, RADIUS);
                arr[i].addEventFilter(MouseEvent.MOUSE_CLICKED, getCircleEventHandler(arr[i]));
                graph.addVertex(new CircleVertex(arr[i], workspace));
            }
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    int a = sc.nextInt();
                    if(a==1){
                        Node l = getLine(arr[i], arr[j]);
                        l.addEventFilter(MouseEvent.MOUSE_CLICKED, getLineEventHandler(l, arr[i], arr[j]));
                        try {
                            if (!graph.containsEdge(new LineEdge(new CircleVertex(arr[i], workspace),
                                    new CircleVertex(arr[j], workspace), l, workspace))) {
                                graph.addEdge(new LineEdge(new CircleVertex(arr[i], workspace),
                                        new CircleVertex(arr[j], workspace), l, workspace));
                            }
                        } catch (IllegalArgumentException exception) {
                            showAlert("Wrong edge", "This edge cannot be inserted into poset.");
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("File not found","Please provide existing file");
        } catch (Exception e){
            showAlert("Something has gone wrong", "Data representing graph was in wrong format");
            workspace.getChildren().clear();
            graph = new SavableCircleGraph(SavableCircleGraph.Type.UNDIRECTED);
        }
    }

    private void graphFromList(String s){
        try(Scanner sc = new Scanner(new File(s))) {
            int n = sc.nextInt();
            Circle arr[] = new Circle[n];
            for(int i = 0; i < n; i++){
                arr[i] = new Circle(0, 0, RADIUS);
                arr[i].addEventFilter(MouseEvent.MOUSE_CLICKED, getCircleEventHandler(arr[i]));
                graph.addVertex(new CircleVertex(arr[i], workspace));
            }
            for(int i = 0; i < n; i++){
                int k = sc.nextInt();
                for(int j = 0; j < k; j++){
                    int a = sc.nextInt();
                    Node l = getLine(arr[i], arr[a-1]);
                    l.addEventFilter(MouseEvent.MOUSE_CLICKED, getLineEventHandler(l, arr[i], arr[a-1]));
                    try {
                        if (!graph.containsEdge(new LineEdge(new CircleVertex(arr[i], workspace),
                                new CircleVertex(arr[a-1], workspace), l, workspace))) {
                            graph.addEdge(new LineEdge(new CircleVertex(arr[i], workspace),
                                    new CircleVertex(arr[a-1], workspace), l, workspace));
                        }
                    } catch (IllegalArgumentException exception) {
                        showAlert("Wrong edge", "This edge cannot be inserted into poset.");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("File not found","Please provide existing file");
        } catch (Exception e){
            showAlert("Something has gone wrong", "Data representing graph was in wrong format");
            workspace.getChildren().clear();
            graph = new SavableCircleGraph(SavableCircleGraph.Type.UNDIRECTED);
        }
    }
}
