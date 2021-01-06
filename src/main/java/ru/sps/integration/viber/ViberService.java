package ru.sps.integration.viber;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sps.integration.Messenger;
import ru.sps.integration.viber.entries.IncomingMessageEntry;
import ru.sps.integration.viber.entries.OrderInput;
import ru.sps.integration.viber.entries.OutgoingMessageEntry;
import ru.sps.repository.ProductRepository;
import ru.sps.utils.HttpHelper;

import java.math.BigDecimal;
import java.util.*;

import static ru.sps.repository.specification.ProductSpecification.likeTitle;

@Component
public class ViberService implements Messenger {

    private final String token;
    private final String sendMessageUrl;
    private final String receiver;
    private final ProductRepository productRepository;

    private Logger log = LoggerFactory.getLogger(ViberService.class);

    public ViberService(@Value("${viber.token}") String token,
                        @Value("${viber.send.url}") String sendUrl,
                        @Value("${viber.user}") String user,
                        ProductRepository productRepository) {
        this.token = token;
        this.sendMessageUrl = sendUrl;
        this.receiver = user;
        this.productRepository = productRepository;
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
    public List<OrderInput> retrieveMessage(IncomingMessageEntry message) {
        if (message.getMessage() != null && message.getMessage().getText() != null) {
            var text = message.getMessage().getText();
            var result = new ArrayList<OrderInput>();
            var unreadable = new ArrayList<String>();
            var unreadableTitle = new ArrayList<String>();
            var unreadableQty = new ArrayList<String>();

            List<String> titleQtyBlocks = Arrays.asList(text.split(", "));

            titleQtyBlocks.forEach(block -> {
                var pair = block.split(":");
                if (pair.length != 2) unreadable.add(block);
                var product = productRepository.findAll(likeTitle(pair[0])).stream().findAny();
                var qty = BigDecimal.ZERO;
                try {
                    qty = new BigDecimal(pair[1]);
                } catch (NumberFormatException e) {
                    log.error("Error while parsing qty: " + pair[1]);
                }
                if (!product.isPresent()) {
                    unreadableTitle.add(block);
                } else if (qty.compareTo(BigDecimal.ZERO) == 0) {
                    unreadableQty.add(block);
                } else {
                    result.add(new OrderInput(product.get().getId(), qty));
                }
            });
            if (!unreadable.isEmpty()) sendMessage("Unreadable: " + String.join(", ", unreadable));
            if (!unreadableTitle.isEmpty()) sendMessage("Unreadable title: " + String.join(", ", unreadableTitle));
            if (!unreadableQty.isEmpty()) sendMessage("Unreadable qty: " + String.join(", ", unreadableQty));

            return result;
        }

        return Collections.emptyList();
    }
}
