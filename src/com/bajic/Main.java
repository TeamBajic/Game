package com.bajic;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
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
import javafx.scene.text.Font;
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
    public static Text currentLevel;
    public static boolean isSave = false;
    public static int loadVisRows = 0;
    public static int coinsTaken = 0;
    public static boolean outOfTime = false;
    public static ArrayList<String> Highscores = new ArrayList<>();

    //The animation timer that is called every frame
    public AnimationTimer animTimer = new AnimationTimer() {
        public void handle(long currentNanoTime) {
            time.setText(Integer.toString(timeSeconds.getValue())); //updates the timer

            // Timer reaches 0 - lose life and restart.
            if(timeSeconds.getValue() == 0){
                setTime(); //resets the time
                outOfTime = true;
                animTimer.stop();

                //creates an alert for the player
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Out of time!");
                alert.setHeaderText(null);
                alert.setContentText("You ran out of time and lost a life!");
                alert.show();

                loseLife();
                animTimer.start();
            }

            MoveObjectsManager.moveImages(level.getImages());
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
    Button hResumeGameButton = new Button();
    Button saveGameButton = new Button();
    Button highscoresButton = new Button();
    Button quitButton = new Button();

    // Highscores.
    public static Text scoreText1 = new Text(300,200,"");
    public static Text scoreText2 = new Text(300,300,"");
    public static Text scoreText3 = new Text(300,400,"");
   // final Text scoreText2 = new Text(215, 25, Highscores.get(0));
    //final Text scoreText3 = new Text(215, 25, Highscores.get(0));

    @FXML
    void startNewGame(){
        isSave = false;
        showMainMenu(false);
        animTimer.start();
        setTime();
        isGameRunning = true;
    }

    @FXML
    // Load game from a file.
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

    // Save game to a file.
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

    // Calculate if current score is big enough to be in the highscores.
        static void calcHighscores(ArrayList<String> newHighScore){
            // Add new score to highscores.
            newHighScore.add(score.getText());
            int lastElemPos = newHighScore.size() - 1;
            // Sort highscores descending.
            for (int i = 0; i <= lastElemPos; i++) {
                if (Integer.parseInt(newHighScore.get(lastElemPos)) >
                        Integer.parseInt(newHighScore.get(i))) {
                    Collections.swap(newHighScore, i, lastElemPos);
                }
            }
            // If there are more than 3 entries, remove last one (4th one).
            if(lastElemPos > 2){
                newHighScore.remove(lastElemPos);
            }
            saveHighscores(newHighScore);
    }

    // Load highscores from a file into the program.
    static void loadHighscores(ArrayList<String> newHighScore){
        String userHomeFolder = System.getProperty("user.home");
        File highscoresFile = new File(userHomeFolder,"frogger-highscores.txt");
        try (Scanner sc = new Scanner(highscoresFile)) {
            while( sc.hasNext() ) {
                newHighScore.add(sc.next());
            }
            sc.close();
        }catch (IOException e) {
            try {
                highscoresFile.createNewFile();
            } catch (IOException e1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Could not create a highscores file!");
                alert.showAndWait();
            }
        }catch(Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Highscores file is unreadable!");
            alert.showAndWait();
        }
    }

    // Save current highscores to a file.
    static void saveHighscores(ArrayList<String> newHighScore){
        String userHomeFolder = System.getProperty("user.home");
        File highscoresFile = new File(userHomeFolder, "frogger-highscores.txt");
        try {
            highscoresFile.createNewFile();
            PrintWriter writer = new PrintWriter(highscoresFile, "UTF-8");
            for(int i = 0; i < newHighScore.size(); i++){
                writer.println(newHighScore.get(i));
            }
            writer.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(null);
            alert.setContentText("Could not write highscores to a file!");
            alert.showAndWait();
        }
    }

    static void exitGame(){
        calcHighscores(Highscores);
        System.exit(0);
    }

    @FXML
    void quitGame(){
        calcHighscores(Highscores);
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
        resumeGameButton.setOnAction(e -> {
            hidePauseMenu();
            isGameRunning = true;
            animTimer.start();
        });
        window.getChildren().add(resumeGameButton);
        saveGameButton.setLayoutX(215);
        saveGameButton.setLayoutY(147);
        saveGameButton.setText("Save Game");
        saveGameButton.setPrefHeight(57);
        saveGameButton.setPrefWidth(206);
        saveGameButton.setOnAction(e -> saveGame());
        window.getChildren().add(saveGameButton);
        highscoresButton.setLayoutX(215);
        highscoresButton.setLayoutY(218);
        highscoresButton.setText("Highscores");
        highscoresButton.setPrefHeight(57);
        highscoresButton.setPrefWidth(206);
        highscoresButton.setOnAction(e -> showHighscoresMenu());
        window.getChildren().add(highscoresButton);
        quitButton.setLayoutX(215);
        quitButton.setLayoutY(289);
        quitButton.setText("Quit");
        quitButton.setPrefHeight(57);
        quitButton.setPrefWidth(206);
        quitButton.setOnAction(e -> quitGame());
        window.getChildren().add(quitButton);
    }
    void hidePauseMenu(){
        window.getChildren().remove(resumeGameButton);
        window.getChildren().remove(saveGameButton);
        window.getChildren().remove(highscoresButton);
        window.getChildren().remove(quitButton);
    }

    void showHighscoresMenu(){
        hidePauseMenu();
        hResumeGameButton.setLayoutX(215);
        hResumeGameButton.setLayoutY(78);
        hResumeGameButton.setText("Back");
        hResumeGameButton.setPrefHeight(57);
        hResumeGameButton.setPrefWidth(206);
        hResumeGameButton.setOnAction(e -> hideHighscoresMenu());
        window.getChildren().add(hResumeGameButton);
        if (Highscores.size() > 0)
        {
            scoreText1.setText(Highscores.get(0));
        }
        if (Highscores.size() > 1){
            scoreText2.setText(Highscores.get(1));
        }
        if (Highscores.size() == 3){
            scoreText3.setText(Highscores.get(2));
        }
        scoreText1.setFont(Font.font ("Comic Sans MS", 40));
        window.getChildren().add(scoreText1);
        scoreText2.setFont(Font.font ("Comic Sans MS", 40));
        window.getChildren().add(scoreText2);
        scoreText3.setFont(Font.font ("Comic Sans MS", 40));
        window.getChildren().add(scoreText3);

    }

    void hideHighscoresMenu(){
        window.getChildren().remove(hResumeGameButton);
        window.getChildren().remove(scoreText1);
        window.getChildren().remove(scoreText2);
        window.getChildren().remove(scoreText3);
        showPauseMenu();
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
            currentLevel = (Text) root.lookup("#level");
            initializeLevel(levelIndex);
            scene = new Scene(root);
            scene.getStylesheets().add("com/bajic/styles.css");
            primaryStage.setScene(scene);
            primaryStage.setTitle("Frogger");
            primaryStage.show();
            loadHighscores(Highscores);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Game time limit.
    public static void setTime() {
        //sets the time according to the level's time
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
        //sets the time according to the time it was stopped during the save
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

    public static void initializeLevel(int levelIndex) { // initialize the level with the default time
        currentLevel.setText(String.valueOf(levelIndex));
        if(level != null){ //if a level exists we need to set all the current images to invisible
            for (int i = 0; i < level.getImages().size(); i++) {
                level.getImages().get(i).getImageView().setVisible(false);
            }
            level.getBackgroundImage().setVisible(false);
        }
        level = new com.bajic.Level(levelIndex);
        setTime();
    }

    public static void initializeLevel(int levelIndex, int LoadTime) {// initialize the level with the time from the save
        currentLevel.setText(String.valueOf(levelIndex));
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
        if(MoveObjectsManager.isMoving()){
            return;
        }
        if(isGameRunning){
            switch (code){
                case "A":
                case "LEFT": {
                    frogger.setRotate(-90);
                    MoveObjectsManager.moveFrogger(-level.getSquareSize(), 0);
                    break;
                }
                case "D":
                case "RIGHT":{
                    frogger.setRotate(90);
                    MoveObjectsManager.moveFrogger(level.getSquareSize(), 0);
                    break;
                }
                case "S":
                case "DOWN":{
                    frogger.setRotate(-180);
                    MoveObjectsManager.moveFrogger(0, level.getSquareSize());

                    break;
                }
                case "W":
                case "UP":{
                    //score add for going up a row
                    int row = (int) ((frogger.getLayoutY() - level.getBackgroundImage().getLayoutY()) / level.getSquareSize());
                    if(!level.getVisitedRows().get(row)){
                        level.getVisitedRows().set(row, true);
                        score.setText(Integer.toString(Integer.parseInt(score.getText()) + 10));
                    }
                    frogger.setRotate(0);
                    MoveObjectsManager.moveFrogger(0, -level.getSquareSize());

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
        lives.setText(Integer.toString(Integer.parseInt(lives.getText()) - 1)); //reduce the lives by 1

        // No lives left.
        if(Integer.parseInt(lives.getText()) == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over!");
            alert.setHeaderText(null);
            alert.setContentText("Game Over!");
            alert.show();
            try {
                sleep(1500);
            } catch (InterruptedException e) {
                exitGame();
            }
            exitGame();
        }

        // Reset frog position to starting one.
        frogger.setLayoutX(level.getFroggerStartingPositionX());
        frogger.setLayoutY(level.getFroggerStartingPositionY());
        for (int i = 0; i < level.getVisitedRows().size() - 1; i++) {
            level.getVisitedRows().set(i, false);
        }
        MoveObjectsManager.setStopped(true); //if the death occurs while moving we have to prematurely stop frogger from moving
        MoveObjectsManager.setCarrierItem(null); //if the player is being carried on something like a log we need to reset it to null
        if (Integer.parseInt(lives.getText()) >= 1 && !outOfTime){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Got hit!");
            alert.setHeaderText(null);
            alert.setContentText("You got hit and lost a life!");
            alert.show();
        }
        outOfTime = false;
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
