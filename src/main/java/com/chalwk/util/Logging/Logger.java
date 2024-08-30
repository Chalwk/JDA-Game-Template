/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.util.Logging;

import java.io.PrintStream;
import java.util.Date;

public class Logger {

    private static final PrintStream stream = System.err;
    private static LogLevel logLevel = LogLevel.WARNING;

    /**
     * Sets the log level for the logger.
     *
     * @param level the log level to set
     */
    public static void setLogLevel(LogLevel level) {
        logLevel = level;
    }

    /**
     * Logs a message with the specified log level.
     *
     * @param level   the log level
     * @param message the message to log
     */
    public static void log(LogLevel level, String message) {
        if (level.getValue() >= logLevel.getValue()) {
            log(level.name() + " - " + message);
        }
    }

    // Convenience methods for logging at different levels (severe, warning, info, etc.)
    // These methods call the log method with the appropriate log level

    public static void severe(String message) {
        log(LogLevel.SEVERE, message);
    }

    public static void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    public static void info(String message) {
        log(LogLevel.INFO, message);
    }

    public static void config(String message) {
        log(LogLevel.CONFIG, message);
    }

    public static void fine(String message) {
        log(LogLevel.FINE, message);
    }

    public static void finer(String message) {
        log(LogLevel.FINER, message);
    }

    public static void finest(String message) {
        log(LogLevel.FINEST, message);
    }

    private static void log(String message) {
        stream.println(new Date() + " - " + message);
    }
}
