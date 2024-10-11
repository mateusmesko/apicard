1. Login
POST /api/login
Descrição:
Este endpoint permite que um usuário faça login na API e obtenha um token JWT para autenticação em outras rotas protegidas.

Parâmetros:
Body (JSON):
- username: (string) Nome de usuário do cliente.
- password: (string) Senha do cliente.


2. Autorizar Pagamento
Descrição:
Autoriza um pagamento baseado nos detalhes fornecidos (número do cartão e valor).

Método:
POST

Requisição:
Endpoint: /api/payments/authorize

Parâmetros:
Body (JSON):
- cardnumber: Numero do cartão.
- amount: valor.

3. Extorno de Pagamento (Chargeback)
Descrição:
Processa um estorno (chargeback) de um pagamento autorizado anteriormente.

Método:
POST

Requisição:
/api/payments/chargeback

- transactionId: (string) Nome de usuário do cliente.


Regras do cartões 
Cartão válido
Número do Cartão: 1234-5678-9876-5432
CVC: 123
Data de Validade: 12/2025
Descrição: Esse cartão é válido e pode ser utilizado para autorizar pagamentos de qualquer valor positivo.


 Cartão expirado
Número do Cartão: 1111-2222-3333-4444
CVC: 456
Data de Validade: 12/2020
Descrição: Este cartão está expirado e qualquer tentativa de pagamento com ele falhará.


Saldo insuficiente
Número do Cartão: 5555-6666-7777-8888
CVC: 789
Data de Validade: 06/2026
Descrição: Este cartão não possui saldo suficiente para autorizar pagamentos.


 Cartão inválido
Número do Cartão: Qualquer outro número de cartão que não seja um dos mencionados acima.
CVC: Qualquer valor.
Data de Validade: Qualquer data.
Descrição: Qualquer número de cartão que não esteja listado é considerado inválido.


/api/payments/login
sempre passar 
{
  "username": "seu_usuario",
  "password": "sua_senha"
}

caso erre 3 vezes sistema bloqueia IP para novas tentativas
