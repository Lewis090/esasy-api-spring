# easy-api (Spring Boot)

Projeto minimal em Spring Boot com entidades Usuario, Receita e Despesa.

Como executar:

1. Tenha JDK 17+ e Maven instalados.
2. No terminal, rode:
   mvn spring-boot:run
3. Endpoints:
   - GET /usuarios
   - POST /usuarios
   - PUT /usuarios/{id}
   - DELETE /usuarios/{id}
   - GET /receitas
   - POST /receitas
   - PUT /receitas/{id}
   - DELETE /receitas/{id}
   - GET /despesas
   - POST /despesas
   - PUT /despesas/{id}
   - DELETE /despesas/{id}

H2 console: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
