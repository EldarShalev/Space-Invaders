package gamecontrol;

import biuoop.DialogManager;
import biuoop.GUI;
import creationfromfile.Background;

import gameobjects.Constants;
import gameobjects.Counter;
import gameobjects.HighScoresAnimation;
import interfaces.LevelInformation;
import biuoop.KeyboardSensor;
import interfaces.Task;
import levels.EnemyLevel;

import java.awt.Color;
import java.io.File;
import java.io.IOException;


import static gameobjects.Constants.HIGH_SCORE_TABLE_FILE_SIZE;


/**
 * This class is for running all the levels.
 */
public class GameFlow {

    // Members
    private biuoop.KeyboardSensor keyboard;
    private AnimationRunner animationRunner;
    private Counter numOfLives = new Counter(Constants.NUM_OF_LIVES);
    private Counter score = new Counter(0);
    private HighScoresTable scoresTable;

    /**
     * Instantiates a new Game flow.
     */
    public GameFlow() {
        GUI gui = new GUI("Space Invaders", Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        this.keyboard = gui.getKeyboardSensor();
        this.scoresTable = loadHighScoreTable();
        this.animationRunner = new AnimationRunner(gui);
    }

    /**
     */
    public void startGame() {
        while (true) {
            EnemyLevel enemyLevel = new EnemyLevel();
            LevelInformation currentLevel;
            currentLevel = enemyLevel.clone();
            // Create new menu with other function, according to the logic wa want (s to start, q to quit..)
            MenuAnimation<Task> menu = getMenu(currentLevel);
            // Run the menu
            this.animationRunner.run(menu);
            // get the status from the menu
            Task taskStatus = menu.getStatus();
            // When the key is pressed and we got the task, run it if it's not null
            if (taskStatus != null) {
                taskStatus.run();
            }

        }
    }

    /**
     * @param enemyLevel the level information.
     */
    public void runLevels(LevelInformation enemyLevel) {
        // Init the indicators to their init values
        Integer levelNumber = 1;
        Double movingInterval = 4.0;
        this.score.setNumber(0);
        // Starting the levels one by one
        this.numOfLives.setNumber(Constants.NUM_OF_LIVES);
        while (numOfLives.getValue() > 0) {
            EnemyLevel enemyLevel1 = new EnemyLevel();
            GameLevel level = new GameLevel(enemyLevel1, this.keyboard, this.animationRunner, score, numOfLives
                    , movingInterval, String.format("Battle no.%d", levelNumber));
            level.initialize();
            // run the level
            while (level.getRemainingAliens().getValue() > 0 && numOfLives.getValue() > 0) {
                level.playOneTurn();
            }
            movingInterval *= 1.1;
            levelNumber++;
        }

        // When finish to play
        if (scoresTable.getRank(score.getValue()) >= 0) {
            // Getting the name
            DialogManager dialogManager = this.animationRunner.getGui().getDialogManager();
            String name = dialogManager.showQuestionDialog("Name", "What is your name?", "");
            // Add to the table (if it should be added)
            scoresTable.add(new ScoreInfo(name, score.getValue()));
            // Saving the new high score table according to the new score we got
            try {
                scoresTable.save(new File(Constants.HIGH_SCORE_TABLE_FILE_NAME));
            } catch (IOException ex) {
                System.out.println("Failed to save the table");
                ex.printStackTrace(System.err);
            }
        }
        // Showing end screen
        this.animationRunner.run(new KeyPressStoppableAnimation(keyboard, KeyboardSensor.SPACE_KEY,
                new EndScreen(numOfLives, score)));

        // Showing high score table
        this.animationRunner.run(new KeyPressStoppableAnimation(keyboard, KeyboardSensor.SPACE_KEY,
                new HighScoresAnimation(scoresTable)));
    }

    /**
     * @return the high score table object if exists, else create new one and return it.
     */
    private HighScoresTable loadHighScoreTable() {
        File file = new File(Constants.HIGH_SCORE_TABLE_FILE_NAME);
        if (file.exists()) {
            return HighScoresTable.loadFromFile(file);
        } else {
            HighScoresTable highScoresTable = new HighScoresTable(HIGH_SCORE_TABLE_FILE_SIZE);
            try {
                highScoresTable.save(file);
            } catch (IOException e) {
                System.out.println("Failed to save the table");
            }
            return highScoresTable;
        }
    }

    /**
     * @param enemyLevel a given level information.
     * @return new menu animation.
     */
    private MenuAnimation<Task> getMenu(LevelInformation enemyLevel) {
        // Start the menu
        MenuAnimation<Task> menu = new MenuAnimation<>("Arkanoid",
                this.keyboard,
                new Background(Color.orange),
                animationRunner);


        Task<Void> startGame = new Task<Void>() {
            @Override
            public Void run() {
                runLevels(enemyLevel);
                return null;
            }
        };
        Task<Void> highScoreTask = new Task<Void>() {
            public Void run() {
                animationRunner.run(new KeyPressStoppableAnimation(keyboard,
                        KeyboardSensor.SPACE_KEY,
                        new HighScoresAnimation(scoresTable)));
                return null;
            }
        };

        Task<Void> quit = new Task<Void>() {
            public Void run() {
                System.exit(0);
                return null;
            }
        };

        menu.addSelection("s", "Start", startGame);
        menu.addSelection("h", "High Scores", highScoreTask);
        menu.addSelection("q", "Quit", quit);
        return menu;
    }


}