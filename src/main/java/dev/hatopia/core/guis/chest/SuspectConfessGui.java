package dev.hatopia.core.guis.chest;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.hatopia.global.utils.GuiUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class SuspectConfessGui {

    public static void openGui(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1);
        Gui gui = GuiUtil.createGui(3, "przyznajesz sie?", true, true);

        GuiItem confirmItem = ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE)
                .setName(MessageUtil.fixColors("&a&l" + MessageUtil.formatToFancyFont("tak")))
                .addLore(
                        " ",
                        MessageUtil.fixColors("&7Kliknij &a%LMB%&8%RMB%&7, jeśli przyznajesz się do bycia podejrzanym.")
                )
                .asGuiItem(inventoryClickEvent -> {
                    player.kickPlayer(MessageUtil.fixColors("&cPrzyznałeś się do bycia podejrzanym. ヾ(￣▽￣) Bye~Bye~"));
                });

        GuiItem infoItem = ItemBuilder.from(Material.BOOK)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .setName(MessageUtil.fixColors("&4&l" + MessageUtil.formatToFancyFont("bardzo wazne")))
                .setLore(
                        MessageUtil.fixColors("&7Przyznanie się do bycia podejrzanym skraca długość Twojej kary.")
                )
                .asGuiItem();

        GuiItem declineItem = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE)
                .setName(MessageUtil.fixColors("&c&l" + MessageUtil.formatToFancyFont("nie")))
                .addLore(
                        " ",
                        MessageUtil.fixColors("&7Kliknij &a%LMB%&8%RMB%&7, jeśli nie przyznajesz się do bycia podejrzanym.")
                )
                .asGuiItem(inventoryClickEvent -> {
                    gui.close(player);
                    player.sendMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getMessagePrefix() + "Nie przyznajesz się do bycia podejrzanym. Udaj się na nasz serwer &4DISCORD &7i wejdź na kanał &4#jestem-podejrzany&7."));
                });

        int[] confirmSlots = {0, 1, 2, 9, 10, 11, 18, 19, 20};
        int[] declineSlots = {6, 7, 8, 15, 16, 17, 24, 25, 26};

        for (int slot : confirmSlots) {
            gui.setItem(slot, confirmItem);
        }

        gui.setItem(13, infoItem);

        for (int slot : declineSlots) {
            gui.setItem(slot, declineItem);
        }

        gui.open(player);
    }
}

