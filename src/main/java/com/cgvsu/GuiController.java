package com.cgvsu;

import com.cgvsu.Scene.Scene;
import com.cgvsu.math.Matrix.Matrix;
import com.cgvsu.math.Vector.Vector;
import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.model.ModelOnScene;
import com.cgvsu.model.Polygon;
import com.cgvsu.model.TriangulatedModelWithCorrectNormal;
import com.cgvsu.objWriter.ObjWriter;
import com.cgvsu.objreader.IncorrectFileException;
import com.cgvsu.rasterization.DrawUtilsJavaFX;
import com.cgvsu.rasterization.GraphicsUtils;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.RenderRasterization;
import com.cgvsu.triangulation.Triangulation;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GuiController {

    final private float TRANSLATION = 0.5F;
    private boolean isStructure = false;
    public static boolean isLight = false;
    private BufferedImage image = null;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;
    Scene scene = new Scene();

    @FXML
    private ComboBox<String> chooseModel;
    @FXML
    private ComboBox<String> chooseCamera;

    private final List<Model> mesh = new ArrayList<>();
    private final List<String> names = new ArrayList<>();
    private final List<String> namesCamera = new ArrayList<>();
    private final List<Model> model = new ArrayList<>();

    private List<Camera> camera = new ArrayList<>(Arrays.asList(new Camera(
            new Vector3f(0, 00, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100)));


    private int numberCamera = 0;
    public static int numberMesh = 0;

    private Timeline timeline;

    @FXML
    private void initialize() {
        if (mesh.size() == 0) {
            chooseCamera.getItems().add(String.valueOf(numberCamera));
            namesCamera.add(String.valueOf(numberCamera));
        }
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        GraphicsUtils<Canvas> graphicsUtils = new DrawUtilsJavaFX(canvas);

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            scene.camera.setAspectRatio((float) (width / height));

            if (mesh.size() != 0) {
                try {
                    RenderRasterization.render(canvas.getGraphicsContext2D(), graphicsUtils, camera.get(numberCamera), mesh.get(numberMesh), (int) width, (int) height, image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (isStructure) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera.get(numberCamera), mesh.get(numberMesh), (int) width, (int) height);
                }

            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }


    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Загрузить файл");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            String fileContent = Files.readString(fileName);
            mesh.add(ObjReader.read(fileContent));
            ModelOnScene model = new ModelOnScene((Model) mesh);
            scene.modelsList.add(model);
            names.add(file.getName());
            chooseModel.getItems().add(file.getName());

            //ModelOnScene model = new ModelOnScene(mesh);
            //scene.modelsList.add(model);
            //model.add(ObjReader.read(fileContent));

            // todo: обработка ошибок
        } catch (IOException | IncorrectFileException exception) {

        }
        /**for (int i = 0; i < model.get(model.size() - 1).polygons.size(); i++) {
            model.get(model.size() - 1).trianglePolygons.add(new Polygon());
            model.get(model.size() - 1).trianglePolygons.get(i).getVertexIndices().addAll(model.get(model.size() - 1).polygons.get(i).getVertexIndices());
            model.get(model.size() - 1).trianglePolygons.get(i).getTextureVertexIndices().addAll(model.get(model.size() - 1).polygons.get(i).getTextureVertexIndices());
            model.get(model.size() - 1).trianglePolygons.get(i).getNormalIndices().addAll(model.get(model.size() - 1).polygons.get(i).getNormalIndices());
        }
        ArrayList<Polygon> triangles = TriangulatedModelWithCorrectNormal.triangulatePolygons(model.get(model.size() - 1).trianglePolygons);
        model.get(model.size() - 1).setTrianglePolygons(triangles);

        /**try {
            String fileContent = Files.readString(fileName);
            mesh = new Model(ObjReader.read(fileContent));
            // todo: обработка ошибок
        } catch (IOException | IncorrectFileException exception) {

        }*/
    }

    public void onSaveModelMenuItemClick(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Сохранить файл");
        File selectedFile = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        if(selectedFile != null) {
            try {
                Model model;
                model = scene.modelsList.get(0);
                ObjWriter.write(selectedFile, model);
                JOptionPane.showMessageDialog(null, "Модель успешно сохранена");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Ошибка при сохранении модели: " + e.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.get(numberCamera).movePosition(new Vector3f(0, -TRANSLATION, 0));
    }

    public void loadStructure() {
        isStructure = !isStructure;
    }

    public void loadLight() {
        isLight = !isLight;
    }


    public void handleModelForward(ActionEvent actionEvent) {
        for (ModelOnScene model : scene.modelsList) {
            model.setTranslationY(TRANSLATION);
            RenderEngine.render(canvas.getGraphicsContext2D(), scene.camera, model, (int) canvas.getWidth(),
                    (int) canvas.getHeight());
        }
    }

    public void handleModelLeft(ActionEvent actionEvent) throws Matrix.MatrixException, Vector.VectorException {
        for (ModelOnScene model : scene.modelsList) {
            model.setTranslationX(TRANSLATION);
            RenderEngine.render(canvas.getGraphicsContext2D(), scene.camera, model, (int) canvas.getWidth(),
                    (int) canvas.getHeight());
        }
    }

    public void handleModelBackward(ActionEvent actionEvent) {
        for (ModelOnScene model : scene.modelsList) {
            model.setTranslationY(-TRANSLATION);
            RenderEngine.render(canvas.getGraphicsContext2D(), scene.camera, model, (int) canvas.getWidth(),
                    (int) canvas.getHeight());
        }
    }

    public void handleModelRight(ActionEvent actionEvent) {

    }
}