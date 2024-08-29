/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.CommandManager;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.List;

/**
 * Sets up abstract methods for commands that will be used in the CommandManager class.
 * This interface defines the common methods required for any command implementation.
 */
public interface CommandInterface {
    /**
     * Returns the name of the command.
     *
     * @return The command name as a String.
     */
    String getName();

    /**
     * Returns the description of the command.
     *
     * @return The command description as a String.
     */
    String getDescription();

    /**
     * Returns the options available for the command.
     *
     * @return A list of OptionData objects representing the command options.
     */
    List<OptionData> getOptions();

    /**
     * Executes the command with the given event.
     *
     * @param event The SlashCommandInteractionEvent object containing event details.
     * @throws IOException if there's an error during command execution.
     */
    void execute(SlashCommandInteractionEvent event) throws IOException;
}