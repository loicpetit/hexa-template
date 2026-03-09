package hexa.template.email.console.command;

public abstract class SafeCommand implements Runnable {
    @Override
    public void run() {
        try {
            runSafe();
        } catch (final Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    protected abstract void runSafe();
}
