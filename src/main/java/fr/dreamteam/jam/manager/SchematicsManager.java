package fr.dreamteam.jam.manager;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class SchematicsManager {
    public static Clipboard loadSchematic(File file) {
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        assert format != null;
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            return reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void pasteSchematic(Clipboard clipboard, Location loc, boolean air) {
        World world = FaweAPI.getWorld(Objects.requireNonNull(loc.getWorld()).getName());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            editSession.setFastMode(true);
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                    .ignoreAirBlocks(!air)
                    .build();
            Operations.complete(operation);
        }
    }
}
