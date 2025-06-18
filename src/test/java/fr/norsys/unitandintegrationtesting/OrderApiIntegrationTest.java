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

    @Test
    void shouldGetAllOrdersFromSqlData() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].product").value("Laptop"))
                .andExpect(jsonPath("$[1].product").value("Smartphone"))
                .andExpect(jsonPath("$[2].product").value("Tablet"))
                .andExpect(jsonPath("$[3].product").value("Monitor"))
                .andExpect(jsonPath("$[4].product").value("Keyboard"));
    }

    @Test
    void shouldGetOrderById() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.product").value("Laptop"));
    }

    @Test
    void shouldCreateNewOrder() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String newOrderJson = "{ \"product\": \"Mouse\", \"price\": 29.99, \"qty\": 1 }";

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newOrderJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.product").value("Mouse"))
                .andExpect(jsonPath("$.price").value(29.99))
                .andExpect(jsonPath("$.qty").value(1));

        //get the newly created order to verify it exists
        Order newOrder = entityManager.createQuery("SELECT o FROM Order o WHERE o.product = 'Mouse'", Order.class)
                .getSingleResult();
        assertThat(newOrder).isNotNull();
        assertThat(newOrder.getProduct()).isEqualTo("Mouse");
    }

    @Test
    void shouldDeleteOrderById() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted - Order ID:1"));

        // Verify the order is deleted
        Optional<Order> deletedOrder = Optional.ofNullable(entityManager.find(Order.class, 1L));
        assertThat(deletedOrder).isNotPresent();
    }

}
