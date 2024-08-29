/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.game;

import com.chalwk.util.settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a game between two players, managing game-related operations such as starting a game and scheduling game end tasks.
 */
public class Game {

    /**
     * The user who initiated the game.
     */
    private final User invitingPlayer;
    /**
     * The user who was invited to join the game.
     */
    private final User invitedPlayer;
    /**
     * The start time of the game.
     */
    private Date startTime;

    private final SlashCommandInteractionEvent event;

    /**
     * Creates a new Game instance for the specified players and assigns a GameManager.
     *
     * @param invitingPlayer the user who initiated the game
     * @param invitedPlayer  the user who was invited to join the game
     * @param event the event that triggered the game creation
     */
    public Game(User invitingPlayer, User invitedPlayer, SlashCommandInteractionEvent event) {
        this.event = event;
        this.invitingPlayer = invitingPlayer;
        this.invitedPlayer = invitedPlayer;
    }

    /**
     * Starts the game, sends a notification to both players, and schedules the game end task.
     * @param event the event that triggered the game start
     */
    public void startGame(SlashCommandInteractionEvent event) {
        this.startTime = new Date();
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("New Game")
                .setDescription("A new game between " + invitingPlayer.getName() + " and " + invitedPlayer.getName() + " has started!")
                .setColor(Color.GREEN).build()).queue();
        scheduleGameEndTask();
    }

    /**
     * Schedules a task to end the game when the default time limit is reached.
     */
    private void scheduleGameEndTask() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isTimeUp()) {
                    this.cancel();
                    event.replyEmbeds(new EmbedBuilder()
                            .setTitle("Times up!")
                            .setDescription("Game between " + invitingPlayer.getName() + " and " + invitedPlayer.getName() + " has ended!")
                            .setColor(Color.GREEN).build()).queue();
                }
                // todo: Create checkGameState() (or checkForWinner()) method to check if a player has won the game
            }
        }, 0, 1000);
    }

    /**
     * Checks if the default time limit for the game has been exceeded.
     *
     * @return true if the time limit has been exceeded, false otherwise
     */
    private boolean isTimeUp() {
        long elapsedTime = System.currentTimeMillis() - startTime.getTime();
        return elapsedTime > settings.getDefaultTimeLimit() * 1000L;
    }
}