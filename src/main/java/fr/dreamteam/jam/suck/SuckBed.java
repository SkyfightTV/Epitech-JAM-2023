package fr.dreamteam.jam.suck;

import fr.dreamteam.jam.manager.GameManager;
import fr.dreamteam.jam.manager.roles.Role;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Villager;

import java.util.Objects;

public class SuckBed {
    private static final int MAX_BLOOD = 5;

    private final GameManager manager;
    private final Location location;
    private final Villager villager;
    private double blood;

    public SuckBed(GameManager manager, Location location) {
        this.manager = manager;
        this.location = location;
        this.blood = MAX_BLOOD;
        Villager villager = Objects.requireNonNull(location.getWorld()).spawn(location, Villager.class);
        villager.setAI(false);
        villager.setSilent(true);
        villager.setCollidable(false);
        villager.setBaby();
        villager.sleep(location);
        this.villager = villager;
    }

    public void suck(double amount) {
        if (this.blood > 0) {
            this.blood -= amount;
        } else {
            dead();
        }
    }

    public void dead() {
        this.blood = 0;
        this.villager.remove();
        this.manager.endGame(Role.HUNTER);
    }

    public Location getLocation() {
        return location;
    }

    public Villager getVillager() {
        return villager;
    }

    public double getBlood() {
        return blood;
    }
}
