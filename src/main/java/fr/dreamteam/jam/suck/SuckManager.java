package fr.dreamteam.jam.suck;

import fr.dreamteam.jam.manager.GameManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuckManager {
    public static List<Location> parseLocationsFromConfig(World world, YamlConfiguration config) {
        List<Location> locations = new ArrayList<>();
        for (String key : config.getKeys(false)) {
            String[] split = key.split(":");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            int z = Integer.parseInt(split[2]);
            locations.add(new Location(world, x, y, z));
        }
        return locations;
    }

    public static SuckBed createSuckBedFrom(GameManager manager, List<Location> locations) {
        if (locations.isEmpty()) {
            return null;
        }
        return new SuckBed(manager, locations.remove(new Random().nextInt(locations.size())));
    }
}
