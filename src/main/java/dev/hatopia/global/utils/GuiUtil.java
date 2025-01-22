package dev.hatopia.global.utils;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class GuiUtil {

    public static GuiItem fillItem = ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE)
            .setName(" ")
            .asGuiItem();

    public static Gui createGui(int rows, String title, boolean guiFill, boolean disableAllInteractions) {
        Gui gui = Gui.gui()
                .title(Component.text(MessageUtil.fixColors("&8" + MessageUtil.formatToFancyFont(title))))
                .rows(rows)
                .create();

        if (guiFill) {
            gui.getFiller().fill(fillItem);
        }

        if (disableAllInteractions) {
            gui.disableAllInteractions();
        }
        return gui;
    }

    public static PaginatedGui createPaginatedGUI(int rows, int pageSize, String title, boolean disableAllInteractions) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(MessageUtil.fixColors("&8" + MessageUtil.formatToFancyFont(title))))
                .rows(rows)
                .pageSize(pageSize)
                .create();

        if (disableAllInteractions) {
            gui.disableAllInteractions();
        }
        return gui;
    }

}
