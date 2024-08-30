/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.commands;

import com.chalwk.CommandManager.CommandCooldownManager;
import com.chalwk.CommandManager.CommandInterface;
import com.chalwk.game.GameManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

import static com.chalwk.util.fileIO.isChannelIdConfigured;
import static com.chalwk.util.fileIO.saveChannelID;

/**
 * Represents a command for setting or removing the channel for the game to use.
 */
public class channel implements CommandInterface {

    private static final CommandCooldownManager COOLDOWN_MANAGER = new CommandCooldownManager();
    private final GameManager gameManager;

    public channel(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "channel";
    }

    @Override
    public String getDescription() {
        return "Set or remove the channel for the game to use (admins only)";
    }

    public List<OptionData> getOptions() {

        List<OptionData> options = new ArrayList<>();

        OptionData operation = new OptionData(OptionType.STRING, "operation", "The type of operation to perform", true);
        operation.addChoice("add", "add");
        operation.addChoice("remove", "remove");

        OptionData channel = new OptionData(OptionType.CHANNEL, "channel", "The channel to set or remove", true);
        options.add(operation);
        options.add(channel);

        return options;
    }

    /**
     * Executes the accept command when called.
     *
     * @param event the event associated with the command execution
     */
    @Override
    public void execute(SlashCommandInteractionEvent event) {

        if (COOLDOWN_MANAGER.isOnCooldown(event)) return;

        String optionName = event.getOptions().get(0).getName();
        String optionValue = event.getOption(optionName).getAsString();

        TextChannel channel = event.getOption("channel").getAsChannel().asTextChannel();

        String channelID = channel.getId();
        boolean isAddOperation = optionValue.equals("add");

        if (initialErrorChecking(event, channel)) return;
        if (isAddOperation) {
            if (isChannelIdConfigured(channelID)) {
                event.reply("## Channel ID is already configured!").setEphemeral(true).queue();
                return;
            }
        } else {
            if (!isChannelIdConfigured(channelID)) {
                event.reply("## Channel ID is not configured! Unable to remove.").setEphemeral(true).queue();
                return;
            }
        }

        saveChannelID(channelID, isAddOperation, event, gameManager);

        COOLDOWN_MANAGER.setCooldown(getName(), event.getUser());
    }

    private boolean initialErrorChecking(SlashCommandInteractionEvent event, TextChannel channel) {
        Member member = event.getMember();
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("## You must be an administrator to use this command!").setEphemeral(true).queue();
            return false;
        } else if (channel == null) {
            event.reply("## Invalid channel ID!").setEphemeral(true).queue();
            return true;
        }
        return false;
    }
}