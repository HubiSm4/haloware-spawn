package dev.hatopia.core.configs;

import dev.hatopia.global.utils.ConfigUtil;
import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;

import java.util.List;

@Getter
public class TemplateConfig extends OkaeriConfig {

    String permissionCommandPrefix = "hatopia.command.";

    String permissionListenerPrefix = "hatopia.listener.";

    String permissionPubSubPrefix = "hatopia.view.";

    String permissionRunnablePrefix = "hatopia.runnable.";

    String messagePrefix = "§8› ";

    List<String> autoMessageList = List.of("auto message #1", "auto message #2");

    List<String> playerCommandsAvailable = ConfigUtil.getRegisteredCommands();

    List<String> availableCommandsForPlayersInSuspect = List.of("about", "discord", "msg", "message", "ignore", "ignoruj", "confess", "przyznajesie", "response", "r", "helpop", "report", "zglos");

    List<String> skipCooldownCommands = List.of("message", "msg", "wiadomosc", "response", "r", "odpowiedz", "hub");
}
