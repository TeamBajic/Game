package com.bajic;

import javafx.animation.AnimationTimer;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

/**
 * Created by Team Bajic on 3.11.2015 ã..
 */
public final class Move {

    public static boolean moving = false;
    public static boolean stopped = false;

    // Move frog.
    public static void moveFrogger(double x, double y){
        if(Math.round(Main.frogger.getLayoutX() + x) <= 0 || Math.round(Main.frogger.getLayoutX() + x) >= Main.window.getWidth() ||
                Math.round(Main.frogger.getLayoutY() + y) <= 0 || Math.round(Main.frogger.getLayoutY() + y + Main.SQUARE_SIZE) >= Main.window.getHeight()){
            return;
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
                if(images.get(i).getImageView().getLayoutX() < 0 - imageWidth - Main.SQUARE_SIZE){
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() + Main.window.getWidth() +
                            imageWidth + Main.SQUARE_SIZE);
                }
                else {
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() - distance);
                }
            }
            else {
                if(images.get(i).getImageView().getLayoutX() > Main.window.getWidth() + imageWidth  + Main.SQUARE_SIZE){
                    images.get(i).getImageView().setLayoutX(images.get(i).getImageView().getLayoutX() - Main.window.getWidth() -
                            imageWidth  - Main.SQUARE_SIZE);
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
