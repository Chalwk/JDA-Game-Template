/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */

package com.chalwk.game;

import net.dv8tion.jda.api.entities.User;

public class GameInvite {

    private final User invitingPlayer;
    private final User invitedPlayer;

    public GameInvite(User invitingPlayer, User invitedPlayer) {
        this.invitingPlayer = invitingPlayer;
        this.invitedPlayer = invitedPlayer;
    }

    public User getInvitingPlayer() {
        return invitingPlayer;
    }

    public User getInvitedPlayer() {
        return invitedPlayer;
    }
}
