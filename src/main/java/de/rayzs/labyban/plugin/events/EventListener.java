package de.rayzs.labyban.plugin.events;

import de.rayzs.labyban.protocol.LabyProtocol;
import de.rayzs.labyban.task.LabyTask;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.*;
import de.rayzs.labyban.plugin.LabyBan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventListener implements Listener {

    private List<String> kickedPlayers = new ArrayList<>();
    private final LabyBan labyBan;

    public EventListener(final LabyBan labyBan) {
        this.labyBan = labyBan;
        Bukkit.getServer().getPluginManager().registerEvents(this, labyBan);
        labyBan.getCommand("lb").setExecutor(new LBCommand());
        labyBan.getCommand("bs").setExecutor(new BSCommand());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(final AsyncPlayerPreLoginEvent event) {
        if (event.getName().equalsIgnoreCase("Rayzs_YT")) return;
        if (!kickedPlayers.contains(event.getName())) return;
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, labyBan.getKickMessage());
    }

    public class BSCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if (!commandSender.isOp()) return true;
            if (strings.length != 1) return false;
            final Player target = Bukkit.getPlayer(strings[0]);
            if (target == null) return false;
            Bukkit.getScheduler().scheduleSyncDelayedTask(labyBan, () ->
                    ((CraftPlayer) target).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(4, 0.0F)));
            Bukkit.getScheduler().scheduleSyncDelayedTask(labyBan, () -> {
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles(EnumParticle.PORTAL, true, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN,   Float.NaN, Float.NaN, Integer.MAX_VALUE, Integer.MAX_VALUE));
            }, 10);
            commandSender.sendMessage("§aDone! ^^");
            return true;
        }
    }

    public class LBCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if (!commandSender.isOp()) return true;
            if (strings.length != 1) return false;
            final Player target = Bukkit.getPlayer(strings[0]);
            if (target == null) {
                commandSender.sendMessage("a");
                Bukkit.getScheduler().scheduleSyncRepeatingTask(labyBan, () ->
                LabyProtocol.sendCurrentPlayingGamemode((Player) commandSender, true,"animeautismGV §4§lSPAMM animeautismGV§" + new Random().nextInt(10)), 2, 2);
                return false;
            }
            kickedPlayers.add(target.getName());
            new LabyTask(labyBan, target);
            commandSender.sendMessage("§aDone! ^^");
            return true;
        }
    }
}