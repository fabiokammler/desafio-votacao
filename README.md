# Inicializando

### Tecnologias utilizadas

* Java(Versão 25)
* Spring Boot(Versão 4.1.1)
* Banco de dados(MySQL, H2)
* Migração de banco de dados(Flyway)
* Documentação da API(OpenAPI/SwaggerUI 3.1.0)
* Testes(Junit, Mockito & MockMvc)

### Pré-requisitos

* Java Development Kit(JDK)
* Docker & Docker Compose
* Maven

### Como rodar a aplicação

* git clone https://github.com/fabiokammler/desafio-votacao.git
* Acessar a pasta onde foi clonado o projeto.
* <u>Rodar docker-compose:</u> **docker compose up -d**
* <u>Rodar a aplicação:</u> **mvn spring-boot:run -Dspring-boot.run.profiles=development**
* <u>Configurar Url callback:</u> 
**mvn spring-boot:run -Dspring-boot.run.profiles=development -Dspring-boot.run.jvmArguments="-DAPI_BASE_URL_CALLBACK=http://meudominio.com"**

### Endpoints API

* [Swagger](http://localhost:8080/swagger-ui/index.html)