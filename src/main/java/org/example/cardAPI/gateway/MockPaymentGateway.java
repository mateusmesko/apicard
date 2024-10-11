package org.example.cardAPI.gateway;

import java.util.HashMap;
import java.util.Map;

// Simula a interação com um Gateway de Pagamento
public class MockPaymentGateway {

    // Simula a autorização do pagamento com base em dados fornecidos
    public Map<String, String> authorizePayment(String cardNumber, double amount) {
        Map<String, String> response = new HashMap<>();

        // Sucesso: cartão válido e valor positivo
        if ("1234-5678-9876-5432".equals(cardNumber) && amount > 0) {
            response.put("status", "success");
            response.put("message", "Payment authorized successfully.");
        }
        // Erro: Cartão expirado
        else if ("1111-2222-3333-4444".equals(cardNumber)) {
            response.put("status", "error");
            response.put("message", "Card expired.");
        }
        // Erro: Saldo insuficiente
        else if ("5555-6666-7777-8888".equals(cardNumber)) {
            response.put("status", "error");
            response.put("message", "Insufficient funds.");
        }
        // Qualquer outro caso: Erro geral
        else {
            response.put("status", "error");
            response.put("message", "Invalid card number or amount.");
        }

        return response;
    }
}
