package fr.dreamteam.jam.manager;

import fr.dreamteam.jam.Main;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameManager implements Runnable {

    private GameState gameState;
    private final List<EpiPlayer> players = new ArrayList<>();
    private long startTime;
    private Scoreboard scoreboard;

    public GameManager() {
        this.gameState = GameState.WAITING;
        this.startTime = System.currentTimeMillis();
        this.createScoreboard();
    }

    public void addPlayer(EpiPlayer epiPlayer) {
        this.players.add(epiPlayer);
    }

    public void removePlayer(EpiPlayer epiPlayer) {
        this.players.remove(epiPlayer);
    }

    public List<EpiPlayer> getPlayers() {
        return this.players;
    }

    public boolean isPlayerInGame(Player player) {
        for (EpiPlayer epiPlayer : this.players) {
            if (epiPlayer.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    private void setGameState(GameState gameState) {
        this.startTime = System.currentTimeMillis();
        this.gameState = gameState;
    }

    public void startGame() {
        this.setGameState(GameState.STARTING);
    }

    private void createScoreboard() {
        ScoreboardManager scoreboardManager = Main.getInstance().getServer().getScoreboardManager();
        if (scoreboardManager == null)
            return;

        scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("gameState", "dummy", "§6§lÉtat du jeu");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    private void updateScoreboard() {
        ScoreboardManager scoreboardManager = Main.getInstance().getServer().getScoreboardManager();
        if (scoreboardManager == null)
            return;

        scoreboardManager.getMainScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        Objective objective = scoreboard.getObjective("gameState");
        if (objective == null)
            return;

        int timeLeft = (int) (this.startTime + (this.gameState.duration * 1000) - System.currentTimeMillis()) / 1000;
        objective.getScore("§6§lÉtat du jeu").setScore(0);
        objective.getScore("§e" + this.gameState.name()).setScore(1);
        objective.getScore("§6§lTemps restant").setScore(2);
        objective.getScore("§e" + timeLeft + "s").setScore(3);

        for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    @Override
    public void run() {
        int timeLeft = (int) (this.startTime + (this.gameState.duration * 1000) - System.currentTimeMillis()) / 1000;
        // Check whether the actual state is ending

        if (timeLeft <= 0) {
            switch (gameState) {
                case STARTING -> this.setGameState(GameState.IN_GAME);
                case IN_GAME -> this.setGameState(GameState.INTERMISSION);
                case INTERMISSION -> {
                    this.setGameState(GameState.ENDING);
                }
                case ENDING -> {
                    this.setGameState(GameState.WAITING);
                    // TODO Kick all players
                }
            }
        }

        updateScoreboard();
    }
}
