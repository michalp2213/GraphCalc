package com.github.michalp2213.GraphCalc.Control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


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
    public Canvas workspace;
    public GridPane mainFrame;
    public GridPane newMenu;
    public ComboBox graphTypeBox;
    public ComboBox sourceTypeBox;
    public TextField pathField;
    public Button newMenuExitButton;
    public Button newMenuAcceptButton;

    @FXML
    public void showFileMenu(){
        fileMenu.setVisible(true);
    }

    @FXML
    public void hideFileMenu(){
        fileMenu.setVisible(false);
    }

    @FXML
    public void showNewMenu(ActionEvent event){
        newMenu.setVisible(true);
    }

    @FXML
    public void newMenuCancelAndExit(ActionEvent event){
        //todo
        newMenu.setVisible(false);
    }

    @FXML
    public void newMenuAcceptAndExit(ActionEvent event){
        //todo
        newMenu.setVisible(false);
    }
}
