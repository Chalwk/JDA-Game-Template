/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.game;

import com.chalwk.util.settings;
import net.dv8tion.jda.api.entities.User;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a game between two players, managing game-related operations such as starting a game and scheduling game end tasks.
 */
public class Game {


    private final GameManager gameManager;
    /**
     * The user who initiated the game.
     */
    private final User invitingPlayer;
    /**
     * The user who was invited to join the game.
     */
    private final User invitedPlayer;
    /**
     * The start time of the game.
     */
    private Date startTime;

    /**
     * Creates a new Game instance for the specified players and assigns a GameManager.
     *
     * @param invitingPlayer the user who initiated the game
     * @param invitedPlayer  the user who was invited to join the game
     * @param gameManager    the GameManager managing game operations
     */
    public Game(User invitingPlayer, User invitedPlayer, GameManager gameManager) {
        this.gameManager = gameManager;
        this.invitingPlayer = invitingPlayer;
        this.invitedPlayer = invitedPlayer;
    }

    /**
     * Starts the game, sends a notification to both players, and schedules the game end task.
     */
    public void startGame() {
        this.startTime = new Date();
        gameManager.sendPrivateMessage(invitingPlayer, "Game started with " + invitedPlayer.getName() + "!");
        gameManager.sendPrivateMessage(invitedPlayer, "Game started with " + invitingPlayer.getName() + "!");
        scheduleGameEndTask();
    }

    /**
     * Schedules a task to end the game when the default time limit is reached.
     */
    private void scheduleGameEndTask() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isTimeUp()) {
                    this.cancel();
                    gameManager.sendPrivateMessage(invitingPlayer, "Time's up! Game with " + invitedPlayer.getName() + " has ended.");
                    gameManager.sendPrivateMessage(invitedPlayer, "Time's up! Game with " + invitingPlayer.getName() + " has ended.");
                }
            }
        }, 0, 1000);
    }

    /**
     * Checks if the default time limit for the game has been exceeded.
     *
     * @return true if the time limit has been exceeded, false otherwise
     */
    private boolean isTimeUp() {
        long elapsedTime = System.currentTimeMillis() - startTime.getTime();
        return elapsedTime > settings.getDefaultTimeLimit() * 1000L;
    }
}