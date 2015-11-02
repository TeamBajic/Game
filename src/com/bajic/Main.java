package com.bajic;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application{

    public static final int FRAMES_PER_SECOND = 60;
    public static final int SPEED_FACTOR = 20;
    public static double froggerStartingPositionX;
    public static double froggerStartingPositionY;
    public static final double SQUARE_SIZE = 600 / 13;
    public static double rows = 13;
    public static double columns = 13;
    public static final int ANIMATION_TIME = 100;
    public static Text lives;
    public static Text score;
    public static Node frogger;
    public static Pane window;
    public ArrayList<MyImage> images = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[]) null);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Game window.fxml"));
            frogger = root.lookup("#frogger");
            window = (Pane) root.lookup("#window");
            lives = (Text) root.lookup("#lives");
            score = (Text) root.lookup("#score");
            froggerStartingPositionX = frogger.getLayoutX();
            froggerStartingPositionY = frogger.getLayoutY();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Frogger");
            initializeLevel(1);

            new AnimationTimer()
            {
                public void handle(long currentNanoTime)
                {
                    MoveImages();
                    scene.setOnKeyPressed(
                            e -> {
                                String code = e.getCode().toString();
                                onKeyPress(code);
                            });
                }
            }.start();

            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void MoveImages() {
        for (int i = 0; i < images.size(); i++) {
            double distance = ((double) FRAMES_PER_SECOND / 1000d) * images.get(i).getSpeed() * SPEED_FACTOR;
            double imageWidth = images.get(i).getImageView().getLayoutBounds().getWidth();
            if(images.get(i).isFacingLeft()){
                if(images.get(i).getImageView().getLayoutX() < 0 - imageWidth - SQUARE_SIZE){
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() + window.getWidth() +
                            imageWidth + SQUARE_SIZE);
                }
                else {
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() - distance);
                }
            }
            else {
                if(images.get(i).getImageView().getLayoutX() > window.getWidth() + imageWidth  + SQUARE_SIZE){
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() - window.getWidth() -
                            imageWidth  - SQUARE_SIZE);
                } else {
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() + distance);
                }
            }
            if(ImageOverlapsFrogger(images.get(i).getImageView())){
                lives.setText(Integer.toString(Integer.parseInt(lives.getText()) - 1));
                frogger.setLayoutX(froggerStartingPositionX);
                frogger.setLayoutX(froggerStartingPositionY);
            }
        }
    }

    private boolean ImageOverlapsFrogger(ImageView imageView) {
        if(frogger.intersects(imageView.getBoundsInLocal())){
            //return true;
        }
        return false;
    }

    private void initializeLevel(int level) {
        createImage("Car", 11, 1, true,1);
        createImage("Car", 11, 6, true,1);
        createImage("Car", 11, 11, true,1);
    }

    public void createImage(String name, int row, int column, boolean facingLeft, int speed){
        ImageView image = new ImageView(new Image("Files/Sprites/" + name +".jpg"));
        image.relocate(column * SQUARE_SIZE, row * SQUARE_SIZE);
        if(facingLeft){
            image.setRotate(180);
        }
        images.add(new MyImage(image, facingLeft, speed));
        window.getChildren().add(image);
    }

    private void onKeyPress(String code) {
        switch (code){
            case "LEFT":{
                frogger.setRotate(-90);
                moveFrogger(-SQUARE_SIZE, 0);
                break;
            }
            case "RIGHT":{
                frogger.setRotate(90);
                moveFrogger(SQUARE_SIZE, 0);
                break;
            }
            case "DOWN":{
                frogger.setRotate(-180);
                moveFrogger(0, SQUARE_SIZE);
                break;
            }
            case "UP":{
                frogger.setRotate(0);
                moveFrogger(0, -SQUARE_SIZE);
                break;
            }
        }
    }

    private void moveFrogger(double x, double y) {
        if(Math.round(frogger.getLayoutX() + x) <= 0 || Math.round(frogger.getLayoutX() + x) >= window.getWidth() ||
                Math.round(frogger.getLayoutY() + y) <= 0 || Math.round(frogger.getLayoutY() + y + SQUARE_SIZE) >= window.getHeight()){
            return;
        }
        for (int i = 0; i < FRAMES_PER_SECOND; i++) {
            try {
                Thread.sleep((FRAMES_PER_SECOND/ 1000) * ANIMATION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frogger.setLayoutX(frogger.getLayoutX() + x / FRAMES_PER_SECOND);
            frogger.setLayoutY(frogger.getLayoutY() + y / FRAMES_PER_SECOND);
        }
    }
}
