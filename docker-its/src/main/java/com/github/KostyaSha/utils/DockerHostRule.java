package com.github.KostyaSha.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.DockerCmdExecFactoryImpl;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Connects to remote Docker Host and provides client
 *
 * @author Kanstantsin Shautsou
 */
public class DockerHostRule implements TestRule {
    @CheckForNull
    public DockerClient cli;

    private String sshUser = "docker";
    private String sshPass = "tcuser";
    private String host;
    private int dockerPort = 2375;


    public DockerHostRule() {
    }

    public DockerHostRule(String sshUser, String sshPass, @Nonnull String host, int dockerPort) {
        this.sshUser = sshUser;
        this.sshPass = sshPass;
        this.host = host;
        this.dockerPort = dockerPort;
    }

    public String getDockerUri() {
        return "http://" + host + ":" + dockerPort;
    }

    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();

                base.evaluate();
            }
        };
    }

    private void before() {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withUri(getDockerUri())
                .build();

        DockerCmdExecFactoryImpl dockerCmdExecFactory = new DockerCmdExecFactoryImpl()
                .withReadTimeout(10000)
                .withConnectTimeout(10000);

        cli = DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();
    }
}
