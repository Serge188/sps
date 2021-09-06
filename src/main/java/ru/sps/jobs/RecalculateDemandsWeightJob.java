package ru.sps.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sps.api.ProductsApi;

@Component
public class RecalculateDemandsWeightJob {

    private final ProductsApi productsApi;

    public RecalculateDemandsWeightJob(ProductsApi productsApi) {
        this.productsApi = productsApi;
    }

    @Scheduled(cron="0 0 23 ? * *")
    public void recalculateDemandsJob() {
        productsApi.calculateDemands().forEach(p ->
                p.setDemandWeight(p.getDemandWeight() > 0 ? p.getDemandWeight() - 1 : 0));
    }
}
