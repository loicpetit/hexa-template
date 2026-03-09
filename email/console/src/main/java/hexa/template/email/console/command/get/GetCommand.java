package hexa.template.email.console.command.get;

import hexa.template.email.console.command.SafeCommand;
import hexa.template.email.console.core.EmailFactory;
import hexa.template.email.core.usecase.GetEmails;
import picocli.CommandLine.Command;

@Command(
        name ="get",
        description = "get email by id",
        mixinStandardHelpOptions = true
)
public class GetCommand extends SafeCommand {
    private final GetEmails getter;

    public GetCommand() {
        this(EmailFactory.get());
    }

    public GetCommand(
            final EmailFactory factory
    ) {
        getter = factory.getEmails();
    }

    @Override
    protected void runSafe() {
        System.out.println("get email...");
        final var email = getter.getEmailById(1L);
        System.out.println("email found: " + email);
    }
}
