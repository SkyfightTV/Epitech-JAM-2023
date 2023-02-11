package fr.dreamteam.jam.transformation;

import fr.dreamteam.jam.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerTransformation {
    public static HashMap<Player, Entity> transformedPlayers;

    static {
        transformedPlayers = new HashMap<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            List<Player> playersToRemove = new ArrayList<>();
            for (Player player : transformedPlayers.keySet()) {
                if (!player.isOnline()) {
                    playersToRemove.add(player);
                    continue;
                }
                transformedPlayers.get(player).teleport(player.getLocation().add(0, 0.5, 0));
            }
            for (Player player : playersToRemove) {
                unTransform(player);
            }
        }, 0, 0);
    }

    public static void switchTransform(Player player) {
        if (transformedPlayers.containsKey(player)) {
            unTransform(player);
        } else {
            transform(player);
        }
    }

    public static void transform(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 255, false, false));
        player.setAllowFlight(true);
        player.setFlying(true);

        Bat bat = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0), Bat.class);
        bat.setInvulnerable(true);
        bat.setAI(true);
        bat.setCollidable(false);
        transformedPlayers.put(player, bat);
    }

    public static void unTransform(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        transformedPlayers.get(player).remove();
        transformedPlayers.remove(player);
    }

}
