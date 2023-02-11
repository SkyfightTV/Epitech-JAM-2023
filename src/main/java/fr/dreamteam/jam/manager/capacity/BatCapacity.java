package fr.dreamteam.jam.manager.capacity;

import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BatCapacity extends AbstractCapacity {

    public BatCapacity(Player player, int updateSpeed, boolean active, int slot, int cooldown) {
        super(player, updateSpeed, active, slot, cooldown);
    }

    @Override
    public void onActive(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 255, false, false));
        player.setAllowFlight(true);
        player.setFlying(true);

        Bat bat = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0), Bat.class);
        bat.setInvulnerable(true);
        bat.setAI(true);
        bat.setCollidable(false);
        // add bat to variable
    }

    @Override
    public void onUse(Player player) {
        if (!player.isOnline()) {
            disable(player);
            return;
        }
        // teleport bat to player
    }

    @Override
    public void onDisable(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        // remove bat
    }
}
