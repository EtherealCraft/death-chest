package com.github.devcyntrix.deathchest.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.bukkit.parsers.WorldArgument;
import com.github.devcyntrix.deathchest.DeathChestModel;
import com.github.devcyntrix.deathchest.DeathChestPlugin;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ListCommandProvider implements CommandProvider {

    private final DeathChestPlugin plugin;

    public ListCommandProvider(DeathChestPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Command.Builder<CommandSender> provide(Command.Builder<CommandSender> builder) {
        return builder
                .handler(commandContext -> {
                    if (!(commandContext.getSender() instanceof Player)) {
                        commandContext.getSender().sendMessage(
                                plugin.getPrefix() + "§cOnly players can use this command"
                        );
                        return;
                    }
                    handleCommand((Player) commandContext.getSender());
                });
    }

    private void handleCommand(Player player) {
        ArrayList<DeathChestModel> chests = new ArrayList<>();
        if (plugin.getLastChest(player) == null) {
            player.sendMessage(ChatColor.GREEN + "You don't have any Spirit Chests!");
            return;
        }
        for (DeathChestModel chest : plugin.getChests().toList()) {
            if (chest.getOwner().getUniqueId().equals(player.getUniqueId())) {
                chests.add(chest);
            }
        }
        chests.sort(Comparator.comparing(DeathChestModel::getCreatedAt));
        int index = 1;
        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("world", "The Overworld");
        nameMap.put("world_the_nether", "The Nether");
        nameMap.put("world_the_end", "The End");
        for (DeathChestModel chest : chests) {
            player.sendMessage(ChatColor.AQUA + ""  + index + ". " + ChatColor.GREEN
                    + nameMap.getOrDefault(chest.getWorld().getName(), chest.getWorld().getName()) +
                    ": " + (int)chest.getLocation().getX() +
                    " " + (int)chest.getLocation().getY() +
                    " " + (int)chest.getLocation().getZ());
            index++;
        }
    }
}
