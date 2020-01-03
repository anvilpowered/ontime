package rocks.milspecsg.msontime.commands;

@FunctionalInterface
public interface CommandManager {

    void register(Object plugin);
}
