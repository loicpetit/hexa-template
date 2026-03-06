package hexa.template.email.console.command.create;

import picocli.CommandLine.Command;

@Command(
        name ="create",
        description = "create email",
        mixinStandardHelpOptions = true
)
public class CreateCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("CreateCommand executed");
    }
}
