package fr.norsys.unitandintegrationtesting.order.repositories;

import fr.norsys.unitandintegrationtesting.order.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
