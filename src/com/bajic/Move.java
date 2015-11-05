package com.bajic;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public final class Move {

    public static boolean moving = false;
    public static boolean stopped = false;

    // Move frog.
    public static void moveFrogger(double x, double y){

        //Check for trying to move outside of the bounds
        if(y == 0) {
            if(Math.round(Main.frogger.getLayoutX() + x) <= 0 ||
            Math.round(Main.frogger.getLayoutX() + x) >= Main.window.getWidth()) {
                return;
            }
        }
        else {
            if (Math.round(Main.frogger.getLayoutY() + y) <= 0) {
                Main.levelIndex++;
                Main.currentLevelScene++;
                for (int i = 0; i < Main.level.getImages().size(); i++) {
                    Main.level.getImages().get(i).getImageView().setVisible(false);
                }
                Main.frogger.setLayoutX(Main.level.getFroggerStartingPositionX());
                Main.frogger.setLayoutY(Main.level.getFroggerStartingPositionY());
                Main.initializeLevel(Main.levelIndex);

                return;
            } else if (Math.round(Main.frogger.getLayoutY() + y + Main.level.squareSize) >= Main.window.getHeight()) {

                if (Main.currentLevelScene == 0) {
                    return;
                }
                Main.currentLevelScene--;
                Main.levelIndex--;
                for (int i = 0; i < Main.level.getImages().size(); i++) {
                    Main.level.getImages().get(i).getImageView().setVisible(false);
                }
                Main.frogger.setLayoutX(Main.level.getFroggerStartingPositionX());
                Main.frogger.setLayoutY(Main.level.getFroggerStartingPositionY());
                Main.initializeLevel(Main.levelIndex);

                return;
            }
        }

        final int[] currentFrames = {0};
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                if(stopped){
                    currentFrames[0] = (int) (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND) + 1;
                    moving = false;
                    stopped = false;
                    this.stop();
                }
                if(currentFrames[0] < Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND){
                    Main.frogger.setLayoutX(Main.frogger.getLayoutX() + x / (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND));
                    Main.frogger.setLayoutY(Main.frogger.getLayoutY() + y / (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND));
                    currentFrames[0]++;
                    moving = true;
                }
                else {
                    moving = false;
                    this.stop();
                }
            }
        }.start();
    }

    // Move enemies on the screen.
    public static void moveImages(ArrayList<MyImage> images) {
        for (int i = 0; i < images.size(); i++) {
                double distance = ((double) Main.FRAMES_PER_SECOND / (double) Main.SECOND_IN_MILLISECONDS) * images.get(i).getSpeed() * Main.SPEED_FACTOR;
                double imageWidth = images.get(i).getImageView().getLayoutBounds().getWidth();
                if(images.get(i).isFacingLeft()){
                    if(images.get(i).getImageView().getLayoutX() < 0 - imageWidth - Main.level.squareSize){
                        images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() + Main.window.getWidth() +
                                imageWidth + Main.level.squareSize);
                    }
                    else {
                        images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() - distance);
                    }
                }
                else {
                    if(images.get(i).getImageView().getLayoutX() > Main.window.getWidth() + imageWidth  + Main.level.squareSize){
                        images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() - Main.window.getWidth() -
                                imageWidth  - Main.level.squareSize);
                    } else {
                        images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() + distance);
                    }
                }
                if(imageOverlapsFrogger(images.get(i).getImageView())){
                    Main.LoseLife();
                }
        }
    }

    // Collision check.
    private static boolean imageOverlapsFrogger(ImageView imageView) {
        return Main.frogger.getBoundsInParent().intersects(imageView.getBoundsInParent());
    }
}
