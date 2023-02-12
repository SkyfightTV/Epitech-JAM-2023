package fr.dreamteam.jam.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigParser {
    public static List<Location> parseLocationsFromConfig(World world, YamlConfiguration config) {
        return parseLocationsFromConfig(world, (ConfigurationSection) config);
    }

    public static List<Location> parseLocationsFromConfig(World world, ConfigurationSection config) {
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

}
