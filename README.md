# EasyApi - Gestão Financeira para MEI 🚀

Uma solução robusta em **Spring Boot 3.x** com **Java 21** projetada para ajudar Microempreendedores Individuais (MEI) a gerenciarem suas receitas, despesas e obrigações fiscais de forma simples e segura.

## 🛡️ Segurança e Autenticação

A API conta com uma camada de segurança avançada utilizando **Spring Security** e **JSON Web Tokens (JWT)**.

- **Autenticação Stateless**: Todas as requisições (exceto login/registro) exigem um token JWT válido no cabeçalho `Authorization: Bearer <token>`.
- **Proteção de Dados**: Implementamos validação de propriedade. Um usuário autênticado só pode visualizar ou manipular seus próprios dados.
- **Senhas Seguras**: As senhas são criptografadas no banco de dados utilizando o algoritmo **BCrypt**.

## 🛠️ Stack Tecnológico

| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.x | Framework web e aplicação |
| Spring Security | 3.x | Autenticação e autorização |
| JWT (jjwt) | Latest | Tokens de autenticação |
| PostgreSQL | 15+ | Banco de dados relacional |
| H2 Database | - | Banco em memória para testes |
| Maven | 3.9+ | Gerenciamento de dependências |
| Docker | 24+ | Containerização |
| Docker Compose | 2+ | Orquestração de containers |

## 📋 Pré-requisitos

- **Docker Desktop** (recomendado para ambiente isolado)
- **Java 21** + **Maven 3.9+** (para execução local sem Docker)
- **Git** (para clonar o repositório)

## 🚀 Como Executar o Projeto

### Opção 1: Usando Docker (Recomendado)

A forma mais simples e reproducível de executar o projeto.

```bash
# Clone o repositório
git clone https://github.com/Lewis090/esasy-api-spring.git
cd esasy-api-spring

# Inicie os containers (API + PostgreSQL)
docker compose up --build -d app postgres

# Verifique o status dos containers
docker compose ps

# Veja os logs da API em tempo real
docker compose logs -f app

# Para parar os serviços
docker compose down

# Para parar e remover dados do banco (reset completo)
docker compose down -v
```

**Serviços disponibilizados:**
- **API**: `http://localhost:8080`
- **PostgreSQL**: `localhost:5432` (credenciais no `docker-compose.yml`)

### Opção 2: Postgres no Docker + IDE Local

Se preferir rodar a API pela sua IDE:

```bash
# Inicie apenas o PostgreSQL
docker compose up -d postgres

# Na sua IDE, execute a aplicação (via Maven ou diretamente)
# A API iniciará em http://localhost:8080
```

### Opção 3: Execução Totalmente Local

Para ambiente completamente local (sem Docker):

```bash
# 1. Certifique-se que PostgreSQL está instalado e rodando localmente
# 2. Configure as variáveis de ambiente (ou edite src/main/resources/application.properties):
#    - spring.datasource.url
#    - spring.datasource.username
#    - spring.datasource.password

# 3. Execute via Maven
mvn clean install
mvn spring-boot:run

# Ou via IDE (execute a classe main: com.easy.easyapi.EasyapiApplication)
```

## 🧪 Como Testar a API

### Usando Insomnia (Recomendado)

Disponibilizamos coleções prontas na raiz do projeto:
- `insomnia_v2_ease.json` - Versão anterior
- `insomnia_v3_collection.json` - Versão atual com JWT

**Passos:**
1. Abra o **Insomnia**
2. Clique em **Import** → **From File**
3. Selecione `insomnia_v3_collection.json`
4. Todas as rotas estarão pré-configuradas com autenticação

### Fluxo de Teste Manual

```bash
# 1. Cadastro - Crie uma nova conta
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "senha": "senha123",
  "nome": "João Silva"
}

# 2. Login - Obtenha seu token JWT
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "senha": "senha123"
}

# Resposta conterá: { "token": "eyJhbGc..." }

# 3. Acesso Autenticado - Use o token nas requisições
GET http://localhost:8080/api/receitas
Authorization: Bearer eyJhbGc...
```

### Usando cURL

```bash
# Cadastro
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com", "senha":"pass123", "nome":"User"}'

# Login (copie o token da resposta)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com", "senha":"pass123"}'

# Requisição autenticada
curl -X GET http://localhost:8080/api/receitas \
  -H "Authorization: Bearer <TOKEN_AQUI>"
```

## 📁 Estrutura do Projeto

```
esasy-api-spring/
├── src/
│   ├── main/
│   │   ├── java/com/easy/easyapi/
│   │   │   ├── config/              # Configurações (CORS, Security, etc)
│   │   │   ├── controller/          # Endpoints REST
│   │   │   ├── service/             # Lógica de negócio
│   │   │   ├── repository/          # Acesso a dados
│   │   │   ├── entity/              # Modelos JPA
│   │   │   └── security/            # JWT, autenticação
│   │   └── resources/
│   │       └── application.properties # Configurações da aplicação
│   └── test/                        # Testes unitários e integração
├── docker-compose.yml               # Orquestração de containers
├── pom.xml                          # Dependências Maven
├── Dockerfile                       # Imagem Docker da API
└── README.md                        # Este arquivo
```

## 🔧 Configuração

### Variáveis de Ambiente

O arquivo `docker-compose.yml` já inclui as configurações padrão. Para ambientes customizados, edite `src/main/resources/application.properties`:

```properties
# Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/easyapi
spring.datasource.username=postgres
spring.datasource.password=senha123

# JWT
jwt.secret=sua_chave_secreta_aqui_com_minimo_256_bits
jwt.expiration=86400000

# Perfil ativo (dev, prod)
spring.profiles.active=dev
```

## 🐛 Solução de Problemas

| Problema | Solução |
|----------|--------|
| **Erro de conexão com PostgreSQL** | Verifique se o container está rodando: `docker compose ps` |
| **Porta 8080 já em uso** | Mude a porta no `docker-compose.yml` ou matalize o processo usando-a |
| **Erro de SSL no banco** | Adicione `?sslmode=disable` na URL JDBC de development |
| **H2 Console 404** | Acesse `/h2-console` apenas quando usando perfil de teste com H2 |
| **Token JWT inválido** | Verifique se está usando `Bearer <token>` e se o token não expirou |
| **CORS bloqueando requisições** | As origens permitidas estão em `CorsConfig.java` |

## 📊 Endpoints Principais

### Autenticação
- `POST /auth/register` - Criar nova conta
- `POST /auth/login` - Fazer login e obter token

### Receitas
- `GET /api/receitas` - Listar receitas do usuário
- `POST /api/receitas` - Criar nova receita
- `PUT /api/receitas/{id}` - Atualizar receita
- `DELETE /api/receitas/{id}` - Deletar receita

### Despesas
- `GET /api/despesas` - Listar despesas do usuário
- `POST /api/despesas` - Criar nova despesa
- `PUT /api/despesas/{id}` - Atualizar despesa
- `DELETE /api/despesas/{id}` - Deletar despesa

*Nota: Todos endpoints (exceto auth) requerem token JWT*

## 🚢 Deploy

### Deploy com Docker

```bash
# Build da imagem
docker build -t easyapi:latest .

# Push para registry (Docker Hub, GitHub Container Registry, etc)
docker tag easyapi:latest seu_registry/easyapi:latest
docker push seu_registry/easyapi:latest
```

### Deploy em Cloud (Azure, AWS, etc)

Consulte a documentação específica de cada provedor cloud. A aplicação está containerizada e pronta para Kubernetes, App Services, ECS, etc.

## 🤝 Contribuindo

1. Faça um **fork** do repositório
2. Crie uma **branch** para sua feature (`git checkout -b feature/minha-feature`)
3. Commit suas mudanças (`git commit -m 'Add: minha feature'`)
4. Push para a branch (`git push origin feature/minha-feature`)
5. Abra um **Pull Request**

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Lewis Silva**
- GitHub: [@Lewis090](https://github.com/Lewis090)
- Repositório: [esasy-api-spring](https://github.com/Lewis090/esasy-api-spring)

## 💬 Suporte

Encontrou um bug ou tem uma sugestão? Abra uma [Issue](https://github.com/Lewis090/esasy-api-spring/issues) no GitHub.

---

**Desenvolvido com ❤️ para facilitar a vida do empreendedor brasileiro.**

*Última atualização: 28 de março de 2026*
