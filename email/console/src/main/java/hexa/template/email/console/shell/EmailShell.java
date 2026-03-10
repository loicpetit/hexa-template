package hexa.template.email.console.shell;

import hexa.template.email.console.command.EmailCommand;
import hexa.template.email.console.command.ExitCommand;
import org.jline.jansi.AnsiConsole;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.shell.jline3.PicocliJLineCompleter;

import java.io.IOException;

public class EmailShell {
    public void start() {
        try (Terminal terminal = createTerminal()) {
            final var commandline = new CommandLine(new EmailCommand());
            final var reader = createLineReader(
                    terminal,
                    createCompleter(commandline)
            );
            final var exitCommand = getExitCommand(commandline);
            while (!exitCommand.isExit()) {
                final String command = reader.readLine("email> ");
                commandline.execute(parseCommandArgs(
                        reader.getParser(),
                        command
                ));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }

    private Terminal createTerminal() throws IOException {
        return TerminalBuilder.builder()
                .system(true)
                .build();
    }

    private LineReader createLineReader(
            final Terminal terminal,
            final Completer completer
            ) {
        return LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .build();
    }

    private Completer createCompleter(final CommandLine commandline) {
        final var picocliCompleter = new PicocliJLineCompleter(commandline.getCommandSpec());
        final var exitCompleter = new StringsCompleter(ExitCommand.COMMAND);
        return new AggregateCompleter(picocliCompleter, exitCompleter);
    }

    private ExitCommand getExitCommand(final CommandLine mainCommand) {
        return (ExitCommand) mainCommand.getSubcommands()
                .get(ExitCommand.COMMAND)
                .getCommandSpec()
                .userObject();
    }

    private String[] parseCommandArgs(
            final Parser parser,
            final String line
    ) {
        return parser.parse(line, 0)
                .words()
                .toArray(new String[0]);
    }
}
