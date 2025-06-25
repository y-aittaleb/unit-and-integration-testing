package fr.norsys.unitandintegrationtesting.order.repositories;

import fr.norsys.unitandintegrationtesting.order.models.Order;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Testcontainers
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

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
