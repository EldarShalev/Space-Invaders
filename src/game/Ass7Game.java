package game;


import gamecontrol.GameFlow;

/**
 * game.Ass7Game class to start the game.
 */
public class Ass7Game {
    /**
     * Start the game.
     *
     * @param args a given levels to run according to the insertion.
     */
    public static void main(String[] args) {
        GameFlow gameFlow = new GameFlow();
        gameFlow.startGame();
    }

}



