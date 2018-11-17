package levels;

import creationfromfile.ImageCreation;
import gameobjects.EnemyBackground;
import gameobjects.Block;
import geometry.Point;
import geometry.Rectangle;
import interfaces.LevelInformation;
import interfaces.Sprite;

import java.awt.Color;
import java.awt.Image;


/**
 * This is the 1st level.
 */
public class EnemyLevel implements LevelInformation {

    // Members
    private int paddleSpeed;
    private int paddleWidth;
    private String levelName;
    private Sprite background;
    private Block[][] aliansBlocks;
    private int numberOfBlocksToRemove;


    /**
     * Constructor for level1.
     */
    public EnemyLevel() {
        this.paddleSpeed = 500;
        this.paddleWidth = 70;
        this.levelName = "Alians";
        this.numberOfBlocksToRemove = 50;
        this.aliansBlocks = new Block[5][10];
        setblocks();
        this.setBackground();
    }

    /**
     * Setting the background to the sprite EnemyBackground 2 we created.
     */
    public void setBackground() {
        this.background = new EnemyBackground(Color.BLACK);

    }

    /**
     * @return the paddle movement.
     */
    public int paddleSpeed() {
        return this.paddleSpeed;
    }

    /**
     * @return the paddle Width.
     */
    public int paddleWidth() {
        return this.paddleWidth;
    }

    /**
     * @return the level name. it will be displayed at the top of the screen.
     */
    public String levelName() {
        return this.levelName;
    }

    /**
     * @return a sprite with the background of the level
     */
    public Sprite getBackground() {
        return this.background;
    }

    /**
     * Set blocks.
     */
    public void setblocks() {
        // Getting imgae of alien
        ImageCreation parsedImage = new ImageCreation();
        Image image = parsedImage.imageFromString("block_images/enemy.png");

        int startX = 100;
        int startY = 60;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                Block block = new Block(new Rectangle(new Point(startX, startY), 40, 30), 1, image);
                this.aliansBlocks[i][j] = block;
                startX += 60;
            }
            startX = 100;
            startY += 40;

        }
    }

    /**
     * @return the formation of the blocks.
     */
    public Block[][] getAliansBlocksblocks() {
        return this.aliansBlocks;
    }

    /**
     * @return Number of levels that should be removed before the level is considered to be "cleared".
     * This number should be <= blocks.size();
     */
    public int numberOfBlocksToRemove() {
        return numberOfBlocksToRemove;
    }

    /**
     * @return the cloned one.
     */
    public LevelInformation clone() {
        try {
            // Trying to clone
            EnemyLevel cloned = (EnemyLevel) super.clone();
            Block[][] newList = new Block[5][10];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    newList[i][j] = this.aliansBlocks[i][j].clone();
                }
                return cloned;
            }
        } catch (CloneNotSupportedException ex) {
            return null;
        }
        return null;
    }


}