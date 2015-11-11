package com.bajic;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Level{
    private int squaresLength = 20; //keep it as a non constant in case we want to make a level with more squares than 20/20
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
    private int coinsPicked;
    private int maxCoins;

    public Level(int levelIndex){
        initializeLevel(levelIndex);

       // createRiverBank();
    }

    public void initializeLevel(int level) {

        switch (level){
            case 1:{
                CreateBackground();
                setTimeForLevel(60);
                initializeVisitedRows(squaresLength);
                initializeCoins(squaresLength - Main.coinsTaken);
                Main.coinsTaken = 0;
                if (Main.isSave){
                    loadGameVisRows(Main.loadVisRows);
                }

                //Create the movable objects for the level following: Name, row, column, is moving left, the object's speed, can carry frogger.
                createImage("Car.png", 18, 5, true,1, false);
                createImage("Car.png", 18, 15, true,1, false);
                createImage("Car.png", 18, 30, true,1, false);
                createImage("Car.png", 16, 6, false,1, false);
                createImage("Car.png", 16, 11, false,1, false);
                createImage("Car.png", 16, 16, false,1, false);
                createImage("Car.png", 16, 21, false,1, false);
                createImage("Car.png", 13, 4, true,2, false);
                createImage("Car.png", 13, 9, true,2, false);
                createImage("Car.png", 13, 16, true,2, false);
                createImage("Car.png", 10, 6, false,1, false);
                createImage("Car.png", 10, 11, false,1, false);
                createImage("Car.png", 10, 16, false,1, false);
                createImage("Car.png", 9, 5, false,1, false);
                createImage("Car.png", 9, 15, false,1, false);
                createImage("Car.png", 6, 0, false,2, false);
                createImage("Car.png", 6, 10, false,2, false);
                createImage("Car.png", 6, 15, false,2, false);
                createImage("Car.png", 5, 0, true,2, false);
                createImage("Car.png", 5, 10, true,2, false);
                createImage("Car.png", 5, 15, true,2, false);

                //set frogger's starting position to
                Main.frogger.relocate(positionOnColumn(9), positionOnRow(19));
                getBackgroundImage().toBack(); // send the background image to the back so it doesn't hide anything
                break;
            }
            case 2:{
                CreateBackground();
                setTimeForLevel(60);
                initializeVisitedRows(squaresLength);
                initializeCoins(squaresLength - Main.coinsTaken);
                Main.coinsTaken = 0;
                if (Main.isSave){
                    loadGameVisRows(Main.loadVisRows);
                }

                //Create the movable objects for the level following: Name, row, column, is moving left, the object's speed, can carry frogger.
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
                createImage("Car.png", 14, 5, false,1, false);
                createImage("Car.png", 14, 15, false,1, false);

                createImage("Log.png", 11, 14, false,1, true);
                createImage("Log.png", 11, 7, false,1, true);
                createImage("Log.png", 11, 0, false,1, true);
                createImage("Log.png", 10, 15, false,1, true);
                createImage("Log.png", 10, 10, false,1, true);
                createImage("Log.png", 10, 5, false,1, true);
                createImage("Log.png", 10, 0, false,1, true);
                createImage("Log.png", 9, 1, false,3, true);
                createImage("Log.png", 9, 8, false,3, true);
                createImage("Log.png", 9, 15, false,3, true);
                createImage("Log.png", 8, 2, false,1, true);
                createImage("Log.png", 8, 9, false,1, true);
                createImage("Log.png", 8, 16, false,1, true);

                createImage("Car.png", 6, 5, true,2, false);
                createImage("Car.png", 6, 11, true,2, false);
                createImage("Car.png", 6, 17, true,2, false);
                createImage("Car.png", 4, 5, false,2, false);
                createImage("Car.png", 4, 10, false,2, false);

                //set frogger's starting position to
                Main.frogger.relocate(positionOnColumn(9), positionOnRow(19));
                getBackgroundImage().toBack();
                createWater();
                break;
            }
            case 3:{
                CreateBackground();
                setTimeForLevel(60);
                initializeVisitedRows(squaresLength);
                initializeCoins(squaresLength - Main.coinsTaken);
                Main.coinsTaken = 0;
                if (Main.isSave){
                    loadGameVisRows(Main.loadVisRows);
                }

                //Create the movable objects for the level following: Name, row, column, is moving left, the object's speed, can carry frogger.
                createImage("Car.png", 18, 8, true,1, false);
                createImage("Car.png", 18, 13, true,1, false);
                createImage("Car.png", 18, 18, true,1, false);
                createImage("Car.png", 17, 9, false,4, false);
                createImage("Car.png", 17, 14, false,4, false);
                createImage("Car.png", 17, 19, false,4, false);
                createImage("Car.png", 16, 7, true,2, false);
                createImage("Car.png", 16, 12, true,2, false);
                createImage("Car.png", 16, 19, true,2, false);
                createImage("Car.png", 15, 9, false,1, false);
                createImage("Car.png", 15, 14, false,1, false);
                createImage("Car.png", 15, 19, false,1, false);
                createImage("Car.png", 14, 19, false,10, false);

                createImage("Log.png", 11, 14, false,1, true);
                createImage("Log.png", 11, 7, false,1, true);
                createImage("Log.png", 11, 0, false,1, true);
                createImage("Log.png", 10, 15, false,2, true);
                createImage("Log.png", 10, 10, false,2, true);
                createImage("Log.png", 10, 5, false,2, true);
                createImage("Log.png", 10, 0, false,2, true);
                createImage("Log.png", 9, 1, false,3, true);
                createImage("Log.png", 9, 8, false,3, true);
                createImage("Log.png", 9, 15, false,3, true);
                createImage("Log.png", 8, 2, true,1, true);
                createImage("Log.png", 8, 9, true,1, true);
                createImage("Log.png", 8, 16, true,1, true);
                createImage("Log.png", 8, 23, true,1, true);

                createImage("Car.png", 6, 5, false,4, false);
                createImage("Car.png", 6, 15, false,4, false);
                createImage("Car.png", 5, 5, true,4, false);
                createImage("Car.png", 5, 15, true,4, false);
                createImage("Car.png", 4, 19, true,10, false);

                //set frogger's starting position to
                Main.frogger.relocate(positionOnColumn(9), positionOnRow(19));
                getBackgroundImage().toBack();
                createWater();
                break;
            }
            case 4:{
                CreateBackground();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thank you!");
                alert.setHeaderText(null);
                alert.setContentText("Thank you for playing our demo!");
                alert.show();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    Main.exitGame();
                }
                Main.exitGame();
            }
        }
        Main.hud.toFront();
        this.setFroggerStartingPositionX(Main.frogger.getLayoutX());
        this.setFroggerStartingPositionY(Main.frogger.getLayoutY());
        Main.loadHighscores(Main.Highscores);
    }

    private void initializeCoins(int numberOfCoins) {
        this.setMaxCoins(numberOfCoins);
        Random rand = new Random();
        for (int i = this.getCoinsPicked(); i < numberOfCoins; i++) {
            ImageView image = new ImageView(new Image("Files/Sprites/coin-25x25.png"));
            int row = rand.nextInt(this.getVisitedRows().size() - 1);
            int column = rand.nextInt(this.getVisitedRows().size() - 1);
            image.relocate(positionOnColumn(column), positionOnRow(row));
            images.add(new MyImage(image, false, 0, false));
            images.get(images.size() - 1).setCoin(true);
            Main.window.getChildren().add(image);
            image.toBack();
        }
    }
    // since the rows and columns start from the edge of the background and not the form we need a formula to get the real
    // row and column depending on the current position of the form
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
        squareSize = backgroundImage.getBoundsInParent().getWidth() / squaresLength; // background width is 900
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
        riverBank.relocate(getBackgroundImage().getLayoutX() ,getBackgroundImage().getLayoutY() + squareSize * 7 - squaresLength);
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

    public void setCoinsPicked(int coinsPicked) {
        this.coinsPicked = coinsPicked;
    }

    public void setMaxCoins(int maxCoins) {
        this.maxCoins = maxCoins;
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

    public int getCoinsPicked() {
        return coinsPicked;
    }

    public double getBackgroundImageStartingY() {
        return backgroundImageStartingY;
    }

    public int getMaxCoins() {
        return maxCoins;
    }
}
