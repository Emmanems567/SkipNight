package com.vch.skipNight.tasks;

import com.vch.skipNight.managers.VoteManager;
import org.bukkit.Bukkit;

public class TimeCheckTask implements Runnable{

    private final VoteManager voteManager;
    private boolean voteCalled = false;

    public TimeCheckTask(VoteManager voteManager) {

        this.voteManager = voteManager;

    }

    @Override
    public void run() {

        for (var world : Bukkit.getWorlds()) {

            long time = world.getTime();

            if (time >= 12000L && !voteCalled && !voteManager.isVoteActive()) {

                voteCalled = true;
                voteManager.startVote();

            } else if (time < 12000L) {

                voteCalled = false;

            }

        }

    }

}