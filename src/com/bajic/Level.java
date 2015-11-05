package com.bajic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Created by name on 5.11.2015 г..
 */
public class Level{
    public double squareSize;
    private double froggerStartingPositionX;
    private double froggerStartingPositionY;
    private ArrayList<Boolean> visitedRows = new ArrayList<>();
    private double timeForLevel;
    private int visRowsCount = 0;
    private ArrayList<MyImage> images = new ArrayList<>();

    public Level(int levelIndex){
        this.setFroggerStartingPositionX(Main.frogger.getLayoutX());
        this.setFroggerStartingPositionY(Main.frogger.getLayoutY());
        initializeLevel(levelIndex);
    }
    public void initializeLevel(int level) {
        switch (level){
            case 1:{
                setTimeForLevel(60);
                InitializeVisitedRows(13);
                squareSize = 600 / 13;
                createImage("Car", 11, 1, true,1);
                createImage("Car", 11, 6, true,1);
                createImage("Car", 11, 11, true,1);
                createImage("Car", 10, 2, false,1);
                createImage("Car", 10, 7, false,1);
                createImage("Car", 10, 12, false,1);
                createImage("Car", 9, 0, true,2);
                createImage("Car", 9, 5, true,2);
                createImage("Car", 9, 12, true,2);
                createImage("Car", 8, 2, false,1);
                createImage("Car", 8, 7, false,1);
                createImage("Car", 8, 12, false,1);
                break;
            }
            case 2:{
                setTimeForLevel(60);
                InitializeVisitedRows(13);
                squareSize = 600 / 13;
                createImage("Car", 11, 1, true,1);
                createImage("Car", 11, 6, true,1);
                createImage("Car", 11, 11, true,1);
                createImage("Car", 10, 2, false,1);
                createImage("Car", 10, 7, false,1);
                createImage("Car", 10, 12, false,1);
                createImage("Car", 9, 0, true,2);
                createImage("Car", 9, 5, true,2);
                createImage("Car", 9, 12, true,2);
                createImage("Car", 8, 2, false,1);
                createImage("Car", 8, 7, false,1);
                createImage("Car", 8, 12, false,1);
                createImage("Car", 7, 12, false,10);
                break;
            }
        }
    }

    // Keep track of score.
    private void InitializeVisitedRows(int length) {
        for (int i = 0; i < length; i++) {
            this.getVisitedRows().add(false);
        }
    }

    // Create an image from a picture in resources.
    public void createImage(String name, int row, int column, boolean facingLeft, double speed){
        ImageView image = new ImageView(new Image("Files/Sprites/" + name +".jpg"));
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
