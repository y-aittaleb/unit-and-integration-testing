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

    @Test
    public void shouldGetOrdersList() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));
        List<Order> orderList = orderService.getOrders();
        assertEquals(orderList.size(), 2);
        assertEquals(orderList.get(0).getProduct(), "ps5");
        assertEquals(orderList.get(1).getProduct(), "xbox");
        assertEquals(orderList.get(0).getPrice(), 99.99);
        assertEquals(orderList.get(1).getPrice(), 49.99);
    }

    @Test
    public void shouldGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        Order orderById = orderService.getOrderById(1L);
        assertNotEquals(orderById, null);
        assertEquals(orderById.getProduct(), "ps5");
        assertEquals(orderById.getPrice(), 99.99);
    }

    @Test
    public void shouldGetInvalidOrderById() {
        when(orderRepository.findById(17L)).thenThrow(new OrderNotFoundException("Order Not Found with ID"));
        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(17L);
        });
        assertTrue(exception.getMessage().contains("Order Not Found with ID"));
    }

    @Test
    public void shouldCreateOrder() {
        orderService.createOrder(order2);
        verify(orderRepository, times(1)).save(order2);
        ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderArgumentCaptor.capture());
        Order orderCreated = orderArgumentCaptor.getValue();
        assertNotNull(orderCreated.getId());
        assertEquals("xbox", orderCreated.getProduct());
    }

    @Test
    public void shouldDeleteOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        orderService.deleteOrderById(order1.getId());
        verify(orderRepository, times(1)).deleteById(order1.getId());
        ArgumentCaptor<Long> orderArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(orderRepository).deleteById(orderArgumentCaptor.capture());
        Long orderIdDeleted = orderArgumentCaptor.getValue();
        assertNotNull(orderIdDeleted);
        assertEquals(1L, orderIdDeleted);
    }

    @Test
    public void shouldNotDeleteOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        verify(orderRepository, times(0)).deleteById(order1.getId());
        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            orderService.deleteOrderById(order1.getId());
        });
        assertTrue(exception.getMessage().contains("Order Not Found with ID"));
    }
}
