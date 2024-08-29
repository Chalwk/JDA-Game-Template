/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.CommandManager;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages command cooldowns by storing and checking command usage timestamps for each user and command.
 */
public class CommandCooldownManager {

    /**
     * Default cooldown duration for commands, in seconds.
     */
    private static final long COOLDOWN_DURATION_SECONDS = 5;

    /**
     * A map containing user IDs as keys and another map for each user with command names as keys and timestamps as values.
     */
    private final ConcurrentMap<String, Map<String, Instant>> commandUserCooldowns = new ConcurrentHashMap<>();

    /**
     * Checks if a specific command is on cooldown for the given user.
     *
     * @param commandName the name of the command to check
     * @param user        the user executing the command
     * @return true if the command is not on cooldown, false otherwise
     */
    public boolean isOnCooldown(String commandName, User user) {
        return !checkCooldown(commandName, user);
    }

    /**
     * Sets a command on cooldown for the given user by updating their timestamp.
     *
     * @param commandName the name of the command to set on cooldown
     * @param user        the user executing the command
     */
    public void setCooldown(String commandName, User user) {
        Map<String, Instant> userCommandCooldowns = commandUserCooldowns.computeIfAbsent(user.getId(), k -> new ConcurrentHashMap<>());
        userCommandCooldowns.put(commandName, Instant.now());
    }

    /**
     * Checks if the given command is on cooldown for the specified user by comparing their last execution timestamp with
     * the current time.
     *
     * @param commandName the name of the command to check
     * @param user        the user executing the command
     * @return true if the command is on cooldown, false otherwise
     */
    private boolean checkCooldown(String commandName, User user) {
        Map<String, Instant> userCommandCooldowns = commandUserCooldowns.get(user.getId());
        if (userCommandCooldowns == null) {
            return false;
        }

        Instant lastExecutionTime = userCommandCooldowns.get(commandName);
        if (lastExecutionTime == null) {
            return false;
        }

        long elapsedTime = Duration.between(lastExecutionTime, Instant.now()).getSeconds();
        return elapsedTime >= getCooldownDuration();
    }

    /**
     * Handles a cooldown error by sending a message to the user and setting the command on cooldown.
     *
     * @param event the event containing the command and user
     */
    public void handleCooldownError(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        User user = event.getUser();
        Map<String, Instant> userCommandCooldowns = commandUserCooldowns.get(user.getId());
        Instant lastExecutionTime = userCommandCooldowns.get(commandName);
        long elapsedTime = Duration.between(lastExecutionTime, Instant.now()).getSeconds();
        long remainingTime = getCooldownDuration() - elapsedTime;
        if (remainingTime > 0) {
            event.reply(String.format("Cooldown in progress. Please wait %d seconds before using the command again.", remainingTime))
                    .setEphemeral(true).queue();
        }
    }

    /**
     * Checks if a command is on cooldown for the given user and handles cooldown errors if necessary.
     *
     * @param event the event containing the command and user
     * @return true if the command is on cooldown and an error has been handled, false otherwise
     */
    public boolean isOnCooldown(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        User user = event.getUser();
        Map<String, Instant> userCommandCooldowns = commandUserCooldowns.get(user.getId());
        if (userCommandCooldowns == null || !userCommandCooldowns.containsKey(commandName)) {
            return false;
        }
        if (isOnCooldown(commandName, user)) {
            handleCooldownError(event);
            return true;
        }
        return false;
    }

    /**
     * Gets the cooldown duration, in seconds, for commands.
     *
     * @return the cooldown duration
     */
    public long getCooldownDuration() {
        return COOLDOWN_DURATION_SECONDS;
    }
}