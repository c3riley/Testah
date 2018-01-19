package org.testah.util.dto;

import org.testah.util.SshUtil;

public class ShellInfoDto {

    private StringBuilder output = new StringBuilder();
    private int exitCode = SshUtil.LAST_EXIT_CODE_DEFAULT;

    public ShellInfoDto() {

    }

    public StringBuilder getOutput() {
        return output;
    }

    public void setOutput(final StringBuilder output) {
        this.output = output;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(final int exitCode) {
        this.exitCode = exitCode;
    }

}
