package org.example.cardAPI.gateway;

import java.util.HashMap;
import java.util.Map;

// Simula a interação com um Gateway de Pagamento
public class MockPaymentGateway {

    // Armazena transações autorizadas
    private Map<String, Double> authorizedTransactions = new HashMap<>();


    // Simula a autorização do pagamento com base em dados fornecidos
    public Map<String, String> authorizePayment(String cardNumber, double amount) {
        Map<String, String> response = new HashMap<>();

        // Sucesso: cartão válido e valor positivo
        if ("1234-5678-9876-5432".equals(cardNumber) && amount > 0) {
            response.put("status", "success");
            response.put("message", "Payment authorized successfully.");
            String transactionId = generateTransactionId(cardNumber, amount); // Gerar um ID de transação
            authorizedTransactions.put(transactionId, amount); // Salvar transação autorizada
            response.put("transactionId", transactionId); // Retornar o ID da transação
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

    // Simula o estorno de um pagamento com base no ID da transação
    public Map<String, String> processChargeback(String transactionId) {
        Map<String, String> response = new HashMap<>();

        // Verificar se a transação existe
        if (authorizedTransactions.containsKey(transactionId)) {
            double amount = authorizedTransactions.remove(transactionId); // Remover transação autorizada
            response.put("status", "success");
            response.put("message", "Chargeback processed successfully.");
            response.put("amountRefunded", String.valueOf(amount)); // Retorna o valor estornado
        } else {
            response.put("status", "error");
            response.put("message", "Transaction not found or already refunded.");
        }

        return response;
    }

    private String generateTransactionId(String cardNumber, double amount) {
        return cardNumber + "-" + amount + "-" + System.currentTimeMillis();
    }
}
