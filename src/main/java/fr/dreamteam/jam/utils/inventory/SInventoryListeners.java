package fr.dreamteam.jam.utils.inventory;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.utils.inventory.button.Button;
import fr.dreamteam.jam.utils.inventory.button.ClickButton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class SInventoryListeners implements Listener {
    private static final Map<Inventory, SInventory> inventories = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.CHEST &&
                inventories.containsKey(event.getInventory())) {
            SInventory inventory = inventories.get(event.getClickedInventory());
            if (inventory != null) {
                event.setCancelled(!inventory.isStorage());
                if ((event.getClick() != ClickType.SHIFT_LEFT && event.getClick() != ClickType.SHIFT_RIGHT)
                    || event.getInventory() == event.getClickedInventory()) {
                    if (inventory.getButtons().containsKey(event.getSlot())) {
                        Button button = inventory.getButton(event.getSlot());
                        if (button instanceof ClickButton) {
                            event.setCancelled(true);
                            ((ClickButton) button).getConsumer().apply((Player) event.getWhoClicked(), inventory, event.getClick());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getType() == InventoryType.CHEST &&
                inventories.containsKey(event.getInventory())) {
            SInventory inventory = inventories.get(event.getInventory());
            if (inventory != null) {
                event.setCancelled(!inventory.isStorage());
                if (event.getType() == DragType.SINGLE) {
                    int slot = event.getInventorySlots().iterator().next();
                    if (inventory.getButtons().containsKey(slot)) {
                        Button button = inventory.getButton(slot);
                        if (button instanceof ClickButton) {
                            event.setCancelled(true);
                            ((ClickButton)button).getConsumer().apply((Player) event.getWhoClicked(), inventory, ClickType.LEFT);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.CHEST
                && inventories.containsKey(event.getInventory())) {
            SInventory inventory = inventories.get(event.getInventory());
            if (inventory != null
                    && inventory.getCloseConsumer() != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        inventory.getCloseConsumer().accept((Player) event.getPlayer(), inventory);
                    }
                }.runTaskAsynchronously(Main.getInstance());
            }
        }
    }

    public static Map<Inventory, SInventory> getInventories() {
        return inventories;
    }
}
