package ru.sps.integration.users.command;

public interface Command {

    String getName();

    String getDescription();

    String execute(String param);
}
