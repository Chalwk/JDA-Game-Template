/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.commands;

import com.chalwk.CommandManager.CommandCooldownManager;
import com.chalwk.CommandManager.CommandInterface;
import com.chalwk.game.GameManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a command for accepting a game invite in the context of a game or a chat bot.
 */
public class accept implements CommandInterface {

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
    public accept(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getDescription() {
        return "Accept an invite to play a game";
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

        User acceptingPlayer = event.getUser();

        if (gameManager.isInGame(acceptingPlayer)) {
            event.reply("You are already in a game.").setEphemeral(true).queue();
            return;
        }

        if (gameManager.getPendingInvites().containsKey(acceptingPlayer)) {
            gameManager.acceptInvite(acceptingPlayer, event);
        } else {
            event.reply("You don't have any pending invites.").setEphemeral(true).queue();
        }

        COOLDOWN_MANAGER.setCooldown("accept", event.getUser());
    }
}
