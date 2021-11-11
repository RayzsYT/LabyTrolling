package de.rayzs.labyban.task;

import de.rayzs.labyban.plugin.LabyBan;
import de.rayzs.labyban.protocol.LabyProtocol;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class LabyTask {

    private int taskId, runs;
    private final Player player;

    public LabyTask(final LabyBan labyBan, final Player player) {
        this.player = player;
        this.runs = 0;
        this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(labyBan, () -> {
            if(!player.isOnline()) {
                Bukkit.getScheduler().cancelTask(taskId);
                return;
            }
            if(runs == 40) {
                player.sendMessage("§7DISCONNECTED§f: You were banned by using the labymod client!");
            }

            if(runs >= 140) {
                Bukkit.getScheduler().cancelTask(taskId);
                labyKick(labyBan);
                return;
            }
            LabyProtocol.sendCurrentPlayingGamemode(player, true, "§" + new Random().nextInt(10));
            runs++;
        }, 1, 1);
    }

    protected void labyKick(final LabyBan labyBan) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(labyBan, () -> player.kickPlayer(labyBan.getKickMessage()));
    }
}
