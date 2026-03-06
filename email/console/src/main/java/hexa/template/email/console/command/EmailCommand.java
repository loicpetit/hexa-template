package hexa.template.email.console.command;

import picocli.CommandLine.Command;

@Command(
        name ="email",
        description = "Email CLI",
        mixinStandardHelpOptions = true
)
public class EmailCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("EmailCommand executed");
    }
}
