package fr.dreamteam.jam.manager;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.utils.ConfigParser;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MapLoader {
    public static final List<MapData> maps = new ArrayList<>();

    public static void loadMaps() {
        File file = new File(Main.getInstance().getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        ConfigurationSection section = config.getConfigurationSection("maps");
        assert section != null;
        for (String key : section.getKeys(false)) {
            ConfigurationSection mapSection = section.getConfigurationSection(key);
            assert mapSection != null;
            File schematic = new File(Main.getInstance().getDataFolder(), Objects.requireNonNull(mapSection.getString("schematic")));
            World world = Main.getInstance().getServer().getWorlds().get(0);
            ConfigurationSection spawnSection = mapSection.getConfigurationSection("spawns");
            assert spawnSection != null;
            List<Location> spawnVampire = ConfigParser.parseLocationsFromConfig(world, (List<String>) spawnSection.getList("vampire"));
            List<Location> spawnHunter = ConfigParser.parseLocationsFromConfig(world, (List<String>) spawnSection.getList("hunter"));
            maps.add(new MapData(schematic, spawnVampire, spawnHunter));
        }
        generateMaps();
    }

    private static void generateMaps() {
        AtomicInteger offsetX = new AtomicInteger();
        maps.forEach(mapData -> {
            int offset = offsetX.getAndAdd(10000);
            Clipboard clipboard = SchematicsManager.loadSchematic(mapData.schematic());
            Location location = new Location(Main.getInstance().getServer().getWorld("world"), offset, 0, 0);
            SchematicsManager.pasteSchematic(clipboard, location, true);
            mapData.generate(offset, 0, 0);
        });
    }
}
