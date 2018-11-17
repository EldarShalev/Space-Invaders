package gamecontrol;


import gameobjects.BlockRemover;
import gameobjects.Constants;
import gameobjects.Counter;
import gameobjects.Paddle;
import gameobjects.SpriteCollection;
import gameobjects.BallRemover;
import gameobjects.ScoreTrackingListener;
import gameobjects.ScoreIndicator;
import gameobjects.LivesIndicator;
import gameobjects.NameIndicator;
import gameobjects.Block;
import gameobjects.Ball;
import geometry.Rectangle;
import interfaces.Animation;
import interfaces.Collidable;
import interfaces.LevelInformation;
import interfaces.Sprite;
import biuoop.DrawSurface;
import biuoop.GUI;
import geometry.Point;
import biuoop.KeyboardSensor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


import static gameobjects.Constants.BLOCK_LEFT;
import static gameobjects.Constants.BLOCK_RIGHT;
import static gameobjects.Constants.BLOCK_TOP;
import static gameobjects.Constants.DEATH_REGION;

/**
 * A class of game to control the running game.
 */
public class GameLevel implements Animation {
    // Members of game
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private String levelName;
    private Paddle paddle;
    private GroupOfAlians groupOfAlians;
    // Counters
    private Counter remainingBlocks;
    private Counter remainingAliens = new Counter(50);
    private Counter ballsCounter;
    private Counter score;
    private Counter numOfLives;
    // Listeners
    private BlockRemover blockRemoverForAlians;
    private BlockRemover blockRemoverForShields;
    private BallRemover ballRemover;
    private ScoreTrackingListener scoreSum;
    private ScoreIndicator scoreIndicator;
    private LivesIndicator livesIndicator;
    private NameIndicator nameIndicator;
    private LevelInformation levelInformation;

    // Members of Animation
    private AnimationRunner runner;
    private boolean running;
    private GUI gui;
    private biuoop.KeyboardSensor keyboard;

    //Other
    private List<Ball> ballList = new ArrayList<>();
    private Double movingIterval;

    /**
     * @param levelInformation a given level to get the details from and assign to other members.
     * @param keyboardSensor   a keyboard in which the game is played on.
     * @param animationRunner  the animation runner including the gui which the game is played on.
     * @param score            the indicator of score.
     * @param numOfLives       the indicator for num of lives.
     * @param gameLevelName    a given level name with the number of level.
     * @param movingInterval1  how much speed was changed (10%).
     */
    public GameLevel(LevelInformation levelInformation, KeyboardSensor keyboardSensor, AnimationRunner
            animationRunner, Counter score, Counter numOfLives, Double movingInterval1,
                     String gameLevelName) {
        // Game flow accessing
        this.levelInformation = levelInformation;
        this.keyboard = keyboardSensor;
        this.runner = animationRunner;
        this.score = score;
        this.numOfLives = numOfLives;

        // Other members to initialize for further using
        this.remainingBlocks = new Counter(levelInformation.numberOfBlocksToRemove());
        this.ballRemover = new BallRemover(this, ballsCounter);
        this.scoreSum = new ScoreTrackingListener(score);
        this.scoreIndicator = new ScoreIndicator(score);
        this.livesIndicator = new LivesIndicator(numOfLives);
        this.levelName = gameLevelName;
        this.nameIndicator = new NameIndicator(levelName);
        this.gui = animationRunner.getGui();
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.paddle = new Paddle(keyboardSensor, levelInformation.paddleWidth(), levelInformation.paddleSpeed()
                , this);
        this.movingIterval = movingInterval1;
        this.groupOfAlians = new GroupOfAlians(levelInformation.getAliansBlocksblocks(), this,
                movingInterval1, score);
        this.blockRemoverForAlians = new BlockRemover(this, remainingAliens, groupOfAlians.getBlocks());
        this.blockRemoverForShields = new BlockRemover(this, remainingBlocks);


    }

    /**
     * Add a collidable.
     *
     * @param c A interfaces.Collidable.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);

    }

    /**
     * @param c a collidable to remove from the game.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * @param s a sprite to remove from the game.
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * Add a interfaces.Sprite.
     *
     * @param s A interfaces.Sprite.
     */
    public void addSprite(Sprite s) {
        if (s != null) {
            this.sprites.addSprite(s);
        }
    }

    /**
     * Initialize a new game.
     */
    public void initialize() {
        // Adding the background
        sprites.addSprite(levelInformation.getBackground());
        // Adding the paddle.
        paddle.addToGame(this);

        // Adding the frame
        BLOCK_TOP.addToGame(this);
        BLOCK_RIGHT.addToGame(this);
        BLOCK_LEFT.addToGame(this);
        DEATH_REGION.addToGame(this);


        Block[][] blocks = levelInformation.getAliansBlocksblocks();

        // Adding the Alians
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                Block block = blocks[i][j];
                block.setGameLevel(this);
                block.addToGame(this);
                // Adding the listeners to the block
                block.addHitListener(blockRemoverForAlians);
                block.addHitListener(scoreSum);

            }
        }

        // Adding the walls
        int startX = 100;
        int startY = Constants.SHIELDS_HEIGHT;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 70; j++) {
                Block block = new Block(new Rectangle(new Point(startX, startY), 5, 5), 1, Color.BLUE);
                block.addToGame(this);
                // Adding the listeners to the block
                block.addHitListener(blockRemoverForShields);
                startX += 5;
                if (j == 23 || j == 46) {
                    startX += 100;
                }

            }
            startX = 100;
            startY += 5;
        }

        //Adding the indicators
        scoreIndicator.addToGame(this);
        livesIndicator.addToGame(this);
        nameIndicator.addToGame(this);


    }

    /**
     * @return the game environment.
     */
    public GameEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Play one turn (1 lives).
     */
    public void playOneTurn() {
        // Creation of the balls
        // Setting the paddle in the center
        paddle.setStartingRectangle();
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        groupOfAlians.addToGame(this);
        this.running = true;
        this.runner.run(this);
    }

    /**
     * @return true if the game should stop.
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * The logic of the game.
     *
     * @param d  a given surface.
     * @param dt - definition of time
     */
    public void doOneFrame(DrawSurface d, double dt) {
        // Checking for pause
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(keyboard, KeyboardSensor.SPACE_KEY,
                    new PauseScreen()));
        }
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        // Checking for blocks left
        if (this.remainingAliens.getValue() == 0) {
            this.running = false;
        }

        if (environment.getPaddle().paddleWasHit()) {
            environment.getPaddle().setPaddleLost(false);
            numOfLives.decrease(1);
            removeAllBullets();
            if (numOfLives.getValue() != 0) {
                groupOfAlians.backToStartingPosition();
                // Start countdown
                this.runner.run(new CountdownAnimation(2, 3, this.sprites));

            } else {
                this.running = false;
            }
        }
        if (groupOfAlians.getAliensWon()) {
            numOfLives.decrease(1);
            removeAllBullets();
            if (numOfLives.getValue() != 0) {
                groupOfAlians.backToStartingPosition();
                // Start countdown
                this.runner.run(new CountdownAnimation(2, 3, this.sprites));

            } else {
                this.running = false;
            }
        }


    }

    /**
     * Gets remaining blocks.
     *
     * @return the remaining blocks
     */
    public Counter getRemainingAliens() {
        return remainingAliens;
    }

    /**
     * @return remove ball.
     */
    public BallRemover getBallRemover() {
        return ballRemover;
    }

    /**
     * @param ball a given ball.
     */
    public void addBall(Ball ball) {
        ballList.add(ball);
    }

    /**
     * Removing balls from screen.
     */
    private void removeAllBullets() {
        for (Ball bullet : ballList) {
            sprites.removeSprite(bullet);
        }
        ballList = new ArrayList<>();
    }


}