package com.bajic;

import javafx.scene.image.ImageView;

/**
 * Created by name on 2.11.2015 ã..
 */
public class MyImage {
    private ImageView imageView;
    private boolean facingLeft;
    private double speed;

    public MyImage(ImageView image, boolean facingLeft, double speed) {
        this.imageView = image;
        this.facingLeft = facingLeft;
        this.speed = speed;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ImageView getImageView() {

        return imageView;
    }

    public boolean isFacingLeft() {

        return facingLeft;
    }

    public double getSpeed() {
        return speed;
    }
}
