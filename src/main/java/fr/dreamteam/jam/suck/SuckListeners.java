package fr.dreamteam.jam.suck;

import fr.dreamteam.jam.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SuckListeners implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().name().contains("BED")) {
            // add player to suck bed
            Main.getInstance().getGameManager(event.getPlayer()).getSuckBeds()
                    .stream()
                    .filter(suckBed -> suckBed.getLocation().equals(event.getClickedBlock().getLocation()))
                    .findFirst()
                    .ifPresent(suckBed -> suckBed.suck(1));
        }
    }
}
