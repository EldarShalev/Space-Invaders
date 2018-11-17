package creationfromfile;

import gameobjects.Block;
import gameobjects.Velocity;
import interfaces.LevelInformation;
import interfaces.Sprite;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is for default level information which is used to clone and makeing informations for each level.
 */
public class DefaultLevelInformation implements LevelInformation, Cloneable {
    // All the features a game can have
    private String name;
    private List<Velocity> ballVelocities = new LinkedList<>();
    private Block[][] blocks = new Block[5][10];
    private int numOfBlocks;
    private Integer paddleSpeed;
    private Integer paddleWidth;
    private Sprite background;
    private Color backgroundColor = Color.BLACK;



    /**
     * @return the blocks.
     */
    @Override
    public Block[][] getAliansBlocksblocks() {
        return new Block[0][];
    }

    /**
     * Paddle speed int.
     *
     * @return the int
     */
    @Override
    public int paddleSpeed() {
        return paddleSpeed;
    }

    /**
     * Paddle width int.
     *
     * @return the int
     */
    @Override
    public int paddleWidth() {
        return paddleWidth;
    }

    /**
     * The level name will be displayed at the top of the screen.
     *
     * @return the string
     */
    @Override
    public String levelName() {
        return name;
    }

    /**
     * Returns a sprite with the background of the level.
     *
     * @return the background
     */
    @Override
    public Sprite getBackground() {
        return background;
    }


    /**
     * Number of blocks to remove int.
     *
     * @return the int
     */
    public int numberOfBlocksToRemove() {
        return numOfBlocks;
    }

    /**
     * Gets background color.
     *
     * @return the background color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param blocks1 set the blocks.
     */
    public void setBlocks(Block[][] blocks1) {
        this.blocks = blocks1;
    }

    /**
     * @param backgroundColor1 given color for background.
     */
    public void setBackgroundColor(Color backgroundColor1) {
        this.backgroundColor = backgroundColor1;
    }


    /**
     * @return the exact level information of the game for the next round (we must this method in order to avoid
     * overriding blocks and files we have.
     */
    public LevelInformation clone() {
        try {
            // Trying to clone
            DefaultLevelInformation cloned = (DefaultLevelInformation) super.clone();
            Block[][] newList = new Block[5][10];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    newList[i][j] = (blocks[i][j].clone());
                }

            }
            cloned.setBlocks(newList);
            return cloned;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
