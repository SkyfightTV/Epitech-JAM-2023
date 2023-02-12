package fr.dreamteam.jam.utils.inventory;

import com.speedment.common.function.TriConsumer;
import fr.dreamteam.jam.utils.inventory.button.Button;
import fr.dreamteam.jam.utils.inventory.button.ClickButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class SInventoryBuilder {
    public String title = "";
    public int size = 27;
    public boolean storage = false;
    private BiConsumer<Player, SInventory> closeConsumer;
    private final HashMap<Integer, ItemStack> items = new HashMap<>();
    private final HashMap<Integer, Button> buttons = new HashMap<>();

    public SInventoryBuilder title(String title) {
        this.title = title;
        return this;
    }

    public SInventoryBuilder size(int size) {
        this.size = size;
        return this;
    }

    public SInventoryBuilder canStore(boolean storable) {
        this.storage = storable;
        return this;
    }

    public SInventoryBuilder item(int slot, ItemStack itemStack) {
        this.items.put(slot, itemStack);
        return this;
    }

    public SInventoryBuilder clickButton(int slot, TriConsumer<Player, SInventory, ClickType> consumer) {
        return button(slot, (Button)new ClickButton(slot, consumer));
    }

    public SInventoryBuilder voidButton(int slot) {
        return button(slot, new Button(slot));
    }

    public SInventoryBuilder button(int slot, Button button) {
        this.buttons.put(slot, button);
        return this;
    }

    public SInventoryBuilder onClose(BiConsumer<Player, SInventory> closeConsumer) {
        this.closeConsumer = closeConsumer;
        return this;
    }

    public HashMap<Integer, ItemStack> getItems() {
        return this.items;
    }

    public HashMap<Integer, Button> getButtons() {
        return this.buttons;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public boolean isStorage() {
        return storage;
    }

    public SInventory build() {
        SInventory inventoryGUI = new SInventory(Bukkit.createInventory(null, this.size, this.title), this.storage, this.buttons, this.closeConsumer);
        this.items.forEach((integer, itemStack) -> inventoryGUI.getInventory().setItem(integer, itemStack));
        return inventoryGUI;
    }


    public SInventoryBuilder clone() {
        try {
            return (SInventoryBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
