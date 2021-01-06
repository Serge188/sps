package ru.sps.integration.viber.entries;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageEntry {

    @JsonProperty
    private String text;

    public MessageEntry() {
    }

    public String getText() {
        return text;
    }
}
