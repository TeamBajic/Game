package com.bajic;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public final class Move {

    public static boolean moving = false;
    public static boolean stopped = false;
    private static MyImage carrierItem = null;

    public static MyImage getCarrierItem() {
        return carrierItem;
    }

    public static void setCarrierItem(MyImage carrierItem) {
        Move.carrierItem = carrierItem;
    }

    // Move frog.
    public static void moveFrogger(double x, double y){

        //Check for an invalid move
        if (isInvalidAnimationMove(x, y)) {
            return;
        }
        //if the windows is between the background bounds we need to move everything except frogger to mimic the movement of camera
        if(isAbleToMoveEverything(x, y)){
            moving = true;
            MoveEverything(x, y);
            return;
        }

        final int[] currentFrames = {0};
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                if(stopped){
                    currentFrames[0] = (int) (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND) + 1;
                    setCarrierItem(null);
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
                    setCarrierItem(null);
                    moving = false;
                    this.stop();
                }
            }
        }.start();
    }

    private static boolean isAbleToMoveEverything(double x, double y) {
        double backgroundWidth = Main.level.getBackgroundImage().getBoundsInParent().getWidth();
        double backgroundHeight = Main.level.getBackgroundImage().getBoundsInParent().getHeight();
        return (froggerIsInXCenter() &&
          ((x > 0 && cameraCanBeMovedRight(backgroundWidth)) || (x < 0 && cameraCanBeMovedLeft()))) ||
           (froggerIsInYCenter() &&
           ((y < 0 && cameraCanBeMovedUp()) || (y > 0 && cameraCanBeMovedDown(backgroundHeight))));
    }

    private static boolean isInvalidAnimationMove(double x, double y) {
        if(y == 0) {
            //if the frog will leave the bounds of the background image
            if(Math.round(Main.frogger.getLayoutX() + x + Main.frogger.getBoundsInParent().getWidth()) <= 0 ||
               Math.round(Main.frogger.getLayoutX() + x + Main.frogger.getBoundsInParent().getWidth()) >=
                       Math.round(Main.window.getPrefWidth())) {
                return true;
            }
        }
        else {
            //If frogger will leave the map through the y he will change levels which can also be counted as an invalid
            // animation move
            if (Math.round(Main.frogger.getLayoutY() + y) <= 0) {
                Main.levelIndex++;
                Main.currentLevelScene++;
                for (int i = 0; i < Main.level.getImages().size(); i++) {
                    Main.level.getImages().get(i).getImageView().setVisible(false);
                }
                Main.frogger.setLayoutX(Main.level.getFroggerStartingPositionX());
                Main.frogger.setLayoutY(Main.level.getFroggerStartingPositionY());
                Main.initializeLevel(Main.levelIndex);

                return true;
            } else if (Math.round(Main.frogger.getLayoutY() + y + Main.level.getSquareSize()) >= Main.window.getHeight() - Main.hud.getHeight()) {
                /*
                if (Main.currentLevelScene == 0) {
                    return true;
                }
                Main.currentLevelScene--;
                Main.levelIndex--;
                for (int i = 0; i < Main.level.getImages().size(); i++) {
                    Main.level.getImages().get(i).getImageView().setVisible(false);
                }
                Main.frogger.setLayoutX(Main.level.getFroggerStartingPositionX());
                Main.frogger.setLayoutY(Main.level.getFroggerStartingPositionY());
                Main.initializeLevel(Main.levelIndex);
                */
                return true;

            }
        }
        return false;
    }

    private static void MoveEverything(final double x, final double y) {
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
                    for (int i = 0; i < Main.level.getImages().size(); i++) {
                        MyImage currentImage = Main.level.getImages().get(i);
                        if(currentImage.equals(getCarrierItem()) && !moving){
                            continue;
                        }
                        currentImage.getImageView().relocate(currentImage.getImageView().getLayoutX() - x / (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND),
                                currentImage.getImageView().getLayoutY() - y / (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND));
                    }
                    Main.level.getBackgroundImage().relocate(Main.level.getBackgroundImage().getLayoutX() - x /
                            (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND), Main.level.getBackgroundImage().getLayoutY() - y /
                            (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND));
                    currentFrames[0]++;
                }
                else {
                    moving = false;
                    this.stop();
                }
            }
        }.start();
    }

    private static boolean froggerIsInXCenter() {
        return Math.round(Main.frogger.getLayoutX()) > Main.window.getPrefWidth() / 2 - Main.level.getSquareSize() &&
                Math.round(Main.frogger.getLayoutX()) < Main.window.getPrefWidth() / 2 + Main.level.getSquareSize();
    }

    private static boolean froggerIsInYCenter() {
        return  Math.round(Main.frogger.getLayoutY()) > Main.window.getPrefHeight() / 2 - Main.level.getSquareSize() &&
                Math.round(Main.frogger.getLayoutY()) < Main.window.getPrefHeight() / 2 + Main.level.getSquareSize();
    }

    private static boolean cameraCanBeMovedRight(double backgroundWidth) {
        return Main.level.getBackgroundImage().getLayoutX() >= -(backgroundWidth - Main.window.getPrefWidth()) + Main.level.getSquareSize();
    }

    private static boolean cameraCanBeMovedLeft() {
        return Main.level.getBackgroundImage().getLayoutX() <= -Main.level.getSquareSize();
    }

    private static boolean cameraCanBeMovedDown(double backgroundHeight) {
        return Main.level.getBackgroundImage().getLayoutY() >= -(backgroundHeight - Main.window.getPrefHeight()) + Main.level.getSquareSize();
    }

    private static boolean cameraCanBeMovedUp() {
        return Main.level.getBackgroundImage().getLayoutY() <= -Main.level.getSquareSize();
    }

    // Move enemies on the screen.
    public static void moveImages(ArrayList<MyImage> images) {
        boolean willDie = false;
        for (int i = 0; i < images.size(); i++) {
            double distance = ((double) Main.FRAMES_PER_SECOND / (double) Main.SECOND_IN_MILLISECONDS) * images.get(i).getSpeed() * Main.SPEED_FACTOR;
            if (imageOverlapsFrogger(images.get(i).getImageView()) && !moving){
                if(images.get(i).isCarrier()){
                    setCarrierItem(images.get(i));
                    if(isAbleToMoveEverything(distance, 0)){
                        continue;
                    }
                }
                else{
                    willDie = true;
                }
            }
            if(images.get(i).isFacingLeft()){
                if(!images.get(i).getImageView().getBoundsInParent().intersects(Main.level.getBackgroundImage().getBoundsInParent())){
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() +
                            Main.level.getBackgroundImage().getBoundsInParent().getWidth() +
                            images.get(i).getImageView().getBoundsInParent().getWidth());
                }
                else {
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() - distance);
                }
            }
            else {
                if(!images.get(i).getImageView().getBoundsInParent().intersects(Main.level.getBackgroundImage().getBoundsInParent())){
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() -
                            Main.level.getBackgroundImage().getBoundsInParent().getWidth() -
                            images.get(i).getImageView().getBoundsInParent().getWidth());
                } else {
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() + distance);
                }
            }
        }
        if(getCarrierItem() != null){
            double distance = ((double) Main.FRAMES_PER_SECOND / (double) Main.SECOND_IN_MILLISECONDS) * getCarrierItem().getSpeed() * Main.SPEED_FACTOR;
            if(isAbleToMoveEverything(distance, 0)) {
                MoveEverything(distance, 0);
            }
            else if (getCarrierItem().isFacingLeft()){
                if(Main.frogger.getLayoutX() - Main.frogger.getLayoutBounds().getWidth() / 2 < 0){
                    Main.loseLife();
                } else {
                    Main.frogger.setLayoutX(Main.frogger.getLayoutX() - distance);
                    getCarrierItem().getImageView().setLayoutX(getCarrierItem().getImageView().getLayoutX() - distance);
                }
            } else {
                if(Main.frogger.getLayoutX() + Main.frogger.getLayoutBounds().getWidth() / 2 > Main.window.getWidth()){
                    Main.loseLife();
                } else {
                    Main.frogger.setLayoutX(Main.frogger.getLayoutX() + distance);
                    getCarrierItem().getImageView().setLayoutX(getCarrierItem().getImageView().getLayoutX() + distance);
                }
            }
            return;
        }
        if(willDie){
            Main.loseLife();
            Main.ResetEverything();
        }
    }

    // Collision check.
    private static boolean imageOverlapsFrogger(ImageView imageView) {
        return Main.frogger.getBoundsInParent().intersects(imageView.getBoundsInParent());
    }
}
