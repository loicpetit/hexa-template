package hexa.template.email.console;

import hexa.template.email.console.command.EmailCommand;
import hexa.template.email.console.command.create.CreateCommand;
import hexa.template.email.console.command.get.GetCommand;
import picocli.CommandLine;

public class Application {
        public static void main(String[] args) {
            int exitCode = new CommandLine(new EmailCommand())
                    .addSubcommand(new GetCommand())
                    .addSubcommand(new CreateCommand())
                    .execute(args);
            System.exit(exitCode);
        }
}
