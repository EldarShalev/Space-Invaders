package gameobjects;

import gamecontrol.GameLevel;
import geometry.Rectangle;
import geometry.Point;

import interfaces.Collidable;
import interfaces.Sprite;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;


import java.awt.Color;

/**
 * This class creates the paddle which can move inside the game.
 */
public class Paddle implements Sprite, Collidable {
    //members and constants.
    private int width;
    private int movementSpeed;
    private Rectangle rect;
    private biuoop.KeyboardSensor keyboard;
    private Point paddleStartingPosition;
    private Double shootingInterval = 0.0;
    private GameLevel gameLevel;
    private Boolean isAlreadyPressed = true;
    private Boolean lost = false;


    /**
     * The constructor of paddle.
     *
     * @param keyboardSensor a given keyboard.
     * @param width          a given width.
     * @param movementSpeed  a given movement speed.
     * @param game           a given game level.
     */
    public Paddle(KeyboardSensor keyboardSensor, int width, int movementSpeed, GameLevel game) {
        this.width = width;
        this.movementSpeed = movementSpeed;
        this.paddleStartingPosition = new Point((Constants.GAME_WIDTH - width) / 2,
                Constants.GAME_HEIGHT - Constants.PADDLE_HEIGHT - 20);
        this.rect = new Rectangle(paddleStartingPosition, width, Constants.PADDLE_HEIGHT);
        this.keyboard = keyboardSensor;
        this.gameLevel = game;

    }

    /**
     * @return the keyboard.
     */
    public KeyboardSensor getKeyboard() {
        return this.keyboard;
    }

    /**
     * Moving left.
     *
     * @param dt - definition of time.
     */
    public void moveLeft(double dt) {
        if (rect.getUpperLeft().getX() > 20) {
            this.rect = getNextRectangleLeft(dt);
        } else {
            this.rect = rect;
        }
    }

    /**
     * Moving Right.
     *
     * @param dt - definition of time.
     */
    public void moveRight(double dt) {
        if (rect.getUpperLeft().getX() + this.width + 20 < Constants.GAME_WIDTH) {
            this.rect = getNextRectangleRight(dt);
        } else {
            this.rect = rect;
        }
    }

    /**
     * @param dt - definition of time.
     * @return the next rectangle to the right.
     */
    public Rectangle getNextRectangleRight(double dt) {
        return (new Rectangle(new Point(rect.getUpperLeft().getX() + this.movementSpeed * dt,
                rect.getUpperLeft().getY()), this.width, Constants.PADDLE_HEIGHT));
    }

    /**
     * @param dt - definition of time.
     * @return the next rectangle to the left.
     */
    public Rectangle getNextRectangleLeft(double dt) {
        return new Rectangle(new Point(rect.getUpperLeft().getX() - this.movementSpeed * dt,
                rect.getUpperLeft().getY()), this.width, Constants.PADDLE_HEIGHT);
    }


    // interfaces.Sprite

    /**
     * checking what is the next step - if need to move the paddle left,right or do nothing.
     *
     * @param dt - definition of time.
     */
    public void timePassed(double dt) {
        if (keyboard.isPressed(keyboard.LEFT_KEY)) {
            moveLeft(dt);
        } else if (keyboard.isPressed(keyboard.RIGHT_KEY)) {
            moveRight(dt);
        }

        if (keyboard.isPressed(KeyboardSensor.SPACE_KEY) && !isAlreadyPressed) {
            this.isAlreadyPressed = true;
        } else if (!keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
            this.isAlreadyPressed = false;
        } else if (shootingInterval >= 0.35) {
            if (isAlreadyPressed) {
                shoot();
            }
            shootingInterval = 0.0;
        }
        shootingInterval += dt;

    }

    /**
     * Drawing the paddle on the game.
     *
     * @param d a given surface.
     */
    public void drawOn(DrawSurface d) {
        d.setColor(Color.yellow);
        d.fillRectangle((int) this.rect.getUpperLeft().getX(), (int) this.rect.getUpperLeft().getY(),
                this.width, Constants.PADDLE_HEIGHT);
    }

    // interfaces.Collidable

    /**
     * @return the rectangle of the paddle.
     */
    public Rectangle getRectangle() {
        return this.rect;
    }

    /**
     * @return the start position of the paddle.
     */
    public Point paddleLeftPointStarting() {
        return this.paddleStartingPosition;
    }

    /**
     * Setting the paddle in the starting rectangle.
     */
    public void setStartingRectangle() {
        this.rect = new Rectangle(paddleStartingPosition, width, Constants.PADDLE_HEIGHT);
    }

    /**
     * @param ball            a given ball.
     * @param collisionPoint  - The point that the ball is going to intersect with the paddle.
     * @param currentVelocity The velocity of the ball.
     * @return the new velocity according to the way the ball is going to intersect with (from top, bot,right, ot left).
     */
    public Velocity hit(Ball ball, Point collisionPoint, Velocity currentVelocity) {
        this.lost = true;
        ball.removeFromGame(gameLevel);
        return currentVelocity;
    }


    /**
     * Adding the paddle to the game.
     *
     * @param g a given game.
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }

    /**
     * A method that cause the paddle to shoot.
     */
    public void shoot() {
        Point middlePaddle = new Point(this.getRectangle().getTop().middle().getX()
                , this.getRectangle().getTop().middle().getY() - 20);
        Ball shoot = new Ball(middlePaddle, 3, Color.white, this.gameLevel.getEnvironment());
        Velocity v1 = Velocity.fromAngleAndSpeed(0, 400);
        shoot.setVelocity(v1);
        shoot.addToGame(gameLevel);
        shoot.addHitListener(gameLevel.getBallRemover());
        gameLevel.addBall(shoot);
    }

    /**
     * @return if paddle was hit or not.
     */
    public Boolean paddleWasHit() {
        return this.lost;
    }

    /**
     * Sets spaceship lost.
     *
     * @param isLost the spaceship lost
     */
    public void setPaddleLost(Boolean isLost) {
        this.lost = isLost;
    }
}
