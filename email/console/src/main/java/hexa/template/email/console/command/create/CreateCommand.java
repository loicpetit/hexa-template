package hexa.template.email.console.command.create;

import hexa.template.email.console.command.SafeCommand;
import hexa.template.email.console.core.EmailFactory;
import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.SaveEmail;
import picocli.CommandLine.Command;

@Command(
        name ="create",
        description = "create email",
        mixinStandardHelpOptions = true
)
public class CreateCommand extends SafeCommand {
    private final SaveEmail save;

    public CreateCommand() {
        this(EmailFactory.get());
    }

    public CreateCommand(
            final EmailFactory factory
    ) {
        save = factory.saveEmail();
    }

    @Override
    protected void runSafe() {
        System.out.println("create email...");
        final var email = new Email("test@mail.com");
        final var savedEmail = save.save(email);
        System.out.println("email saved: " + savedEmail);
    }
}
