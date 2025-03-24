package com.vch.skipNight.commands;

import com.vch.skipNight.managers.VoteManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {

    private final VoteManager voteManager;

    public VoteCommand(VoteManager voteManager) {
        this.voteManager = voteManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player player)) {

            commandSender.sendMessage(ChatColor.RED + "Solo jugadores pueden votar");
            return true;

        }

        if (!voteManager.isVoteActive()) {

            player.sendMessage(ChatColor.RED + "No hay ningún voto activo");
            return true;

        }

        if (strings.length < 1) {

            player.sendMessage(ChatColor.RED + "Usar: /vote <yes|no>");

        }

        String vote = strings[0].toLowerCase();

        switch (vote) {

            case "yes":

                voteManager.castVote(player.getUniqueId(), true);
                break;

            case "no":

                voteManager.castVote(player.getUniqueId(), false);
                break;

            default:

                player.sendMessage(ChatColor.RED + "Usar: /vote <yes|no>");
                break;

        }

        return true;

    }
}
