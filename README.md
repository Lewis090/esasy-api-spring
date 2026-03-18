# EasyApi - Gestão Financeira para MEI 🚀

Este é o backend da **EasyApi**, uma solução robusta em Spring Boot projetada para ajudar Microempreendedores Individuais (MEI) a gerenciarem suas receitas, despesas e obrigações fiscais de forma simples e segura.

## 🛡️ Segurança e Autenticação

A API agora conta com uma camada de segurança avançada utilizando **Spring Security** e **JSON Web Tokens (JWT)**.

- **Autenticação Stateless**: Todas as requisições (exceto login/registro) exigem um token JWT válido no cabeçalho `Authorization: Bearer <token>`.
- **Proteção de Dados**: Implementamos validação de propriedade. Um usuário autênticado só pode visualizar ou manipular seus próprios dados.
- **Senhas Seguras**: As senhas são criptografadas no banco de dados utilizando o algoritmo **BCrypt**.

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security**
- **JWT (jjwt)**
- **PostgreSQL / H2 Database** (conforme configuração)
- **Maven**
- **Docker & Docker Compose**

## 🚀 Como Executar o Projeto

### Usando Docker (Recomendado)

Para subir o banco de dados e a aplicação simultaneamente:

```bash
docker-compose up --build
```

O `docker-compose` já está configurado com healthchecks e as variáveis de ambiente necessárias para o funcionamento local.

### Execução Local (IDE / Maven)

1. Certifique-se de que o Postgres está rodando (via Docker ou instalação local).
2. Verifique as configurações no arquivo `src/main/resources/application.properties`.
3. Execute o comando:

```bash
mvn spring-boot:run
```

## 🧪 Como Testar a API

### Coleção do Insomnia
Disponibilizamos o arquivo `insomnia_jwt_collection.json` na raiz do projeto. Basta importá-lo no seu Insomnia para ter acesso a todas as rotas traduzidas e pré-configuradas.

### Fluxo de Teste Manual
1. **Cadastro**: Use o endpoint `POST /auth/register` para criar uma conta.
2. **Login**: Use o endpoint `POST /auth/login` para obter seu token.
3. **Acesso**: Copie o token e cole-o na variável de ambiente `jwt_token` do Insomnia (ou use o cabeçalho `Authorization: Bearer <token>`).

## 🔍 Solução de Problemas (Troubleshooting)

- **Erro de SSL no Banco**: Se encontrar erros de SSL ao conectar localmente, verifique se a URL JDBC contém `sslmode=disable`.
- **Nome do Banco**: O Docker cria o banco com o nome `easyapi`. Garanta que sua URL JDBC aponta para este nome.
- **H2 Console**: Disponível em `/h2-console` para fins de desenvolvimento (apenas quando usando o perfil de banco em memória).

---
Desenvolvido com ❤️ para facilitar a vida do empreendedor brasileiro.


