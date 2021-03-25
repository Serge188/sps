package ru.sps.integration.users;

import org.springframework.stereotype.Service;
import ru.sps.integration.users.command.Command;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserCommandExecutor {

    private Map<String, Command> commandsMap;

    public UserCommandExecutor(List<Command> commands) {
        this.commandsMap = commands.stream().collect(Collectors.toMap(Command::getName, v -> v));
    }

    public String execute(String text) {

        if (text.startsWith("/help")) return getHelpText();

        var command = text.split(" ")[0];
        var param = text.contains(" ") ? text.split(" ")[1] : "";
        if (commandsMap.containsKey(command)) {
            return commandsMap.get(command).execute(param);
        } else {
            return ("Unknown command: " + command + ". Use /help to get list of commands");
        }
    }

    private String getHelpText() {
        return commandsMap.values().stream().map(c -> c.getName() + " - " + c.getDescription()).collect(Collectors.joining("\n"));
    }
}
