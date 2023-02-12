package fr.dreamteam.jam.manager.capacities;

import fr.dreamteam.jam.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractCapacity {
    private final Player player;
    private BukkitRunnable runnable;
    private boolean active;
    private int slot;
    private int cooldown;
    private int runnableSpeed;

    public AbstractCapacity(Player player, int updateSpeed, int slot, int cooldown) {
        this.player = player;
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!active || !player.isOnline()) {
                    if (active) {
                        disable();
                    }
                    cancel();
                    return;
                }
                onActive(player);
            }
        };
        this.active = false;
        this.slot = slot;
        this.cooldown = cooldown;
        this.runnableSpeed = updateSpeed;
    }

    public void active() {
        onUse(player);
        active = true;
        runnable.runTaskTimer(Main.getInstance(), 0, runnableSpeed);
    }

    public void disable() {
        onDisable(player);
        active = false;
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
