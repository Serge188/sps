package ru.sps.integration.viber.entries;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OutgoingMessageEntry {
    @JsonProperty
    private String receiver;
    @JsonProperty("broadcast_list")
    private List<String> broadcastList;
    @JsonProperty("min_api_version")
    private int minApiVersion = 1;
    @JsonProperty
    private SenderEntry sender = new SenderEntry("SPS bot");
    @JsonProperty
    private String type = "text";
    @JsonProperty
    private String text;

    public OutgoingMessageEntry(String receiver, String message) {
        this.receiver = receiver;
        this.text = message;
    }

    public OutgoingMessageEntry(List<String> broadcastList, String message) {
        this.broadcastList = broadcastList;
        this.text = message;
    }
}
