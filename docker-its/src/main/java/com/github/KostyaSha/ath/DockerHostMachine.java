package com.github.KostyaSha.ath;

import org.jenkinsci.test.acceptance.machine.SimpleMachine;
import org.jenkinsci.test.acceptance.machine.SimpleMachineProvider;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerHostMachine extends SimpleMachine {
    public DockerHostMachine(DockerHostMachineProvider machineProvider, String id, String ipAddress, String user) {
        super(machineProvider, id, ipAddress, user);
    }
}
