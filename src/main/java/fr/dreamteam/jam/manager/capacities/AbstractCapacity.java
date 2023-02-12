package fr.dreamteam.jam.manager.capacities;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.manager.EpiPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractCapacity {
    protected final EpiPlayer player;
    private final BukkitRunnable runnable;
    private boolean active;
    private int slot;
    private int cooldown;
    private long last_usage;
    private final int runnableSpeed;

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
                onActive();
            }
        };
        this.active = false;
        this.slot = slot;
        this.cooldown = cooldown;
        this.runnableSpeed = updateSpeed;

        player.getPlayer().getInventory().setItem(slot, getItem());
    }

    public void switchStatus() {
        if (active)
            disable();
        else
            active();
    }

    public void active() {
        onUse();
        active = true;
        last_usage = System.currentTimeMillis();
        runnable.runTaskTimer(Main.getInstance(), 0, runnableSpeed);
    }

    public void disable() {
        if ((System.currentTimeMillis() - last_usage) < cooldown)
            return;
        onDisable();
        active = false;
    }

    public void remove() {
        disable();
        player.getPlayer().getInventory().setItem(slot, new ItemStack(Material.AIR));
        player.removeCapacity(this);
    }

    public abstract void onActive();
    public abstract void onUse();
    public abstract void onDisable();
    public abstract ItemStack getItem();

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
}
