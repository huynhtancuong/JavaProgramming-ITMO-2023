package common.data;

public interface Command {
    String getDescription();
    String getName();
    boolean execute(String argument, Object objectArgument);
}
