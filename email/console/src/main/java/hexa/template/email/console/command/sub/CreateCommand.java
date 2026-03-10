package hexa.template.email.console.command.sub;

import hexa.template.email.console.command.SafeCommand;
import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.SaveEmail;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name ="create",
        description = "create email",
        mixinStandardHelpOptions = true
)
public class CreateCommand extends SafeCommand {
    private final SaveEmail save;

    @Option(
            names = { "-e", "--email" },
            description = "email address",
            required = true
    )
    String emailValue;

    public CreateCommand(
            final SaveEmail save
    ) {
        this.save = save;
    }

    @Override
    protected void runSafe() {
        System.out.println("create email...");
        final var email = new Email(emailValue);
        final var savedEmail = save.save(email);
        System.out.println("email saved: " + savedEmail);
    }
}
