package dev.hatopia.core.guis.book;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.upperlevel.spigot.book.BookUtil;

public class AboutGui {

    public static void openGui(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1);

        ItemStack book = BookUtil.writtenBook()
                .author(" ")
                .title(" ")
                .pages(
                        new BookUtil.PageBuilder()
                                .add(new TextComponent(MessageUtil.fixColors("&0Najedź i kliknij &a%LMB%&8%RMB% &0na tekst, aby otworzyć link do przekierowania.")))
                                .newLine()
                                .newLine()
                                .add(
                                        BookUtil.TextBuilder.of("STRONA INTERNETOWA")
                                                .color(ChatColor.GOLD)
                                                .style(ChatColor.BOLD)
                                                .onClick(BookUtil.ClickAction.openUrl("https://" + ServerPlugin.getPlugin().getDescription().getWebsite()))
                                                .onHover(BookUtil.HoverAction.showText(MessageUtil.fixColors("&7Kliknij &a%LMB%&8%RMB%&7, aby otworzyć przekierowanie do strony internetowej!")))
                                                .build()
                                )
                                .newLine()
                                .newLine()
                                .add(
                                        BookUtil.TextBuilder.of("SERWER DISCORD")
                                                .color(ChatColor.BLUE)
                                                .style(ChatColor.BOLD)
                                                .onClick(BookUtil.ClickAction.openUrl("https://dc." + ServerPlugin.getPlugin().getDescription().getWebsite()))
                                                .onHover(BookUtil.HoverAction.showText(MessageUtil.fixColors("&7Kliknij &a%LMB%&8%RMB%&7, aby otworzyć przekierowanie do serwera Discord!")))
                                                .build()
                                )
                                .newLine()
                                .newLine()
                                .add(
                                        BookUtil.TextBuilder.of("PROFIL TIKTOK")
                                                .color(ChatColor.RED)
                                                .style(ChatColor.BOLD)
                                                .onClick(BookUtil.ClickAction.openUrl("https://tt." + ServerPlugin.getPlugin().getDescription().getWebsite()))
                                                .onHover(BookUtil.HoverAction.showText(MessageUtil.fixColors("&7Kliknij &a%LMB%&8%RMB%&7, aby otworzyć przekierowanie do profilu TikTok!")))
                                                .build()
                                )
                                .newLine()
                                .newLine()
                                .add(
                                        BookUtil.TextBuilder.of("KANAŁ YOUTUBE")
                                                .color(ChatColor.DARK_RED)
                                                .style(ChatColor.BOLD)
                                                .onClick(BookUtil.ClickAction.openUrl("https://yt." + ServerPlugin.getPlugin().getDescription().getWebsite()))
                                                .onHover(BookUtil.HoverAction.showText(MessageUtil.fixColors("&7Kliknij &a%LMB%&8%RMB%&7, aby otworzyć przekierowanie do kanału YouTube!")))
                                                .build()
                                )
                                .newLine()
                                .newLine()
                                .add(
                                        BookUtil.TextBuilder.of("PROFIL NAMEMC")
                                                .color(ChatColor.AQUA)
                                                .style(ChatColor.BOLD)
                                                .onClick(BookUtil.ClickAction.openUrl("https://nmc." + ServerPlugin.getPlugin().getDescription().getWebsite()))
                                                .onHover(BookUtil.HoverAction.showText(MessageUtil.fixColors("&7Kliknij &a%LMB%&8%RMB%&7, aby otworzyć przekierowanie do profilu NameMC!")))
                                                .build()
                                )
                                .build()
                )
                .build();

        BookUtil.openPlayer(player, book);
    }
}
