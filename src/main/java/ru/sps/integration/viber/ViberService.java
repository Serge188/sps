package ru.sps.integration.viber;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sps.integration.Messenger;
import ru.sps.integration.viber.entries.OutgoingMessageEntry;
import ru.sps.repository.MessengerUsersRepository;
import ru.sps.utils.HttpHelper;

import java.util.*;

@Component
public class ViberService implements Messenger {

    private final String token;
    private final String sendMessageUrl;
    private final String broadcastMessageUrl;
    private final String receiver;
    private final MessengerUsersRepository messengerUsersRepository;

    private Logger log = LoggerFactory.getLogger(ViberService.class);

    public ViberService(@Value("${viber.token}") String token,
                        @Value("${viber.send.url}") String sendUrl,
                        @Value("${viber.broadcast.url}") String broadcastUrl,
                        @Value("${viber.user}") String user,
                        MessengerUsersRepository messengerUsersRepository) {
        this.token = token;
        this.sendMessageUrl = sendUrl;
        this.broadcastMessageUrl = broadcastUrl;
        this.receiver = user;
        this.messengerUsersRepository = messengerUsersRepository;
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

    @Override
    public void broadcastMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            var headers = Collections.singletonMap("X-Viber-Auth-Token", token);
            var users = new ArrayList<String>();

            messengerUsersRepository.findAll().forEach(ui -> users.add(ui.getExternalId()));

            var messageEntry = mapper.writeValueAsString(new OutgoingMessageEntry(users, message));

            HttpHelper.sendPost(broadcastMessageUrl, headers, messageEntry);
        } catch (Exception e) {
            log.error("Error while broadcasting message in viber", e);
        }

    }
}
