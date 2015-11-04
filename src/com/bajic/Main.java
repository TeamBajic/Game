package com.bajic;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application{

    public static final int FRAMES_PER_SECOND = 60;
    public static final int SPEED_FACTOR = 15;
    public static final int SECOND_IN_MILLISECONDS = 1000;
    public static double froggerStartingPositionX;
    public static double froggerStartingPositionY;
    public static final double SQUARE_SIZE = 600 / 13;
    public static ArrayList<Boolean> visitedRows = new ArrayList<>();
    public static final double ANIMATION_TIME = 0.15;
    public static double timeForLevel;
    public static Pane window;
    public static Node frogger;
    public static Text lives;
    public static Text score;
    public static Text time;
    private Timeline timeline;
    private IntegerProperty timeSeconds;
    public static Scene scene;
    public static boolean isGameRunning = false;
    public static int visRowsCount = 0;
    public static ArrayList<MyImage> images = new ArrayList<>();
    public static int level = 1;

    public AnimationTimer animTimer = new AnimationTimer() {
        public void handle(long currentNanoTime) {
            time.setText(Integer.toString(timeSeconds.getValue()));
            // Timer reaches 0 - lose life and restart.
            if(timeSeconds.getValue() == 0){
                setTime();
                LoseLife();
            }
            Move.moveImages(images);
            // Handle user input.
            scene.setOnKeyPressed(
                    e -> {
                        String code = e.getCode().toString();
                        onKeyPress(code);
                    });
        }
    };
    @FXML
    Button newGameButton,loadGameButton,quitGameButton;

    @FXML
    void startNewGame(){
        showMainMenu(false);
        setTime();
        animTimer.start();
        isGameRunning = true;
    }
    @FXML
    void loadGame(){
        String userHomeFolder = System.getProperty("user.home");
        File loadFile = new File(userHomeFolder,"loadFile.txt");
        ArrayList<String> loadSpecs = new ArrayList<>();

        try (Scanner sc = new Scanner(loadFile)) {
            while( sc.hasNext() ) {
                String content = sc.useDelimiter("\\@").next();
                loadSpecs.add(content);
            }
            sc.close();
            try {
                timeForLevel = Double.parseDouble(loadSpecs.get(1));
                visRowsCount = Integer.parseInt(loadSpecs.get(3));
                score.setText(Integer.toString(Integer.parseInt(loadSpecs.get(2))));
                level = Integer.parseInt(loadSpecs.get(0));
                lives.setText(Integer.toString(Integer.parseInt(loadSpecs.get(4))));

                initializeLevel(level);
                showMainMenu(false);
                setTime();
                animTimer.start();
                isGameRunning = true;
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Save file is unreadable!");
                alert.showAndWait();
            }
        }
        catch (java.io.IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("No save file found!");
            alert.showAndWait();
        }

    }
    @FXML
    void quitGame(){
        System.exit(0);
    }

    void showMainMenu(boolean visible){
        newGameButton.setVisible(visible);
        loadGameButton.setVisible(visible);
        quitGameButton.setVisible(visible);
    }


    // when you reach the top of the screen you go to the next scene of the level
    // and also if you go to the bottom you go back to the previous one.
    public static int currentLevelScene = 0;

    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[]) null);
    }

    // Display game window.
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize layout.
            Parent root = FXMLLoader.load(getClass().getResource("Game window.fxml"));
            frogger = root.lookup("#frogger");
            window = (Pane) root.lookup("#window");
            lives = (Text) root.lookup("#lives");
            score = (Text) root.lookup("#score");
            time = (Text) root.lookup("#time");
            // Set player starting position.
            froggerStartingPositionX = frogger.getLayoutX();
            froggerStartingPositionY = frogger.getLayoutY();
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Frogger");

            initializeLevel(level);
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Game time limit.
    private void setTime() {
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds = new SimpleIntegerProperty((int) timeForLevel);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(60 + 1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
    }

    // Prepare level.
    private void initializeLevel(int level) {
        switch (level){
            case 1:{
                visitedRows.clear();
                InitializeVisitedRows(13);
                timeForLevel = 60;
                time.setText(Double.toString(60d));
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
                createImage("Car", 18, 12, false,10);
            }
            case 2:{
                visitedRows.clear();
                InitializeVisitedRows(13);
                time.setText(Double.toString(timeForLevel));
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
                createImage("Car", 18, 12, false,10);
            }
        }
    }

    // Keep track of score.
    private void InitializeVisitedRows(int length) {
        for (int i = 0; i < length; i++) {
            visitedRows.add(false);
        }
    }

    // Create an image from a picture in resources.
    public void createImage(String name, int row, int column, boolean facingLeft, double speed){
        ImageView image = new ImageView(new Image("Files/Sprites/" + name +".jpg"));
        image.relocate(column * SQUARE_SIZE, row * SQUARE_SIZE);
        if(facingLeft){
            image.setRotate(180);
        }
        images.add(new MyImage(image, facingLeft, speed));
        window.getChildren().add(image);
    }

    // Handle user input.
    private void onKeyPress(String code) {
        if(Move.moving){
            return;
        }
        if(isGameRunning){
            switch (code){
                case "A":
                case "LEFT": {
                    frogger.setRotate(-90);
                    Move.moveFrogger(-SQUARE_SIZE, 0);
                    break;
                }
                case "D":
                case "RIGHT":{
                    frogger.setRotate(90);
                    Move.moveFrogger(SQUARE_SIZE, 0);
                    break;
                }
                case "S":
                case "DOWN":{
                    frogger.setRotate(-180);
                    Move.moveFrogger(0, SQUARE_SIZE);

                    break;
                }
                case "W":
                case "UP":{
                    int row = (int) (frogger.getLayoutY() / SQUARE_SIZE);
                    if(!visitedRows.get(row)){
                        visitedRows.set(row, true);
                        score.setText(Integer.toString(Integer.parseInt(score.getText()) + 10));
                    }
                    frogger.setRotate(0);
                    Move.moveFrogger(0, -SQUARE_SIZE);

                    break;
                }
            }
        }
        switch (code) {
            case "ESCAPE":
                animTimer.stop();
                isGameRunning = false;
                break;
            case "SPACE":
                animTimer.start();
                isGameRunning = true;
                break;
        }
        }




    // Lose a life.
    public static void LoseLife() {
        lives.setText(Integer.toString(Integer.parseInt(lives.getText()) - 1));
        // No lives left.
        if(Integer.parseInt(lives.getText()) == 0){
            Platform.exit();
        }
        // Reset frog position to starting one.
        frogger.setLayoutX(froggerStartingPositionX);
        frogger.setLayoutY(froggerStartingPositionY);
        for (int i = 0; i < visitedRows.size() - 1; i++) {
            visitedRows.set(i, false);
        }
        Move.stopped = true;
    }
}
