package com.github.KostyaSha.tests;

import com.github.KostyaSha.utils.DockerHostRule;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Ports;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Kanstantsin Shautsou
 */
public class SomeTest {

    public static ArrayList<String> plugins = new ArrayList<String>(Arrays.asList(
            "http://mirrors.jenkins-ci.org/plugins/ssh-credentials/latest/ssh-credentials.hpi",
            "http://mirrors.jenkins-ci.org/plugins/token-macro/latest/token-macro.hpi",
            "http://mirrors.jenkins-ci.org/plugins/ssh-slaves/latest/ssh-slaves.hpi"
    ));

    @ClassRule
    public static DockerHostRule d = new DockerHostRule("192.168.1.4");

    @Test
    public void someTest() {
        System.out.println(d.cli.infoCmd().exec());
        final String id = d.cli.createContainerCmd("jenkins:1.609.3")
                .withName("jenkins-master")
                .withPublishAllPorts(true)
                .exec().getId();
        d.cli.startContainerCmd(id).exec();
        final InspectContainerResponse inspect = d.cli.inspectContainerCmd(id).exec();
        final Ports.Binding[] bindings = inspect.getNetworkSettings().getPorts().getBindings().get("8080");
        String jenkinsHost;
        Integer jenkinsPort = null;
        for (Ports.Binding b : bindings) {
            jenkinsPort = b.getHostPort();
            jenkinsHost = b.getHostIp();
        }
    }


    @Test
    public void checkRule() {

    }
}
