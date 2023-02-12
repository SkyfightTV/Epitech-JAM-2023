package fr.dreamteam.jam.utils.inventory;

import fr.dreamteam.jam.utils.inventory.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class SInventory {
    private final Inventory inventory;
    private final HashMap<Integer, Button> buttons;
    private BiConsumer<Player, SInventory> closeConsumer;
    private boolean storage;

    public SInventory(Inventory inventory, boolean storage, HashMap<Integer, Button> buttons, BiConsumer<Player, SInventory> closeConsumer) {
        this.inventory = inventory;
        this.buttons = buttons;
        this.closeConsumer = closeConsumer;
        this.storage = storage;
        SInventoryListeners.getInventories().put(inventory, this);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void remove() {
        this.inventory.getViewers().forEach(humanEntity -> remove((Player)humanEntity));
        SInventoryListeners.getInventories().remove(this.inventory);
    }

    public void remove(Player player) {
        player.closeInventory();
    }

    public HashMap<Integer, Button> getButtons() {
        return this.buttons;
    }

    public Button getButton(int slot) {
        return this.buttons.get(slot);
    }

    public void onClose(BiConsumer<Player, SInventory> closeConsumer) {
        this.closeConsumer = closeConsumer;
    }

    public BiConsumer<Player, SInventory> getCloseConsumer() {
        return closeConsumer;
    }

    public void canStore(boolean storage) {
        this.storage = storage;
    }

    public boolean isStorage() {
        return storage;
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
    }
    public static SInventoryBuilder builder() {
        return new SInventoryBuilder();
    }

    public static void init(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new SInventoryListeners(), plugin);
    }
}
