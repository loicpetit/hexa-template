package hexa.template.email.console.command.sub;

import hexa.template.email.console.command.SafeCommand;
import picocli.CommandLine.Command;

@Command(
        name = "exit"
)
public class ExitCommand extends SafeCommand {
    private boolean exit = false;

    public boolean isExit() {
        return this.exit;
    }

    @Override
    public void runSafe() {
        exit = true;
    }
}
