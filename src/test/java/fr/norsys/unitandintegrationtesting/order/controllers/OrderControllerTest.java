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

}
