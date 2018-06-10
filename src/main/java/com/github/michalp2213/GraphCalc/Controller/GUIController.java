package com.github.michalp2213.GraphCalc.Controller;

import com.github.michalp2213.GraphCalc.Model.AlgorithmEvents.*;
import com.github.michalp2213.GraphCalc.Model.AlgorithmEvents.TouchEvent;
import com.github.michalp2213.GraphCalc.Model.Algorithms.*;
import com.github.michalp2213.GraphCalc.Model.Graph.Edge;
import com.github.michalp2213.GraphCalc.Model.Graph.Graph;
import com.github.michalp2213.GraphCalc.Model.Graph.Vertex;
import com.github.michalp2213.GraphCalc.Model.Graphs.DirectedGraph;
import com.github.michalp2213.GraphCalc.Model.Graphs.Poset;
import com.github.michalp2213.GraphCalc.Model.Graphs.UndirectedGraph;
import com.github.michalp2213.GraphCalc.Model.IO.FileIO;
import com.github.michalp2213.GraphCalc.Model.Utility.DirectedLine;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.function.Supplier;


public class GUIController {
    private static final int RADIUS = 6;
    private static final double WIDTH = 2;
    private static final int FPS = 300;

    public Button fileButton;
    public Button addVerticesButton;
    public Button addEdgesButton;
    public Button removeObjectsButton;
    //public Button zoomInButton;
    //public Button zoomOutButton;
    public CheckBox smoothCheckBox;
    public VBox leftSideButtons;
    public VBox graphOperationButtons;
    //public VBox viewOptionButtons;
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
    public Button runAlgorithmButton;
    public VBox algorithmMenu;
    public Button runDFSButton;
    public Button runBFSButton;
    public VBox algorithmControlMenu;
    public Button prevStepButton;
    public Button nextStepButton;
    public HBox previousNextButtons;
    public Button runPauseAndResumeButton;
    public TextField algorithmDelayField;
    public Button cancelAlgorithmButton;
    public Label delayLabel;
    public VBox spreadMenu;
    public Button spreadEvenlyButton;
    public Button spreadFRButton;
    public Button openGraphButton;
    public Button spreadRandomlyButton;
    public Button spreadToposortButton;
    public Text infoField;
    private int id = 0;
    private Graph graph = new UndirectedGraph();
    private File file = null;
    private FileChooser fileChooser = new FileChooser();
    private Circle c1, c2;
    private Boolean addVerticesMode = false;
    private Boolean addEdgesMode = false;
    private Boolean removeObjectsMode = false;
    private Boolean chooseVertexMode = false;
    private Boolean algorithmMode = false;
    private HashMap<Vertex, Circle> circles = new HashMap<>();
    private HashMap<Edge, Node> lines = new HashMap<>();
    private ArrayList<AlgorithmEvent> events;
    private CountDownLatch latch;
    private ListIterator<AlgorithmEvent> it;
    private Vertex v;
    private Stack<Runnable> changes = new Stack<>();
    private ArrayList<TouchEvent> touched = new ArrayList<>();
    private Thread running;
    private VisitEvent visited;
    private int algorithmState;
    private double targetX;
    private double targetY;

    public void setupShortcuts(Scene s) {
        s.addEventFilter(KeyEvent.KEY_PRESSED, (keyEvent) -> {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.ESCAPE);
            if (keyComb.match(keyEvent)) {
                if (chooseVertexMode) {
                    if (running != null) {
                        running.interrupt();
                        running = null;
                    }
                    chooseVertexMode = false;
                    algorithmMode = false;
                    changeMode(true, runAlgorithmButton);
                }
                //System.out.println("Panek");
                keyEvent.consume();
            }
        });
    }

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
    public void showAlgorithmMenu() {
        if (algorithmMode) return;
        AnchorPane.setTopAnchor(algorithmMenu, runAlgorithmButton.localToScene(runAlgorithmButton.getBoundsInLocal()).getMinY());
        algorithmMenu.setVisible(true);
        algorithmMenu.toFront();
    }

    @FXML
    public void hideAlgorithmMenu() {
        algorithmMenu.setVisible(false);
    }

    @FXML
    public void showSpreadMenu() {
        AnchorPane.setTopAnchor(spreadMenu, spreadVerticesButton.localToScene(spreadVerticesButton.getBoundsInLocal()).getMinY());
        spreadMenu.setVisible(true);
        spreadMenu.toFront();
    }

    @FXML
    public void hideSpreadMenu() {
        spreadMenu.setVisible(false);
    }

    @FXML
    public void spreadVerticesEvenlyPressed() {
        if (smoothCheckBox.isSelected()){
            spreadVerticesEvenlySmooth();
        } else {
            spreadVerticesEvenly();
        }
        hideSpreadMenu();
    }

    @FXML
    public void spreadVerticesFRPressed() {
        if (smoothCheckBox.isSelected()) {
            spreadVerticesFRSmooth();
        } else {
            spreadVerticesFR();
        }
        hideSpreadMenu();
    }

    @FXML
    public void spreadVerticesRandomlyPressed() {
        if (smoothCheckBox.isSelected()) {
            spreadVerticesRandomlySmooth();
        } else {
            spreadVerticesRandomly();
        }
        hideSpreadMenu();
    }

    @FXML
    public void spreadVerticesToposortPressed() {
        if (smoothCheckBox.isSelected()) {
            spreadVerticesToposortSmooth();
        } else {
            spreadVerticesToposort();
        }
        hideSpreadMenu();
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
    public void openGraphClicked(ActionEvent event) {
        File f = fileChooser.showOpenDialog(mainFrame.getScene().getWindow());
        if (f == null) return;
        pathField.setText(f.getAbsolutePath());
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
        spreadVerticesFR();
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

        if (file == null)
            return;

        reset();

        try {
            FileIO.readData r = FileIO.readFromFile(file);
            graph = r.g;
            circles = r.circles;
        } catch (IOException e) {
            showAlert("Error", e.toString());
            return;
        }

        for (Circle c : circles.values()) {
            c.addEventHandler(MouseEvent.MOUSE_CLICKED, getCircleEventHandler(c));
            c.setOnMousePressed((e) ->{
                targetX = e.getX();
                targetY = e.getY();
            });
            c.setOnMouseDragged((e) ->{
                if(!removeObjectsMode&&!addVerticesMode&&!addEdgesMode){
                    double offsetX = e.getX() - targetX;
                    double offsetY = e.getY() - targetY;

                    moveCircle(c, c.getCenterX()+offsetX, c.getCenterY()+offsetY);

                    targetX = e.getX();
                    targetY = e.getY();
                }
            });
            workspace.getChildren().add(c);
        }

        lines.clear();

        for (Map.Entry<Vertex, ? extends HashSet<Edge>> entry : graph.getAdjacencyList().entrySet()) {
            for (Edge e : entry.getValue()) {

                if (!lines.keySet().contains(e)) {
                    if (graph instanceof UndirectedGraph && lines.keySet().contains(e.transpose()))
                        continue;
                    Node l = getLine(circles.get(e.from), circles.get(e.to));

                    lines.put(e, l);
                    l.addEventHandler(MouseEvent.MOUSE_CLICKED, getLineEventHandler(l));
                    workspace.getChildren().add(l);

                    circles.get(e.from).toFront();
                    circles.get(e.to).toFront();
                }
            }
        }
        id = graph.getAdjacencyList().size() + 1;
    }

    @FXML
    public void saveClicked(ActionEvent event) {
        if (file == null)
            saveAsClicked(event);

        try {
            FileIO.saveToFile(graph, circles, file);
        } catch (IOException e) {
            showAlert("Error", e.toString());
        }
    }

    @FXML
    public void saveAsClicked(ActionEvent event) {
        file = fileChooser.showSaveDialog(mainFrame.getScene().getWindow());

        if (file == null)
            return;

        saveClicked(event);
    }

    @FXML
    public void addVertices(MouseEvent mouseEvent) {
        if (algorithmMode) return;
        changeMode(addVerticesMode, addVerticesButton);
        addVerticesMode = !addVerticesMode;
        if (addVerticesMode) {
            changeMode(false, true, true);
        }
    }

    @FXML
    public void addEdges(MouseEvent mouseEvent) {
        if (algorithmMode) return;
        changeMode(addEdgesMode, addEdgesButton);
        addEdgesMode = !addEdgesMode;
        if (addEdgesMode) {
            changeMode(true, false, true);
        }
    }

    @FXML
    public void removeObjects(MouseEvent mouseEvent) {
        if (algorithmMode) return;
        changeMode(removeObjectsMode, removeObjectsButton);
        removeObjectsMode = !removeObjectsMode;
        if (removeObjectsMode) {
            changeMode(true, true, false);
        }
    }

    private void showInfoField(String text) {
        infoField.setText(text);
        infoField.setVisible(true);
        //infoField.toBack();
    }

    @FXML
    public void runDFSPressed(ActionEvent event) {
        if (algorithmMode) return;
        showInfoField("Choose starting vertex or press ESC to cancel");
        hideAlgorithmMenu();
        changeMode(true, true, true);
        chooseVertexMode = true;
        algorithmMode = true;
        changeMode(false, runAlgorithmButton);
        latch = new CountDownLatch(1);
        running = new Thread(() -> {
            try {
                latch.await();
                infoField.setVisible(false);
            } catch (InterruptedException e) {
                infoField.setVisible(false);
                return;
            }
            events = DFS.run(graph, v);
            chooseVertexMode = false;
            it = events.listIterator();
            changeMode(true, runAlgorithmButton);
            Thread.yield();
        });
        running.setDaemon(true);
        running.start();
    }

    @FXML
    public void runBFSPressed(ActionEvent event) {
        if (algorithmMode) return;
        showInfoField("Choose starting vertex or press ESC to cancel");
        hideAlgorithmMenu();
        changeMode(true, true, true);
        chooseVertexMode = true;
        algorithmMode = true;
        latch = new CountDownLatch(1);
        changeMode(false, runAlgorithmButton);
        running = new Thread(() -> {
            try {
                latch.await();
                infoField.setVisible(false);
            } catch (InterruptedException e) {
                infoField.setVisible(false);
                return;
            }
            events = BFS.run(graph, v);
            chooseVertexMode = false;
            it = events.listIterator();
            changeMode(true, runAlgorithmButton);
            Thread.yield();
        });
        running.setDaemon(true);
        running.start();
    }

    @FXML
    public void workspaceClicked(MouseEvent mouseEvent) {
        if (addVerticesMode) {
            Circle c = getCircle(mouseEvent.getX(), mouseEvent.getY());
            if (!containsCircle(c)) {
                Vertex v = getVertex();
                circles.put(v, c);
                graph.addVertex(v);
                workspace.getChildren().add(c);
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
        double startX = a.getCenterX(), startY = a.getCenterY(), endX = b.getCenterX(), endY = b.getCenterY();
        double vecLength = Math.sqrt((a.getCenterX() - b.getCenterX()) * (a.getCenterX() - b.getCenterX()) +
                (a.getCenterY() - b.getCenterY()) * (a.getCenterY() - b.getCenterY()));
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
            Line l = new Line(startX, startY, endX, endY);
            l.setStrokeWidth(WIDTH);
            return l;
        } else {
            DirectedLine l = new DirectedLine(startX, startY, endX, endY);
            l.setStrokeWidth(WIDTH);
            return l;
        }
    }

    private void showAlert(String message, String message2) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(message);
        alert.setContentText(message2);
        alert.showAndWait();
    }

    /*
     ** BEGIN CIRCLE MOVEMENT FUNCTIONS
     */

    @SuppressWarnings("Duplicates")
    private void moveCircle(Circle a, double toX, double toY) {
        Vertex v = getVertex(a);
        Circle c = getCircle(toX, toY);
        for (Edge e : graph.getAdjacencyList().get(v)) {
            if (graph.getClass().equals(UndirectedGraph.class)) {
                Line l = (Line) lines.get(e);
                if (l == null) l = (Line) lines.get(e.transpose());
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

    private void moveCircleGroup(Map<Circle, Pair<Double, Double>> positions) {
        for (Map.Entry<Circle, Pair<Double, Double>> e : positions.entrySet()) {
            moveCircle(e.getKey(), e.getValue().getKey(), e.getValue().getValue());
        }
    }

    private Thread moveCircleSmooth(Circle a, double toX, double toY, int time) {
        Thread t = new Thread(() -> {
                if (time == 0) {
                    Platform.runLater(() -> moveCircle(a, toX, toY));
                    return;
                }
                double frameCount = (time * FPS) / 1000;
                double stepX = (toX - a.getCenterX()) / frameCount;
                double stepY = (toY - a.getCenterY()) / frameCount;
                for (int i = 0; i < frameCount; i++) {
                    Platform.runLater(() -> moveCircle(a, a.getCenterX() + stepX, a.getCenterY() + stepY));
                    try {
                        Thread.sleep((1000 / FPS));
                    } catch (InterruptedException e) {
                        moveCircle(a, toX, toY);
                        return;
                    }
                }
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    private Thread moveCircleGroupSmooth(Map<Circle, Pair<Double, Double>> positions, int time) {
        Thread t = new Thread(() -> {
            if (time == 0) {
                for (Map.Entry<Circle, Pair<Double, Double>> e : positions.entrySet()) {
                    Platform.runLater(() -> moveCircle(e.getKey(), e.getValue().getKey(), e.getValue().getValue()));
                }
                return;
            }
            double frameCount = (time * FPS) / (1000*positions.keySet().size());
            Map<Circle, Pair<Double, Double>> steps = new HashMap<>();
            for (Map.Entry<Circle, Pair<Double, Double>> e : positions.entrySet()) {
                double stepX = (e.getValue().getKey() - e.getKey().getCenterX()) / frameCount;
                double stepY = (e.getValue().getValue() - e.getKey().getCenterY()) / frameCount;
                steps.put(e.getKey(), new Pair<>(stepX, stepY));
            }
            for (int i = 0; i < frameCount; i++) {
                for (Circle c : positions.keySet()) {
                    Platform.runLater(() -> moveCircle(c, c.getCenterX() + steps.get(c).getKey(),
                            c.getCenterY() + steps.get(c).getValue()));
                    try {
                        Thread.sleep((1000 / FPS));
                    } catch (InterruptedException e) {
                        moveCircle(c, positions.get(c).getKey(), positions.get(c).getValue());
                        return;
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    /*
     ** END CIRCLE MOVEMENT FUNCTIONS
     */

    private boolean isCyclic() {
        Set<Vertex> globalVisited = new HashSet<>();
        for (Vertex v : graph.getAdjacencyList().keySet()) {
            if (!globalVisited.contains(v)) {
                globalVisited.add(v);
                Queue<Vertex> q = new ArrayDeque<>();
                q.add(v);
                Set<Vertex> visitedInIteration = new HashSet<>();
                while (!q.isEmpty()) {
                    Vertex u = q.poll();
                    visitedInIteration.add(u);
                    for (Edge e : graph.getAdjacencyList().get(u)) {
                        if (visitedInIteration.contains(e.to)) {
                            return true;
                        }
                        if (!globalVisited.contains(e.to)) {
                            globalVisited.add(e.to);
                            q.add(e.to);
                        }
                    }
                }
            }
        }
        return false;
    }

    /*
     ** BEGIN VERTEX SPREAD ALGORITHMS
     */

    private Map<Circle, Pair<Double, Double>> getSpreadVerticesRandomlyPositions() {
        Map<Circle, Pair<Double, Double>> positions = new HashMap<>();
        Random gen = new Random();
        Supplier<Double> getViableWidthRandom = () -> (gen.nextDouble() * ((8.0/10.0) * workspace.getWidth())) + (1.0/10.0) * workspace.getWidth();
        Supplier<Double> getViableHeightRandom = () -> (gen.nextDouble() * ((8.0/10.0) * workspace.getHeight())) + (1.0/10.0) * workspace.getHeight();
        for (Circle c : circles.values()) {
            positions.put(c, new Pair<>(getViableWidthRandom.get(), getViableHeightRandom.get()));
        }
        return positions;
    }

    private void spreadVerticesRandomly() {
        Map<Circle, Pair<Double, Double>> positions = getSpreadVerticesRandomlyPositions();
        if (positions.size() != 0) {
            moveCircleGroup(positions);
        }
    }

    private Thread spreadVerticesRandomlySmooth() {
        Map<Circle, Pair<Double, Double>> positions = getSpreadVerticesRandomlyPositions();
        if (positions.size() != 0) {
            return moveCircleGroupSmooth(positions, 1250);
        }
        return null;
    }

    private Map<Circle, Pair<Double, Double>> getSpreadVerticesEvenlyPositions() {
        Map<Circle, Pair<Double, Double>> positions = new HashMap<>();
        if (graph.getAdjacencyList().keySet().size() != 0) {
            double midX = workspace.getWidth() / 2;
            double midY = workspace.getHeight() / 2;
            if (graph.getAdjacencyList().keySet().size() == 1) {
                Circle v = circles.get(graph.getAdjacencyList().keySet().iterator().next());
                positions.put(v, new Pair<>(midX, midY));
            } else {
                double polygonRadius = Math.min(2 * midX / 3, 2 * midY / 3);
                int k = graph.getAdjacencyList().keySet().size();
                int j = 0;
                for (Vertex vT : graph.getAdjacencyList().keySet()) {
                    Circle v = circles.get(vT);
                    double arg = (2 * j * Math.PI) / k;
                    double toX = Math.cos(arg) * polygonRadius + midX;
                    double toY = Math.sin(arg) * polygonRadius + midY;
                    positions.put(v, new Pair<>(toX, toY));
                    j++;
                }
            }
        }
        return positions;
    }

    private void spreadVerticesEvenly() {
        Map<Circle, Pair<Double, Double>> positions = getSpreadVerticesEvenlyPositions();
        if (positions.size() != 0) {
            moveCircleGroup(positions);
        }
    }

    private Thread spreadVerticesEvenlySmooth() {
        Map<Circle, Pair<Double, Double>> positions = getSpreadVerticesEvenlyPositions();
        if (positions.size() != 0) {
            return moveCircleGroupSmooth(positions, 1250);
        }
        return null;
    }

    private ArrayList<Map<Circle, Pair<Double, Double>>> getSpreadVerticesFRIterationPositions() {
        ArrayList<Map<Circle, Pair<Double, Double>>> toReturn = new ArrayList<>();
        if (graph.getAdjacencyList().keySet().size() <= 0) {
            return toReturn;
        }
        double W = workspace.getWidth(), L = workspace.getHeight();
        double area = W * L;

        final double kScalar = 10.0;
        double k = (Math.sqrt(area) / graph.getAdjacencyList().keySet().size()) * kScalar;

        int iterations = 100;
        double t = (W + L) / 20;

        final double fAttScalar = 1.0;
        final double fRepScalar = 1.0;

        Function<Double, Double> fAtt = x -> ((x * x) / k) * fAttScalar;
        Function<Double, Double> fRep = x -> ((k * k) / (x * x)) * fRepScalar;
        Function<Double, Double> cool = x -> x - (W + L) / (20 * iterations);

        Map<Vertex, Pair<Double, Double>> pos = new HashMap<>();
        for (Vertex v : graph.getAdjacencyList().keySet()) {
            pos.put(v, new Pair<>(circles.get(v).getCenterX(), circles.get(v).getCenterY()));
        }
        for (int i = 0; i < iterations; i++) {
            Map<Vertex, Pair<Double, Double>> disp = new HashMap<>();
            for (Vertex v : graph.getAdjacencyList().keySet()) {
                disp.put(v, new Pair<>(new Double(0), new Double(0)));
                for (Vertex u : graph.getAdjacencyList().keySet()) {
                    if (!u.equals(v)) {
                        Pair<Double, Double> diff = new Pair<>(
                                pos.get(v).getKey() - pos.get(u).getKey(),
                                pos.get(v).getValue() - pos.get(u).getValue());
                        double diffVal = Math.sqrt(
                                diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                        disp.put(v, new Pair<>(
                                disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                                disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                        ));
                    }
                }
                Pair<Double, Double> diff = new Pair<>(
                        pos.get(v).getKey(),
                        pos.get(v).getValue() - L / 2);
                double diffVal = Math.sqrt(
                        diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                disp.put(v, new Pair<>(
                        disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                        disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                ));
                diff = new Pair<>(
                        pos.get(v).getKey() - W / 2,
                        pos.get(v).getValue());
                diffVal = Math.sqrt(
                        diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                disp.put(v, new Pair<>(
                        disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                        disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                ));
                diff = new Pair<>(
                        pos.get(v).getKey() - W,
                        pos.get(v).getValue() - L / 2);
                diffVal = Math.sqrt(
                        diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                disp.put(v, new Pair<>(
                        disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                        disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                ));
                diff = new Pair<>(
                        pos.get(v).getKey() - W / 2,
                        pos.get(v).getValue() - L);
                diffVal = Math.sqrt(
                        diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                disp.put(v, new Pair<>(
                        disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                        disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                ));
                diff = new Pair<>(
                        pos.get(v).getKey(),
                        pos.get(v).getValue());
                diffVal = Math.sqrt(
                        diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                disp.put(v, new Pair<>(
                        disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                        disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                ));
                diff = new Pair<>(
                        pos.get(v).getKey() - W,
                        pos.get(v).getValue());
                diffVal = Math.sqrt(
                        diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                disp.put(v, new Pair<>(
                        disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                        disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                ));
                diff = new Pair<>(
                        pos.get(v).getKey() - W,
                        pos.get(v).getValue() - L);
                diffVal = Math.sqrt(
                        diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                disp.put(v, new Pair<>(
                        disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                        disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                ));
                diff = new Pair<>(
                        pos.get(v).getKey(),
                        pos.get(v).getValue() - L);
                diffVal = Math.sqrt(
                        diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                disp.put(v, new Pair<>(
                        disp.get(v).getKey() + (diff.getKey() / diffVal) * fRep.apply(diffVal),
                        disp.get(v).getValue() + (diff.getValue() / diffVal) * fRep.apply(diffVal)
                ));
            }
            if (!graph.getClass().equals(UndirectedGraph.class)) {
                for (Vertex v : graph.getAdjacencyList().keySet()) {
                    for (Edge e : graph.getAdjacencyList().get(v)) {
                        Pair<Double, Double> diff = new Pair<>(
                                pos.get(e.to).getKey() - pos.get(e.from).getKey(),
                                pos.get(e.to).getValue() - pos.get(e.from).getValue()
                        );
                        double diffVal = Math.sqrt(
                                diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                        disp.put(e.from, new Pair<>(
                                disp.get(e.from).getKey() + (diff.getKey() / diffVal) * fAtt.apply(diffVal),
                                disp.get(e.from).getValue() + (diff.getValue() / diffVal) * fAtt.apply(diffVal)
                        ));
                        disp.put(e.to, new Pair<>(
                                disp.get(e.to).getKey() - diff.getKey() * (fAtt.apply(diffVal) / diffVal),
                                disp.get(e.to).getValue() - diff.getValue() * (fAtt.apply(diffVal) / diffVal)
                        ));
                    }
                }
            } else {
                Set<Vertex> visited = new HashSet<>();
                for (Vertex v : graph.getAdjacencyList().keySet()) {
                    visited.add(v);
                    for (Edge e : graph.getAdjacencyList().get(v)) {
                        if (visited.contains(e.to)) {
                            continue;
                        }
                        Pair<Double, Double> diff = new Pair<>(
                                pos.get(e.to).getKey() - pos.get(e.from).getKey(),
                                pos.get(e.to).getValue() - pos.get(e.from).getValue()
                        );
                        double diffVal = Math.sqrt(
                                diff.getKey() * diff.getKey() + diff.getValue() * diff.getValue());
                        disp.put(e.to, new Pair<>(
                                disp.get(e.to).getKey() - (diff.getKey() / diffVal) * fAtt.apply(diffVal),
                                disp.get(e.to).getValue() - (diff.getValue() / diffVal) * fAtt.apply(diffVal)
                        ));
                        disp.put(e.from, new Pair<>(
                                disp.get(e.from).getKey() + (diff.getKey() / diffVal) * fAtt.apply(diffVal),
                                disp.get(e.from).getValue() + (diff.getValue() / diffVal) * fAtt.apply(diffVal)
                        ));
                    }
                }
            }
            for (Vertex v : graph.getAdjacencyList().keySet()) {
                double dispVal = Math.sqrt(
                        disp.get(v).getKey() * disp.get(v).getKey() + disp.get(v).getValue() * disp.get(v).getValue());
                pos.put(v, new Pair<>(
                        pos.get(v).getKey() + disp.get(v).getKey() * (Math.min(dispVal, t) / dispVal),
                        pos.get(v).getValue() + disp.get(v).getValue() * (Math.min(dispVal, t) / dispVal)
                ));
                pos.put(v, new Pair<>(
                        Math.min((9 * W) / 10, Math.max(W / 10, pos.get(v).getKey())),
                        pos.get(v).getValue()
                ));
                pos.put(v, new Pair<>(
                        pos.get(v).getKey(),
                        Math.min((9 * L) / 10, Math.max(L / 10, pos.get(v).getValue()))
                ));
            }
            Map<Circle, Pair<Double, Double>> newPositions = new HashMap<>();
            for (Map.Entry<Vertex, Pair<Double, Double>> e : pos.entrySet()) {
                newPositions.put(circles.get(e.getKey()), new Pair<>(e.getValue().getKey(), e.getValue().getValue()));
            }
            toReturn.add(newPositions);
            t = cool.apply(t);
        }
        return toReturn;
    }

    private void spreadVerticesFR() {
        spreadVerticesRandomly();
        ArrayList<Map<Circle, Pair<Double, Double>>> positions  = getSpreadVerticesFRIterationPositions();
        if (positions.size() != 0) {
            moveCircleGroup(positions.get(positions.size() - 1));
        }
    }

    private void spreadVerticesFRSmooth() {
        Thread t = new Thread(() -> {
            try {
                spreadVerticesRandomlySmooth().join();
                ArrayList<Map<Circle, Pair<Double, Double>>> positions = getSpreadVerticesFRIterationPositions();
                if (positions.size() == 0) {
                    return;
                }
                for (int i = 0; i < positions.size() - 1; i++) {
                    if (i % (positions.size() / 10) == 0) {
                        moveCircleGroupSmooth(positions.get(i), 1250 / (i/10 + 1)).join();
                    }
                }
                moveCircleGroupSmooth(positions.get(positions.size() - 1), 125).join();
            } catch (InterruptedException e) {
                spreadVerticesEvenly();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private Map<Circle, Pair<Double, Double>> getSpreadVerticesToposortPositions() {
        if (isCyclic()) {
            return null;
        }
        Map<Circle, Pair<Double, Double>> positions = new HashMap<>();
        Map<Vertex, Integer> inDeg = new HashMap<>();
        for (Vertex v : graph.getAdjacencyList().keySet()) {
            inDeg.put(v, 0);
        }
        for (Vertex v : graph.getAdjacencyList().keySet()) {
            for (Edge e : graph.getAdjacencyList().get(v)) {
                inDeg.put(e.to, inDeg.get(e.to) + 1);
            }
        }

        ArrayList<ArrayList<Vertex>> layers = new ArrayList<>();
        Queue<Vertex> zeroInDeg = new ArrayDeque<>();
        Set<Vertex> visited = new HashSet<>();
        zeroInDeg.add(new Vertex(-1));

        for (Vertex v : graph.getAdjacencyList().keySet()) {
            if (inDeg.get(v) == 0) {
                zeroInDeg.add(v);
                visited.add(v);
            }
        }

        while (!zeroInDeg.isEmpty()) {
            Vertex v = zeroInDeg.poll();
            if (v.getLabel() == -1) {
                if (zeroInDeg.size() == 0) {
                    break;
                }
                layers.add(new ArrayList<>());
                layers.get(layers.size() - 1).addAll(zeroInDeg);
                zeroInDeg.add(new Vertex(-1));
            } else {
                for (Edge e : graph.getAdjacencyList().get(v)) {
                    visited.add(e.to);
                    inDeg.put(e.to, inDeg.get(e.to) - 1);
                    if (inDeg.get(e.to) == 0) {
                        zeroInDeg.add(e.to);
                    }
                }
            }
        }

        double xDiff = workspace.getWidth()/(layers.size() + 1);
        double distFromLeft = xDiff;
        for (ArrayList<Vertex> arr : layers) {
            double yDiff = workspace.getHeight()/(arr.size() + 1);
            double distFromBottom = yDiff;
            for (Vertex v : arr) {
                positions.put(circles.get(v), new Pair<>(distFromLeft, distFromBottom));
                distFromBottom += yDiff;
            }
            distFromLeft += xDiff;
        }
        return positions;
    }

    private void spreadVerticesToposort() {
        Map<Circle, Pair<Double, Double>> positions = getSpreadVerticesToposortPositions();
        if (positions != null) {
            moveCircleGroup(positions);
        }
    }

    private Thread spreadVerticesToposortSmooth() {
        Map<Circle, Pair<Double, Double>> positions = getSpreadVerticesToposortPositions();
        if (positions != null) {
            return moveCircleGroupSmooth(positions, 1250);
        } else {
            return null;
        }
    }

    /*
     ** END VERTEX SPREAD ALGORITHMS
     */

    private EventHandler<MouseEvent> getLineEventHandler(Node l) {
        return event -> {
            if (removeObjectsMode) {
                Edge e = getEdge(l);
                if (e != null) {
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
            } else if (chooseVertexMode) {
                v = getVertex(c);
                latch.countDown();
                algorithmControlMenu.setVisible(true);
                runPauseAndResumeButton.setText("Run");
                algorithmState = 2;
            }
        };
    }

    @SuppressWarnings("Duplicates")
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

    @SuppressWarnings("Duplicates")
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
                    Edge edge = getEdge(arr1[i], arr1[a - 1]);
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

    @SuppressWarnings("Duplicates")
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
                Edge edge = getEdge(arr1[a - 1], arr1[b - 1]);
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

    private void removeLine(Edge e) {
        Node l = lines.get(e);
        lines.remove(e);
        workspace.getChildren().remove(l);
        l = lines.get(e.transpose());
        lines.remove(e.transpose());
        workspace.getChildren().remove(l);
    }

    private void removeCircle(Circle a) {
        Vertex v = getVertex(a);
        if (v == null) return;
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

    private Vertex getVertex(Circle a) {
        Vertex v = null;
        for (Map.Entry<Vertex, Circle> m : circles.entrySet()) {
            if (m.getValue().equals(a)) {
                v = m.getKey();
                break;
            }
        }
        return v;
    }

    private Edge getEdge(Node l) {
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

    private Vertex getVertex() {
        return new Vertex(id++);
    }

    /**
     * @return edge with desired attributes.
     */

    private Edge getEdge(Vertex v1, Vertex v2) {
        return new Edge(v1, v2);
    }

    private Circle getCircle(double x, double y){
        Circle c = new Circle(x, y, RADIUS);
        c.addEventHandler(MouseEvent.MOUSE_CLICKED, getCircleEventHandler(c));
        c.setOnMousePressed((e) ->{
            targetX = e.getX();
            targetY = e.getY();
        });
        c.setOnMouseDragged((e) ->{
            if(!removeObjectsMode&&!addVerticesMode&&!addEdgesMode){
                double offsetX = e.getX() - targetX;
                double offsetY = e.getY() - targetY;

                moveCircle(c, c.getCenterX()+offsetX, c.getCenterY()+offsetY);

                targetX = e.getX();
                targetY = e.getY();
            }
        });
        return c;
    }

    private void putEdge(Edge edge, Node l) {
        if (!graph.containsEdge(edge)) {
            graph.addEdge(edge);
            lines.put(edge, l);
            l.addEventHandler(MouseEvent.MOUSE_CLICKED, getLineEventHandler(l));
            workspace.getChildren().add(l);

        }
    }

    private void reset() {
        workspace.getChildren().clear();
        graph = new UndirectedGraph();
        id = 0;
        circles.clear();
        if (running != null) {
            running.interrupt();
            running = null;
        }
        if (chooseVertexMode) {
            chooseVertexMode = false;
            algorithmMode = false;
            changeMode(true, runAlgorithmButton);
        }
        touched.clear();
        changes.clear();
        visited = null;
        algorithmMode = false;
        lines.clear();
        c1 = null;
        c2 = null;
        v = null;
    }

    private void prepareArrays(Circle arr[], Vertex arr1[], int n) {
        for (int i = 0; i < n; i++) {
            arr[i] = getCircle(0, i+1);
            Vertex v = getVertex();
            arr1[i] = v;
            graph.addVertex(v);
            circles.put(v, arr[i]);
            workspace.getChildren().add(arr[i]);
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void setColor(Object o, Paint p) {
        if (o.getClass() == Vertex.class) {
            Circle c = circles.get(o);
            c.setFill(p);
        } else if (o.getClass() == Edge.class) {
            Node n = lines.get(o) == null ? lines.get(((Edge) o).transpose()) : lines.get(o);
            if (n.getClass() == Line.class) {
                ((Line) n).setStroke(p);
            } else {
                ((DirectedLine) n).setStroke(p);
            }
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private Paint getColor(Object o) {
        if (o.getClass() == Vertex.class) {
            Circle c = circles.get(o);
            return c.getFill();
        } else if (o.getClass() == Edge.class) {
            Node n = lines.get(o) == null ? lines.get(((Edge) o).transpose()) : lines.get(o);
            if (n.getClass() == Line.class) {
                return ((Line) n).getStroke();
            } else {
                return ((DirectedLine) n).getStroke();
            }
        }
        return Color.BLACK;
    }

    private void runWithDelay(int delay) {
        while (it.hasNext()) {
            nextStep();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.yield();
                return;
            }
        }
        resetColoring();
        changes.clear();
        touched.clear();
        algorithmControlMenu.setVisible(false);
        algorithmMode = false;
    }

    @FXML
    private void nextStep() {
        if (it.hasNext()) {
            AlgorithmEvent event = it.next();
            if (event.getClass() == TouchEvent.class) {
                touched.add((TouchEvent) event);
                Paint p = getColor(event.getTarget());
                changes.push(() -> {
                    touched.remove(event);
                    setColor(event.getTarget(), p);
                });
                setColor(event.getTarget(), Color.GREEN);
            } else if (event.getClass() == VisitEvent.class) {
                ArrayList<Paint> list = new ArrayList<>();
                for (TouchEvent e : touched) {
                    list.add(getColor(e.getTarget()));
                    setColor(e.getTarget(), Color.BLACK);
                }
                Paint p = getColor(event.getTarget());
                Paint p1 = visited == null ? null : getColor(visited.getTarget());
                VisitEvent prev = visited;
                if (visited != null)
                    setColor(visited.getTarget(), Color.BLACK);
                ArrayList<TouchEvent> temp = new ArrayList<>(touched);
                changes.push(() -> {
                    touched = temp;
                    for (int i = 0; i < touched.size(); i++) {
                        setColor(touched.get(i).getTarget(), list.get(i));
                    }
                    setColor(event.getTarget(), p);
                    visited = prev;
                    if (prev != null)
                        setColor(visited.getTarget(), p1);
                });
                touched.clear();
                setColor(event.getTarget(), Color.CRIMSON);
                visited = (VisitEvent) event;
            }
        }
    }

    @FXML
    private void previousStep() {
        if (it.hasPrevious()) {
            it.previous();
            changes.pop().run();
        }
    }

    private void pauseAlgorithm() {
        if (running != null) {
            running.interrupt();
            running = null;
        }
    }

    private void resumeAlgorithm() {
        int delay;
        try {
            delay = Integer.parseInt(algorithmDelayField.getText());
        }catch (Exception e){
            showAlert("NaN", "Please, enter a valid number in delay field.");
            throw e;
        }
        running = new Thread(() -> runWithDelay(delay));
        running.setDaemon(true);
        running.start();
    }

    @FXML
    private void runPauseAndResumeButtonPressed() {
        switch (algorithmState) {
            case 2:
                try {
                resumeAlgorithm();
                } catch (Exception e){
                    return;
                }
                algorithmState = 1;
                runPauseAndResumeButton.setText("Pause");
                break;
            case 1:
                pauseAlgorithm();
                algorithmState = 0;
                runPauseAndResumeButton.setText("Resume");
                break;
            case 0:
                try {
                    resumeAlgorithm();
                } catch (Exception e){
                    return;
                }
                algorithmState = 1;
                runPauseAndResumeButton.setText("Pause");
        }
    }

    @FXML
    private void cancelAlgorithmButtonPressed() {
        if (running != null) running.interrupt();
        resetColoring();
        changes.clear();
        algorithmControlMenu.setVisible(false);
        running = null;
        algorithmMode = false;
        touched.clear();
    }

    private void resetColoring() {
        for (Vertex v : circles.keySet()) {
            setColor(v, Color.BLACK);
        }
        for (Edge e : lines.keySet()) {
            setColor(e, Color.BLACK);
        }
    }
}
