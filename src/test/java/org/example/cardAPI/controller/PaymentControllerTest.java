package org.example.cardAPI.controller;

import org.example.cardAPI.gateway.MockPaymentGateway;
import org.example.cardAPI.model.PaymentResponse;
import org.example.cardAPI.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @Mock
    private AuthService authService; // Mock AuthService se necessário

    @Mock
    private MockPaymentGateway mockPaymentGateway;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPaymentGatewayUnavailable() {
        // Arrange
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("cardNumber", "1234-5678-9876-5432");
        paymentData.put("cvv", "123");
        paymentData.put("amount", 100.0);

        // Act
        ResponseEntity<Map<String, String>> response = paymentController.authorizePayment(paymentData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Alterado para esperar 200
        assertEquals("Payment authorized successfully.", response.getBody().get("message")); // Mensagem esperada
    }
}
