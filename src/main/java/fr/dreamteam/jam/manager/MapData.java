package fr.dreamteam.jam.manager;

import org.bukkit.Location;

import java.io.File;
import java.util.List;

public record MapData(File schematic, List<Location> spawnVampire, List<Location> spawnHunter) {

    public void generate(int offsetX, int offsetY, int offsetZ) {
        // apply all offset
        spawnVampire.forEach(location -> {
            location.setX(location.getX() + offsetX);
            location.setY(location.getY() + offsetY);
            location.setZ(location.getZ() + offsetZ);
        });
        spawnHunter.forEach(location -> {
            location.setX(location.getX() + offsetX);
            location.setY(location.getY() + offsetY);
            location.setZ(location.getZ() + offsetZ);
        });
    }
}
