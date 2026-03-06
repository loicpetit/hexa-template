package hexa.template.email.console;

import hexa.template.email.console.command.get.GetCommand;
import picocli.CommandLine;

public class Application {
        public static void main(String[] args) {
            int exitCode = new CommandLine(new GetCommand()).execute(args);
            System.exit(exitCode);
        }
}
