package ru.sps;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sps.services.OrderService;

import java.util.Collections;

@SpringBootTest
public class OrderTests {

    @Autowired
    private OrderService orderService;

    @Test
    void calculate_consumed_part() {
        orderService.calculateConsumedPart(Collections.EMPTY_LIST);
    }
}
