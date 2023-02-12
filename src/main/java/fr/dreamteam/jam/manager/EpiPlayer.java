package fr.dreamteam.jam.manager;

import fr.dreamteam.jam.manager.capacities.AbstractCapacity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EpiPlayer {

    private final Player player;
    private final List<AbstractCapacity> capacities;

    public int currency;

    public EpiPlayer(Player player) {
        this.player = player;
        this.capacities = new ArrayList<>();
    }

    public void addCapacity(AbstractCapacity capacity) {
        capacities.add(capacity);
    }

    public void removeCapacity(AbstractCapacity capacity) {
        capacities.remove(capacity);
    }

    public void addCapacity(Class<? extends AbstractCapacity> clazz) {
        try {
            capacities.add(clazz.getConstructor(EpiPlayer.class).newInstance(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeCapacity(Class<? extends AbstractCapacity> clazz) {
        capacities.removeIf(clazz::isInstance);
    }

    public boolean hasCapacity(Class<? extends AbstractCapacity> clazz) {
        return capacities.stream().anyMatch(clazz::isInstance);
    }

    public boolean hasCapacity(AbstractCapacity capacity) {
        return hasCapacity(capacity.getClass());
    }

    public List<AbstractCapacity> getCapacities() {
        return capacities;
    }

    public AbstractCapacity getCapacityBySlot(int slot) {
        return capacities.stream().filter(capacity -> capacity.getSlot() == slot).findFirst().orElse(null);
    }

    public Player getPlayer() {
        return this.player;
    }
}
