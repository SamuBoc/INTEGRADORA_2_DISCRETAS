package com.example.animationg3.control;

import com.example.animationg3.screens.ScreenA;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Canvas canvas;
    private ScreenA screenA;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.screenA = new ScreenA(this.canvas);

        this.canvas.setOnKeyPressed(event -> {
            screenA.onKeyPressed(event);
        });

        canvas.setOnKeyReleased(event -> {
            screenA.onKeyReleased(event);
        });

        // Hilo de JavaFX
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    screenA.paint();
                });
            }
        }).start();

        // Manejar clics en las imágenes
        canvas.setOnMouseClicked(this::handleMouseClick);
    }

    private void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        // Verificar si el clic está dentro de la región de la imagen "respawn"
        if (isMouseInRegion(x, y, screenA.getRespawnX(), screenA.getRespawnY(),
                screenA.getRespawnWidth(), screenA.getRespawnHeight())) {
            screenA.reiniciarJuego();
        }

        // Verificar si el clic está dentro de la región de la imagen "quit_game"
        if (isMouseInRegion(x, y, screenA.getQuitGameX(), screenA.getQuitGameY(),
                screenA.getQuitGameWidth(), screenA.getQuitGameHeight())) {
            Platform.exit();
        }
    }

    private boolean isMouseInRegion(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
