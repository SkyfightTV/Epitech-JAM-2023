package fr.dreamteam.jam.commands.tab;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.manager.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class GameCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return new ArrayList<>();

        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("create");
            list.add("join");
            list.add("leave");
            list.add("start");
            list.add("stop");
            list.add("list");
            return list;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("join")) {
                List<String> list = new ArrayList<>();
                for (GameManager manager : Main.getInstance().getGameManagers()) {
                    list.add(manager.getUuid().toString());
                }
                return list;
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("join")) {
                List<String> list = new ArrayList<>();
                list.add("hunter");
                list.add("vampire");
                return list;
            }
        }

        return new ArrayList<>();
    }
}
