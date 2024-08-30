/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */

package com.chalwk.game;

import net.dv8tion.jda.api.entities.User;

public class GameInvite {

    private final User invitingPlayer;
    private final User invitedPlayer;

    /**
     * Creates a new GameInvite instance for the specified players.
     *
     * @param invitingPlayer the user who initiated the game
     * @param invitedPlayer  the user who was invited to join the game
     */
    public GameInvite(User invitingPlayer, User invitedPlayer) {
        this.invitingPlayer = invitingPlayer;
        this.invitedPlayer = invitedPlayer;
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
}
