package gamecontrol;

import biuoop.DrawSurface;

import gameobjects.Block;
import gameobjects.Constants;
import gameobjects.Counter;
import gameobjects.ScoreTrackingListener;
import geometry.Point;
import geometry.Rectangle;
import interfaces.Sprite;


import java.util.Random;

/**
 * This class if for the formation of all the aliens.
 */
public class GroupOfAlians implements Sprite {

    private Block[][] blocks;
    private boolean left;
    private double speed;
    private Double startingMovingInterval;
    private static final Double SHOOTING_INTERVAL = 0.5;
    private Double timePassedShooting = 0.0;
    private Boolean aliensWon = false;


    /**
     * @param blocks1          a given array of blocks.
     * @param gameLevel        a given game level.
     * @param startingInterval a given starting interval for changing speed.
     * @param score            score.
     */
    public GroupOfAlians(Block[][] blocks1, GameLevel gameLevel, Double startingInterval, Counter score) {
        ScoreTrackingListener scoreTrackingListener = new ScoreTrackingListener(score);
        this.blocks = blocks1;
        this.left = false;
        this.startingMovingInterval = startingInterval;
        this.speed = startingInterval;
    }

    /**
     * Draw the sprite to the screen.
     *
     * @param d a given surface.
     */
    public void drawOn(DrawSurface d) {

    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt the amount of seconds passed since the last call.
     */
    public void timePassed(double dt) {
        double moveX;
        double moveY = 20;
        boolean shouldChangeDirection = false;
        // If we move left
        if (left) {
            // Check If we hit the wall
            for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                    if (this.blocks[i][j].getHitPoints() > 0) {
                        if (this.blocks[i][j].getRectangle().getUpperLeft().getX() < 40) {
                            shouldChangeDirection = true;
                            left = false;
                            break;
                        }
                    }
                }
            }
            // If we should change direction to right
            if (shouldChangeDirection) {
                this.speed = speed * 1.1;
                Block bottomAlien = getMostBottom();
                // Checking in case we hit the shields
                if (bottomAlien != null && bottomAlien.getRectangle().getUpperLeft().getY()
                        + bottomAlien.getRectangle().getHeight() + 20 > Constants.SHIELDS_HEIGHT) {
                    aliensWon = true;
                }
                for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                    for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                        if (this.blocks[i][j].getHitPoints() > 0) {
                            double currentX = this.blocks[i][j].getRectangle().getUpperLeft().getX();
                            double currentY = this.blocks[i][j].getRectangle().getUpperLeft().getY();
                            this.blocks[i][j].setRect(new Rectangle(new Point(currentX + 5, currentY + moveY)
                                    , 40, 30));
                        }

                    }
                }
                // We shouldn't change direction - keep moving
            } else {
                moveX = speed / 3;
                for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                    for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                        if (this.blocks[i][j].getHitPoints() > 0) {
                            double currentX = this.blocks[i][j].getRectangle().getUpperLeft().getX();
                            double currentY = this.blocks[i][j].getRectangle().getUpperLeft().getY();
                            this.blocks[i][j].setRect(new Rectangle(new Point(currentX - moveX, currentY)
                                    , 40, 30));
                        }
                    }
                }
            }
            // Else move right
        } else {
            shouldChangeDirection = false;
            // Checking if we hit the wall
            for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                    if (this.blocks[i][j].getHitPoints() > 0) {
                        if (this.blocks[i][j].getRectangle().getUpperLeft().getX() > 740) {
                            shouldChangeDirection = true;
                            left = true;
                            break;
                        }
                    }
                }
            }
            // If we did hit the wall to the right
            if (shouldChangeDirection) {
                this.speed = speed * 1.1;
                Block bottomAlien = getMostBottom();
                // Checking in case we hit the shields
                if (bottomAlien != null && bottomAlien.getRectangle().getUpperLeft().getY()
                        + bottomAlien.getRectangle().getHeight() + 20 > Constants.SHIELDS_HEIGHT) {
                    aliensWon = true;

                }
                for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                    for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                        if (this.blocks[i][j].getHitPoints() > 0) {
                            double currentX = this.blocks[i][j].getRectangle().getUpperLeft().getX();
                            double currentY = this.blocks[i][j].getRectangle().getUpperLeft().getY();
                            this.blocks[i][j].setRect(new Rectangle(new Point(currentX - 5, currentY + moveY)
                                    , 40, 30));
                        }
                    }

                }

                //  just move right
            } else {
                moveX = speed / 3;
                for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                    for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                        if (this.blocks[i][j].getHitPoints() > 0) {
                            double currentX = this.blocks[i][j].getRectangle().getUpperLeft().getX();
                            double currentY = this.blocks[i][j].getRectangle().getUpperLeft().getY();
                            this.blocks[i][j].setRect(new Rectangle(new Point(currentX + moveX, currentY)
                                    , 40, 30));
                        }
                    }
                }
            }
        }
        // Time passed for another shoot
        timePassedShooting += dt;
        // Checking if we can shoot
        if (timePassedShooting > SHOOTING_INTERVAL) {
            Random random = new Random();
            int rand = random.nextInt(10);
            // New array for most bottom alien who can shoot
            int[][] array = new int[10][2];
            for (int i = 0; i < Constants.ALIANS_COLUMNS; i++) {
                array[i][0] = 0;
            }
            // Checking the most bottom
            for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                    if (this.blocks[i][j].getHitPoints() > 0) {
                        array[j][0] = 1;
                        array[j][1] = i;
                    }
                }
            }
            // Creating shoot if the block exists
            while (true) {
                if (array[rand][0] != 0) {
                    int index = array[rand][1];
                    this.blocks[index][rand].shoot();
                    timePassedShooting = 0.0;
                    break;
                } else {
                    rand = random.nextInt(10);
                }
            }
        }
    }

    /**
     * add the object to game.
     *
     * @param g a gamecontrol.GameLevel.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }


    /**
     * @return the blocks.
     */
    public Block[][] getBlocks() {
        return blocks;
    }

    /**
     * restart the alians.
     */
    public void backToStartingPosition() {
        int column = 0;
        boolean breakFromLoop = false;
        Boolean emptyColumn = true;
        Integer emptyColumnsOnTheLeft = 0;
        // Get the blocks to their starting position by calculating how much total columns we already deleted
        for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
            for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
                if (blocks[i][column].getHitPoints() > 0) {
                    breakFromLoop = true;
                    break;
                }
            }
            if (breakFromLoop) {
                break;
            } else if (emptyColumn) {
                // Adding to the counter the num of columns
                emptyColumnsOnTheLeft++;
            }
            column++;
        }
        // Set the blocks
        for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
            for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                if (blocks[i][j].getHitPoints() > 0) {
                    Point startUpperLeft = blocks[i][j].getStartingUpperLeft().clone();
                    startUpperLeft.setX(startUpperLeft.getX() - 60 * emptyColumnsOnTheLeft);
                    startUpperLeft.setY(startUpperLeft.getY());
                    blocks[i][j].getRectangle().setUpperLeft(startUpperLeft);
                }
            }
        }
        // Alians won =false & set the speed to the normal
        aliensWon = false;
        speed = startingMovingInterval;
    }


    /**
     * Gets aliens won.
     *
     * @return the aliens won
     */
    public Boolean getAliensWon() {
        return aliensWon;
    }

    /**
     * @return the most bottom block from the aliens.
     */
    public Block getMostBottom() {
        int indexI = 0;
        int indexJ = 0;
        double maxHeightY = 0;
        for (int i = 0; i < Constants.ALIANS_ROWS; i++) {
            for (int j = 0; j < Constants.ALIANS_COLUMNS; j++) {
                if (this.blocks[i][j].getHitPoints() > 0) {
                    if (this.blocks[i][j].getRectangle().getBot().start().getY() > maxHeightY) {
                        maxHeightY = this.blocks[i][j].getRectangle().getBot().start().getY();
                        indexI = i;
                        indexJ = j;
                    }
                }
            }
        }
        return blocks[indexI][indexJ];
    }
}
