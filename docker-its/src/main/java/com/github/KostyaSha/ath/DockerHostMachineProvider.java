package com.github.KostyaSha.ath;

import org.jenkinsci.test.acceptance.Authenticator;
import org.jenkinsci.test.acceptance.machine.Machine;
import org.jenkinsci.test.acceptance.machine.MachineProvider;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerHostMachineProvider implements MachineProvider {

    private final UserPasswordAuthenticator authenticator;
    private String user;
    private String password;
    private String ipAddress;

    public DockerHostMachineProvider(String user, String password, String ipAddress) {
        this.user = user;
        this.password = password;
        this.ipAddress = ipAddress;
        this.authenticator = new UserPasswordAuthenticator(user, password);
    }

    public int[] getAvailableInboundPorts() {
        return new int[0];
    }

    public Authenticator authenticator() {
        return authenticator;
    }

    public Machine get() {
        return new DockerHostMachine(this, "docker-host", ipAddress, user);
    }
}
