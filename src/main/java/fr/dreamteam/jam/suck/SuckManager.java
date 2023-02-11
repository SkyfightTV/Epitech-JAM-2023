package fr.dreamteam.jam.suck;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

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

    public static SuckBed createSuckBedFrom(List<Location> locations) {
        return new SuckBed(locations.remove());
    }
}
