package dev.hatopia.core.runnables;

import dev.hatopia.ServerPlugin;
import dev.hatopia.global.utils.MessageUtil;
import org.bukkit.Bukkit;

public class AutoMessageRunnable implements Runnable {

    private int currentIndex = 0;

    @Override
    public void run() {
        if (!ServerPlugin.getTemplateConfig().getAutoMessageList().isEmpty()) {
            if (currentIndex < ServerPlugin.getTemplateConfig().getAutoMessageList().size()) {
                Bukkit.broadcastMessage(MessageUtil.fixColors(ServerPlugin.getTemplateConfig().getAutoMessageList().get(currentIndex)));
                currentIndex++;
            } else {
                currentIndex = 0;
            }
        }
    }
}
