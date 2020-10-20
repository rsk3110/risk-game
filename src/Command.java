import java.util.*;

public interface Command {
    public void execute();

    public void execute(List<String> args);
}
