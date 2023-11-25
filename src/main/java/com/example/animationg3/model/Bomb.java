package com.example.animationg3.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Bomb {

    //array de imagenes
    private ArrayList<Image> arrayImagesPre;
    private ArrayList<Image> arrayImagesPost;
    private ArrayList<Box> boxesToChange;
    private ArrayList<Box> explosionPositions;


    private boolean RIGHT;

    private boolean LEFT;

    private boolean UP;

    private boolean DOWN;


    // elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    public static final String PATH_PRE = "/animations/Bomba/sprite_";
    public static final String PATH_POST= "/animations/ParticulasBomba/sprite_";


    private Position position;

    private int frame;

    public boolean finish;

    private long startTime;
    private long animationDuration;

    private long explosionStartTime;
    private long explosionDuration;


    public Bomb(double x,double y, Canvas canvas) {
        this.position = new Position(x,y);
        this.canvas = canvas;
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.arrayImagesPre = new ArrayList<>();
        this.arrayImagesPost = new ArrayList<>();
        this.boxesToChange = new ArrayList<>();
        this.explosionPositions = new ArrayList<>();

        this.frame = 0;
        this.finish = false;

        this.RIGHT = false;
        this.DOWN = false;
        this.LEFT = false;
        this.UP = false;


        this.explosionStartTime = -1;  // Se establece en -1 para indicar que la explosión aún no ha comenzado
        this.explosionDuration = 1500; // Duración de la animación de explosión en milisegundos (en este caso, 2 segundos)

        this.startTime = System.currentTimeMillis();
        this.animationDuration = 2000; // Duración en milisegundos (en este caso, 2 segundos)

        String imagePath = "";

        for (int i = 0; i < 33; i++) {
            if(i<10){
                imagePath = PATH_PRE + "0" + i + ".png";
            }
            if(i>=10){
                imagePath = PATH_PRE + i + ".png";
            }
            Image image = new Image(getClass().getResourceAsStream(imagePath), 20, 27, false, false);
            this.arrayImagesPre.add(image);
        }

        for (int i = 0; i < 8; i++) {
            imagePath = PATH_POST + i + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath), 30,36, false, false);
            this.arrayImagesPost.add(image);
        }
    }

    public Position getPosition() {
        return position;
    }


    public void setPosition(Position position) {
        this.position = position;
    }

    public void paint(double width, double height) {
        if (!finish) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;

            if (elapsedTime < animationDuration) {
                graphicsContext.drawImage(arrayImagesPre.get(frame % 32), position.getX(), position.getY());
            } else {
                // La animación de la bomba ha terminado, pero aún no se ha pintado la explosión
                drawExplosion();
                expandExplosion(Direction.up);
                expandExplosion(Direction.down);
                expandExplosion(Direction.left);
                expandExplosion(Direction.right);

                // Verifica si la animación de la explosión ha comenzado y si ha pasado menos tiempo que la duración deseada
                if (explosionStartTime == -1) {
                    explosionStartTime = System.currentTimeMillis();
                }

                // Actualiza la duración de la animación de explosión según tus necesidades
                explosionDuration = 1500; // Cambia el valor a la duración deseada en milisegundos (en este caso, 3 segundos)

                finish = currentTime - explosionStartTime > explosionDuration;
            }
        }
        frame++;
    }



    private void expandExplosion(Direction direction) {
        if (canExpandInDirection(direction)) {
            // Calcula la posición de expansión en la dirección especificada
            double x = position.getX();
            double y = position.getY();
            switch (direction) {
                case up -> y -= 46;
                case down -> y += 40;
                case left -> x -= 41.7;
                case right -> x += 41.7;
            }

            // Dibuja la imagen de explosión en la posición calculada
            graphicsContext.drawImage(arrayImagesPost.get(frame % 8), x, y);
        }
    }


    private boolean canExpandInDirection(Direction direction) {
        switch (direction) {
            case up -> {return UP;}
            case down -> {return DOWN;}
            case left -> {return LEFT;}
            case right -> {return RIGHT;}
            default -> {return false;}
        }
    }

    private void drawExplosion() {
        // Dibuja la imagen de explosión en las direcciones permitidas
        if (UP) expandExplosion(Direction.up);
        if (DOWN) expandExplosion(Direction.down);
        if (LEFT) expandExplosion(Direction.left);
        if (RIGHT) expandExplosion(Direction.right);
    }



    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Rectangle obtenerRectangle(Direction direction){
        switch (direction){
            case up -> {return new Rectangle(this.position.getX(), this.position.getY() - 15.0, 10, 10);}
            case left -> {return new Rectangle(this.position.getX() - 50.0, this.position.getY(), 25, 25);}
            case right ->{return new Rectangle(this.position.getX() + 25.0, this.position.getY(), 25, 10);}
            case down -> {return new Rectangle(this.position.getX()- 6.0, this.position.getY() + 60.0 , 25, 10);}
        }
        return new  Rectangle(this.position.getX(), this.position.getY(), 25, 35);
    }


    public void verificarColision(ArrayList<Box> boxList) {

        Rectangle bombDown = obtenerRectangle(Direction.down);
        Rectangle bombUp = obtenerRectangle(Direction.up);
        Rectangle bombRight = obtenerRectangle(Direction.right);
        Rectangle bombLeft = obtenerRectangle(Direction.left);

        for (Box box : boxList) {
            Rectangle boxRectangle = box.obtenerRectangle();
            switch (box.getTypeOcuped()) {
                case empty, wood -> {
                    if (bombDown.getBoundsInLocal().intersects(boxRectangle.getBoundsInLocal()) && (box.getTypeOcuped() == TypeOcuped.wood || box.getTypeOcuped() == TypeOcuped.empty)) {
                        DOWN = true;
                        System.out.println("abajo");
                        bombLeft.setStroke(Color.RED);
                        boxesToChange.add(box);
                    } else if (bombUp.getBoundsInLocal().intersects(boxRectangle.getBoundsInLocal()) && (box.getTypeOcuped() == TypeOcuped.wood || box.getTypeOcuped() == TypeOcuped.empty)) {
                        UP = true;
                        System.out.println("arriba");
                        boxesToChange.add(box);
                    } else if (bombRight.getBoundsInLocal().intersects(boxRectangle.getBoundsInLocal()) && (box.getTypeOcuped() == TypeOcuped.wood || box.getTypeOcuped() == TypeOcuped.empty)) {
                        RIGHT = true;
                        System.out.println("derecha");
                        boxesToChange.add(box);
                    } else if (bombLeft.getBoundsInLocal().intersects(boxRectangle.getBoundsInLocal()) && (box.getTypeOcuped() == TypeOcuped.wood || box.getTypeOcuped() == TypeOcuped.empty)) {
                        LEFT = true;
                        System.out.println("izquierda");
                        boxesToChange.add(box);
                    }
                }
            }
        }
    }

    public ArrayList<Box> getBoxesToChange() {
        return boxesToChange;
    }


    public boolean isFinish() {
        return finish;
    }
}
