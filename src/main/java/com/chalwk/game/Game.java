/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.game;

import com.chalwk.util.settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.chalwk.bot.BotInitializer.getShardManager;

/**
 * Represents a game between two players, managing game-related operations such as starting a game and scheduling game end tasks.
 */
public class Game {

    private final User invitingPlayer;
    private final User invitedPlayer;
    private final SlashCommandInteractionEvent event;
    private final GameManager gameManager;
    private User whos_turn;
    private Date startTime;

    /**
     * Creates a new Game instance for the specified players and event.
     *
     * @param invitingPlayer the user who initiated the game
     * @param invitedPlayer  the user who was invited to join the game
     * @param event          the event that triggered the game creation
     */
    public Game(User invitingPlayer, User invitedPlayer, SlashCommandInteractionEvent event, GameManager gameManager) {
        this.event = event;
        this.invitingPlayer = invitingPlayer;
        this.invitedPlayer = invitedPlayer;
        this.whos_turn = getStartingPlayer();
        this.gameManager = gameManager;
        startGame(event);
    }

    /**
     * Creates an embed for the game, displaying the players and the current turn.
     *
     * @return the game embed
     */
    public EmbedBuilder createGameEmbed() {
        return new EmbedBuilder()
                .setTitle("\uD83D\uDD74 \uD80C\uDF6F Hangman \uD80C\uDF6F \uD83D\uDD74")
                .addField("Players: ", this.getInvitingPlayer().getAsMention() + " VS " + this.getInvitedPlayer().getAsMention(), true)
                .addField("Turn: ", this.getWhosTurn().getAsMention(), false)
                .setColor(Color.BLUE);
    }

    /**
     * Sets the user who's turn it is to play.
     */
    public void setWhosTurn() {
        this.whos_turn = this.whos_turn.equals(invitingPlayer) ? invitedPlayer : invitingPlayer;
    }

    /**
     * Gets the user who's turn it is to play.
     *
     * @return the user who's turn it is
     */
    public User getWhosTurn() {
        return whos_turn;
    }

    /**
     * Starts the game, sends a notification to both players, and schedules the game end task.
     *
     * @param event the event that triggered the game start
     */
    public void startGame(SlashCommandInteractionEvent event) {
        this.startTime = new Date();
        scheduleGameEndTask();
        event.replyEmbeds(createGameEmbed().build()).queue();
    }

    /**
     * Ends the game and notifies the players of the winner.
     *
     * @param winner the user who won the game
     */
    public void endGame(User winner) {
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Game Over!")
                .setDescription("The game between " + invitingPlayer.getName() + " and " + invitedPlayer.getName() + " has ended!")
                .addField("Winner: ", winner.getAsMention(), true)
                .setColor(Color.BLUE).build()).queue();

        gameManager.removeGame(invitingPlayer, invitedPlayer);
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
                    String channelID = GameManager.getChannelID();
                    TextChannel channel = getShardManager().getTextChannelById(channelID);
                    channel.sendMessage("Times up! Game between " + invitingPlayer.getAsMention() + " and " + invitedPlayer.getAsMention() + " has ended!").queue();
                }
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

    /**
     * Gets the user who initiated the game.
     *
     * @return the inviting player
     */
    public User getInvitingPlayer() {
        return invitingPlayer;
    }

    /**
     * Gets the user who was invited to join the game.
     *
     * @return the invited player
     */
    public User getInvitedPlayer() {
        return invitedPlayer;
    }

    /**
     * Determines the starting player for the game.
     *
     * @return the user who will start the game
     */
    public User getStartingPlayer() {
        return new Random().nextBoolean() ? invitingPlayer : invitedPlayer;
    }

    /**
     * Checks if a user is a player in the game.
     *
     * @param player the user to check
     * @return true if the user is a player, false otherwise
     */
    public boolean isPlayer(User player) {
        return player.equals(invitingPlayer) || player.equals(invitedPlayer);
    }
}