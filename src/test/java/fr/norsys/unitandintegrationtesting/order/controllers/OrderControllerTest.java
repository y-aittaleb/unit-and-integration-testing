package fr.norsys.unitandintegrationtesting.order.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import fr.norsys.unitandintegrationtesting.order.models.Order;
import fr.norsys.unitandintegrationtesting.order.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private Order order1;
    private Order order2;


    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        order1 = new Order(1L, "Product 1", 99.99, 1);
        order2 = new Order(2L, "Product 2", 49.99, 2);
    }

    @Test
    void shouldGetAllOrders() throws Exception {
        when(orderService.getOrders()).thenReturn(Arrays.asList(order1, order2));
        mockMvc.perform(get("/api/orders"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    public void shouldGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(order1);
        mockMvc.perform(get("/api/orders/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(order1.getId()))
                .andExpect(jsonPath("$.product").value(order1.getProduct()))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldCreateOrder() throws Exception {
        when(orderService.createOrder(any(Order.class))).thenReturn(order1);

        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(order1))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.product").value(order1.getProduct()))
                .andExpect(jsonPath("$.id").value(order1.getId()))
                .andExpect(jsonPath("$").isNotEmpty());

    }

    @Test
    void shouldDeleteOrderSuccessfully() throws Exception {
        when(orderService.deleteOrderById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted - Order ID:1"));
    }

    @Test
    void shouldFailToDeleteOrder() throws Exception {
        when(orderService.deleteOrderById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Order deletion failed - Order ID:1"));
    }
}
