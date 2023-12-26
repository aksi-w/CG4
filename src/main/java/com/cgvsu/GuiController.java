package com.cgvsu;

import com.cgvsu.Scene.Scene;
import com.cgvsu.math.Matrix.Matrix;
import com.cgvsu.math.Vector.Vector;
import com.cgvsu.math.Vector.Vector3f;
import com.cgvsu.math.affinetransf.AffineTransf;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

import javax.imageio.ImageIO;
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
    private AffineTransf affineTransf = new AffineTransf();

    //private Model transformModel = null;
    private Model noTransformModel = null;

    @FXML
    private ComboBox<String> chooseModel;
    @FXML
    private ComboBox<String> chooseCamera;
    private String selectedValue;
    private String selectedValueCamera;
    private final ArrayList<Model> mesh = new ArrayList<>();
    private final List<String> names = new ArrayList<>();
    private final List<String> namesCamera = new ArrayList<>();
    private Model originalModel = null;

    private Camera camera = new Camera(new Vector3f(0, 00, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);



    /**private List<Camera> cameraList = new ArrayList<>(Arrays.asList(new Camera(
            new Vector3f(0, 00, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100)));*/

    GraphicsUtils<Canvas> graphicsUtils = new DrawUtilsJavaFX(canvas);


    private int numberCamera = 0;
    public static int numberMesh = 0;

    private Timeline timeline;
    private TextField setSX;
    @FXML
    private TextField scaleX;
    @FXML
    private TextField scaleY;
    @FXML
    private TextField scaleZ;
    @FXML
    private TextField rotateX;
    @FXML
    private TextField rotateY;
    @FXML
    private TextField rotateZ;
    @FXML
    private TextField translateX;
    @FXML
    private TextField translateY;
    @FXML
    private TextField translateZ;

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

            /**canvas.setOnScroll(scrollEvent -> {
                handleMouseScroll(scrollEvent);
            });
            canvas.setOnMousePressed(mouseEvent -> {
                mousePressed(mouseEvent);
            });*/

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            scene.camera.setAspectRatio((float) (width / height));

            if (mesh.size() != 0) {
                try {
                    RenderRasterization.render(canvas.getGraphicsContext2D(), graphicsUtils, camera, mesh.get(numberMesh), (int) width, (int) height, image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (isStructure) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh.get(numberMesh), (int) width, (int) height);
                }

            }

            if (canvas != null) {
                canvas.setOnMouseMoved(event2 -> camera.handleMouseInput(event2.getX(), event2.getY(), false, false));
                canvas.setOnMouseDragged(event2 -> camera.handleMouseInput(event2.getX(), event2.getY(), event2.isPrimaryButtonDown(), event2.isSecondaryButtonDown()));
                canvas.setOnScroll(scrollEvent -> handleMouseScroll(scrollEvent));
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

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());


        try {
            String fileContent = Files.readString(fileName);
            originalModel = ObjReader.read(fileContent);
            mesh.add(originalModel);
            noTransformModel = originalModel;
            names.add(file.getName());
            chooseModel.getItems().add(file.getName());
        } catch (IOException | IncorrectFileException exception) {

        }

        for (int i = 0; i < mesh.get(mesh.size() - 1).polygons.size(); i++) {
         mesh.get(mesh.size() - 1).trianglePolygons.add(new Polygon());
         mesh.get(mesh.size() - 1).trianglePolygons.get(i).getVertexIndices().addAll(mesh.get(mesh.size() - 1).polygons.get(i).getVertexIndices());
         mesh.get(mesh.size() - 1).trianglePolygons.get(i).getTextureVertexIndices().addAll(mesh.get(mesh.size() - 1).polygons.get(i).getTextureVertexIndices());
         mesh.get(mesh.size() - 1).trianglePolygons.get(i).getNormalIndices().addAll(mesh.get(mesh.size() - 1).polygons.get(i).getNormalIndices());
         }
         ArrayList<Polygon> triangles = Triangulation.triangulation(mesh.get(mesh.size() - 1).trianglePolygons);
         mesh.get(mesh.size() - 1).setTrianglePolygons(triangles);

    }

    public void onSaveModelMenuItemClick(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Сохранить файл");
        File selectedFile = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        if (selectedFile != null) {
            try {
                Model model = new Model();
                //model = ;
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
        Vector3f direction = Vector3f.subtraction(camera.getTarget(), camera.getPosition());
        direction.normalize();
        Vector3f.multiplication(direction, -TRANSLATION);
        camera.movePosition(direction);
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        Vector3f direction = Vector3f.subtraction(camera.getTarget(), camera.getPosition());
        direction.normalize();
        direction = Vector3f.multiplication(direction, TRANSLATION);
        camera.movePosition(direction);
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        Vector3f right = new Vector3f(camera.vectorX().getX(), camera.vectorX().getY(), camera.vectorX().getZ());

        right.normalize();
        right = Vector3f.multiplication(right, -TRANSLATION);
        camera.movePosition(right);
    }


    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        Vector3f right = new Vector3f(camera.vectorX().getX(), camera.vectorX().getY(), camera.vectorX().getZ());
        right.normalize();
        right = Vector3f.multiplication(right, TRANSLATION);
        camera.movePosition(right);
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
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
    @FXML
    private void loadTexture() throws IOException {

        if (!mesh.get(numberMesh).isTexture) {
            FileChooser fileChooser = new FileChooser();
            //fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG (*.png)", "*.png"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg"));
            fileChooser.setTitle("Загрузить текстуру");
            File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());

            if (file == null) {
                return;
            }
            image = ImageIO.read(file);
        }
        mesh.get(numberMesh).isTexture = !mesh.get(numberMesh).isTexture;

    }

    /**@FXML
    public void addCamera() {
        cameraList.add(new Camera(
                new Vector3f(0, 0, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100));
        numberCamera++;
        namesCamera.add(String.valueOf(numberCamera));
        chooseCamera.getItems().add(String.valueOf(numberCamera));
    }*/

    /**@FXML
    public void deleteCamera() {
        if (cameraList.size() > 1) {
            if (numberCamera == cameraList.size() - 1) numberCamera--;
            cameraList.remove(cameraList.size() - 1);
            names.remove(cameraList.size() - 1);
            chooseCamera.getItems().remove(numberCamera + 1);
        }
    }*/
    public void deleteMesh() {
        if (mesh.size() > 1) {
            if (numberMesh == mesh.size() - 1) numberMesh--;
            mesh.remove(mesh.size() - 1);
            names.remove(mesh.size() - 1);
            chooseModel.getItems().remove(numberMesh + 1);
        }
    }

    @FXML
    public void choosingCamera(ActionEvent actionEvent) {
        selectedValueCamera = chooseCamera.getSelectionModel().getSelectedItem();
        for (int i = 0; i < namesCamera.size(); i++) {
            if (namesCamera.get(i).equals(selectedValueCamera)) {
                numberCamera = i;
            }
        }
    }
    public void choosingActualModel(ActionEvent actionEvent) {
        selectedValue = chooseModel.getSelectionModel().getSelectedItem();
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(selectedValue)) {
                numberMesh = i;
            }
        }

    }

//    public void updateScale(float scaleX, float scaleY, float scaleZ) {
//        affineTransf.setSx(scaleX);
//        affineTransf.setSy(scaleY);
//        affineTransf.setSz(scaleZ);
//    }
//
//    public void updateRotation(float rotateX, float rotateY, float rotateZ) {
//        affineTransf.setRx(rotateX);
//        affineTransf.setRy(rotateY);
//        affineTransf.setRz(rotateZ);
//    }
//
//
//    public void updateTranslation(float translateX, float translateY, float translateZ) {
//        affineTransf.setTx(translateX);
//        affineTransf.setTy(translateY);
//        affineTransf.setTz(translateZ);
//    }


    public void transform(float scaleX, float scaleY, float scaleZ,
                          float rotateX, float rotateY, float rotateZ,
                          float translateX, float translateY, float translateZ) {
        // Проверка на наличие оригинальной модели
        /**if (originalModel == null) {
         return;
         }*/

        // Создание копии оригинальной модели перед каждым преобразованием
        Model transformModel = new Model();
        transformModel = originalModel;

        // Применение трансформаций к модели
        affineTransf.setSx(scaleX);
        affineTransf.setSy(scaleY);
        affineTransf.setSz(scaleZ);
        affineTransf.setRx(rotateX);
        affineTransf.setRy(rotateY);
        affineTransf.setRz(rotateZ);
        affineTransf.setTx(translateX);
        affineTransf.setTy(translateY);
        affineTransf.setTz(translateZ);

        // Вывод значений аффинных трансформаций в консоль
        System.out.println("Трансформации:");
        System.out.println("Scale X: " + scaleX);
        System.out.println("Scale Y: " + scaleY);
        System.out.println("Scale Z: " + scaleZ);
        System.out.println("Rotate X: " + rotateX);
        System.out.println("Rotate Y: " + rotateY);
        System.out.println("Rotate Z: " + rotateZ);
        System.out.println("Translate X: " + translateX);
        System.out.println("Translate Y: " + translateY);
        System.out.println("Translate Z: " + translateZ);

        // Преобразование модели
        transformModel = affineTransf.transformModel(transformModel);
        originalModel = affineTransf.transformModel(transformModel);

        // Рендеринг трансформированной модели
        renderTransformedModel(transformModel);
        System.out.println("модель изменилась");
    }


    private void renderTransformedModel(Model transformModel) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
        camera.setAspectRatio((float) (width / height));

        if (transformModel != null) {
            try {
                RenderRasterization.render(canvas.getGraphicsContext2D(), graphicsUtils, camera, transformModel, (int) width, (int) height, image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (isStructure) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, transformModel, (int) width, (int) height);
            }
        }

        /**canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
        scene.camera.setAspectRatio((float) (width / height));

        if (mesh.size() != 0) {
            try {
                RenderRasterization.render(canvas.getGraphicsContext2D(), graphicsUtils, camera.get(numberCamera), mesh.get(numberMesh), (int) width, (int) height, image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }*/
        System.out.println("изменение в рендере");
    }
    private float parseTextField(TextField textField, boolean isTranslate) {
        try {
            return Float.parseFloat(textField.getText());
        } catch (NumberFormatException e) {
            if (!isTranslate) {
                return 1;
            }
            return 0;
        }
    }

    public void onClick(ActionEvent actionEvent) {

        //String text = scaleX.getText();
        //parseTextField(scaleX, false);
        transform(
                parseTextField(scaleX, false), parseTextField(scaleY, false), parseTextField(scaleZ, false),
                parseTextField(rotateX, false), parseTextField(rotateY, false), parseTextField(rotateZ, false),
                parseTextField(translateX, true), parseTextField(translateY, true), parseTextField(translateZ, true)
        );
    }
    @FXML
    private void handleMouseScroll(ScrollEvent event) {
        double delta = event.getDeltaY();
        camera.handleMouseScroll((float) delta);
    }

    private double startX;
    private double startY;

    @FXML
    private void mousePressed(MouseEvent mouseEvent) {
        startX = mouseEvent.getX();
        startY = mouseEvent.getY();

        canvas.setOnMouseDragged(mouseEvent1 -> {
            double endX = mouseEvent1.getX();
            double endY = mouseEvent1.getY();
            double dx = startX - endX;
            double dy = endY - startY;

            dx *= 0.05;
            dy *= 0.05;

            camera.movePosition(new Vector3f((float) dx, (float) dy, 0));
            startX = endX;
            startY = endY;
        });
    }


//    @FXML
//    private void handleKeyPress(KeyEvent event) {
//        String direction = event.getCode().toString();
//        camera.get(numberCamera).handleKeyPress(direction);
//    }


    @FXML
    public void handleModelRight(ActionEvent actionEvent) {
        for (ModelOnScene model : scene.modelsList) {
            model.setTranslationX(TRANSLATION);
            RenderEngine.render(canvas.getGraphicsContext2D(), scene.camera, model, (int) canvas.getWidth(),
                    (int) canvas.getHeight());
        }
    }

    @FXML
    public void handleModelBackward(ActionEvent actionEvent) {
        for (ModelOnScene model : scene.modelsList) {
            model.setTranslationY(-TRANSLATION);
            RenderEngine.render(canvas.getGraphicsContext2D(), scene.camera, model, (int) canvas.getWidth(),
                    (int) canvas.getHeight());
        }
    }
    public void handleModelLeft(ActionEvent actionEvent) {
        /**for (ModelOnScene model : scene.modelsList) {
         model.setTranslationX(TRANSLATION);
         //RenderEngine.render(canvas.getGraphicsContext2D(), scene.camera, model, (int) canvas.getWidth(), (int) canvas.getHeight());
         System.out.println("");
         //camera.get(numberCamera).movePosition(new Vector3f(0, TRANSLATION, 0));
         }*/
        affineTransf.setTx(-TRANSLATION);
        /**if (transformModel == null) {
         transformModel = new Model(noTransformModel);
         }*/

        //transformModel = affineTransf.transformModel(transformModel);
    }

    @FXML
    public void handleModelForward(ActionEvent actionEvent) {
        for (ModelOnScene model : scene.modelsList) {
            model.setTranslationY(TRANSLATION);
            RenderEngine.render(canvas.getGraphicsContext2D(), scene.camera, model, (int) canvas.getWidth(),
                    (int) canvas.getHeight());
        }
    }


}
