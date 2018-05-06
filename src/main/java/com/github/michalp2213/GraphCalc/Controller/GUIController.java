package com.github.michalp2213.GraphCalc.Controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import com.github.michalp2213.GraphCalc.Model.*;
import com.github.michalp2213.GraphCalc.Model.SavableCircleGraph.Type;

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
    private SavableCircleGraph graph = new SavableCircleGraph(SavableCircleGraph.Type.UNDIRECTED);
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
                //todo
                break;
            case "Adjacency matrix":
                //todo
                break;
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
    	file = fileChooser.showOpenDialog(mainFrame.getScene().getWindow());
    	try {
			Graph <SerializableCircle> tmp = FileIO.readFromFile(file, workspace);
			
			if (tmp instanceof UndirectedGraph) {
				graph = new SavableCircleGraph(SavableCircleGraph.Type.UNDIRECTED);
			} else if (tmp instanceof Poset) {
				graph = new SavableCircleGraph(SavableCircleGraph.Type.POSET);
			} else if (tmp instanceof DirectedGraph) {
				graph = new SavableCircleGraph(SavableCircleGraph.Type.DIRECTED);
			}
			
			for (Object v : tmp.getAdjacencyList().keySet()) {
				graph.addVertex(((SerializableCircleVertex) v).getCircleVertex(workspace));
			}

			for (Object neigh : tmp.getAdjacencyList().entrySet()) {
				for (Object x : (HashSet) neigh) {
					Edge <SerializableCircle> e = (Edge<SerializableCircle>) x;
					
					SerializableCircleVertex v = ((SerializableCircleVertex) e.from);
					SerializableCircleVertex u = ((SerializableCircleVertex) e.to);
					
					c1 = v.getCircleVertex(workspace).getLabel();
					c2 = u.getCircleVertex(workspace).getLabel();
					
					graph.addEdge(new LineEdge(v.getCircleVertex(workspace), u.getCircleVertex(workspace), (Object) getLine(), workspace));
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
    public void workspaceClicked(MouseEvent mouseEvent) {
        if (addVerticesMode) {
            Circle c = new Circle(mouseEvent.getX(), mouseEvent.getY(), RADIUS);

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
                        Node l = getLine();
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

    private Node getLine() {
        Node line;
        if (graph.getClass().equals(UndirectedGraph.class)) {
            line = new Line(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY());
        } else {
            line = new DirectedLine(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY());
        }
        return line;
    }

    private void showAlert(String message, String message2) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(message);
        alert.setContentText(message2);
        alert.showAndWait();
    }
}
