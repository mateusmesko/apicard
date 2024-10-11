package org.example.cardAPI.controller;

import org.example.cardAPI.gateway.MockPaymentGateway;
import org.example.cardAPI.model.LoginRequest;
import org.example.cardAPI.model.PaymentResponse;
import org.example.cardAPI.service.AuthService;
import org.example.cardAPI.model.LoginAttempt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private AuthService authService;
    private final MockPaymentGateway mockPaymentGateway;
    // Mapa para armazenar tentativas de login por IP
    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 3; // Número máximo de tentativas
    private static final int BLOCK_TIME_MINUTES = 10; // Tempo de bloqueio em minutos

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    // Injeção do MockPaymentGateway no controlador
    public PaymentController() {
        this.mockPaymentGateway = new MockPaymentGateway();
    }

    @PostMapping("/login")
    public ResponseEntity<PaymentResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr(); // Obtém o IP do cliente
        LoginAttempt attempt = loginAttempts.getOrDefault(clientIp, new LoginAttempt(0, LocalDateTime.now()));

        // Verifica se o IP está bloqueado
        if (attempt.getAttempts() >= MAX_ATTEMPTS &&
                LocalDateTime.now().isBefore(attempt.getLastAttempt().plusMinutes(BLOCK_TIME_MINUTES))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new PaymentResponse(false, "Too many login attempts. Try again later."));
        }

        // Aqui você deve validar as credenciais do usuário
        if (isValidUser(loginRequest)) { // Implementar o método isValidUser
            String token = authService.generateToken(loginRequest.getUsername());
            attempt.resetAttempts(); // Reseta tentativas após sucesso
            loginAttempts.put(clientIp, attempt); // Atualiza tentativas no mapa
            return ResponseEntity.ok(new PaymentResponse(true, token));
        } else {
            attempt.incrementAttempts(); // Incrementa tentativas em caso de falha
            loginAttempts.put(clientIp, attempt); // Atualiza tentativas no mapa
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new PaymentResponse(false, "Invalid username or password."));
        }
    }

    private boolean isValidUser(LoginRequest loginRequest) {
        // Lógica para validar o usuário
        return "seu_usuario".equals(loginRequest.getUsername()) && "sua_senha".equals(loginRequest.getPassword());
    }

    @PostMapping("/authorize")
    public ResponseEntity<Map<String, String>> authorizePayment(@RequestBody Map<String, Object> paymentData) {
        String cardNumber = (String) paymentData.get("cardNumber");
        double amount = (double) paymentData.get("amount");
        logger.info("Recebendo solicitação de autorização de pagamento: {}", paymentData);
        // Usar o MockPaymentGateway para simular a autorização do pagamento
        Map<String, String> response = mockPaymentGateway.authorizePayment(cardNumber, amount);

        // Responder com base no status retornado pelo MockPaymentGateway
        if ("success".equals(response.get("status"))) {
            logger.debug("Processando pagamento...");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("message", "Access denied.");
            }});
        }
    }

    @PostMapping("/chargeback")
    public ResponseEntity<Map<String, String>> processChargeback(@RequestBody Map<String, String> requestData) {
        String transactionId = requestData.get("transactionId");
        logger.info("Recebendo solicitação de chargeback para a transação: {}", transactionId);

        // Usar o MockPaymentGateway para processar o estorno
        Map<String, String> response = mockPaymentGateway.processChargeback(transactionId);

        // Responder com base no status retornado pelo MockPaymentGateway
        if ("success".equals(response.get("status"))) {
            logger.debug("Chargeback processado com sucesso para a transação {}", transactionId);
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Falha ao processar o chargeback para a transação {}", transactionId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
