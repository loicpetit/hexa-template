package hexa.template.email.console.command;

import hexa.template.email.console.command.sub.CreateCommand;
import hexa.template.email.console.command.sub.ExitCommand;
import hexa.template.email.console.command.sub.GetCommand;
import hexa.template.email.console.core.EmailFactory;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

public class CommandFactory implements IFactory {
    private final GetCommand getCommand;
    private final CreateCommand createCommand;
    private final ExitCommand exitCommand;

    public CommandFactory() {
        this(EmailFactory.get());
    }

    public CommandFactory(
            final EmailFactory emailFactory
    ) {
        this.getCommand = new GetCommand(emailFactory.getEmails());
        this.createCommand = new CreateCommand(emailFactory.saveEmail());
        this.exitCommand = new ExitCommand();
    }

    public ExitCommand exitCommand() {
        return exitCommand;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        if (cls.isInstance(getCommand)) {
            return cls.cast(getCommand);
        }
        if (cls.isInstance(createCommand)) {
            return cls.cast(createCommand);
        }
        if (cls.isInstance(exitCommand)) {
            return cls.cast(exitCommand);
        }
        return CommandLine.defaultFactory().create(cls);
    }
}
