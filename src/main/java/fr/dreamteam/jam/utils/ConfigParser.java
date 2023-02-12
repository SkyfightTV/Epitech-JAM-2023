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
        return parseLocationsFromConfig(world, (List<String>) config.getKeys(false));
    }

    public static List<Location> parseLocationsFromConfig(World world, List<String> config) {
        List<Location> locations = new ArrayList<>();
        config.forEach(o -> {
            String[] split = o.split(":");
            locations.add(new Location(world, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2])));
        });
        return locations;
    }

}
