package com.github.KostyaSha.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.DockerCmdExecFactoryImpl;
import org.jenkinsci.test.acceptance.Ssh;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;

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

    public DockerHostRule(String host) {
        this.host = host;
    }

    public void setSshUser(String sshUser) {
        this.sshUser = sshUser;
    }

    public void setSshPass(String sshPass) {
        this.sshPass = sshPass;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setDockerPort(int dockerPort) {
        this.dockerPort = dockerPort;
    }

    public String getDockerUri() {
        return "http://" + host + ":" + dockerPort;
    }

    public String getHost() {
        return host;
    }

    public String getSshUser() {
        return sshUser;
    }

    public String getSshPass() {
        return sshPass;
    }

    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                prepareDockerCli();
                checkSsh();
                base.evaluate();
            }
        };
    }

    private void prepareDockerCli() {
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

    private void checkSsh() {
        getSsh().executeRemoteCommand("env");
    }

    public Ssh getSsh() {
        try (Ssh ssh = new Ssh(getHost())) {
            ssh.getConnection().authenticateWithPassword(getSshUser(), getSshPass());
            return ssh;
        } catch (IOException e) {
            throw new AssertionError("Failed to create ssh connection", e);
        }
    }

}
