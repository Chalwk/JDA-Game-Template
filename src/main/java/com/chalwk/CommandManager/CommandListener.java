/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.CommandManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A listener class for registering and handling commands in the Virtual Pets game project.
 * It extends JDA's ListenerAdapter class and overrides its methods to manage command execution.
 */
public class CommandListener extends ListenerAdapter {

    /**
     * A list of CommandInterface implementations representing available commands.
     */
    private final List<CommandInterface> commands = new ArrayList<>();

    /**
     * Registers all available commands in all guilds upon JDA's ready event.
     *
     * @param event The ReadyEvent object containing JDA's initialization details.
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (CommandInterface command : commands) {
                guild.upsertCommand(command.getName(), command.getDescription()).addOptions(command.getOptions()).queue();
            }
        }
    }

    /**
     * Executes the appropriate command based on the given slash command interaction event.
     *
     * @param event The SlashCommandInteractionEvent object containing command event details.
     * @throws IOException if an error occurs during command execution.
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (CommandInterface command : commands) {
            String cmd = event.getName();
            if (cmd.equals(command.getName())) {
                try {
                    command.execute(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }

    /**
     * Adds a new command to the list of available commands.
     *
     * @param command The CommandInterface instance representing the new command.
     */
    public void add(CommandInterface command) {
        commands.add(command);
    }
}