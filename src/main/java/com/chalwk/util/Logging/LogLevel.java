/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.util.Logging;

public enum LogLevel {
    SEVERE(0),
    WARNING(1),
    INFO(2),
    CONFIG(3),
    FINE(4),
    FINER(5),
    FINEST(6),
    ALL(7);

    private final int levelValue;

    LogLevel(int value) {
        this.levelValue = value;
    }

    public int getValue() {
        return levelValue;
    }
}
