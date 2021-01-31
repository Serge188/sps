package ru.sps.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sps.api.ProductsApi;

@Component
public class SendDemandsJob {

    private final ProductsApi productApi;

    public SendDemandsJob(ProductsApi productApi) {
        this.productApi = productApi;
    }

    @Scheduled(cron="0 0 8 ? * *")
    public void sendDemands() {
        productApi.calculateDemandsAndBroadcastMessage();
    }
}
