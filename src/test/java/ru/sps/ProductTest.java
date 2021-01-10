package ru.sps;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.sps.integration.Messenger;
import ru.sps.services.ProductService;

@SpringBootTest
public class ProductTest {

    @Autowired
    private Messenger testMessenger;

    @Mock
    private ProductService productService;

    @Test
    public void messageTest() {
        
    }
}
