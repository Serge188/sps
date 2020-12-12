package ru.sps.integration.viber.entries;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutgoingMessageEntry {
    @JsonProperty
    private String receiver;
    @JsonProperty("min_api_version")
    private int minApiVersion;
    @JsonProperty
    private SenderEntry sender;
    @JsonProperty
    private String type = "text";
    @JsonProperty
    private String text;

    public OutgoingMessageEntry(String receiver, String message) {
        this.receiver = receiver;
        this.minApiVersion = 1;
        this.sender = new SenderEntry("SPS bot");
        this.type = "text";
        this.text = message;
    }
}
