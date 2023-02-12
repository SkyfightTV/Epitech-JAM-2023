package fr.dreamteam.jam.manager.capacities;

import fr.dreamteam.jam.manager.EpiPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@IsHunterCapacity
public class StakeCapacity extends AbstractCapacity {

    public StakeCapacity(EpiPlayer player) {
        super(player, 0, 0, 0);
    }

    @Override
    public void onActive() {

    }

    @Override
    public void onUse() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD);
        item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Stake");
        List<String> lore = new ArrayList<>();
        lore.add("§7Use your stake to kill a vampire");
        meta.setLore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPrice() {
        return 0;
    }
}
