package com.example.animationg3.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Box {

    private Position position;
    private TypeOcuped typeOcuped;

    public static final String PATH_WOOD = "/animations/objetos/cuadros/MADERA.png";

    private Canvas canvas;
    private GraphicsContext graphicsContext;
    Image normal;

    private int wood_V = 40;

    private int wood_v_1= 44;



    public Box(double x,double y, TypeOcuped typeOcuped, Canvas canvas) {
        this.position = new Position(x,y);
        this.typeOcuped = typeOcuped;
        this.canvas = canvas;
        this.graphicsContext = this.canvas.getGraphicsContext2D();


        switch (typeOcuped){
            case wood -> {
                normal = new Image(getClass().getResourceAsStream(PATH_WOOD), wood_V, wood_v_1, false, true);
            }
        }

    }

    public void paint(double width, double height){
        switch (typeOcuped){
            case concrete -> {
                graphicsContext.strokeRect(position.getX(), position.getY(), 44, 46);
                graphicsContext.setStroke(Color.VIOLET);
            }
            case empty -> {
                graphicsContext.drawImage(normal, position.getX(), position.getY());
            }
            case wood -> {
                graphicsContext.drawImage(normal, position.getX(), position.getY());
            }
            case bomb -> {
                graphicsContext.strokeRect(position.getX(), position.getY(), 17, 27);
                graphicsContext.drawImage(normal, position.getX(), position.getY());
            }
        }
    }



    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public TypeOcuped getTypeOcuped() {
        return typeOcuped;
    }

    public void setTypeOcuped(TypeOcuped typeOcuped) {
        this.typeOcuped = typeOcuped;
    }

    public Rectangle obtenerRectangle(){
        switch (typeOcuped){
            case concrete, wood -> {return new Rectangle(this.position.getX(), this.position.getY(), 40, 46);}
            case empty ->  {return new Rectangle(this.position.getX(), this.position.getY()+20, 20, 27);}
        }
        return new Rectangle(this.position.getX(), this.position.getY(), 40, 46);
    }

    public String toString(){
        String out = "";
        switch (typeOcuped){
            case concrete -> {
                out = "concrete";
            }
            case empty -> {
                out = "empty";
            }
        }
        return out;
    }

}
