package fr.norsys.unitandintegrationtesting.order.services;

import fr.norsys.unitandintegrationtesting.order.exceptions.OrderNotFoundException;
import fr.norsys.unitandintegrationtesting.order.models.Order;
import fr.norsys.unitandintegrationtesting.order.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    OrderService orderService;


    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        order1 = new Order(1L, "ps5", 99.99, 1);
        order2 = new Order(2L, "xbox", 49.99, 2);
    }


}
