package dev.haloware.spawn.configs;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import lombok.Getter;

@Getter
public class SpawnConfig extends OkaeriConfig {

    String locationWorld = "world";
    String locationX = "0.5";
    String locationY = "100";
    String locationZ = "0.5";
    String locationYaw = "-180";
    String locationPitch = "0";
    
    String messagePrefix = "§8› &7";
    String teleportTitle = "&cSPAWN";
    String teleportSubtitle = "&7Pomyślnie przeteleportowano!";
    String teleportMessage = "&7Pomyślnie przeteleportowano!";

    boolean enableCountdown = true;
    int countdownInterval = 5;

    @Comment({"%countdown%", "remaining time to teleportation"})
    String countdownTitle = "&aTELEPORTACJA";
    String countdownSubtitle = "&7Zostaniesz przeteleportowany za: &c%countdown%";

    boolean cancelOnMove = true;
    String cancelOnMoveTitle = "&aTELEPORTACJA";
    String cancelOnMoveSubtitle = "&7Ruszyłeś się, teleportacja anulowana!";
    String cancelOnMoveMessage = "Ruszyłeś się, teleportacja anulowana!";

    boolean cancelOnDamage = true;
    String cancelOnDamageTitle = "&aTELEPORTACJA";
    String cancelOnDamageSubtitle = "&7Otrzymałeś obrażenia, teleportacja anulowana!";
    String cancelOnDamageMessage = "Otrzymałeś obrażenia, teleportacja anulowana!";

}
