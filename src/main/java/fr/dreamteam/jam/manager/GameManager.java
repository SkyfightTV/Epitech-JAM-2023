package fr.dreamteam.jam.manager;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.manager.capacities.BatCapacity;
import fr.dreamteam.jam.suck.SuckBed;
import fr.dreamteam.jam.manager.roles.Hunter;
import fr.dreamteam.jam.manager.roles.Role;
import fr.dreamteam.jam.manager.roles.Vampire;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameManager implements Runnable {

    private GameState gameState;
    private final List<SuckBed> suckBeds = new ArrayList<>();
    private final List<Vampire> vampires = new ArrayList<>();
    private final List<Hunter> hunters = new ArrayList<>();
    private long startTime;
    private Scoreboard scoreboard;
    private UUID uuid;

    public int attackerWin = 0;
    public int defenderWin = 0;

    public GameManager() {
        this.gameState = GameState.WAITING;
        this.startTime = System.currentTimeMillis();
        this.createScoreboard();
        this.uuid = UUID.randomUUID();
    }

    public void addPlayer(Player player, Role role) {
        if (role == Role.VAMPIRE) {
            Vampire vampire = new Vampire(player);
            vampire.addCapacity(BatCapacity.class);
            this.vampires.add(vampire);
        } else if (role == Role.HUNTER) {
            this.hunters.add(new Hunter(player));
        }
    }

    public void removePlayer(Player player) {
        this.vampires.removeIf(vampire -> vampire.getPlayer().equals(player));
        this.hunters.removeIf(hunter -> hunter.getPlayer().equals(player));
    }

    public List<EpiPlayer> getPlayers() {
        List<EpiPlayer> players = new ArrayList<>();
        players.addAll(this.vampires);
        players.addAll(this.hunters);
        return players;
    }

    public List<Vampire> getVampires() {
        return this.vampires;
    }

    public List<Hunter> getHunters() {
        return this.hunters;
    }

    public List<SuckBed> getSuckBeds() {
        return suckBeds;
    }

    public boolean isPlayerInGame(Player player) {
        return this.getPlayers().stream().anyMatch(epiPlayer -> epiPlayer.getPlayer().equals(player));
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

    public void stopGame() {
        this.setGameState(GameState.ENDING);
    }

    public void endGame(Role winner) {
        this.setGameState(GameState.INTERMISSION);
        // TODO Manage end game
    }

    public void resetGame() {
        this.setGameState(GameState.WAITING);
    }

    public void kickPlayer(EpiPlayer epiPlayer) {
        // TODO Manage return to lobby
    }

    public void kickAllPlayers() {
        for (EpiPlayer epiPlayer : this.getPlayers()) {
            this.kickPlayer(epiPlayer);
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public int getSecondsLeft() {
        return (int) (this.startTime + (this.gameState.duration * 1000) - System.currentTimeMillis()) / 1000;
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
