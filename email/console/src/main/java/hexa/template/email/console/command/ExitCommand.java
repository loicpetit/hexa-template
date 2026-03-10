package hexa.template.email.console.command;

import picocli.CommandLine.Command;

@Command(
        name = ExitCommand.COMMAND
)
public class ExitCommand extends SafeCommand {
    public static final String COMMAND = "exit";

    private boolean exit = false;

    public boolean isExit() {
        return this.exit;
    }

    @Override
    public void runSafe() {
        exit = true;
    }
}
