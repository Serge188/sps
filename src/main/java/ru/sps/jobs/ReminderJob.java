package ru.sps.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sps.api.MessageApi;
import ru.sps.api.OrdersApi;

@Component
public class ReminderJob {

    private final OrdersApi ordersApi;
    private final MessageApi messageApi;

    public ReminderJob(OrdersApi ordersApi, MessageApi messageApi) {
        this.ordersApi = ordersApi;
        this.messageApi = messageApi;
    }

    @Scheduled(cron="0 0 20 ? * *")
    public void remindToConfirmOrders() {
        if (ordersApi.unconfirmedOrdersExist()) {
            messageApi.sendMessage("Confirm orders?");
        }
    }
}
