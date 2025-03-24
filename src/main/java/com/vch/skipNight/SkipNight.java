package com.vch.skipNight;

import com.vch.skipNight.commands.VoteCommand;
import com.vch.skipNight.managers.VoteManager;
import com.vch.skipNight.tasks.TimeCheckTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class SkipNight extends JavaPlugin {

    private VoteManager voteManager;
    private BukkitTask timeCheckTask;

    @Override
    public void onEnable() {

        this.voteManager = new VoteManager(this);

        getCommand("vote").setExecutor(new VoteCommand(this.voteManager));

        this.timeCheckTask = Bukkit.getScheduler().runTaskTimer(this, new TimeCheckTask(this.voteManager), 0L, 20L);

    }

    @Override
    public void onDisable() {

        if (this.timeCheckTask != null) {

            this.timeCheckTask.cancel();
            this.timeCheckTask = null;

        }

    }
}
