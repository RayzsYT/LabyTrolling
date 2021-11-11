package de.rayzs.labyban.plugin;

import de.rayzs.labyban.plugin.events.EventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class LabyBan extends JavaPlugin {

    private String kickMessage;

    @Override
    public void onEnable() {
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 500; i++) stringBuilder.append("§r\n");
        this.kickMessage = stringBuilder + "\n§r§e§lLABYMOD\n" +
                "§cDue to an unauthorized activity in the Labymod chat, you were permanently banned from playing with the LabyMod client!" +
                "\n§fFor more information, please contact our support: §bhttp://labymod.net/support§r"+ "\n" + stringBuilder;
        new EventListener(this);
    }

    public String getKickMessage() {
        return kickMessage;
    }
}