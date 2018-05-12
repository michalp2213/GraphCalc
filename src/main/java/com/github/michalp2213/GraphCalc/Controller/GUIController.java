package com.github.michalp2213.GraphCalc.Controller;

import com.github.michalp2213.GraphCalc.Model.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class GUIController {
    private static final int RADIUS = 5;
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
    public Button spreadVerticesButton;
    public HBox newMenuPath;
    public Label graphLabel;
    public Label sourceLabel;
    public Label pathLabel;
    private int id = 0;
    private Graph graph = new UndirectedGraph();
    private File file = null;
    private FileChooser fileChooser = new FileChooser();
    private Circle c1, c2;
    private Boolean addVerticesMode = false;
    private Boolean addEdgesMode = false;
    private Boolean removeObjectsMode = false;
    private HashMap<Vertex, Circle> circles = new HashMap<>();
    private HashMap<Edge, Node> lines = new HashMap<>();

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
                    "Adjacency matrix",
                    "Edge list");
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
        reset();
        switch (graphTypeBox.getSelectionModel().getSelectedItem()) {
            case "Undirected":
                graph = new UndirectedGraph();
                break;
            case "Directed":
                graph = new DirectedGraph();
                break;
            case "Poset":
                graph = new Poset();
                break;
        }
        switch (sourceTypeBox.getSelectionModel().getSelectedItem()) {
            case "Clear":
                break;
            case "Adjacency lists":
                graphFromList(pathField.getText());
                break;
            case "Adjacency matrix":
                graphFromMatrix(pathField.getText());
                break;
            case "Edge list":
                graphFromEdgeList(pathField.getText());
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
        //TODO
    }

    @FXML
    public void saveClicked(ActionEvent event) {
        //TODO
    }

    @FXML
    public void saveAsClicked(ActionEvent event) {
        //TODO
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
            if (!containsCircle(c)) {
                circles.put(getVertex(), c);
                workspace.getChildren().add(c);
                c.addEventHandler(MouseEvent.MOUSE_CLICKED,getCircleEventHandler(c));
            }
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
    @SuppressWarnings("Duplicates")
    private void moveCircle(Circle a, double toX, double toY) {
        Vertex v = getVertex(a);
        Circle c = new Circle(toX, toY, RADIUS);
        for (Edge e : graph.getAdjacencyList().get(v)) {
            if (graph.getClass().equals(UndirectedGraph.class)) {
                Line l = (Line) lines.get(e);
                if(l == null) l = (Line) lines.get(e.transpose());
                Line temp;
                if (e.to.equals(v)) {
                    if (e.from.equals(v)) {
                        temp = (Line) getLine(c, c);
                    } else {
                        temp = (Line) getLine(circles.get(e.from), c);
                    }
                } else {
                    temp = (Line) getLine(c, circles.get(e.to));
                }
                l.setStartX(temp.getStartX());
                l.setStartY(temp.getStartY());
                l.setEndX(temp.getEndX());
                l.setEndY(temp.getEndY());
            } else {
                DirectedLine l = (DirectedLine) lines.get(e);
                DirectedLine temp;
                if (e.to.equals(v)) {
                    if (e.from.equals(v)) {
                        temp = (DirectedLine) getLine(c, c);
                    } else {
                        temp = (DirectedLine) getLine(circles.get(e.from), c);
                    }
                } else {
                    temp = (DirectedLine) getLine(c, circles.get(e.to));
                }
                l.setStartX(temp.getStartX());
                l.setStartY(temp.getStartY());
                l.setEndX(temp.getEndX());
                l.setEndY(temp.getEndY());
            }
        }
        if (!graph.getClass().equals(UndirectedGraph.class)) {
            for (Edge e : graph.getTransposedAdjacencyList().get(v)) {
                DirectedLine l = (DirectedLine) lines.get(e.transpose());
                DirectedLine temp;
                if (e.to.equals(v)) {
                    if (e.from.equals(v)) {
                        temp = (DirectedLine) getLine(c, c);
                    } else {
                        temp = (DirectedLine) getLine(c, circles.get(e.from));
                    }
                } else {
                    temp = (DirectedLine) getLine(circles.get(e.to), c);
                }
                l.setStartX(temp.getStartX());
                l.setStartY(temp.getStartY());
                l.setEndX(temp.getEndX());
                l.setEndY(temp.getEndY());
            }
        }
        a.setCenterX(toX);
        a.setCenterY(toY);
    }

    private void spreadVerticesEvenly() {
        if (graph.getAdjacencyList().keySet().size() != 0) {
            double midX = workspace.getWidth() / 2;
            double midY = workspace.getHeight() / 2;
            if (graph.getAdjacencyList().keySet().size() == 1) {
                Circle v = circles.get(graph.getAdjacencyList().keySet().iterator().next());
                moveCircle(v, midX, midY);
            } else {
                double polygonRadius = Math.min(2 * midX / 3, 2 * midY / 3);
                int k = graph.getAdjacencyList().keySet().size();
                int j = 0;
                for (Vertex vT : graph.getAdjacencyList().keySet()) {
                    Circle v = circles.get(vT);
                    double arg = (2 * j * Math.PI) / k;
                    double toX = Math.cos(arg) * polygonRadius + midX;
                    double toY = Math.sin(arg) * polygonRadius + midY;
                    moveCircle(v, toX, toY);
                    j++;
                }
            }
        }
    }

    private EventHandler<MouseEvent> getLineEventHandler(Node l) {
        return event -> {
            if (removeObjectsMode) {
                Edge e = getEdge(l);
                if(e!=null){
                    workspace.getChildren().remove(l);
                    graph.removeEdge(e);
                    lines.remove(e);
                }
            }
        };
    }

    private EventHandler<MouseEvent> getCircleEventHandler(Circle c) {
        return e -> {
            if (removeObjectsMode) {
                removeCircle(c);
            } else if (addEdgesMode) {
                if (c1 == null) {
                    c1 = c;
                    c.setFill(Color.RED);
                } else if (c2 == null) {
                    c2 = c;
                    Node l = getLine(c1, c2);
                    Vertex v1 = getVertex(c1), v2 = getVertex(c2);
                    Edge edge = getEdge(v1, v2);
                    try {
                        putEdge(edge, l);
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

    private void graphFromMatrix(String s) {
        try (Scanner sc = new Scanner(new File(s))) {
            int n = sc.nextInt();
            Circle arr[] = new Circle[n];
            Vertex arr1[] = new Vertex[n];
            prepareArrays(arr, arr1, n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int a = sc.nextInt();
                    if (a == 1) {
                        Node l = getLine(arr[i], arr[j]);
                        Edge edge = getEdge(arr1[i], arr1[j]);
                        try {
                            putEdge(edge, l);
                        } catch (IllegalArgumentException exception) {
                            showAlert("Wrong edge", "File has an edge\n that cannot be inserted into poset.");
                            reset();
                            return;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("File not found", "Please provide existing file");
        } catch (Exception e) {
            showAlert("Something has gone wrong", "Data representing graph was in wrong format");
            reset();
        }
    }

    private void graphFromList(String s) {
        try (Scanner sc = new Scanner(new File(s))) {
            int n = sc.nextInt();
            Circle arr[] = new Circle[n];
            Vertex arr1[] = new Vertex[n];
            prepareArrays(arr, arr1, n);
            for (int i = 0; i < n; i++) {
                int k = sc.nextInt();
                for (int j = 0; j < k; j++) {
                    int a = sc.nextInt();
                    Node l = getLine(arr[i], arr[a - 1]);
                    Edge edge = getEdge(arr1[i], arr1[a-1]);
                    try {
                        putEdge(edge, l);
                    } catch (IllegalArgumentException exception) {
                        showAlert("Wrong edge", "File has an edge\n that cannot be inserted into poset.");
                        reset();
                        return;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("File not found", "Please provide existing file");
        } catch (Exception e) {
            showAlert("Something has gone wrong", "Data representing graph was in wrong format");
            reset();
        }
    }

    private void graphFromEdgeList(String s) {
        try (Scanner sc = new Scanner(new File(s))) {
            int n = sc.nextInt();
            int m = sc.nextInt();
            Circle arr[] = new Circle[n];
            Vertex arr1[] = new Vertex[n];
            prepareArrays(arr, arr1, n);
            for (int i = 0; i < m; i++) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                Node l = getLine(arr[a - 1], arr[b - 1]);
                Edge edge = getEdge(arr1[a-1], arr1[b-1]);
                try {
                    putEdge(edge, l);
                } catch (IllegalArgumentException exception) {
                    showAlert("Wrong edge", "File has an edge\n that cannot be inserted into poset.");
                    reset();
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("File not found", "Please provide existing file");
        } catch (Exception e) {
            showAlert("Something has gone wrong", "Data representing graph was in wrong format");
            reset();
        }
    }

    private boolean containsCircle(Circle a) {
        for (Circle c : circles.values()) {
            if (c.getCenterX() == a.getCenterX() && c.getCenterY() == a.getCenterY())
                return true;
        }
        return false;
    }

    private void removeLine(Edge e){
        Node l = lines.get(e);
        lines.remove(e);
        workspace.getChildren().remove(l);
        l = lines.get(e.transpose());
        lines.remove(e.transpose());
        workspace.getChildren().remove(l);
    }

    private void removeCircle(Circle a) {
        Vertex v = getVertex(a);
        if(v==null) return;
        for (Edge e : graph.getAdjacencyList().get(v)) {
            removeLine(e);
        }
        for (Edge e : graph.getTransposedAdjacencyList().get(v)) {
            removeLine(e);
        }
        circles.remove(v);
        workspace.getChildren().remove(a);
        graph.removeVertex(v);
    }

    private Vertex getVertex(Circle a){
        Vertex v = null;
        for (Map.Entry<Vertex, Circle> m : circles.entrySet()) {
            if (m.getValue().equals(a)) {
                v = m.getKey();
                break;
            }
        }
        return v;
    }

    private Edge getEdge(Node l){
        Edge e = null;
        for (Map.Entry<Edge, Node> m : lines.entrySet()) {
            if (m.getValue().equals(l)) {
                e = m.getKey();
                break;
            }
        }
        return e;
    }

    /**
     * @return vertex with desired attributes;
     */

    private Vertex getVertex(){
        return new Vertex(id++);
    }

    /**
     * @return edge with desired attributes.
     */

    private Edge getEdge(Vertex v1, Vertex v2){
        return new Edge(v1, v2);
    }

    private void putEdge(Edge edge, Node l){
        if (!graph.containsEdge(edge)){
            graph.addEdge(edge);
            lines.put(edge, l);
            l.addEventHandler(MouseEvent.MOUSE_CLICKED, getLineEventHandler(l));
            workspace.getChildren().add(l);

        }
    }

    private void reset(){
        workspace.getChildren().clear();
        graph = new UndirectedGraph();
        id = 0;
        circles.clear();
        lines.clear();
    }

    private void prepareArrays(Circle arr[], Vertex arr1[], int n){
        for (int i = 0; i < n; i++) {
            arr[i] = new Circle(0, i+1, RADIUS);
            arr[i].addEventHandler(MouseEvent.MOUSE_CLICKED, getCircleEventHandler(arr[i]));
            Vertex v = getVertex();
            arr1[i] = v;
            graph.addVertex(v);
            circles.put(v, arr[i]);
            workspace.getChildren().add(arr[i]);
        }
    }
}
