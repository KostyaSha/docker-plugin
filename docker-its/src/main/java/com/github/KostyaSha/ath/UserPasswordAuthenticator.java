package com.github.KostyaSha.ath;

import com.trilead.ssh2.Connection;
import org.jenkinsci.test.acceptance.Authenticator;

import java.io.IOException;

/**
 * @author Kanstantsin Shautsou
 */
public class UserPasswordAuthenticator implements Authenticator {
    private String user;
    private String password;

    public UserPasswordAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public void authenticate(Connection connection) throws IOException {
        if (!connection.authenticateWithPassword(user, password)) {
            throw new IllegalArgumentException("Can't auth with user/password");
        }
    }
}
