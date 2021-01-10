package ru.sps.integration;

import ru.sps.integration.viber.entries.IncomingMessageEntry;
import ru.sps.integration.viber.entries.OrderInput;

import java.util.List;

public interface Messenger {

    void sendMessage(String message);
}
