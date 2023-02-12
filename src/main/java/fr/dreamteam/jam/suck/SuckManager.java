package fr.dreamteam.jam.suck;

import fr.dreamteam.jam.manager.GameManager;
import org.bukkit.Location;

import java.util.List;
import java.util.Random;

public class SuckManager {
    public static SuckBed createSuckBedFrom(GameManager manager, List<Location> locations) {
        if (locations.isEmpty()) {
            return null;
        }
        return new SuckBed(manager, locations.remove(new Random().nextInt(locations.size())));
    }
}
