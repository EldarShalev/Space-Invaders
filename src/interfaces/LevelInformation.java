package interfaces;

import gameobjects.Block;


/**
 * This is the interface of levels including all information a level should have.
 */
public interface LevelInformation extends Cloneable {


    /**
     * @return the blocks of aliens.
     */
    Block[][] getAliansBlocksblocks();

    /**
     * @return the paddle movement.
     */
    int paddleSpeed();

    /**
     * @return the paddle Width.
     */
    int paddleWidth();

    /**
     * @return the level name. it will be displayed at the top of the screen.
     */
    String levelName();

    /**
     * @return a sprite with the background of the level
     */
    Sprite getBackground();


    /**
     * @return Number of levels that should be removed before the level is considered to be "cleared".
     * This number should be <= blocks.size();
     */
    int numberOfBlocksToRemove();

    /**
     * @return the clone of the same level information, used for next round to build the level again.
     */
    LevelInformation clone();
}