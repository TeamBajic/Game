package com.bajic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Created by name on 5.11.2015 г..
 */
public class Level{
    private double squareSize;
    private double froggerStartingPositionX;
    private double froggerStartingPositionY;
    private ArrayList<Boolean> visitedRows = new ArrayList<>();
    private double timeForLevel;
    private int visRowsCount = 0;
    private ArrayList<MyImage> images = new ArrayList<>();
    private ImageView backgroundImage;

    public Level(int levelIndex){
        backgroundImage = new ImageView(new Image("Files/Sprites/Frogger_background.jpg"));
        double levelHeight = backgroundImage.getBoundsInParent().getHeight();
        Main.window.getChildren().add(backgroundImage);
        backgroundImage.toBack();
        squareSize = backgroundImage.getBoundsInParent().getWidth() / 20; // background width is 900
        backgroundImage.relocate(-(squareSize * 3), -(levelHeight - Main.window.getPrefHeight()) - Main.HUD_SPACE);
        Main.frogger.relocate(positionOnColumn(10),
                positionOnRow(19) - Main.frogger.getBoundsInParent().getHeight() / 2);
        this.setFroggerStartingPositionX(Main.frogger.getLayoutX());
        this.setFroggerStartingPositionY(Main.frogger.getLayoutY());
        initializeLevel(levelIndex);
    }
    public void initializeLevel(int level) {
        switch (level){
            case 1:{
                setTimeForLevel(60);
                initializeVisitedRows(20);
                createImage("Car.jpg", 11, 1, true,1);
                createImage("Car.jpg", 11, 6, true,1);
                createImage("Car.jpg", 11, 11, true,1);
                createImage("Car.jpg", 10, 2, false,1);
                createImage("Car.jpg", 10, 7, false,1);
                createImage("Car.jpg", 10, 12, false,1);
                createImage("Car.jpg", 9, 0, true,2);
                createImage("Car.jpg", 9, 5, true,2);
                createImage("Car.jpg", 9, 12, true,2);
                createImage("Car.jpg", 8, 2, false,1);
                createImage("Car.jpg", 8, 7, false,1);
                createImage("Car.jpg", 8, 12, false,1);
                break;
            }
            case 2:{
                setTimeForLevel(60);
                initializeVisitedRows(20);
                createImage("Car.jpg", 11, 1, true,1);
                createImage("Car.jpg", 11, 6, true,1);
                createImage("Car.jpg", 11, 11, true,1);
                createImage("Car.jpg", 10, 2, false,1);
                createImage("Car.jpg", 10, 7, false,1);
                createImage("Car.jpg", 10, 12, false,1);
                createImage("Car.jpg", 9, 0, true,2);
                createImage("Car.jpg", 9, 5, true,2);
                createImage("Car.jpg", 9, 12, true,2);
                createImage("Car.jpg", 8, 2, false,1);
                createImage("Car.jpg", 8, 7, false,1);
                createImage("Car.jpg", 8, 12, false,1);
                createImage("Car.jpg", 7, 12, false,10);
                break;
            }
        }
        Main.hud.toFront();
    }

    private double positionOnColumn(int column){
        return -(backgroundImage.getBoundsInParent().getHeight() - Main.window.getPrefHeight()) / 2 + column * squareSize;
    }

    private double positionOnRow(int column){
        return -(backgroundImage.getBoundsInParent().getWidth() - Main.window.getPrefWidth()) + column * squareSize;
    }

    // Keep track of score.
    private void initializeVisitedRows(int length) {
        for (int i = 0; i < length; i++) {
            this.getVisitedRows().add(false);
        }
    }

    // Create an image from a picture in resources.
    public void createImage(String name, int row, int column, boolean facingLeft, double speed){
        ImageView image = new ImageView(new Image("Files/Sprites/" + name));
        image.relocate(column * squareSize, row * squareSize);
        if(facingLeft){
            image.setRotate(180);
        }
        images.add(new MyImage(image, facingLeft, speed));
        Main.window.getChildren().add(image);
    }

    public void setFroggerStartingPositionX(double froggerStartingPositionX) {
        this.froggerStartingPositionX = froggerStartingPositionX;
    }

    public void setFroggerStartingPositionY(double froggerStartingPositionY) {
        this.froggerStartingPositionY = froggerStartingPositionY;
    }

    public void setVisitedRows(ArrayList<Boolean> visitedRows) {
        this.visitedRows = visitedRows;
    }

    public void setTimeForLevel(double timeForLevel) {
        this.timeForLevel = timeForLevel;
    }

    public void setVisRowsCount(int visRowsCount) {
        this.visRowsCount = visRowsCount;
    }

    public void setImages(ArrayList<MyImage> images) {
        this.images = images;
    }

    public void setSquareSize(double squareSize) {
        this.squareSize = squareSize;
    }

    public void setBackgroundImage(ImageView backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public double getSquareSize() {
        return squareSize;
    }

    public ImageView getBackgroundImage() {
        return backgroundImage;
    }

    public double getFroggerStartingPositionX() {
        return froggerStartingPositionX;
    }

    public double getFroggerStartingPositionY() {
        return froggerStartingPositionY;
    }

    public ArrayList<Boolean> getVisitedRows() {
        return visitedRows;
    }

    public double getTimeForLevel() {
        return timeForLevel;
    }

    public int getVisRowsCount() {
        return visRowsCount;
    }

    public ArrayList<MyImage> getImages() {
        return images;
    }
}
