package ru.sps.integration.viber.entries;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("sender")
public class SenderEntry {

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    public SenderEntry() {
    }

    SenderEntry(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
