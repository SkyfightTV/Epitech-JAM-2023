package fr.dreamteam.jam.manager.capacities;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.manager.EpiPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractCapacity {
    private final EpiPlayer player;
    private final BukkitRunnable runnable;
    private boolean active;
    private int slot;
    private int cooldown;
    private long last_usage;
    private final int runnableSpeed;
    private final ItemStack item;

    public AbstractCapacity(EpiPlayer player, int updateSpeed, int slot, int cooldown) {
        this.player = player;
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!active || !player.getPlayer().isOnline()) {
                    if (active) {
                        disable();
                    }
                    cancel();
                    return;
                }
                onActive(player.getPlayer());
            }
        };
        this.active = false;
        this.slot = slot;
        this.cooldown = cooldown;
        this.runnableSpeed = updateSpeed;

        //Create Item
        item = getItem(player.getPlayer());
        player.getPlayer().getInventory().setItem(slot, item);
    }

    public void switchStatus() {
        if (active)
            disable();
        else
            active();
    }

    public void active() {
        onUse(player.getPlayer());
        active = true;
        last_usage = System.currentTimeMillis();
        runnable.runTaskTimer(Main.getInstance(), 0, runnableSpeed);
    }

    public void disable() {
        if ((System.currentTimeMillis() - last_usage) < cooldown)
            return;
        onDisable(player.getPlayer());
        active = false;
    }

    public void remove() {
        disable();
        player.getPlayer().getInventory().remove(item);
        player.removeCapacity(this);
    }

    public abstract void onActive(Player player);
    public abstract void onUse(Player player);
    public abstract void onDisable(Player player);
    public abstract ItemStack getItem(Player player);

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public ItemStack getItem() {
        return item;
    }
}
