/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */

package com.chalwk.util;

import com.chalwk.game.GameManager;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class settings {

    public static final int DEFAULT_TIME_LIMIT = 300;

    public static int getDefaultTimeLimit() {
        return DEFAULT_TIME_LIMIT;
    }

    public static boolean notCorrectChannel(SlashCommandInteractionEvent event) {
        String thisChannel = event.getChannel().getId();
        String requiredChannel = GameManager.getChannelID();

        if (requiredChannel.isEmpty()) {
            event.reply("""
                    # Game is not set up.
                    Please set the channel for the game to use first.
                    Ask an admin to use the `/setchannel` command.
                    """).setEphemeral(true).queue();
            return true;
        } else if (!thisChannel.equals(requiredChannel)) {
            Channel channel = event.getGuild().getTextChannelById(requiredChannel);

            if (channel != null) {
                event.reply("This game only works in " + channel).setEphemeral(true).queue();
            } else {
                event.reply("The required channel is not available").setEphemeral(true).queue();
            }
            return true;
        }
        return false;
    }
}
