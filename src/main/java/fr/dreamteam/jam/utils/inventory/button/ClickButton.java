package fr.dreamteam.jam.utils.inventory.button;

import com.speedment.common.function.TriConsumer;
import fr.dreamteam.jam.utils.inventory.SInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ClickButton extends Button {
  TriConsumer<Player, SInventory, ClickType> consumer;
  
  public ClickButton(int slot, TriConsumer<Player, SInventory, ClickType> consumer) {
    super(slot);
    this.consumer = consumer;
  }
  
  public TriConsumer<Player, SInventory, ClickType> getConsumer() {
    return this.consumer;
  }
}
