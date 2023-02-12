package fr.dreamteam.jam.manager.capacities;

import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@IsVampireCapacity
public class BatCapacity extends AbstractCapacity {
    private static final int SLOT = 0;
    private static final int COOLDOWN = 10;
    private static final int UPDATE_SPEED = 0;


    public BatCapacity(Player player) {
        super(player, UPDATE_SPEED, SLOT, COOLDOWN);
    }

    @Override
    public void onActive(Player player) {
        if (!player.isOnline()) {
            disable();
            return;
        }
        // add bat to variable
    }

    @Override
    public void onUse(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 255, false, false));
        player.setAllowFlight(true);
        player.setFlying(true);

        Bat bat = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0), Bat.class);
        bat.setInvulnerable(true);
        bat.setAI(true);
        bat.setCollidable(false);
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
