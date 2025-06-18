package fr.norsys.unitandintegrationtesting.order.repositories;

import fr.norsys.unitandintegrationtesting.order.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Rollback
    void shouldSaveAndRetrieveOrder() {
        Order savedOrder = orderRepository.save(new Order(null, "nintendo", 89.99, 2));

        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());

        assertThat(retrievedOrder).isPresent();
        assertThat(retrievedOrder.get().getProduct()).isEqualTo("nintendo");
    }

    @Test
    void shouldFindAllOrders() {

        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(5);
        assertThat(orders).extracting(Order::getProduct)
                .containsExactlyInAnyOrder("Laptop", "Smartphone", "Tablet", "Monitor", "Keyboard");
    }

    @Test
    @Rollback
    void shouldDeleteOrder() {
        orderRepository.deleteById(1L);

        Optional<Order> deletedOrder = orderRepository.findById(1L);
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(4);
    }

}
