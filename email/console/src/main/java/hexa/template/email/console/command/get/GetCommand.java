package hexa.template.email.console.command.get;

import picocli.CommandLine.Command;

@Command(name ="get", description = "Get email by id")
public class GetCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("GetCommand executed");
    }
}
