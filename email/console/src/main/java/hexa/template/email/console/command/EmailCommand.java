package hexa.template.email.console.command;

import hexa.template.email.console.command.create.CreateCommand;
import hexa.template.email.console.command.get.GetCommand;
import picocli.CommandLine.Command;

@Command(
        name ="email",
        description = "Email CLI",
        subcommands = {
                CreateCommand.class,
                ExitCommand.class,
                GetCommand.class
        }
)
public class EmailCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("EmailCommand executed");
    }
}
