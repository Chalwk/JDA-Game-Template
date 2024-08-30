/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.commands;

import com.chalwk.CommandManager.CommandCooldownManager;
import com.chalwk.CommandManager.CommandInterface;
import com.chalwk.game.GameManager;
import com.chalwk.util.settings;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command for canceling a game invite.
 */
public class cancel implements CommandInterface {

    /**
     * The cooldown manager for the command.
     */
    private static final CommandCooldownManager COOLDOWN_MANAGER = new CommandCooldownManager();

    /**
     * The game manager for managing game operations.
     */
    private final GameManager gameManager;

    /**
     * Initializes the accept command instance with the provided GameManager.
     *
     * @param gameManager the GameManager managing game operations
     */
    public cancel(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "cancel";
    }

    @Override
    public String getDescription() {
        return "Cancel an invite to play a game";
    }

    @Override
    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    /**
     * Executes the accept command when called.
     *
     * @param event the event associated with the command execution
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (COOLDOWN_MANAGER.isOnCooldown(event)) return;

        if (settings.notCorrectChannel(event)) return;

        User user = event.getUser();
        String invitee = gameManager.getInvitee(user);
        gameManager.cancelInvite(user);
        event.reply("Invite to play a game with " + invitee + " has been canceled.").setEphemeral(true).queue();

        COOLDOWN_MANAGER.setCooldown(getName(), event.getUser());
    }
}
