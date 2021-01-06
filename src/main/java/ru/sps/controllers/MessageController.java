package ru.sps.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sps.api.MessageApi;
import ru.sps.integration.viber.entries.IncomingMessageEntry;

@RestController
public class MessageController {

    private final MessageApi messageApi;

    public MessageController(MessageApi messageApi) {
        this.messageApi = messageApi;
    }

    @PostMapping("/message")
    public void retrieveMessage(@RequestBody IncomingMessageEntry message) {
        messageApi.retrieveMessage(message);
    }
}
