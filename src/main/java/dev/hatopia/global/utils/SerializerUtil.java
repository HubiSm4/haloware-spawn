package dev.hatopia.global.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

public class SerializerUtil {

    public static String serializeItemStack(ItemStack itemStack) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(itemStack);

            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (Exception e) {
            throw new IllegalStateException("Could not serialize item stack.", e);
        }
    }

    public static ItemStack deserializeItemStack(String dataString) {
        byte[] data = Base64.getDecoder().decode(dataString);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BukkitObjectInputStream dataInput = null;
        try {
            dataInput = new BukkitObjectInputStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ItemStack item;
        try {
            item = (ItemStack) dataInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            dataInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    public static String serializeInventory(Inventory inventory) throws IllegalStateException {
        if (inventory == null) {
            return null;
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(inventory.getType());
            dataOutput.writeInt(inventory.getSize());

            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack item = inventory.getItem(i);
                dataOutput.writeObject(item != null ? item.clone() : null);
            }

            dataOutput.close();
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (Exception e) {
            throw new IllegalStateException("Could not serialize inventory.", e);
        }
    }

    public static ItemStack[] deserializeInventoryToItemStacks(String dataString) {
        byte[] data = Base64.getDecoder().decode(dataString);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BukkitObjectInputStream dataInput = null;
        try {
            dataInput = new BukkitObjectInputStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InventoryType type;
        int size;
        try {
            type = (InventoryType) dataInput.readObject();
            size = dataInput.readInt();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ItemStack[] items = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            try {
                ItemStack item = (ItemStack) dataInput.readObject();
                items[i] = item != null ? item.clone() : null;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            dataInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public static String serializeEmptyInventory() {
        return "rO0ABX5yAChvcmcuYnVra2l0LmV2ZW50LmludmVudG9yeS5JbnZlbnRvcnlUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAGUExBWUVSdwQAAAApcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHA=";
    }

    public static String serializeEffects(Player player) {
        StringBuilder serializedEffects = new StringBuilder();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            String type = effect.getType().getName();
            int duration = effect.getDuration();
            int amplifier = effect.getAmplifier();

            serializedEffects.append(type).append(":").append(duration).append(":").append(amplifier).append(";");
        }

        if (serializedEffects.length() == 0) {
            return "";
        }

        return Base64.getEncoder().encodeToString(serializedEffects.toString().getBytes());
    }

    public static ArrayList<PotionEffect> deserializeEffects(String serializedEffects) {
        ArrayList<PotionEffect> effects = new ArrayList<>();

        if (serializedEffects.isEmpty()) {
            return effects;
        }

        byte[] data = Base64.getDecoder().decode(serializedEffects);
        String decodedString = new String(data);
        String[] effectTokens = decodedString.split(";");

        for (String effectToken : effectTokens) {
            String[] parts = effectToken.split(":");
            if (parts.length != 3) {
                continue;
            }

            PotionEffectType type = PotionEffectType.getByName(parts[0]);
            if (type == null) {
                continue;
            }

            int duration = Integer.parseInt(parts[1]);
            int amplifier = Integer.parseInt(parts[2]);

            PotionEffect newEffect = new PotionEffect(type, duration, amplifier);
            effects.add(newEffect);
        }
        return effects;
    }


    public static void applySerializedEffects(Player player, String serializedEffects) {
        ArrayList<PotionEffect> effects = deserializeEffects(serializedEffects);

        for (PotionEffect effect : effects) {
            player.addPotionEffect(effect);
        }
    }

    public static String serializeLocation(Location location) {
        String locationString = location.getWorld().getName() + ";" +
                location.getX() + ";" +
                location.getY() + ";" +
                location.getZ() + ";" +
                location.getYaw() + ";" +
                location.getPitch();

        return Base64.getEncoder().encodeToString(locationString.getBytes());
    }

    public static Location deserializeLocation(String base64Location) {
        byte[] data = Base64.getDecoder().decode(base64Location);
        String decodedString = new String(data);
        String[] str2loc = decodedString.split(";");

        if (str2loc.length != 6) {
            throw new IllegalArgumentException("Invalid base64 location string format.");
        }

        String worldName = str2loc[0];
        double x = Double.parseDouble(str2loc[1]);
        double y = Double.parseDouble(str2loc[2]);
        double z = Double.parseDouble(str2loc[3]);
        float yaw = Float.parseFloat(str2loc[4]);
        float pitch = Float.parseFloat(str2loc[5]);

        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }
}
