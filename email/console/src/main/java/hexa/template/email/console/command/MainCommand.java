package hexa.template.email.console.command;

import hexa.template.email.console.command.sub.CreateCommand;
import hexa.template.email.console.command.sub.ExitCommand;
import hexa.template.email.console.command.sub.GetCommand;
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
public class MainCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("EmailCommand executed");
    }
}
