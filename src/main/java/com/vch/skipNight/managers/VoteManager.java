package com.vch.skipNight.managers;

import com.vch.skipNight.SkipNight;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class VoteManager {

    private final SkipNight plugin;

    private final HashMap<UUID, Boolean> votes = new HashMap<>();
    private boolean voteActive = false;

    private BukkitTask voteTask;

    public VoteManager(SkipNight plugin) {
        this.plugin = plugin;
    }

    public void startVote() {

        if(this.voteActive || Bukkit.getOnlinePlayers().size() == 0) return;

        votes.clear();
        voteActive = true;

        TextComponent message = new TextComponent(ChatColor.GOLD + "Votación para saltar la noche: ");

        TextComponent yesButton = new TextComponent(ChatColor.GREEN + "[SI] ");
        yesButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote yes"));
        yesButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click para hacer de día")));

        TextComponent noButton = new TextComponent(ChatColor.RED + "[NO]");
        noButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote no"));
        noButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click para dejar la noche")));

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.spigot().sendMessage(message, yesButton, noButton);
        });

        this.voteTask = Bukkit.getScheduler().runTaskLater(plugin, this::processVoteResults, 600L);

    }

    public void castVote(UUID uuid, boolean vote) {

        if(isVoteActive()) {

            if(votes.containsKey(uuid) && votes.get(uuid) == vote)
                return Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "Ya has votado para " + (vote ? "saltar la noche" : "continuar la noche"));

            votes.put(uuid, vote);

            String voteMessage = vote ? ChatColor.GREEN + "votó para saltar la noche" : ChatColor.RED + "votó para continuar la noche";
            Bukkit.broadcastMessage(ChatColor.YELLOW + Bukkit.getPlayer(uuid).getName() + " " + voteMessage);

            int onlinePlayersCount = Bukkit.getOnlinePlayers().size();
            if(votes.size() >= onlinePlayersCount) {
                this.voteTask.cancel();
                processVoteResults();
            }

        }

    }

    public boolean isVoteActive() {
        return this.voteActive;
    }

    private void processVoteResults() {

        if(!this.voteActive) return;

        int votesForDay = 0;
        int votesForNight = 0;

        for(Boolean vote : votes.values()) {
            if(vote) votesForDay++;
            else votesForNight++;
        }

        boolean keepDay = votesForDay > votesForNight;
        String border = ChatColor.GOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";

        Bukkit.broadcastMessage(border);

        if(keepDay) {

            Bukkit.broadcastMessage(ChatColor.GREEN + "   Amanecerá en seguida");

            for(World world : Bukkit.getWorlds())
                world.setTime(1000);

            for(Player player : Bukkit.getOnlinePlayers())
                player.setStatistic(Statistic.TIME_SINCE_REST, 0);

        } 
        else {
            Bukkit.broadcastMessage(ChatColor.RED + "   La noche continúa");
        }

        Bukkit.broadcastMessage(border);

        votes.clear();
        voteActive = false;

    }

}
