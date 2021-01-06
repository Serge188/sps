package ru.sps.integration.viber.entries;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IncomingMessageEntry {

    @JsonProperty
    private SenderEntry sender;

    @JsonProperty
    private MessageEntry message;

    public IncomingMessageEntry() {
    }

    public IncomingMessageEntry(SenderEntry sender, MessageEntry message) {
        this.sender = sender;
        this.message = message;
    }

    public SenderEntry getSender() {
        return sender;
    }

    public MessageEntry getMessage() {
        return message;
    }
}
