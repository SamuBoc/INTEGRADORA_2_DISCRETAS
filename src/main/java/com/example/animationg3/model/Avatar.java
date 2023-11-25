package com.example.animationg3.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Avatar{


    // imagenes CAMINAR ABAJO
    public static final String PATH_RUN_DOWN = "/animations/Personaje/CAMINAR_abajo/CAMINAR_abajo";

    //imagenes CAMINAR ARRIBA
    public static final String PATH_RUN_UP= "/animations/Personaje/CAMINAR_arriba/CAMINAR_arriba";

    //imagenes CAMINAR DERECHA
    public static final String PATH_RUN_RIGHT= "/animations/Personaje/CAMINAR_derecha/CAMINAR_derecha";
    //imagenes CAMINAR IZQUIERDA
    public static final String PATH_RUN_LEFT= "/animations/Personaje/CAMINAR_izquierda/CAMINAR_izquierda";

    //estaticos
    public static final String PATH_IDLE= "/animations/Personaje/INMOVIL/estatico";

    private Image left_IDLE = new Image(getClass().getResourceAsStream("/animations/Personaje/INMOVIL/estatico3.png"));
    private Image right_IDLE = new Image(getClass().getResourceAsStream("/animations/Personaje/INMOVIL/estatico1.png"), 15, 30, false, false);
    //v1 anchura


    private int vidas;

    // elementos graficos
    private Canvas canvas;
    private GraphicsContext graphicsContext;

    // arraylist imagenes quieto
    private ArrayList<Image> idles;

    // arraylist imagenes corriendo
    private ArrayList<Image> runs_up;
    private ArrayList<Image> runs_down;
    private ArrayList<Image> runs_right;
    private ArrayList<Image> runs_left;

    //habilidades del jugador
    private int bombsPlaced;
    private int maxBombs; // Número máximo de bombas permitidas
    private ArrayList<Bomb> bombsArray;
    private long startTime;
    private long animationDuration;
    private long lastBombTime;
    private long bombCooldown = 2000;

    private double xBOMB;
    private double yBOMB;




    private int frame;


    // elemntos espaciales
    private Position position;
    private State state;
    private Direction direction;

    private boolean rightPressed;
    private boolean leftPressed;
    private boolean upPressed;
    private boolean downPressed;
    private boolean spaceBar;

    private CollisionType currentCollision;


    public Avatar(Canvas canvas){
        this.canvas = canvas;
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        // 0 is idle | 1 is run
        this.state = State.IDLE;
        this.frame = 1;

        this.startTime = System.currentTimeMillis();
        this.animationDuration = 2000; // Duración en milisegundos (en este caso, 2 segundos)

        this.vidas = 3;

        this.bombsPlaced = 0;
        this.maxBombs = 1;
        this.bombsArray = new ArrayList<>();

        this.idles = new ArrayList<>();
        this.runs_up = new ArrayList<>();
        this.runs_left = new ArrayList<>();
        this.runs_right = new ArrayList<>();
        this.runs_down = new ArrayList<>();
        this.idles = new ArrayList<>();

        this.xBOMB = 0;
        this.yBOMB = 0;

        this.position = new Position(55,55);

        this.direction = Direction.down;

        for (int i = 1; i <= 3; i++) {
            Image image = new Image(getClass().getResourceAsStream(PATH_RUN_DOWN +i+".png"), 20, 35, false, false);
            this.runs_down.add(image);
        }

        for (int i = 1; i <=3; i++) {
            Image image = new Image(getClass().getResourceAsStream(PATH_RUN_UP+i+".png"), 20, 35, false, false);
            this.runs_up.add(image);
        }


        //imagenes quieto
        for (int i = 0; i <=3; i++) {
            Image image = new Image(getClass().getResourceAsStream(PATH_IDLE+i+".png"), 20, 35, false, false);
            this.idles.add(image);
        }

        //lados
        for (int i = 1; i <=4; i++) {
            Image image = new Image(getClass().getResourceAsStream(PATH_RUN_RIGHT+i+".png"), 20, 35, false, false);
            this.runs_right.add(image);
        }

        for (int i = 1; i <=4; i++) {
            Image image = new Image(getClass().getResourceAsStream(PATH_RUN_LEFT+i+".png"), 20, 35, false, false);
            this.runs_left.add(image);
        }


    }

    public void paint(double width, double height){
        switch(state){
            case RUN-> {
                onMove(width,height);
                switch(direction){
                    case left -> {graphicsContext.drawImage(runs_left.get(frame%4), position.getX(), position.getY());
                        graphicsContext.setStroke(Color.RED);
                    }
                    case up -> {graphicsContext.drawImage(runs_up.get(frame%3), position.getX(), position.getY());
                    }
                    case down -> {graphicsContext.drawImage(runs_down.get(frame%3), position.getX(), position.getY());
                    }
                    case right -> {graphicsContext.drawImage(runs_right.get(frame%4), position.getX(), position.getY());
                    }
                }
                frame++;}
            case IDLE -> {
                switch(direction){//3, lado izquierdo
                    case left -> {graphicsContext.drawImage(idles.get(3), position.getX(), position.getY());
                        }
                        //2, arriba
                    case up -> {graphicsContext.drawImage(idles.get(2), position.getX(), position.getY());
                    }
                    //0, abajo
                    case down -> {graphicsContext.drawImage(idles.get(0), position.getX(), position.getY());
                    }
                    //1, lado derecho
                    case right -> {graphicsContext.drawImage(idles.get(1), position.getX(), position.getY());
                    }
                }
                frame++;
            }
        }
    }


    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case RIGHT: {
                state = State.RUN;
                rightPressed = true;
                direction = Direction.right;
                break;
            }
            case LEFT: {
                state = State.RUN;
                leftPressed = true;
                direction = Direction.left;
                break;
            }
            case UP: {
                state = State.RUN;
                upPressed = true;
                direction = Direction.up;
                break;
            }
            case DOWN: {
                state = State.RUN;
                downPressed = true;
                direction = Direction.down;
                break;
            }
        }
    }

    public ArrayList<Bomb> getBombsArray() {
        return bombsArray;
    }

    public void ponerBomba(Bomb bomb) {
        if (bombsArray.isEmpty() || todasBombasHanExplotado()) {
            bombsArray.add(bomb);
            bombsPlaced++;
            lastBombTime = System.currentTimeMillis();
        }
    }

    private boolean todasBombasHanExplotado() {
        for (Bomb bomb : bombsArray) {
            if (!bomb.isFinish()) {
                return false;
            }
        }
        return true;
    }

    public void setPosition(Double x, double y) {
        this.position = new Position(x, y);
    }

    public void onKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case RIGHT: {
                state = State.IDLE;
                direction = Direction.right;
                rightPressed = false;
                break;
            }
            case LEFT: {
                state = State.IDLE;
                direction = Direction.left;
                leftPressed = false;
                break;
            }
            case UP: {
                state = State.IDLE;
                direction = Direction.up;
                upPressed = false;
                break;
            }
            case DOWN: {
                state = State.IDLE;
                direction = Direction.down;
                downPressed = false;
                break;
            }
        }
    }


    // te vas a mover siempre y cuando sea menor a X
    public void onMove(double width, double height) {
            if (rightPressed && (position.getX() + 5) <= width - 70) {
                direction = Direction.right;
                position.setX(position.getX() + 5);
            } else if (leftPressed && (position.getX() - 5) >= 45) {
                direction = Direction.left;
                position.setX(position.getX() - 5);
            } else if (upPressed && (position.getY() - 5) >= 45) {
                direction = Direction.up;
                position.setY(position.getY() - 5);
            } else if (downPressed && (position.getY() + 5) <= height - 78) {
                direction = Direction.down;
                position.setY(position.getY() + 5);

        }
    }

    public String coordenadas(){
        return "Y - " + position.getY() + " \n X - " + position.getX() ;
    }

    public Rectangle obtenerRectangle(){
        return new Rectangle(this.position.getX(), this.position.getY(), 25, 35);
    }

    public Rectangle newRectangle(double x, double y){
        return new Rectangle(x, y, 25, 40);
    }


    public void verificarColision(ArrayList<Box> boxList) {
        Rectangle avatarRectangle = obtenerRectangle();
        Direction collisionDirection = Direction.none;

        for (Box box : boxList) {
            switch (box.getTypeOcuped()){
                case wood, concrete -> {
                    //rectangulo de la box
                    Rectangle boxRectangle = box.obtenerRectangle();
                    if (avatarRectangle.getBoundsInLocal().intersects(boxRectangle.getBoundsInLocal())) {
                        collisionDirection = detectarColision(boxRectangle, box.getTypeOcuped());
                        break;  // Si hay colisión, no es necesario verificar con otras cajas
                    }
                }
                case empty -> {
                    Rectangle boxRectangle = box.obtenerRectangle();
                    if (avatarRectangle.getBoundsInLocal().intersects(boxRectangle.getBoundsInLocal())) {
                        xBOMB = box.getPosition().getX();
                        yBOMB = box.getPosition().getY();
                    }
                }
            }
        }

        detenerMovimientoEnDireccion(collisionDirection);
    }

    public Direction detectarColision(Rectangle rectangle, TypeOcuped typeOcuped) {
        double xOffset = 5; // Ajusta el valor según sea necesario para evitar la superposición
        double yOffset = 5;
        switch (typeOcuped){
            case concrete -> {
                switch (direction) {
                    case up -> {
                        Rectangle newRectangle = newRectangle(position.getX(), position.getY() - yOffset - 10);
                        if (newRectangle.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())) {
                            return Direction.up;
                        }
                    }
                    case right -> {
                        Rectangle newRectangle = newRectangle(position.getX() + 20 + xOffset, position.getY() + yOffset);
                        if (newRectangle.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())) {
                            return Direction.right;
                        }
                    }
                    case left -> {
                        Rectangle newRectangle = newRectangle(position.getX() - xOffset, position.getY() + yOffset);
                        if (newRectangle.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())) {
                            return Direction.left;
                        }
                    }
                    case down -> {
                        Rectangle newRectangle = newRectangle(position.getX(), position.getY()  + yOffset + 7);
                        if (newRectangle.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())) {
                            return Direction.down;
                        }
                    }
                }
            }
            case wood -> {
                switch (direction) {
                    case up -> {
                        Rectangle newRectangle = newRectangle(position.getX(), position.getY() - yOffset - 10);
                        if (newRectangle.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())) {
                            return Direction.up;
                        }
                    }
                    case right -> {
                        Rectangle newRectangle = newRectangle(position.getX() + 40 + xOffset, position.getY() + yOffset);
                        if (newRectangle.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())) {
                            return Direction.right;
                        }
                    }
                    case left -> {
                        Rectangle newRectangle = newRectangle(position.getX() - xOffset, position.getY() + yOffset);
                        if (newRectangle.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())) {
                            return Direction.left;
                        }
                    }
                    case down -> {
                        Rectangle newRectangle = newRectangle(position.getX(), position.getY()  + yOffset + 7);
                        if (newRectangle.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())) {
                            return Direction.down;
                        }
                    }
                }
            }
        }
        return Direction.none;
    }

    public void quitarVida() {
        vidas--;
    }

    public void detenerMovimientoEnDireccion(Direction collisionDirection) {
        switch (collisionDirection) {
            case up -> {
                upPressed = false;
                position.setY(position.getY() + 5); // Ajusta el valor según sea necesario
            }
            case down -> {
                downPressed = false;
                position.setY(position.getY() - 2); // Ajusta el valor según sea necesario
            }
            case left -> {
                leftPressed = false;
                position.setX(position.getX() + 5); // Ajusta el valor según sea necesario
            }
            case right -> {
                rightPressed = false;
                position.setX(position.getX() - 5); // Ajusta el valor según sea necesario
            }
        }
    }

    public Position getPosition() {
        return position;
    }

    public double getxBOMB() {
        return xBOMB;
    }

    public double getyBOMB() {
        return yBOMB;
    }


    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }
}
