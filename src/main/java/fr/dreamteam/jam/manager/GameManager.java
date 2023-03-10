package fr.dreamteam.jam.manager;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.inventories.ShopInventory;
import fr.dreamteam.jam.manager.capacities.AbstractCapacity;
import fr.dreamteam.jam.manager.capacities.BatCapacity;
import fr.dreamteam.jam.manager.capacities.StakeCapacity;
import fr.dreamteam.jam.suck.SuckBed;
import fr.dreamteam.jam.manager.roles.Hunter;
import fr.dreamteam.jam.manager.roles.Role;
import fr.dreamteam.jam.manager.roles.Vampire;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.util.*;

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

    public MapData selectedMap;

    public GameManager() {
        this.gameState = GameState.WAITING;
        this.startTime = System.currentTimeMillis();
        this.createScoreboard();
        this.uuid = UUID.randomUUID();
        this.selectedMap = MapLoader.maps.get(new Random().nextInt(MapLoader.maps.size()));
    }

    public void addPlayer(Player player, Role role) {
        if (role == Role.VAMPIRE) {
            Vampire vampire = new Vampire(player);
            vampire.addCapacity(BatCapacity.class);
            this.vampires.add(vampire);
        } else if (role == Role.HUNTER) {
            Hunter hunter = new Hunter(player);
            hunter.addCapacity(StakeCapacity.class);
            this.hunters.add(hunter);
        }
        // Clear inventory
        player.getInventory().clear();
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
        // Disable movement for everyone
        this.getPlayers().forEach(epiPlayer -> {
            epiPlayer.getPlayer().setWalkSpeed(0);
            epiPlayer.getPlayer().setFlySpeed(0);
            // Disable jump
            epiPlayer.getPlayer().setAllowFlight(true);
            epiPlayer.getPlayer().setFlying(true);
            epiPlayer.getPlayer().setVelocity(new Vector(0, 0, 0));
        });
        // Show a countdown
        for (int i = 5; i > 0; i--) {
            int finalI = i;
            Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                this.vampires.forEach(vampire -> vampire.getPlayer().sendTitle("??c" + finalI, "Vous ??tes un " + ChatColor.RED + "" + ChatColor.BOLD + "vampire", 0, 20, 0));
                this.hunters.forEach(hunter -> hunter.getPlayer().sendTitle("??c" + finalI, "Vous ??tes un " + ChatColor.GREEN + "" + ChatColor.BOLD  + "chasseur", 0, 20, 0));
                }, (5 - i) * 20L);
        }
        Main.getInstance().getServer().getScheduler().runTaskTimer(Main.getInstance(), this, 0L, 5L);
    }

    private void createScoreboard() {
        ScoreboardManager scoreboardManager = Main.getInstance().getServer().getScoreboardManager();
        if (scoreboardManager == null)
            return;

        scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("gameState", "dummy", "??6??l??tat du jeu");
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
        objective.getScore("??6??l??tat du jeu").setScore(0);
        objective.getScore("??e" + this.gameState.name()).setScore(1);
        objective.getScore("??6??lTemps restant").setScore(2);
        objective.getScore("??e" + timeLeft + "s").setScore(3);

        for (EpiPlayer player : this.getPlayers()) {
            player.getPlayer().setScoreboard(scoreboard);
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
        return (int) (System.currentTimeMillis() - startTime - (gameState.duration * 1000)) / 1000;
    }

    @Override
    public void run() {
        int timeLeft = getSecondsLeft();
        // Check whether the actual state is ending

        if (timeLeft <= 0) {
            switch (gameState) {
                case STARTING -> {
                    this.setGameState(GameState.IN_GAME);
                    // Teleport everyone to their spawn
                    sendEveryoneToSpawn();
                    // Enable movement for everyone
                    for (EpiPlayer epiPlayer : this.getPlayers()) {
                        epiPlayer.getPlayer().setWalkSpeed(0.2f);
                        epiPlayer.getPlayer().setFlySpeed(0.2f);
                        // Enable jump
                        epiPlayer.getPlayer().setAllowFlight(false);
                        epiPlayer.getPlayer().setFlying(false);
                    }
                }
                case IN_GAME -> {
                    this.setGameState(GameState.INTERMISSION);
                    // Blindness for everyone
                    for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
                        player.addPotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS.createEffect(100, 1));
                    }
                    // Disable movement for everyone
                    for (EpiPlayer epiPlayer : this.getPlayers()) {
                        epiPlayer.getPlayer().setWalkSpeed(0);
                        epiPlayer.getPlayer().setFlySpeed(0);
                        // Disable jump
                        epiPlayer.getPlayer().setAllowFlight(true);
                        epiPlayer.getPlayer().setFlying(true);
                        epiPlayer.getPlayer().setVelocity(new Vector(0, 0, 0));
                    }
                    // Open shop for everyone
                    for (Vampire vampire : this.vampires) {
                        vampire.getPlayer().openInventory(ShopInventory.getInventor(vampire));
                    }
                    for (Hunter hunter : this.hunters) {
                        hunter.getPlayer().openInventory(ShopInventory.getInventor(hunter));
                    }
                }
                case INTERMISSION -> {
                    this.setGameState(GameState.IN_GAME);
                    // Remove blindness for everyone
                    for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
                        player.removePotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS);
                    }
                    // Close shop for everyone
                    for (EpiPlayer epiPlayer : this.getPlayers()) {
                        epiPlayer.getPlayer().closeInventory();
                    }
                    // Teleport everyone to their spawn
                    sendEveryoneToSpawn();
                    // Enable movement for everyone
                    for (EpiPlayer epiPlayer : this.getPlayers()) {
                        epiPlayer.getPlayer().setWalkSpeed(0.2f);
                        epiPlayer.getPlayer().setFlySpeed(0.1f);
                        // Enable jump
                        epiPlayer.getPlayer().setAllowFlight(false);
                        epiPlayer.getPlayer().setFlying(false);
                    }
                }
                case ENDING -> {
                    this.setGameState(GameState.WAITING);
                }
            }
        }

        updateScoreboard();
    }

    private void sendEveryoneToSpawn() {
        for (Vampire vampire : this.vampires) {
            vampire.getPlayer().teleport(this.selectedMap.spawnVampire().get(new Random().nextInt(this.selectedMap.spawnVampire().size())));
        }
        for (Hunter hunter : this.hunters) {
            hunter.getPlayer().teleport(this.selectedMap.spawnHunter().get(new Random().nextInt(this.selectedMap.spawnHunter().size())));
        }
        // Give everyone their items
        for (EpiPlayer epiPlayer : this.getPlayers()) {
            epiPlayer.getPlayer().getInventory().clear();
            epiPlayer.getPlayer().getInventory().setArmorContents(null);
            for (AbstractCapacity capacity : epiPlayer.getCapacities()) {
                epiPlayer.getPlayer().getInventory().setItem(capacity.getSlot(), capacity.getItem());
            }
        }
    }
}
