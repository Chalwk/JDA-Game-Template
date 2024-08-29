/* Copyright (c) 2024 Jericho Crosby <jericho.crosby227@gmail.com>. Licensed under GNU General Public License v3.0.
   See the LICENSE file or visit https://www.gnu.org/licenses/gpl-3.0.en.html for details. */
package com.chalwk.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A utility class for handling the bot authentication process, specifically for reading
 * the authentication token from a file.
 */
public class authentication {

    /**
     * Retrieves the authentication token from a file named "auth.token" for the bot to connect
     * to the Discord server.
     *
     * @return The authentication token as a String.
     * @throws IOException if an error occurs while reading the file.
     */
    public static String getToken() throws IOException {
        try (BufferedReader text = new BufferedReader(new FileReader("auth.token"))) {
            return text.readLine();
        }
    }
}