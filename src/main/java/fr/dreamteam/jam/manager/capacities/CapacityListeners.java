package fr.dreamteam.jam.manager.capacities;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.manager.EpiPlayer;
import fr.dreamteam.jam.manager.GameManager;
import fr.dreamteam.jam.manager.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CapacityListeners implements Listener {
    @EventHandler
    public void onMoveCapacity(InventoryInteractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        GameManager manager = Main.getInstance().getGameManager(event.getPlayer());
        if (manager == null || manager.getGameState() != GameState.IN_GAME)
            return;
        EpiPlayer player = manager.getPlayers().stream()
                .filter(epiPlayer -> epiPlayer.getPlayer().equals(event.getPlayer())).findFirst().orElse(null);
        if (player == null)
            return;
        AbstractCapacity capacity = player.getCapacityBySlot(event.getPlayer().getInventory().getHeldItemSlot());
        if (capacity != null) {
            capacity.switchStatus();
        }
        event.setCancelled(true);
    }
}
