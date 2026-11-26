# A2 - OWASP API Security Top 10 (SocialConnect)

## Integrantes
- [Preencher Nome 1] (RA: [Preencher])
- [Preencher Nome 2] (RA: [Preencher])
- [Preencher Nome 3] (RA: [Preencher])

## Tema
Investigação e demonstração prática das principais vulnerabilidades do OWASP API Security Top 10 (2023) e Injeção SQL aplicadas à API da rede social fictícia SocialConnect, comparando endpoints vulneráveis (`v1`) e seguros (`v2`).

## Objetivo
Demonstrar na prática por que validações apenas no frontend não protegem uma API e como implementar controles efetivos de autenticação, autorização em nível de objeto (BOLA), propriedades, funções e consultas parametrizadas no backend.

A versão `/api/v1` é vulnerável de propósito. Rode o laboratório apenas em localhost.

## Como Executar
### Pré-requisitos
- Java 17 ou 21
- Maven (ou usar o wrapper `./mvnw`)
- Node.js 20+

### Passos
1. Clone o repositório
2. **Subir o Backend:**
   ```bash
   cd src/backend
   ./mvnw spring-boot:run
   ```
   No Windows (cmd ou PowerShell), use `mvnw.cmd spring-boot:run` no lugar de `./mvnw`.

   Acesse o Swagger em http://localhost:8080/swagger-ui.html ou o OpenAPI JSON em http://localhost:8080/v3/api-docs (importe esse JSON no OWASP ZAP).

   Console H2: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:socialconnect`
   - Usuário: `sa`
   - Senha: (vazia)

3. **Subir o Frontend** (em outro terminal):
   ```bash
   cd src/frontend
   npm install
   npm run dev
   ```
   Abra http://localhost:5173. O Vite encaminha `/api` para o backend. No topo da página, alterne entre Modo Vulnerável (`/api/v1`) e Modo Seguro (`/api/v2`).

### Contas de demonstração
| Usuário | Senha | Papel | Uso |
| --- | --- | --- | --- |
| alice | 123456 | USER | Dona do Post 1 (público) e do Post 2 (privado) |
| bob | 123456 | USER | Conta para testar os ataques |
| admin | admin123 | ADMIN | Única conta que a v2 deixa apagar usuários |

O banco é H2 em memória. Reiniciar o backend restaura Alice, Bob, Admin e os dois posts.

## Estrutura
- `src/backend/`: API REST em Java Spring Boot (v1 vulnerável e v2 segura)
- `src/frontend/`: Interface web em React + Vite para demonstração visual
- `docs/`: Relatório técnico (`relatorio.md`) e roteiro de slides (`ROTEIRO_SLIDES.md`)
- `AI_USAGE.md`: Declaração de uso de IA

### O que comparar
- API2 — autenticação quebrada: a v1 lê o payload do JWT sem validar assinatura nem expiração; a v2 valida HMAC e `exp`
- API1 — BOLA: `GET` e `DELETE /posts/{id}`
- API3 — propriedades: `GET` e `PATCH /users/{id}` (mass assignment de `role`)
- API5 — função: `DELETE /admin/users/{id}`
- Injeção de SQL: `GET /posts/search?q=` com o payload `' OR '1'='1 --`
- API8 — configuração: erro 500 com stack trace na v1 e mensagem genérica na v2
