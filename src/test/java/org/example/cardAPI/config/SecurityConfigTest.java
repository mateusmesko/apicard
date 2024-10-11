package org.example.cardAPI.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPublicEndpoint() throws Exception {
        // JSON com as credenciais do usuário
        String loginRequestJson = "{\"username\": \"testUser\", \"password\": \"testPassword\"}";

        // Testa acesso ao endpoint público (não autenticado)
        mockMvc.perform(post("/api/payments/login")
                        .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                        .content(loginRequestJson)) // Adiciona o corpo com usuário e senha
                .andExpect(status().isOk()); // O endpoint de login deve estar acessível
    }

    @Test
    public void testProtectedEndpointWithoutAuth() throws Exception {
        // Testa acesso ao endpoint protegido sem autenticação
        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isUnauthorized()); // Deve retornar 401 Unauthorized
    }

}
