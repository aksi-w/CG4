package com.cgvsu;

import com.cgvsu.Scene.Scene;
import com.cgvsu.math.Vector.Vector;
import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.math.affinetransf.AffineTransf;
import com.cgvsu.model.ModelOnScene;
import com.cgvsu.objWriter.ObjWriter;
import com.cgvsu.objreader.IncorrectFileException;
import com.cgvsu.rasterization.DrawUtilsJavaFX;
import com.cgvsu.rasterization.GraphicsUtils;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.RenderRasterization;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
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

    private Model mesh = null;

    private AffineTransf affineTransf = new AffineTransf();


    private Camera camera = new Camera(
            new Vector3f(0, 00, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {// Полина добавила и Даше еще надо исправить
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

            if (mesh != null) {
                try {
                    mesh = affineTransf.transformModel(mesh); // добавила Полина
                    RenderRasterization.render(canvas.getGraphicsContext2D(), graphicsUtils, camera, mesh, (int) width, (int) height, image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (isStructure) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
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
            mesh = ObjReader.read(fileContent);
            ModelOnScene model = new ModelOnScene(mesh);
            scene.modelsList.add(model);
            //mesh.add(ObjReader.read(fileContent));
            // todo: обработка ошибок
        } catch (IncorrectFileException | IOException exception) {

        }
    }

    public void onSaveModelMenuItemClick(ActionEvent actionEvent) {

        /**JFileChooser fileChooser = new JFileChooser();
         int userSelection = fileChooser.showSaveDialog(null);
         fileChooser.setDialogTitle("Сохранить файл");
         fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Model (*.obj)", "obj"));
         //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (.obj)", ".obj"));

         if (userSelection == JFileChooser.APPROVE_OPTION) {
         File fileToSave = fileChooser.getSelectedFile();

         try {
         Model model;
         model = scene.modelsList.get(0);
         ObjWriter.write(fileToSave, model);
         JOptionPane.showMessageDialog(null, "Модель успешно сохранена");
         } catch (Exception e) {
         JOptionPane.showMessageDialog(null, "Ошибка при сохранении модели: " + e.getMessage(),
         "Ошибка", JOptionPane.ERROR_MESSAGE);
         }
         }*/

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
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) { // дописать камеру
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) { // дописать камеру
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) { // дописать
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }

    public void loadStructure() {
        isStructure = !isStructure;
    }

    public void loadLight() {
        isLight = !isLight;
    }

    public void updateScale(float scaleX, float scaleY, float scaleZ) { // Полина добавила
        affineTransf.setSx(scaleX);
        affineTransf.setSy(scaleY);
        affineTransf.setSz(scaleZ);
    }

    public void updateRotation(float rotateX, float rotateY, float rotateZ) { // Полина добавила
        affineTransf.setRx(rotateX);
        affineTransf.setRy(rotateY);
        affineTransf.setRz(rotateZ);
    }

    public void updateTranslation(float translateX, float translateY, float translateZ) { // Полина добавила
        affineTransf.setTx(translateX);
        affineTransf.setTy(translateY);
        affineTransf.setTz(translateZ);
    }


}