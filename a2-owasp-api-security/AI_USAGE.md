# Declaração de Uso de IA - Avaliação A2

## Ferramentas utilizadas:
- [ ] ChatGPT / GPT-4
- [x] Claude (Anthropic) / Cursor IDE
- [ ] GitHub Copilot
- [ ] Codeium
- [x] Gemini
- [ ] Nenhuma ferramenta de IA foi utilizada

## Como utilizei:
- Usei o Gemini para sintetizar o guia de pesquisa OWASP API Security Top 10 e estruturar o plano de testes e prompts do laboratório.
- Usei o Cursor IDE para gerar a estrutura inicial separada do backend (Spring Boot) e frontend (React) da aplicação SocialConnect, além do boilerplate de documentação.

## O que eu entendo 100%:
- A diferença entre autenticação (identidade) e autorização (permissão por objeto, propriedade e função).
- Como ocorre a falha BOLA/IDOR e como mitigá-la validando o dono do recurso no backend.
- Por que esconder botões ou campos no React não protege a API sem validação no servidor.
- Como consultas parametrizadas previnem SQL Injection.

## O que precisei estudar mais:
- Configuração do Springdoc OpenAPI para exportação ao OWASP ZAP.
- Como diferenciar alertas passivos, ativos e falsos positivos no OWASP ZAP.

## Prompts Utilizados (Exemplos):
**Prompt 1:**
> "Simplifique e explique o guia de pesquisa sobre OWASP API Security Top 10 e como testar com OWASP ZAP."

**Prompt 2:**
> "Crie um mini-laboratório separado com backend em Java Spring Boot (rotas v1 vulneráveis e v2 seguras) e frontend em React para demonstrar BOLA, Broken Auth, Mass Assignment, BFLA e SQL Injection na SocialConnect."

## Declaração de Autenticidade:
Declaro que:
1. Todo o código entregue foi revisado e compreendido por mim.
2. Sou capaz de explicar cada linha do código em uma arguição oral.
3. O uso de IA foi complementar ao meu aprendizado, não substituto.
4. Não copiei código de colegas ou de fontes não autorizadas.

**Assinatura:** __________________________
**Data:** ____/____/2026
