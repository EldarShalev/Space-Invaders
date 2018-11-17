package gameobjects;

import gamecontrol.GameLevel;
import interfaces.HitListener;

/**
 * a BlockRemover is in charge of removing blocks from the game, as well as keeping count of the number
 * of blocks that remain.
 */
public class BlockRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBlocks;
    private Block[][] blockslist = null;

    /**
     * Constructor.
     *
     * @param game          a game to init.
     * @param removedBlocks removed blocks.
     */
    public BlockRemover(GameLevel game, Counter removedBlocks) {
        this.game = game;
        this.remainingBlocks = removedBlocks;
    }

    /**
     * @param game          a game level.
     * @param removedBlocks the removed block counter.
     * @param blocks        the array of blocks.
     */
    public BlockRemover(GameLevel game, Counter removedBlocks, Block[][] blocks) {
        this.game = game;
        this.remainingBlocks = removedBlocks;
        this.blockslist = blocks;
    }

    /**
     * Blocks that are hit and reach 0 hit-points should be removed from the game.
     *
     * @param beingHit a block that was hit.
     * @param hitter   the ball the hit the block.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.getHitPoints() == 1) {
            if (blockslist != null) {
                remainingBlocks.decrease(1);
            }
            beingHit.removeHitListener(this);
            beingHit.removeFromGame(game);
        }

        // To remove only for aliens the num of blocks
        if (blockslist != null) {
            for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                    if (blockslist[i][j] == beingHit) {
                        blockslist[i][j].removeFromGame(game);
                    }
                }
            }
        }
    }

    /**
     * @return the remaining blocks.
     */
    public Counter getRemainingBlocks() {
        return remainingBlocks;
    }

    /**
     * @param blocksRemain a given counter to set for remaining blocks.
     */
    public void setRemainingBlocks(Counter blocksRemain) {
        this.remainingBlocks = blocksRemain;
    }

    /**
     * @return the game.
     */
    public GameLevel getGame() {
        return game;
    }
}