package dev.hatopia.global.utils;

import dev.hatopia.ServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigUtil {

    public static List<String> getRegisteredCommands() {
        PluginDescriptionFile description = ServerPlugin.getPlugin().getDescription();
        Map<String, Map<String, Object>> commands = description.getCommands();

        // Lista do przechowywania nazw komend i ich aliasów
        List<String> commandList = new ArrayList<>();

        // Iteracja przez komendy
        for (String commandName : commands.keySet()) {
            // Dodaj nazwę komendy do listy
            commandList.add(commandName);

            // Sprawdź, czy są aliasy
            Map<String, Object> commandInfo = commands.get(commandName);
            if (commandInfo.containsKey("aliases")) {
                List<String> aliases = (List<String>) commandInfo.get("aliases");
                // Dodaj aliasy do listy
                commandList.addAll(aliases);
            }
        }

        return commandList;
    }

    public static Location parseLocation(String locationString) {
        if (locationString == null || locationString.isEmpty()) {
            return null;
        }

        String[] parts = locationString.split(";");
        if (parts.length < 4) {
            return null; // Nieprawidłowy format
        }

        World world = Bukkit.getWorld(parts[0]);
        if (world == null) {
            return null; // Świat nie istnieje
        }

        try {
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            // Jeśli podano yaw i pitch
            if (parts.length >= 6) {
                float yaw = Float.parseFloat(parts[4]);
                float pitch = Float.parseFloat(parts[5]);
                return new Location(world, x, y, z, yaw, pitch);
            }

            // Tylko współrzędne bez yaw i pitch
            return new Location(world, x, y, z);
        } catch (NumberFormatException e) {
            return null; // Nieprawidłowe liczby
        }
    }
}