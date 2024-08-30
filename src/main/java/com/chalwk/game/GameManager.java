/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.chalwk.util.fileIO.loadChannelID;

/**
 * Manages game-related operations, including creating games, inviting players, and managing pending invites.
 */
public class GameManager {

    private final Map<User, Game> games;
    private final Map<User, GameInvite> pendingInvites;
    private static String channelID = "";

    /**
     * Initializes an empty map for storing active games and pending invites.
     */
    public GameManager() {
        channelID = loadChannelID();
        this.games = new HashMap<>();
        this.pendingInvites = new HashMap<>();
    }

    /**
     * Checks if a user is currently playing a game.
     *
     * @param player the user to check
     * @return true if the user is in a game, false otherwise
     */
    public boolean isInGame(User player) {
        return games.containsKey(player);
    }

    public static String getChannelID() {
        return GameManager.channelID;
    }

    public void setChannelID(String channelID) {
        GameManager.channelID = channelID;
    }

    /**
     * Accepts a pending invite and creates a new game with the inviting and invited players.
     *
     * @param invitedPlayer the user who accepted the invite
     * @param event         the event that triggered the invite acceptance
     */
    public void acceptInvite(User invitedPlayer, SlashCommandInteractionEvent event) {
        GameInvite invite = pendingInvites.get(invitedPlayer);
        User invitingPlayer = invite.getInvitingPlayer();
        if (isInGame(invitingPlayer)) {
            event.reply(invitingPlayer.getName() + " is already in a game.\nPlease wait until their current game is finished.").setEphemeral(true).queue();
            return;
        }
        createGame(invitingPlayer, invitedPlayer, event);
    }

    /**
     * Declines a pending invite and notifies the inviting player.
     *
     * @param invitedPlayer the user who declined the invite
     * @param event         the event that triggered the invite decline
     */
    public void declineInvite(User invitedPlayer, SlashCommandInteractionEvent event) {
        GameInvite invite = pendingInvites.get(invitedPlayer);
        User invitingPlayer = invite.getInvitingPlayer();
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Game Invite Declined")
                .setDescription(invitedPlayer.getAsMention() + " has declined the invite from " + invitingPlayer.getAsMention() + "!")
                .setColor(Color.RED).build()).queue();
        pendingInvites.remove(invitedPlayer);
    }

    /**
     * Returns the map of pending invites.
     *
     * @return a map containing the pending invites
     */
    public Map<User, GameInvite> getPendingInvites() {
        return pendingInvites;
    }

    public Game getGame(User player) {
        return games.get(player);
    }

    /**
     * Creates a new game and adds the inviting and invited players to it.
     *
     * @param invitingPlayer the user who initiated the game
     * @param invitedPlayer  the user who was invited to join the game
     * @param event          the event that triggered the game creation
     */
    public void createGame(User invitingPlayer, User invitedPlayer, SlashCommandInteractionEvent event) {
        Game game = new Game(invitingPlayer, invitedPlayer, event, this);
        pendingInvites.remove(invitedPlayer);
        games.put(invitingPlayer, game);
        games.put(invitedPlayer, game);
    }

    /**
     * Invites a player to join a game.
     *
     * @param invitingPlayer the user who initiated the game
     * @param invitedPlayer  the user who was invited to join the game
     * @param event          the event that triggered the invite
     */
    public void invitePlayer(User invitingPlayer, User invitedPlayer, SlashCommandInteractionEvent event) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Game Invite");

        if (!isInGame(invitingPlayer) && !isInGame(invitedPlayer)) {
            pendingInvites.put(invitedPlayer, new GameInvite(invitingPlayer, invitedPlayer));
            event.replyEmbeds(embed
                    .setDescription(invitingPlayer.getAsMention() + " has invited " + invitedPlayer.getAsMention() + " to play a game!")
                    .setFooter("Type /accept to join the game or /decline to decline the invite.")
                    .setColor(Color.GREEN).build()).queue();
        } else {
            event.replyEmbeds(embed
                    .setDescription("You or " + invitedPlayer.getName() + " are already in a game.")
                    .setColor(Color.RED).build()).setEphemeral(true).queue();
        }
    }

    public Map<User, Game> getGames() {
        return games;
    }

    public void removeGame(User invitingPlayer, User invitedPlayer) {
        this.getGames().remove(invitingPlayer);
        this.getGames().remove(invitedPlayer);
    }

    public void cancelInvite(User invitingPlayer) {
        pendingInvites.remove(invitingPlayer);
    }

    public String getInvitee(User invitingPlayer) {
        return pendingInvites.get(invitingPlayer).getInvitedPlayer().getAsMention();
    }
}
