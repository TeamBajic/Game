package com.bajic;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Level{
    private double squareSize;
    private double froggerStartingPositionX;
    private double froggerStartingPositionY;
    private ArrayList<Boolean> visitedRows = new ArrayList<>();
    private double timeForLevel;
    private int visRowsCount = 0;
    private ArrayList<MyImage> images = new ArrayList<>();
    private ImageView backgroundImage;
    private double backgroundImageStartingX;
    private double backgroundImageStartingY;

    public Level(int levelIndex){

        initializeLevel(levelIndex);

       // createRiverBank();
    }

    public void initializeLevel(int level) {

        switch (level){
            case 1:{
                CreateBackground();
                setTimeForLevel(60);
                initializeVisitedRows(20);
                if (Main.isSave){
                    loadGameVisRows(Main.loadVisRows);
                }

                //row,column,right/left,speed,kill/no

                createImage("Car.png", 18, 5, true,1, false);
                createImage("Car.png", 18, 10, true,1, false);
                createImage("Car.png", 18, 15, true,1, false);
                createImage("Car.png", 17, 6, false,1, false);
                createImage("Car.png", 17, 11, false,1, false);
                createImage("Car.png", 17, 16, false,1, false);
                createImage("Car.png", 16, 4, true,2, false);
                createImage("Car.png", 16, 9, true,2, false);
                createImage("Car.png", 16, 16, true,2, false);
                createImage("Car.png", 15, 6, false,1, false);
                createImage("Car.png", 15, 11, false,1, false);
                createImage("Car.png", 15, 16, false,1, false);
                createImage("Log.png", 11, 7, false,1, true);
                createImage("Log.png", 11, 0, false,1, true);
                createImage("Log.png", 10, 5, false,1, true);
                createImage("Log.png", 10, 0, false,1, true);
                createImage("Log.png", 9, 8, false,2, true);
                createImage("Log.png", 9, 1, false,2, true);
                createImage("Log.png", 8, 2, false,1, true);
                createImage("Log.png", 8, 7, false,1, true);
                Main.frogger.relocate(positionOnColumn(9), positionOnRow(19));
                getBackgroundImage().toBack();
                createWater();
                break;
            }
            case 2:{
                CreateBackground();
                setTimeForLevel(60);
                initializeVisitedRows(20);
                createImage("Car.png", 18, 8, true,1, false);
                createImage("Car.png", 18, 13, true,1, false);
                createImage("Car.png", 18, 18, true,1, false);
                createImage("Car.png", 17, 9, false,1, false);
                createImage("Car.png", 17, 14, false,1, false);
                createImage("Car.png", 17, 19, false,1, false);
                createImage("Car.png", 16, 7, true,2, false);
                createImage("Car.png", 16, 12, true,2, false);
                createImage("Car.png", 16, 19, true,2, false);
                createImage("Car.png", 15, 9, false,1, false);
                createImage("Car.png", 15, 14, false,1, false);
                createImage("Car.png", 15, 19, false,1, false);
                createImage("Car.png", 14, 19, false,10, false);
                Main.frogger.relocate(positionOnColumn(9), positionOnRow(19));
                getBackgroundImage().toBack();
                break;
            }
            case 3:{
                CreateBackground();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thank you!");
                alert.setHeaderText(null);
                alert.setContentText("Thank you for playing our demo!");
                alert.showAndWait();
            }
        }
        Main.hud.toFront();
        this.setFroggerStartingPositionX(Main.frogger.getLayoutX());
        this.setFroggerStartingPositionY(Main.frogger.getLayoutY());
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

    private void loadGameVisRows(int length) {
        for (int i = 18; i > 18 - length; i--) {
            this.getVisitedRows().set(i, true);
        }
    }

    private void CreateBackground() {

        backgroundImage = new ImageView(new Image("Files/Sprites/Frogger_background.png"));
        Main.window.getChildren().add(backgroundImage);
        backgroundImage.toBack();
        squareSize = backgroundImage.getBoundsInParent().getWidth() / 20; // background width is 900
        this.setBackgroundImageStartingX(-(squareSize * 3));
        this.setBackgroundImageStartingY(-(differenceBetweenBackgroundAndWindow()));
        backgroundImage.relocate(getBackgroundImageStartingX(), getBackgroundImageStartingY());
        Main.frogger.relocate(positionOnColumn(10),
                positionOnRow(19) - Main.frogger.getBoundsInParent().getHeight() / 2);
    }

    private void createWater() {

        ImageView water = new ImageView(new Image("Files/Sprites/Water.png"));
        water.relocate(getBackgroundImage().getLayoutX() ,getBackgroundImage().getLayoutY() + squareSize * 7 );
        images.add(new MyImage(water, false, 0, false));
        Main.window.getChildren().add(water);

        water.toBack();

        getBackgroundImage().toBack();
    }
    private void createRiverBank(){
        ImageView riverBank = new ImageView((new Image("Files/Sprites/riverbank.png")));
        riverBank.relocate(getBackgroundImage().getLayoutX() ,getBackgroundImage().getLayoutY() + squareSize * 7 - 20);
        images.add(new MyImage(riverBank, false, 0, true));
        Main.window.getChildren().add(riverBank);
        riverBank.toBack();
        getBackgroundImage().toBack();

    }

    private double differenceBetweenBackgroundAndWindow(){
        return backgroundImage.getBoundsInParent().getHeight() - Main.window.getPrefHeight();
    }

    // Create an image from a picture in resources.
    public void createImage(String name, int row, int column, boolean facingLeft, double speed, boolean carrier){
        ImageView image = new ImageView(new Image("Files/Sprites/" + name));
        image.relocate(positionOnColumn(column), positionOnRow(row));
        if(facingLeft){
            image.setRotate(180);
        }
        images.add(new MyImage(image, facingLeft, speed, carrier));
        Main.window.getChildren().add(image);
        image.toBack();
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

    public void setBackgroundImageStartingX(double backgroundImageStartingX) {
        this.backgroundImageStartingX = backgroundImageStartingX;
    }

    public void setBackgroundImageStartingY(double backgroundImageStartingY) {
        this.backgroundImageStartingY = backgroundImageStartingY;
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

    public double getBackgroundImageStartingX() {
        return backgroundImageStartingX;
    }

    public double getBackgroundImageStartingY() {
        return backgroundImageStartingY;
    }
}
