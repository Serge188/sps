package ru.sps.integration.viber.entries;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("sender")
class SenderEntry {

    @JsonProperty
    private String name;

    SenderEntry(String name) {
        this.name = name;
    }
}
