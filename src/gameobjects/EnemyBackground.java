package gameobjects;

import gamecontrol.GameLevel;

import interfaces.Sprite;
import biuoop.DrawSurface;

import java.awt.Color;


/**
 * This background is for level 1.
 */
public class EnemyBackground implements Sprite {

    // Members
    private Color color;

    /**
     * Constructor for background 1.
     *
     * @param color a given color to the background.
     */
    public EnemyBackground(Color color) {
        this.color = color;
    }

    /**
     * Draw the sprite to the screen.
     *
     * @param d a given surface.
     */
    public void drawOn(DrawSurface d) {
        d.setColor(color);
        d.fillRectangle(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);


    }

    /**
     * notify the sprite that time has passed.
     *
     * @param dt - definition of time
     */
    public void timePassed(double dt) {
        // Currently- nothing
    }

    /**
     * add the object to game.
     *
     * @param g a gamecontrol.GameLevel.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
    }
}
