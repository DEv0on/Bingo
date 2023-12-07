package pl.dev0on.bingo.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.dev0on.bingo.data.Card;

public class BingoGUI {

    private final Inventory inv;
    private final Card card;

    public BingoGUI(Card card) {
        this.card = card;
        this.inv = Bukkit.createInventory(null, 45, Component.text("Bingo Card"));
        initInventory();
    }

    private void initInventory() {
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                inv.setItem(i * 9 + 2 + j, this.card.getItems().get(i * 5 + j).getItem());
    }

    public void openInventory(Player ent) {
        ent.openInventory(this.inv);
    }

    public static class InventoryListener implements Listener {
        @EventHandler
        public void onClick(InventoryClickEvent ev) {
            if (ev.getView().getTitle().equals("Bingo card")) return;

            ev.setResult(Event.Result.DENY);
            ev.setCancelled(true);
        }

        @EventHandler
        public void onDrag(InventoryDragEvent ev) {
            if (ev.getView().getTitle().equals("Bingo card")) return;

            ev.setResult(Event.Result.DENY);
            ev.setCancelled(true);
        }
    }
}
