package com.bajic;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public final class MoveObjectsManager {

    private static boolean moving = false;
    private static boolean stopped = false;
    private static MyImage carrierItem = null;

    public static boolean isMoving() {
        return moving;
    }

    public static void setMoving(boolean moving) {
        MoveObjectsManager.moving = moving;
    }

    public static boolean isStopped() {
        return stopped;
    }

    public static void setStopped(boolean stopped) {
        MoveObjectsManager.stopped = stopped;
    }

    public static MyImage getCarrierItem() {
        return carrierItem;
    }

    public static void setCarrierItem(MyImage carrierItem) {
        MoveObjectsManager.carrierItem = carrierItem;
    }

    // MoveObjectsManager frog.
    public static void moveFrogger(double x, double y) {
        //Check for an invalid move
        if (isInvalidAnimationMove(x, y)) {
            return;
        }

        setMoving(true);
        //if the windows is between the background bounds we need to move everything except frogger to mimic the movement of camera
        if(isAbleToMoveEverything(x, y)){
            MoveEverything(x, y, true);
            return;
        }

        //check if the frog reached the end of the current level
        if(Main.level.getVisitedRows().get(0)) {
            Main.initializeLevel(++Main.levelIndex, 100);
        }

        final int[] currentFrames = {0};
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                if(stopped){
                    currentFrames[0] = (int) (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND) + 1;
                    setCarrierItem(null);
                    setMoving(false);
                    stopped = false;
                    this.stop();
                }
                if(currentFrames[0] < Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND){
                    Main.frogger.setLayoutX(Main.frogger.getLayoutX() + x / (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND));
                    Main.frogger.setLayoutY(Main.frogger.getLayoutY() + y / (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND));
                    currentFrames[0]++;
                }
                else {
                    setCarrierItem(null);
                    setMoving(false);
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
            if(Math.round(Main.frogger.getLayoutX() + x) <= 0 ||
               Math.round(Main.frogger.getLayoutX() + x + Main.frogger.getBoundsInParent().getWidth() / 2) >=
                       Math.round(Main.window.getPrefWidth())) {
                return true;
            }
        }
        else {
            //If frogger will leave the map through the y he will change levels which can also be counted as an invalid
            // animation move
            if (Math.round(Main.frogger.getLayoutY() + y) <= 0) {
                Main.levelIndex++;
                //Main.currentLevelScene++;
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

    private static void MoveEverything(final double x, final double y, boolean froggerIsMoving) {
        final int[] currentFrames = {0};
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                if(stopped){
                    currentFrames[0] = (int) (Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND) + 1;
                    setMoving(false);
                    stopped = false;
                    if(froggerIsMoving){
                        setCarrierItem(null);
                    }
                    this.stop();
                }
                if(currentFrames[0] < Main.ANIMATION_TIME * Main.FRAMES_PER_SECOND){
                    for (int i = 0; i < Main.level.getImages().size(); i++) {
                        MyImage currentImage = Main.level.getImages().get(i);
                        if(currentImage.equals(getCarrierItem()) && !froggerIsMoving){
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
                    if(froggerIsMoving){
                        setCarrierItem(null);
                    }
                    setMoving(false);
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

    // MoveObjectsManager enemies on the screen.
    public static void moveImages(ArrayList<MyImage> images) {
        boolean willDie = false;
        for (int i = 0; i < images.size(); i++) {
            double distance = ((double) Main.FRAMES_PER_SECOND / (double) Main.SECOND_IN_MILLISECONDS) * images.get(i).getSpeed() * Main.SPEED_FACTOR;
            if (imageOverlapsFrogger(images.get(i).getImageView())){
                if(images.get(i).isCoin()){
                    Main.level.setCoinsPicked(Main.level.getCoinsPicked() + 1);
                    images.get(i).getImageView().setVisible(false);
                    images.remove(i);
                    i--;
                    Main.score.setText(Integer.toString(Integer.parseInt(Main.score.getText()) + Main.POINTS_FROM_COIN));
                    continue;
                }
                if(images.get(i).isCarrier()){
                    setCarrierItem(images.get(i));
                    continue;
                }
                else if (!isMoving()){
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
                MoveEverything(distance, 0, false);
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
        }
    }

    // Collision check for frogger.
    private static boolean imageOverlapsFrogger(ImageView imageView) {
        return Main.frogger.getBoundsInParent().intersects(imageView.getBoundsInParent());
    }
}
