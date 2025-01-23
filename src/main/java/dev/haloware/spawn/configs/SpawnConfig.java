package dev.haloware.spawn.configs;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import lombok.Getter;


@Header("################################################################")
@Header("#                                                              #")
@Header("#  Plugin: haloware-spawn                                      #")
@Header("#  Support: dc.haloware.pl                                     #")
@Header("#  Made with <3 by Haloware.                                   #")
@Header("#                                                              #")
@Header("################################################################")

@Getter
public class SpawnConfig extends OkaeriConfig {
    @Comment
    @Comment
    @Comment
    String locationWorld = "world";
    String locationX = "0.5";
    String locationY = "100";
    String locationZ = "0.5";
    String locationYaw = "-180";
    String locationPitch = "0";
    @Comment
    boolean teleportOnJoin = true;
    @Comment
    String messagePrefix = "§8› &7";
    String teleportTitle = "&cSPAWN";
    String teleportSubtitle = "&7Pomyślnie przeteleportowano!";
    String teleportMessage = "&7Pomyślnie przeteleportowano!";
    @Comment
    boolean enableCountdown = true;
    int countdownInterval = 5;
    @Comment
    @Comment({"PLACEHOLDERS", "%countdown% - time remaining to teleportation."})
    String countdownTitle = "&aTELEPORTACJA";
    String countdownSubtitle = "&7Zostaniesz przeteleportowany za: &c%countdown%";
    @Comment
    boolean cancelOnMove = true;
    String cancelOnMoveTitle = "&aTELEPORTACJA";
    String cancelOnMoveSubtitle = "&7Ruszyłeś się, teleportacja anulowana!";
    String cancelOnMoveMessage = "Ruszyłeś się, teleportacja anulowana!";
    @Comment
    boolean cancelOnDamage = true;
    String cancelOnDamageTitle = "&aTELEPORTACJA";
    String cancelOnDamageSubtitle = "&7Otrzymałeś obrażenia, teleportacja anulowana!";
    String cancelOnDamageMessage = "Otrzymałeś obrażenia, teleportacja anulowana!";
}
