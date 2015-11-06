package com.bajic;

import javafx.scene.image.ImageView;

/**
 * Created by name on 2.11.2015 �..
 */
public class MyImage {
    private ImageView imageView;
    private boolean facingLeft;
    private double speed;
    private boolean carrier;

    public MyImage(ImageView image, boolean facingLeft, double speed, boolean carrier) {
        this.setImageView(image);
        this.setFacingLeft(facingLeft);
        this.setSpeed(speed);
        this.setCarrier(carrier);
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setCarrier(boolean carrier) {
        this.carrier = carrier;
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

    public boolean isCarrier() {
        return carrier;
    }

}
