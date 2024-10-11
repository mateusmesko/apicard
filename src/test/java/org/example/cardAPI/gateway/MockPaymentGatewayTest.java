package org.example.cardAPI.gateway;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Map;

public class MockPaymentGatewayTest {

    private final MockPaymentGateway paymentGateway = new MockPaymentGateway();

    @Test
    public void testAuthorizePayment_Success() {
        Map<String, String> response = paymentGateway.authorizePayment("1234-5678-9876-5432", 100.0);
        assertEquals("success", response.get("status"));
        assertEquals("Payment authorized successfully.", response.get("message"));
    }

    @Test
    public void testAuthorizePayment_CardExpired() {
        Map<String, String> response = paymentGateway.authorizePayment("1111-2222-3333-4444", 100.0);
        assertEquals("error", response.get("status"));
        assertEquals("Card expired.", response.get("message"));
    }

    @Test
    public void testAuthorizePayment_InsufficientFunds() {
        Map<String, String> response = paymentGateway.authorizePayment("5555-6666-7777-8888", 100.0);
        assertEquals("error", response.get("status"));
        assertEquals("Insufficient funds.", response.get("message"));
    }

    @Test
    public void testAuthorizePayment_InvalidCard() {
        Map<String, String> response = paymentGateway.authorizePayment("0000-0000-0000-0000", 100.0);
        assertEquals("error", response.get("status"));
        assertEquals("Invalid card number or amount.", response.get("message"));
    }

}
