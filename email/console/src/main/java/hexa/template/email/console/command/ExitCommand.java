package hexa.template.email.console.command;

import picocli.CommandLine;

import java.util.Optional;

@CommandLine.Command(
        name = ExitCommand.COMMAND
)
public class ExitCommand {
    public static final String COMMAND = "exit";

    public static boolean isExit(final String command) {
        return Optional.ofNullable(command)
                .map(String::trim)
                .map(COMMAND::equalsIgnoreCase)
                .orElse(false);
    }
}
