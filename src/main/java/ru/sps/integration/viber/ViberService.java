package ru.sps.integration.viber;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sps.integration.Messenger;
import ru.sps.integration.viber.entries.OutgoingMessageEntry;
import ru.sps.utils.HttpHelper;

import java.util.Collections;

@Component
public class ViberService implements Messenger {

    private final String token;
    private final String sendMessageUrl;
    private final String receiver;

    private Logger log = LoggerFactory.getLogger(ViberService.class);

    public ViberService(@Value("${viber.token}") String token,
                        @Value("${viber.send.url}") String sendUrl,
                        @Value("${viber.user}") String user) {
        this.token = token;
        this.sendMessageUrl = sendUrl;
        this.receiver = user;
    }

    @Override
    public void sendMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String messageEntry = mapper.writeValueAsString(new OutgoingMessageEntry(receiver, message));
            var headers = Collections.singletonMap("X-Viber-Auth-Token", token);
            HttpHelper.sendPost(sendMessageUrl, headers, messageEntry);
        } catch (Exception e) {
            log.error("Error while sending message to viber", e);
        }
    }
}
