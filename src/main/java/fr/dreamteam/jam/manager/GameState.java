package fr.dreamteam.jam.manager;

public enum GameState {

    WAITING(-1),
    STARTING(10),
    IN_GAME(5*60),
    INTERMISSION(1 * 60),
    ENDING(10);

    /**
     * The duration of the state in seconds
     */
    public final int duration;

    /**
     * The default constructor
     * @param duration The duration of the state in seconds
     */
    GameState(int duration) {
        this.duration = 0;
    }

}
