package dev.hatopia.global.utils;

import org.bukkit.Location;

public class RegionUtil {

    public static boolean inRegion(Location locationOrigin, Location locationCorner1, Location locationCorner2) {
        int minX = (int) Math.min(locationCorner1.getX(), locationCorner2.getX());
        int maxX = (int) Math.max(locationCorner1.getX(), locationCorner2.getX());
        int minY = (int) Math.min(locationCorner1.getY(), locationCorner2.getY());
        int maxY = (int) Math.max(locationCorner1.getY(), locationCorner2.getY());
        int minZ = (int) Math.min(locationCorner1.getZ(), locationCorner2.getZ());
        int maxZ = (int) Math.max(locationCorner1.getZ(), locationCorner2.getZ());
        return locationOrigin.getX() >= minX && locationOrigin.getX() <= maxX
                && locationOrigin.getY() >= minY && locationOrigin.getY() <= maxY
                && locationOrigin.getZ() >= minZ && locationOrigin.getZ() <= maxZ;
    }
}