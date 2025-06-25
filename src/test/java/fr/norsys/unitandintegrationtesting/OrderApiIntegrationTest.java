package fr.norsys.unitandintegrationtesting;


import fr.norsys.unitandintegrationtesting.order.models.Order;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@Testcontainers
@Transactional
public class OrderApiIntegrationTest {


    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("TRUNCATE TABLE orders").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE orders AUTO_INCREMENT = 1").executeUpdate();
        entityManager.persist(new Order(null, "Laptop", 1200.00, 2));
        entityManager.persist(new Order(null, "Smartphone", 800.00, 5));
        entityManager.persist(new Order(null, "Tablet", 300.00, 10));
        entityManager.persist(new Order(null, "Monitor", 400.00, 3));
        entityManager.persist(new Order(null, "Keyboard", 50.00, 20));
        entityManager.flush();
    }


}
