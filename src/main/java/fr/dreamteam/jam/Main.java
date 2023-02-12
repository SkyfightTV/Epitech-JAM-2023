package fr.dreamteam.jam;

import fr.dreamteam.jam.commands.GameCommand;
import fr.dreamteam.jam.commands.tab.GameCompleter;
import fr.dreamteam.jam.manager.GameManager;
import fr.dreamteam.jam.manager.MapLoader;
import fr.dreamteam.jam.manager.capacities.CapacityListeners;
import fr.dreamteam.jam.utils.inventory.SInventory;
import fr.dreamteam.jam.utils.inventory.SInventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {
    private static Main instance;
    private static List<GameManager> gameManagers = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("game").setExecutor(new GameCommand());
        getCommand("game").setTabCompleter(new GameCompleter());
        Bukkit.getPluginManager().registerEvents(new CapacityListeners(), this);
        SInventory.init(this);
        MapLoader.loadMaps();
    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance() {
        return instance;
    }

    /**
     * Get a game through its UUID
     * @param uuid The uuid of the game you want to get
     * @return The game if it exists, null otherwise
     * @author axel eckenberg
     */
    public GameManager getGameManager(UUID uuid) {
        return gameManagers.stream().filter(gameManager -> gameManager.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    /**
     * Get a game where a player is in
     * @param player The player you want to get the game from
     * @return The game if it exists, null otherwise
     * @author axel eckenberg
     */
    public GameManager getGameManager(Player player) {
        return gameManagers.stream().filter(gameManager -> gameManager.isPlayerInGame(player)).findFirst().orElse(null);
    }

    public List<GameManager> getGameManagers() {
        return gameManagers;
    }
}
