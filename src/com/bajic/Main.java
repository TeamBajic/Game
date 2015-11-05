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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application{

    public static final double HUD_SPACE = 60;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int SPEED_FACTOR = 15;
    public static final int SECOND_IN_MILLISECONDS = 1000;
    public static final double ANIMATION_TIME = 0.15;
    private static Timeline timeline;
    private static IntegerProperty timeSeconds;
    public static boolean isGameRunning = false;
    public static int levelIndex = 1;
    public static com.bajic.Level level;
    public static Parent root = null;
    public static Scene scene;
    public static Pane window;
    public static Pane hud;
    public static Node frogger;
    public static Text lives;
    public static Text score;
    public static Text time;

    public AnimationTimer animTimer = new AnimationTimer() {
        public void handle(long currentNanoTime) {
            time.setText(Integer.toString(timeSeconds.getValue()));
            // Timer reaches 0 - lose life and restart.
            if(timeSeconds.getValue() == 0){
                setTime();
                LoseLife();
            }
            Move.moveImages(level.getImages());
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
        animTimer.start();
        setTime();
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
                level.setTimeForLevel(Double.parseDouble(loadSpecs.get(1)));
                level.setVisRowsCount(Integer.parseInt(loadSpecs.get(3)));
                score.setText(Integer.toString(Integer.parseInt(loadSpecs.get(2))));
                levelIndex = Integer.parseInt(loadSpecs.get(0));
                lives.setText(Integer.toString(Integer.parseInt(loadSpecs.get(4))));

                initializeLevel(levelIndex);
                showMainMenu(false);
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


    // when you reach the top of the screen you go to the next scene of the levelIndex
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
            root = FXMLLoader.load(getClass().getResource("Game window.fxml"));
            frogger = root.lookup("#frogger");
            window = (Pane) root.lookup("#window");
            hud = (Pane) root.lookup("#hud");
            lives = (Text) root.lookup("#lives");
            score = (Text) root.lookup("#score");
            time = (Text) root.lookup("#time");
            // Set player starting position.
            initializeLevel(levelIndex);
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Frogger");
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Game time limit.
    public static void setTime() {
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds = new SimpleIntegerProperty((int) level.getTimeForLevel());
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(level.getTimeForLevel() + 1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
    }

    public static void initializeLevel(int levelIndex) {
        level = new com.bajic.Level(levelIndex);
        setTime();
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
                    Move.moveFrogger(-level.getSquareSize(), 0);
                    break;
                }
                case "D":
                case "RIGHT":{
                    frogger.setRotate(90);
                    Move.moveFrogger(level.getSquareSize(), 0);
                    break;
                }
                case "S":
                case "DOWN":{
                    frogger.setRotate(-180);
                    Move.moveFrogger(0, level.getSquareSize());

                    break;
                }
                case "W":
                case "UP":{
                    //score add
                    int row = (int) (frogger.getLayoutY() / level.getSquareSize());
                    if(!level.getVisitedRows().get(row)){
                        level.getVisitedRows().set(row, true);
                        score.setText(Integer.toString(Integer.parseInt(score.getText()) + 10));
                    }
                    frogger.setRotate(0);
                    Move.moveFrogger(0, -level.getSquareSize());

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
        frogger.setLayoutX(level.getFroggerStartingPositionX());
        frogger.setLayoutY(level.getFroggerStartingPositionY());
        for (int i = 0; i < level.getVisitedRows().size() - 1; i++) {
            level.getVisitedRows().set(i, false);
        }
        Move.stopped = true;
    }
}
