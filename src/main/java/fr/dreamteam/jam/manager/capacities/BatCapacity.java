package fr.dreamteam.jam.manager.capacities;

import fr.dreamteam.jam.manager.EpiPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@IsVampireCapacity
public class BatCapacity extends AbstractCapacity {
    private static final int SLOT = 0;
    private static final int COOLDOWN = 10;
    private static final int UPDATE_SPEED = 0;

    private Bat bat;

    public BatCapacity(EpiPlayer player) {
        super(player, UPDATE_SPEED, SLOT, COOLDOWN);
    }

    @Override
    public void onActive() {
        Player player = this.player.getPlayer();
        if (!player.isOnline()) {
            disable();
            return;
        }
        bat.teleport(player);
    }

    @Override
    public void onUse() {
        Player player = this.player.getPlayer();
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 255, false, false));
        player.setAllowFlight(true);
        player.setFlying(true);

        bat = player.getWorld().spawn(player.getLocation().add(0, 0.5, 0), Bat.class);
        bat.setInvulnerable(true);
        bat.setAI(true);
        bat.setCollidable(false);
    }

    @Override
    public void onDisable() {
        Player player = this.player.getPlayer();
        player.setAllowFlight(false);
        player.setFlying(false);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        bat.remove();
    }

    @Override
    public ItemStack getItem() {
        Player player = this.player.getPlayer();
        final ItemStack item = new ItemStack(Material.BAT_SPAWN_EGG);
        return item;
    }

    @Override
    public int getPrice() {
        return 0;
    }
}
