package dev.haloware.spawn.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    public static String fixColors(String coloredMessage) {

        coloredMessage = ChatColor.translateAlternateColorCodes('&', coloredMessage
                        .replace(">>", "»")
                        .replace("<<", "«")
                        .replace("%o%", "●")
                        .replace("%x%", "❌")
                        .replaceAll("%v%", "✔"))
                .replaceAll("%LMB%", "▟")
                .replaceAll("%RMB%", "▙")
        ;

        Matcher hexMatcher = Pattern.compile("#[a-fA-F0-9]{6}").matcher(coloredMessage);
        StringBuffer buffer = new StringBuffer();
        while (hexMatcher.find()) {
            String hexColor = hexMatcher.group();
            hexMatcher.appendReplacement(buffer, ChatColor.of(hexColor).toString());
        }
        hexMatcher.appendTail(buffer);

        return buffer.toString();
    }

    public static String colorToHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static String removeColors(String input) {
        return org.bukkit.ChatColor.stripColor(input);
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(fixColors(message)));
    }

    public static String removeLastCharacter(String str) {
        return Optional.ofNullable(str)
                .filter(sStr -> sStr.length() != 0)
                .map(sStr -> sStr.substring(0, sStr.length() - 1))
                .orElse(str);
    }

    public static boolean isUUID(String input) {
        try {
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean containsBlockedCharacters(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c > 127 && !isPolishCharacter(c)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isPolishCharacter(char c) {
        return (c >= 'ą' && c <= 'ż') || (c >= 'Ą' && c <= 'Ż') || c == 'ł' || c == 'Ł' || c == 'ó' || c == 'Ó';
    }

    public static boolean isNotBlank(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static String generateRandomCharacters() {
        int stringCount = 7;
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        String allowCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < stringCount; i++) {
            int index = random.nextInt(allowCharacters.length());
            char randomCharacter = allowCharacters.charAt(index);
            stringBuilder.append(randomCharacter);
        }

        return stringBuilder.toString();
    }

    public static String formatToFancyFont(String text) {
        Map<Character, Character> map = new HashMap<>();
        map.put('a', 'ᴀ');
        map.put('b', 'ʙ');
        map.put('c', 'ᴄ');
        map.put('d', 'ᴅ');
        map.put('e', 'ᴇ');
        map.put('f', 'ꜰ');
        map.put('g', 'ɢ');
        map.put('h', 'ʜ');
        map.put('i', 'ɪ');
        map.put('j', 'ᴊ');
        map.put('k', 'ᴋ');
        map.put('l', 'ʟ');
        map.put('m', 'ᴍ');
        map.put('n', 'ɴ');
        map.put('o', 'ᴏ');
        map.put('p', 'ᴘ');
        map.put('r', 'ʀ');
        map.put('s', 'ꜱ');
        map.put('t', 'ᴛ');
        map.put('u', 'ᴜ');
        map.put('v', 'ᴠ');
        map.put('w', 'ᴡ');
        map.put('x', 'x');
        map.put('y', 'ʏ');
        map.put('z', 'ᴢ');

        StringBuilder formattedText = new StringBuilder();
        boolean skipNext = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '&' || c == '§') {
                skipNext = true;
                formattedText.append(c);
            } else if (skipNext) {
                formattedText.append(c);
                skipNext = false;
            } else {
                char lowerCaseC = Character.toLowerCase(c);
                char fancyChar = map.getOrDefault(lowerCaseC, c);
                formattedText.append(fancyChar);
            }
        }

        return formattedText.toString();
    }
}