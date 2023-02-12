package fr.dreamteam.jam.inventories;

import fr.dreamteam.jam.Main;
import fr.dreamteam.jam.manager.EpiPlayer;
import fr.dreamteam.jam.manager.capacities.AbstractCapacity;
import fr.dreamteam.jam.manager.capacities.IsHunterCapacity;
import fr.dreamteam.jam.manager.capacities.IsVampireCapacity;
import fr.dreamteam.jam.manager.roles.Vampire;
import fr.dreamteam.jam.utils.inventory.SInventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ShopInventory {

    public static <T extends EpiPlayer> Inventory getInventor(T epiPlayer) {

        List<Class<? extends AbstractCapacity>> availableCapacities = new ArrayList<>();
        boolean isVampire = epiPlayer instanceof Vampire;

        // Find all class that extends AbstractCapacity in the package fr.dreamteam.jam.manager.capacities
        new Reflections("fr.dreamteam.jam.manager.capacities").getSubTypesOf(AbstractCapacity.class).forEach(clazz -> {
            // Get annotation of the class
            Annotation[] annotation = clazz.getAnnotations();
            if (annotation.length == 0) return;
            if (isVampire && annotation[0] instanceof IsVampireCapacity) {
                availableCapacities.add(clazz);
            } else if (!isVampire && annotation[0] instanceof IsHunterCapacity) {
                availableCapacities.add(clazz);
            }
        });
        SInventoryBuilder builder = new SInventoryBuilder().title("§6§lShop §7- §6" + (isVampire ? ChatColor.RED + "Vampire" : ChatColor.GREEN + "Hunter")).size(9);
        availableCapacities.sort((o1, o2) -> {
            try {
                return o1.getConstructor(EpiPlayer.class).newInstance(epiPlayer).getPrice() - o2.getConstructor(EpiPlayer.class).newInstance(epiPlayer).getPrice();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        int i = 0;
        for (Class<? extends AbstractCapacity> clazz : availableCapacities) {
            try {
                AbstractCapacity capacity = clazz.getConstructor(EpiPlayer.class).newInstance(epiPlayer);
                boolean hasCapacity = epiPlayer.hasCapacity(capacity.getClass());
                ItemStack itemStack = capacity.getItem();
                ItemMeta itemMeta = itemStack.getItemMeta();
                assert itemMeta != null;
                itemMeta.setDisplayName(itemMeta.getDisplayName() + " §7(" + (isVampire ? ChatColor.RED : ChatColor.LIGHT_PURPLE) + (hasCapacity ? ChatColor.STRIKETHROUGH : "") + capacity.getPrice() + " " + (isVampire ? "\uD83E\uDDEA" : "▽▽") + "§7)");

                itemStack.setItemMeta(itemMeta);
                builder.item(i, itemStack).clickButton(i++, (player, inventory, clickType) -> {
                    if (hasCapacity) {
                        player.sendMessage("§cVous avez déjà cette capacité !");
                        return;
                    }
                    if (epiPlayer.currency < capacity.getPrice()) {
                        player.sendMessage("§cVous n'avez pas assez de " + (isVampire ? ChatColor.RED + "sang" : ChatColor.LIGHT_PURPLE + "dents") + " §cpour acheter cette capacité !");
                        return;
                    }
                    epiPlayer.currency -= capacity.getPrice();
                    epiPlayer.addCapacity(capacity);
                    player.sendMessage("§aVous avez acheté la capacité §6" + itemMeta.getDisplayName() + "§a !");
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        builder.onClose((player, inventory) ->
                Bukkit.getScheduler().runTaskLater(Main.getInstance(),
                        () -> player.openInventory(inventory.getInventory()), 1L));

        return builder.build().getInventory();
    }

}
