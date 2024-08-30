/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */

package com.chalwk.Listeners;

import com.chalwk.game.Game;
import com.chalwk.game.GameManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static com.chalwk.bot.BotInitializer.getGameManager;
import static com.chalwk.game.Game.createGameEmbed;

public class EventListeners extends ListenerAdapter {

    /**
     * Updates the game embed with the current game state.
     *
     * @param game  the game to update
     * @param event the event that triggered the update
     */
    private static void updateEmbed(Game game, MessageReceivedEvent event) {
        event.getMessage().delete().queue();
        EmbedBuilder embed = createGameEmbed(game);
        event.getChannel()
                .retrieveMessageById(game.getEmbedID())
                .queue(message -> message.editMessageEmbeds(embed.build()).queue());
    }

    /**
     * Displays a welcome message when the bot is ready to receive commands.
     *
     * @param event the event that triggered the welcome message
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        // TODO: REPLACE THIS ASCII ART WITH RELEVANT INFORMATION
        System.out.println("""
                __________________________________________________________
                 _    _                                            ┌─────┐
                | |  | |                                           │     │
                | |__| | __ _ _ __   __ _ _ __ ___   __ _ _ __     │     O
                |  __  |/ _` | '_ \\ / _` | '_ ` _ \\ / _` | '_      │    /|\\
                | |  | | (_| | | | | (_| | | | | | | (_| | | | |   │    / \\
                |_|  |_|\\__,_|_| |_|\\__, |_| |_| |_|\\__,_|_| |_|   │
                                     __/ |                         └─────┘
                                    |___/
                __________________________________________________________""");
    }

    /**
     * Handles player moves in messaged-based games.
     *
     * @param event the event that triggered the player move
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        User player = event.getAuthor();
        if (player.isBot()) return; // ignore bots

        GameManager gameManager = getGameManager();
        if (!gameManager.isInGame(player)) return; // only players in a game can play

        Game game = gameManager.getGame(player);

        if (!game.isPlayer(player)) return; // only the players in this specific game can play
        if (notYourTurn(event, game, player)) return; // only the player whose turn it is can play

        game.setWhosTurn();
        updateEmbed(game, event);
    }

    /**
     * Checks if it is the player's turn.
     *
     * @param event  the event that triggered the player move
     * @param game   the game the player is in
     * @param player the player who made the move
     * @return true if it is not the player's turn, false otherwise
     */
    private boolean notYourTurn(@NotNull MessageReceivedEvent event, Game game, User player) {
        User whos_turn = game.getWhosTurn();
        if (!player.equals(whos_turn)) {
            event.getMessage().delete().queue();
            return true;
        }
        return false;
    }
}