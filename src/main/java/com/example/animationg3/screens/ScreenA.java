package com.example.animationg3.screens;

import com.example.animationg3.model.*;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Iterator;
import java.util.Random;

import java.util.ArrayList;

public class ScreenA {

    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private Avatar avatar;
    private Image mundo_1 = new Image(getClass().getResourceAsStream("/animations/Personaje/Mapas/MUNDO_1.png"));
    private ArrayList<Box> boxList;

    //posiciones del concrete
    private ArrayList<Double> concretePositionsX;
    private ArrayList<Double> concretePositionsY;

    //posiciones casillas vacias
    private ArrayList<Double> emptyPositionsX;
    private ArrayList<Double> emptyPositionsY;

    //posiciones de la madera
    private ArrayList<Double> woodPositionsX;
    private ArrayList<Double> woodPositionsY;

    //array de numeros random para las maderas
    private ArrayList<Integer> indicatorWood;

    private Timeline bombaTimeline;


    private Image heart_Image = new Image(getClass().getResourceAsStream("/animations/objetos/cuadros/CORAZON.png"));

    private Image you_die = new Image(getClass().getResourceAsStream("/animations/MUERTE/sprite_0.png"));
    private Image respawn = new Image(getClass().getResourceAsStream("/animations/MUERTE/sprite_1.png"));
    private Image quit_game = new Image(getClass().getResourceAsStream("/animations/MUERTE/sprite_2.png"));

    private Rectangle respawnOverlay;
    private Rectangle quitGameOverlay;

    private boolean respawnClicked = false;
    private boolean quitGameClicked = false;

    private int heartAnimationCounter = 0;
    private boolean increasingScale = true;

    public ScreenA(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.avatar = new Avatar(this.canvas);
        this.boxList = new ArrayList<>();

        respawnOverlay = new Rectangle(getRespawnX(), getRespawnY(), getRespawnWidth(), getRespawnHeight());
        quitGameOverlay = new Rectangle(getQuitGameX(), getQuitGameY(), getQuitGameWidth(), getQuitGameHeight());

        setMouseEvents();

        bombaTimeline = new Timeline();

        respawnOverlay = new Rectangle(200, 35);
        quitGameOverlay = new Rectangle(200, 35);

        concretePositionsX = new ArrayList<>();
        concretePositionsY = new ArrayList<>();

        emptyPositionsY = new ArrayList<>();
        emptyPositionsX = new ArrayList<>();

        woodPositionsY = new ArrayList<>();
        woodPositionsX = new ArrayList<>();

        indicatorWood = new ArrayList<>();

        Random rand = new Random();

        double inicioX = 43;
        double inicioY = 45;

        double cambioX = 41.7;
        double cambioY = 46;

        int contador = 0;

        for (int i = 0; i < 143; i++) { // Ajusta el número total de casillas según tus necesidades
            int upperbound = 100;
            int int_random = rand.nextInt(upperbound);
            indicatorWood.add(int_random);
        }

        for (int i = 0; i < 13; i++) { // fila, inicia en 1 acaba en 12
            for (int j = 0; j < 11; j++) {
                double x = inicioX + i * cambioX;
                double y = inicioY + j * cambioY;

                //tipos concreto
                if (i % 2 != 0 && j % 2 != 0) {
                    concretePositionsX.add(x);
                    concretePositionsY.add(y);
                    Box box = new Box(x, y, TypeOcuped.concrete, canvas);
                    boxList.add(box);
                } else {
                    int index = i * 11 + j;
                    //tipo madera
                    if (indicatorWood.get(index) % 2 != 0 || indicatorWood.get(index) % 3 == 0 &&  indicatorWood.get(index) != 1) {
                        emptyPositionsX.add(x);
                        emptyPositionsY.add(y);
                        Box box = new Box(x, y, TypeOcuped.wood, canvas);
                        boxList.add(box);
                        //tipo vacia
                    } else {
                        emptyPositionsX.add(x);
                        emptyPositionsY.add(y);
                        Box box = new Box(x, y, TypeOcuped.empty, canvas);
                        boxList.add(box);
                    }
                }
            }
        }
        boxList.get(0).setTypeOcuped(TypeOcuped.empty);
        boxList.get(1).setTypeOcuped(TypeOcuped.empty);
        boxList.get(13).setTypeOcuped(TypeOcuped.empty);
    }

    public void paint() {
        graphicsContext.drawImage(mundo_1, 0, 0, canvas.getWidth(), canvas.getHeight());

        // Dibujar el fondo
        avatar.verificarColision(boxList);
        avatar.paint(canvas.getWidth(), canvas.getHeight());

        double heartWidth = 20;
        double heartHeight = 20;
        double heartSpacing = 10;

        for (int i = 0; i < avatar.getVidas(); i++) {
            double x = 20 + (heartWidth + heartSpacing) * i;
            double y = 5;

            // Cambiar la escala para el efecto de latido
            double scale = 1.0;
            if (i == heartAnimationCounter) {
                scale += (increasingScale ? 0.1 : -0.1);
            }

            graphicsContext.save();
            graphicsContext.scale(scale, scale);
            graphicsContext.drawImage(heart_Image, x / scale, y / scale, heartWidth, heartHeight);
            graphicsContext.restore();
        }

        // Actualizar el contador de animación
        heartAnimationCounter++;
        if (heartAnimationCounter >= avatar.getVidas()) {
            heartAnimationCounter = 0;
            increasingScale = !increasingScale;
        }

        for (Box box : boxList) {
            if (box.getTypeOcuped() == TypeOcuped.wood && !isBoxInBombsToChange(box)) {
                box.paint(canvas.getWidth(), canvas.getHeight());
            }
        }

        for (Bomb bomb : avatar.getBombsArray()) {
            if (!bomb.finish) {
                bomb.paint(canvas.getWidth(), canvas.getHeight());
                for (Box box : bomb.getBoxesToChange()) {
                    box.setTypeOcuped(TypeOcuped.empty);
                }
            }
        }

        if (respawnOverlay.isHover() || respawnClicked) {
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(2);
            graphicsContext.strokeRect(getRespawnX(), getRespawnY(), getRespawnWidth(), getRespawnHeight());
        }

        // Dibujar el rectángulo blanco alrededor de "quit_game" si el mouse está sobre él
        if (quitGameOverlay.isHover() || quitGameClicked) {
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setLineWidth(2);
            graphicsContext.strokeRect(getQuitGameX(), getQuitGameY(), getQuitGameWidth(), getQuitGameHeight());
        }

        // Dibujar el rectángulo rojo si las vidas son cero o menos
        if (avatar.getVidas() <= 0) {
            graphicsContext.setFill(Color.rgb(255, 0, 0, 0.4));
            graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Calcular la posición para centrar la imagen "you_die" en la parte superior
            double youDieX = (canvas.getWidth() - 200) / 2; // La mitad del ancho del canvas menos la mitad del ancho de la imagen
            double youDieY = (canvas.getHeight() - 35) / 4; // Un cuarto de la altura del canvas

            // Dibujar la imagen "you_die" centrada en la parte superior
            graphicsContext.drawImage(you_die, youDieX, youDieY, 200, 35);

            // Espacio de 40 píxeles
            double espacioEntreImagenes = 60;

            // Dibujar la imagen "respawn" debajo de "you_die" con espacio
            double respawnX = (canvas.getWidth() - 200) / 2; // La mitad del ancho del canvas menos la mitad del ancho de la imagen
            double respawnY = youDieY + 35 + espacioEntreImagenes; // Añadir espacio
            graphicsContext.drawImage(respawn, respawnX, respawnY, 200, 35);

            // Dibujar la imagen "quit_game" debajo de "respawn" con espacio
            double quitGameX = (canvas.getWidth() - 200) / 2; // La mitad del ancho del canvas menos la mitad del ancho de la imagen
            double quitGameY = respawnY + 35 + espacioEntreImagenes; // Añadir espacio
            graphicsContext.drawImage(quit_game, quitGameX, quitGameY, 200, 35);
        }
    }

    public void onKeyPressed(KeyEvent event) {
        if(avatar.getVidas() > 0 ){
            this.avatar.onKeyPressed(event);

            if (event.getCode() == KeyCode.SPACE) {
                double bombX = avatar.getxBOMB() + 10.0;
                double bombY = avatar.getyBOMB() + 10.0;
                Bomb bomb = new Bomb(bombX, bombY, canvas);
                avatar.ponerBomba(bomb);
                bomb.setStartTime(System.currentTimeMillis());

                // Agrega un retraso de 2 segundos antes de agregar la bomba a la lista
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(e -> {
                    bomb.verificarColision(boxList);  // Elimina esta línea
                    avatar.getBombsArray().add(bomb);  // Agrega la bomba a la lista después del retraso

                    for(Box box: bomb.getBoxesToChange()){
                        box.setTypeOcuped(TypeOcuped.empty);

                        //si se detecta la colision de la "onda" con el jugadro, se le quita la vida
                        if(avatar.obtenerRectangle().getBoundsInLocal().intersects(box.obtenerRectangle().getBoundsInLocal())){
                            avatar.quitarVida();
                        }
                    }

                });

                pause.play();
            }
        }
    }

    public void onKeyReleased(KeyEvent event){
        this.avatar.onKeyReleased(event);
    }

    private boolean isBoxInBombsToChange(Box box) {
        for (Bomb bomb : avatar.getBombsArray()) {
            if (bomb.getBoxesToChange().contains(box)) {
                return true;
            }
        }
        return false;
    }

    public void setMouseEvents() {
        // ... (código anterior)

        respawnOverlay.setOnMouseEntered(event -> {
            respawnOverlay.setStroke(Color.WHITE);
        });

        respawnOverlay.setOnMouseExited(event -> {
            if (!respawnClicked) {
                respawnOverlay.setStroke(null);
            }
        });

        respawnOverlay.setOnMouseClicked(event -> {
            // Reiniciar el juego (puedes reiniciar la pantalla o reiniciar los valores del juego)
            respawnClicked = true;
            quitGameClicked = false;
            reiniciarJuego();
        });

        quitGameOverlay.setOnMouseEntered(event -> {
            quitGameOverlay.setStroke(Color.WHITE);
        });

        quitGameOverlay.setOnMouseExited(event -> {
            if (!quitGameClicked) {
                quitGameOverlay.setStroke(null);
            }
        });

        quitGameOverlay.setOnMouseClicked(event -> {
            // Cerrar la aplicación
            quitGameClicked = true;
            respawnClicked = false;
            Platform.exit();
        });
    }

    public void reiniciarJuego() {
        // Aquí puedes reiniciar los valores del juego según sea necesario
        // Por ejemplo, reiniciar la posición del jugador, restablecer vidas, etc.

        this.avatar.setVidas(3);
        this.avatar.setPosition(55.0,55.0);

        this.boxList.clear();

        double inicioX = 43;
        double inicioY = 45;

        double cambioX = 41.7;
        double cambioY = 46;

        int contador = 0;

        Random rand = new Random();

        for (int i = 0; i < 143; i++) { // Ajusta el número total de casillas según tus necesidades
            int upperbound = 100;
            int int_random = rand.nextInt(upperbound);
            indicatorWood.add(int_random);
        }

        for (int i = 0; i < 13; i++) { // fila, inicia en 1 acaba en 12
            for (int j = 0; j < 11; j++) {
                double x = inicioX + i * cambioX;
                double y = inicioY + j * cambioY;

                //tipos concreto
                if (i % 2 != 0 && j % 2 != 0) {
                    concretePositionsX.add(x);
                    concretePositionsY.add(y);
                    Box box = new Box(x, y, TypeOcuped.concrete, canvas);
                    boxList.add(box);
                } else {
                    int index = i * 11 + j;
                    //tipo madera
                    if (indicatorWood.get(index) % 2 != 0 || indicatorWood.get(index) % 3 == 0 &&  indicatorWood.get(index) != 1) {
                        emptyPositionsX.add(x);
                        emptyPositionsY.add(y);
                        Box box = new Box(x, y, TypeOcuped.wood, canvas);
                        boxList.add(box);
                        //tipo vacia
                    } else {
                        emptyPositionsX.add(x);
                        emptyPositionsY.add(y);
                        Box box = new Box(x, y, TypeOcuped.empty, canvas);
                        boxList.add(box);
                    }
                }
            }
        }
        boxList.get(0).setTypeOcuped(TypeOcuped.empty);
        boxList.get(1).setTypeOcuped(TypeOcuped.empty);
        boxList.get(13).setTypeOcuped(TypeOcuped.empty);

    }

    public double getRespawnX() {
        // Calcular la posición X de la imagen "respawn"
        return (canvas.getWidth() - 200) / 2;
    }

    public double getRespawnY() {
        // Calcular la posición Y de la imagen "respawn"
        double youDieY = (canvas.getHeight() - 35) / 4;
        double espacioEntreImagenes = 60;
        return youDieY + 35 + espacioEntreImagenes;
    }

    public double getRespawnWidth() {
        // Ancho de la imagen "respawn"
        return 200;
    }

    public double getRespawnHeight() {
        // Altura de la imagen "respawn"
        return 35;
    }

    public double getQuitGameX() {
        // Calcular la posición X de la imagen "quit_game"
        return (canvas.getWidth() - 200) / 2;
    }

    public double getQuitGameY() {
        // Calcular la posición Y de la imagen "quit_game"
        double respawnY = getRespawnY();
        double espacioEntreImagenes = 60;
        return respawnY + 35 + espacioEntreImagenes;
    }

    public double getQuitGameWidth() {
        // Ancho de la imagen "quit_game"
        return 200;
    }

    public double getQuitGameHeight() {
        // Altura de la imagen "quit_game"
        return 35;
    }

}
