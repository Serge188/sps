package ru.sps.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sps.integration.Messenger;
import ru.sps.integration.users.UserCommandExecutor;
import ru.sps.integration.viber.entries.IncomingMessageEntry;
import ru.sps.services.OrderService;

@Component
@Transactional
public class MessageApi {

    private final Messenger viberService;
    private final OrderService orderService;
    private final UserCommandExecutor commandExecutor;

    private Logger log = LoggerFactory.getLogger(MessageApi.class);

    public MessageApi(Messenger viberService,
                      OrderService orderService,
                      UserCommandExecutor commandExecutor) {
        this.viberService = viberService;
        this.orderService = orderService;
        this.commandExecutor = commandExecutor;
    }

    public void retrieveMessage(IncomingMessageEntry message) {
        if (message.getMessage() == null || message.getMessage().getText() == null) {
            log.error("Invalid message: " + message.toString());
            return;
        }

        var text = message.getMessage().getText();
        var result = "";

        if (text.startsWith("/")) {
            result = commandExecutor.execute(text);
        } else {
            result = orderService.createOrdersFromUserInput(text);
        }
        viberService.sendMessage(result);
    }

    public void sendMessage(String message) {
        viberService.sendMessage(message);
    }
}
