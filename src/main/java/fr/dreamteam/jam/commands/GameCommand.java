package fr.dreamteam.jam.commands;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.manager.GameManager;
import fr.dreamteam.jam.manager.GameState;
import fr.dreamteam.jam.manager.roles.Hunter;
import fr.dreamteam.jam.manager.roles.Role;
import fr.dreamteam.jam.manager.roles.Vampire;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (args.length < 1)
            return false;
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("list")) {
            int msgWidth = 45;
            String header = "Liste des parties";
            String separator = "-".repeat((int) ((msgWidth / 2f) - (header.length() / 2f)));
            header = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + separator + ChatColor.RESET + " " + ChatColor.GOLD + header + ChatColor.GRAY + " " + ChatColor.STRIKETHROUGH + separator + ChatColor.RESET;
            player.sendMessage(header);
            for (GameManager manager : Main.getInstance().getGameManagers()) {
                int secondsLeft = manager.getSecondsLeft();
                boolean isNegative = secondsLeft < 0;
                secondsLeft = Math.abs(secondsLeft);
                int minutesLeft = secondsLeft / 60;
                secondsLeft = secondsLeft % 60;
                player.sendMessage(ChatColor.GRAY + " - " + ChatColor.RESET + manager.getUuid());
                player.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RESET + manager.getGameState() + " (" + (isNegative ? "-": "") + minutesLeft + "m" + secondsLeft + "s left)");
                player.sendMessage(ChatColor.GRAY + "   - " + ChatColor.RED + "Vampires: " + ChatColor.RESET + manager.getVampires().size());
                for (Vampire vampire : manager.getVampires()) {
                    player.sendMessage(ChatColor.GRAY + "     - " + ChatColor.RESET + vampire.getPlayer().getName());
                }
                player.sendMessage(ChatColor.GRAY + "   - " + ChatColor.GREEN + "Chasseurs: " + ChatColor.RESET + manager.getHunters().size());
                for (Hunter hunter : manager.getHunters()) {
                    player.sendMessage(ChatColor.GRAY + "     - " + ChatColor.RESET + hunter.getPlayer().getName());
                }
            }
            if (Main.getInstance().getGameManagers().size() == 0) {
                String message = "Aucune partie en cours";
                String sep = " ".repeat((int) ((msgWidth / 2f) - (message.length() / 4f) - 1));
                message = ChatColor.GRAY + sep + " " + message  + " " + sep + ChatColor.RESET;
                player.sendMessage(message);
            }
            String footer = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-".repeat(msgWidth - 1);
            player.sendMessage(footer);
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            GameManager manager = new GameManager();
            Main.getInstance().getGameManagers().add(manager);

            player.sendMessage(ChatColor.GREEN + "Partie créée avec l'ID " + manager.getUuid());
            return true;
        }

        if (args[0].equalsIgnoreCase("join")) {
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Veuillez spécifier l'ID de la partie et votre rôle");
                return true;
            }
            if (Main.getInstance().getGameManager(player) != null) {
                player.sendMessage(ChatColor.RED + "Vous êtes déjà dans une partie");
                return true;
            }

            GameManager manager = Main.getInstance().getGameManagers().stream().filter(gameManager -> gameManager.getUuid().toString().equalsIgnoreCase(args[1])).findFirst().orElse(null);
            if (manager == null) {
                player.sendMessage(ChatColor.RED + "Aucune partie avec l'ID " + args[1]);
                return true;
            }
            if (manager.getGameState() != GameState.WAITING) {
                player.sendMessage(ChatColor.RED + "La partie est déjà en cours");
                return true;
            }
            if (args[2].equalsIgnoreCase("vampire")) {
                manager.addPlayer(player, Role.VAMPIRE);
                player.sendMessage(ChatColor.GREEN + "Vous avez rejoint la partie en tant que vampire");
            }
            else if (args[2].equalsIgnoreCase("hunter")) {
                manager.addPlayer(player, Role.HUNTER);
                player.sendMessage(ChatColor.GREEN + "Vous avez rejoint la partie en tant que chasseur");
            }
            else {
                player.sendMessage(ChatColor.RED + "Veuillez spécifier un rôle valide (vampire ou hunter)");
                return true;
            }

            return true;
        }

        return false;
    }

}
