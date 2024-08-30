/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */

package com.chalwk.Listeners;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventListeners extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
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
}