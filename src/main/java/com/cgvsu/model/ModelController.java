package com.cgvsu.model;

import com.cgvsu.GuiController;
import com.cgvsu.objreader.ObjReader;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelController {
    private final List<Model> modelsList = new ArrayList<>();
    private final TreeView<String> models;
    private final GuiController root;
    private AnchorPane anchorPane;
    private MultipleSelectionModel<TreeItem<String>> selectionModel;

    public ModelController(GuiController root, AnchorPane anchorPane, TreeView<String> models) {
        this.root = root;
        this.anchorPane = anchorPane;
        this.models = models;

        TreeItem<String> rootTreeNode = new TreeItem<>("Objects");
        TreeItem<String> modelsNode = new TreeItem<>("Models");
        rootTreeNode.getChildren().add(modelsNode);
        models.setRoot(rootTreeNode);

        selectionModel = models.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
    }

    public List<Model> getModelsList() {
        return modelsList;
    }

    public void removeModel(Model model) {
        modelsList.remove(model);
    }

    List<String> getModelsName() {
        List<String> names = new ArrayList<>();
        for (Model model : modelsList) {
            names.add(model.getName());
        }

        return names;
    }

    Model getModelByName(String name) {
        for (Model model : modelsList) {
            if (model.getName().equals(name))
                return model;
        }

        return null;
    }

    List<TreeItem<String>> getSelectedModelsNames() {
        return selectionModel.getSelectedItems();
    }




}