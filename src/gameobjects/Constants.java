package gameobjects;

import geometry.Point;
import geometry.Rectangle;

/**
 * This is a class for all the gameobjects.Constants in the project.
 */
public class Constants {
    // gamecontrol.GameLevel
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;

    // gameobjects.Paddle
    public static final int PADDLE_HEIGHT = 13;


    // The Frame of the game
    public static final Block DEATH_REGION = new Block(new Rectangle(new Point(20, Constants.GAME_HEIGHT - 20),
            Constants.GAME_WIDTH - 40, 20));

    public static final Block BLOCK_TOP = new Block(new Rectangle(new Point(0, 20), Constants.GAME_WIDTH, 20));
    public static final Block BLOCK_LEFT = new Block(new Rectangle(new Point(0, 40), 20,
            Constants.GAME_HEIGHT - 20));

    public static final Block BLOCK_RIGHT = new Block(new Rectangle(new Point(Constants.GAME_WIDTH - 20, 20), 20,
            Constants.GAME_HEIGHT - 20));


    // The indicators of the game
    public static final Rectangle INDICATORS_RECT = new Rectangle(new Point(0, 0), GAME_WIDTH, 20);

    // This is the frame per second
    public static final double FPS = 60;

    // This is for the high score
    public static final String HIGH_SCORE_TABLE_FILE_NAME = "highscores";
    public static final int HIGH_SCORE_TABLE_FILE_SIZE = 5;
    // For num of lives
    public static final int NUM_OF_LIVES = 3;

    // Aliens row
    public static final int ALIANS_ROWS = 5;
    // Aliens coulmns
    public static final int ALIANS_COLUMNS = 10;
    // Shield height
    public static final int SHIELDS_HEIGHT = 500;


}
