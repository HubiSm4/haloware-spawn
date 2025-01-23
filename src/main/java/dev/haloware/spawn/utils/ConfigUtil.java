package dev.haloware.spawn.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ConfigUtil {

    public static Location parseLocation(String locationString) {
        if (locationString == null || locationString.isEmpty()) {
            return null;
        }

        String[] parts = locationString.split(";");
        if (parts.length < 4) {
            return null;
        }

        World world = Bukkit.getWorld(parts[0]);
        if (world == null) {
            return null;
        }

        try {
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            if (parts.length >= 6) {
                float yaw = Float.parseFloat(parts[4]);
                float pitch = Float.parseFloat(parts[5]);
                return new Location(world, x, y, z, yaw, pitch);
            }

            return new Location(world, x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}