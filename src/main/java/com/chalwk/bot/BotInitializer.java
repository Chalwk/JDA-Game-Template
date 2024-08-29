/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */

package com.chalwk.bot;

import com.chalwk.CommandManager.CommandListener;
import com.chalwk.Listeners.EventListeners;
import com.chalwk.commands.accept;
import com.chalwk.commands.decline;
import com.chalwk.commands.invite;
import com.chalwk.game.GameManager;
import com.chalwk.util.authentication;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.IOException;

/**
 * A class responsible for initializing and setting up the bot for the Virtual Pets game project.
 */
public class BotInitializer {

    /**
     * An instance of the PetDataHandler class to manage pet data.
     */
    public static ShardManager shardManager;
    /**
     * The bot's authentication token.
     */
    private final String token;

    /**
     * Constructs a BotInitializer instance and retrieves the bot's authentication token.
     *
     * @throws IOException if there's an error reading the token file.
     */
    public BotInitializer() throws IOException {
        this.token = authentication.getToken();
    }

    public static ShardManager getShardManager() {
        return shardManager;
    }

    /**
     * Initializes the bot and sets up event listeners and commands.
     */
    public void initializeBot() {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(this.token)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("GAME"))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.MESSAGE_CONTENT);

        shardManager = builder.build();
        shardManager.addEventListener(new EventListeners());
        registerCommands(shardManager);
    }

    /**
     * Registers the available commands for the bot.
     *
     * @param shardManager The ShardManager instance used to manage the bot.
     */
    private void registerCommands(ShardManager shardManager) {
        CommandListener commands = new CommandListener();

        GameManager gameManager = new GameManager();

        commands.add(new invite(gameManager));
        commands.add(new accept(gameManager));
        commands.add(new decline(gameManager));
        shardManager.addEventListener(commands);
    }
}
