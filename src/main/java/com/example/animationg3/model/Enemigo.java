package com.example.animationg3.model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Enemigo {

    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private int vidas;
    private Position position;
    private State state;
    private Direction direction;

    public Enemigo(Canvas canvas, GraphicsContext graphicsContext, Position position) {
        this.canvas = canvas;
        this.graphicsContext = graphicsContext;
        this.vidas = vidas;
        this.position = position;
    }

    public void paint(double width, double height){

    }


}
