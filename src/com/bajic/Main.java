package com.bajic;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class Main extends Application{

    public static final int FRAMES_PER_SECOND = 60;
    public static final int SPEED_FACTOR = 15;
    public static final int SECOND_IN_MILLISECONDS = 1000;
    public static final double ANIMATION_TIME = 0.15;
    public static final int POINTS_FROM_COIN = 50;
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
    public static boolean isSave = false;
    public static int loadVisRows = 0;
    public static int coinsTaken = 0;
    public AnimationTimer animTimer = new AnimationTimer() {
        public void handle(long currentNanoTime) {
            time.setText(Integer.toString(timeSeconds.getValue()));
            // Timer reaches 0 - lose life and restart.
            if(timeSeconds.getValue() == 0){
                setTime();
                animTimer.stop();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Out of time!");
                alert.setHeaderText(null);
                alert.setContentText("You ran out of time and lost a life!");
                alert.show();
                loseLife();
                animTimer.start();
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

    Button resumeGameButton = new Button();
    Button saveGameButton = new Button();
    Button quitButton = new Button();

    @FXML
    void startNewGame(){
        isSave = false;
        showMainMenu(false);
        animTimer.start();
        setTime();
        isGameRunning = true;
    }

    @FXML
    void loadGame(){
        String userHomeFolder = System.getProperty("user.home");
        File loadFile = new File(userHomeFolder,"frogger-saveFile.txt");
        ArrayList<String> loadSpecs = new ArrayList<>();

        try (Scanner sc = new Scanner(loadFile)) {
            while( sc.hasNext() ) {
                String content = sc.useDelimiter("\\@").next();
                loadSpecs.add(content);
            }
            sc.close();
            try {
                isSave = true;
                loadVisRows = (Integer.parseInt(loadSpecs.get(3)));
                score.setText(Integer.toString(Integer.parseInt(loadSpecs.get(2))));
                levelIndex = Integer.parseInt(loadSpecs.get(0));
                lives.setText(Integer.toString(Integer.parseInt(loadSpecs.get(4))));
                coinsTaken = Integer.parseInt(loadSpecs.get(5));
                initializeLevel(levelIndex,Integer.parseInt(loadSpecs.get(1)));
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

    void saveGame() {
        int visRowsCount = 0;
        for (int i = 0; i < 20 ; i++) {
            if(level.getVisitedRows().get(i)){
                visRowsCount++;
            }
        }
        String userHomeFolder = System.getProperty("user.home");
        File saveFile = new File(userHomeFolder, "frogger-saveFile.txt");
        try {
            saveFile.createNewFile();
            PrintWriter writer = new PrintWriter(saveFile, "UTF-8");
            writer.print(
                      levelIndex + "@" +
                      time.getText() + "@" +
                      score.getText() + "@" +
                      visRowsCount + "@" +
                      lives.getText() + "@" +
                      level.getCoinsPicked());
            writer.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Game successfully saved in " + userHomeFolder);
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Could not save!");
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
    void pauseGame(){
        if(isGameRunning){
            animTimer.stop();
            isGameRunning = false;
            showPauseMenu();
        }else{
            hidePauseMenu();
            isGameRunning = true;
            animTimer.start();
        }
    }
    void showPauseMenu(){

        resumeGameButton.setLayoutX(215);
        resumeGameButton.setLayoutY(78);
        resumeGameButton.setText("Resume Game");
        resumeGameButton.setPrefHeight(57);
        resumeGameButton.setPrefWidth(206);
        resumeGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                hidePauseMenu();
                isGameRunning = true;
                animTimer.start();
            }
        });
        window.getChildren().add(resumeGameButton);
       saveGameButton.setLayoutX(215);
       saveGameButton.setLayoutY(147);
       saveGameButton.setText("Save Game");
       saveGameButton.setPrefHeight(57);
       saveGameButton.setPrefWidth(206);
       saveGameButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent e) {
               saveGame();
           }
       });
        window.getChildren().add(saveGameButton);
       quitButton.setLayoutX(215);
       quitButton.setLayoutY(218);
       quitButton.setText("Quit");
       quitButton.setPrefHeight(57);
       quitButton.setPrefWidth(206);
       quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                quitGame();
            }
        });
        window.getChildren().add(quitButton);
    }
    void hidePauseMenu(){
        window.getChildren().remove(resumeGameButton);
        window.getChildren().remove(saveGameButton);
        window.getChildren().remove(quitButton);
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
            scene.getStylesheets().add("com/bajic/styles.css");
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

    public static void setTime(int loadTime) {
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds = new SimpleIntegerProperty(loadTime);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(loadTime + 1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
    }

    public static void initializeLevel(int levelIndex) {
        if(level != null){
            for (int i = 0; i < level.getImages().size(); i++) {
                level.getImages().get(i).getImageView().setVisible(false);
            }
            level.getBackgroundImage().setVisible(false);
        }
        level = new com.bajic.Level(levelIndex);
        setTime();
    }

    public static void initializeLevel(int levelIndex, int LoadTime) {
        if(level != null){
            for (int i = 0; i < level.getImages().size(); i++) {
                level.getImages().get(i).getImageView().setVisible(false);
            }
            level.getBackgroundImage().setVisible(false);
        }
        level = new com.bajic.Level(levelIndex);
        setTime(LoadTime);
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
                    int row = (int) ((frogger.getLayoutY() - level.getBackgroundImage().getLayoutY()) / level.getSquareSize());
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
                pauseGame();
                break;
        }
    }

    // Lose a life.
    public static void loseLife() {
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
        Move.setCarrierItem(null);
    }

    public static void ResetEverything() {
        double differenceX = Main.level.getBackgroundImageStartingX() - Main.level.getBackgroundImage().getLayoutX();
        double differenceY = Main.level.getBackgroundImageStartingY() - Main.level.getBackgroundImage().getLayoutY();
        for (int i = 0; i < Main.level.getImages().size(); i++) {
            Main.level.getImages().get(i).getImageView().relocate(Main.level.getImages().get(i).getImageView().getLayoutX() + differenceX,
                                                                  Main.level.getImages().get(i).getImageView().getLayoutY() + differenceY);
        }
        Main.level.getBackgroundImage().relocate(Main.level.getBackgroundImage().getLayoutX() + differenceX,
                Main.level.getBackgroundImage().getLayoutY() + differenceY);
    }
}
