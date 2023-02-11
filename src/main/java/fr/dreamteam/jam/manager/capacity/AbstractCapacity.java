package fr.dreamteam.jam.manager.capacity;

import fr.dreamteam.jam.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractCapacity {
    private BukkitRunnable runnable;
    private boolean active;
    private int slot;
    private int cooldown;
    private int runnableSpeed;

    public AbstractCapacity(Player player, int updateSpeed, boolean active, int slot, int cooldown) {
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                onUse(player);
            }
        };
        this.active = active;
        this.slot = slot;
        this.cooldown = cooldown;
        this.runnableSpeed = updateSpeed;
    }

    public void active(Player player) {
        onActive(player);
        runnable.runTaskTimer(Main.getInstance(), 0, runnableSpeed);
    }

    public void disable(Player player) {
        onDisable(player);
        runnable.cancel();
    }

    public abstract void onActive(Player player);
    public abstract void onUse(Player player);
    public abstract void onDisable(Player player);

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
