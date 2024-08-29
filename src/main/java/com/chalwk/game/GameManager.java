/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.game;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages game-related operations, including creating games, inviting players, and managing pending invites.
 */
public class GameManager {

    private final Map<User, Game> games;
    private final Map<User, User> pendingInvites;

    /**
     * Initializes an empty map for storing active games and pending invites.
     */
    public GameManager() {
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

    /**
     * Creates a new game and adds the inviting and invited players to it.
     *
     * @param invitingPlayer the user who initiated the game
     * @param invitedPlayer  the user who was invited to join the game
     */
    public void createGame(User invitingPlayer, User invitedPlayer) {
        if (!isInGame(invitingPlayer) && !isInGame(invitedPlayer)) {
            Game game = new Game(invitingPlayer, invitedPlayer, this);
            game.startGame();
            games.put(invitingPlayer, game);
            games.put(invitedPlayer, game);
        }
    }

    /**
     * Sends a private message to the specified user.
     *
     * @param user    the recipient of the message
     * @param message the content of the message
     */
    public void sendPrivateMessage(User user, String message) {
        user.openPrivateChannel().queue(channel -> {
            channel.sendMessage(message).queue();
        });
    }

    /**
     * Invites a player to join a game.
     *
     * @param invitingPlayer the user who initiated the game
     * @param invitedPlayer  the user who was invited to join the game
     */
    public void invitePlayer(User invitingPlayer, User invitedPlayer) {
        if (!isInGame(invitingPlayer) && !isInGame(invitedPlayer)) {
            pendingInvites.put(invitedPlayer, invitingPlayer);
            sendPrivateMessage(invitedPlayer, "You have been invited to play a game by " + invitingPlayer.getName() + "!");
            sendPrivateMessage(invitingPlayer, "Invite sent to " + invitedPlayer.getName() + "!");
        } else {
            sendPrivateMessage(invitingPlayer, "You or the invited player are already in a game.");
        }
    }

    /**
     * Accepts a pending invite and creates a new game with the inviting and invited players.
     *
     * @param invitedPlayer the user who accepted the invite
     */
    public void acceptInvite(User invitedPlayer) {
        User invitingPlayer = pendingInvites.get(invitedPlayer);
        pendingInvites.remove(invitedPlayer);
        createGame(invitingPlayer, invitedPlayer);
        sendPrivateMessage(invitedPlayer, "You accepted the invite from " + invitingPlayer.getName() + "!");
        sendPrivateMessage(invitingPlayer, "Your invite was accepted by " + invitedPlayer.getName() + "!");
    }

    /**
     * Declines a pending invite and notifies the inviting player.
     *
     * @param invitedPlayer the user who declined the invite
     */
    public void declineInvite(User invitedPlayer) {
        User invitingPlayer = pendingInvites.get(invitedPlayer);
        sendPrivateMessage(invitedPlayer, "You declined the invite.");
        sendPrivateMessage(invitingPlayer, invitedPlayer.getName() + " declined your invite.");
        pendingInvites.remove(invitedPlayer);
    }

    /**
     * Returns the map of pending invites.
     *
     * @return a map containing the pending invites
     */
    public Map<User, User> getPendingInvites() {
        return pendingInvites;
    }
}
