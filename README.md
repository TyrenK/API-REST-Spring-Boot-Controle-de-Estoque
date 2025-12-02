# 💻 API de Controle de Estoque com Módulo de Vendas
## -- Projeto em Spring Boot

Este projeto consiste em uma API REST para gestão de estoque, ampliada com um módulo completo de Vendas, incluindo controle transacional de baixa de estoque e prevenção de inconsistências.

---

## 📌 Descrição do Projeto

A API oferece:

- Cadastro e gestão de Clientes
- Registro de Vendas
- Registro de ItensVenda
- **Baixa automática de estoque ao registrar uma venda**
- **Rollback transacional** caso ocorra qualquer falha (ex.: estoque insuficiente)
- Integração completa entre **Cliente → Venda → ItensVenda → Produto**

O sistema garante **integridade dos dados** e evita vendas com quantidade superior ao estoque disponível.

---

## 🚀 Requisitos para Executar

Antes de iniciar, certifique-se de ter instalado:

1. **Java 17 ou superior**
2. **Maven**
3. **PostgreSQL, MySQL ou H2 Database**

---

## ⚙️ Configuração do Projeto

### 1️⃣ Clonar o Repositório

```bash
git clone https://github.com/TyrenK/API-REST-Spring-Boot-Controle-de-Estoque.git
cd API-REST-Spring-Boot-Controle-de-Estoque
```

---

### 2️⃣ Configurar o Banco de Dados

Edite o arquivo:

```
src/main/resources/application.properties
```

**Exemplo (PostgreSQL):**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_seu_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 3️⃣ Executar a Aplicação

Compile e execute:

```bash
mvn clean install
mvn spring-boot:run
```

A API iniciará em:

```
http://localhost:8080
```

---

## 🧭 Endpoints da API

Todos seguem o padrão:

```
/api/{recurso}
```

---

## 🔹 1. Clientes (CRUD)

| Método | Endpoint             | Descrição              |
|--------|-----------------------|-------------------------|
| POST   | /api/clientes         | Cria um novo cliente    |
| GET    | /api/clientes/{id}    | Busca por ID            |
| GET    | /api/clientes         | Lista todos             |
| PUT    | /api/clientes/{id}    | Atualiza um cliente     |
| DELETE | /api/clientes/{id}    | Exclui                  |

### 📥 Exemplo de Payload

```json
{
  "nome": "Maria Silva",
  "email": "maria.silva@exemplo.com",
}
```

---

## 🔹 2. Vendas

### Endpoint principal:

| Método | Endpoint     | Descrição                               |
|--------|---------------|-------------------------------------------|
| POST   | /api/vendas   | Registra venda + baixa automática do estoque |

### 📥 Exemplo de Payload (Lembre-se que para testar, você deve já ter um produto e este produto deve ter um fornecedor e uma categoria antes de ser postado).

```json
{
  "clienteId": 1,
  "itens": [
    {
      "produtoId": 1,
      "quantidade": 2,
      "precoUnitario": 129.99
    },
    {
      "produtoId": 3,
      "quantidade": 1,
      "precoUnitario": 2499.99
    }
  ]
}
```
## 🔹 3. Produtos

### Endpoint principal:

| Método | Endpoint     | Descrição           |
|--------|-----------------|------------------|
| POST   | /api/produtos   | Registra produto |


### 📥 Exemplo de Produto

```json
{
  "nome": "Mouse Gamer RGB",
  "preco": 159.90,
  "categoria": {
    "id": 2
  },
  "fornecedores": [
    {
      "id": 1
    }
  ],
  "estoque": {
    "quantidade": 25
  }
}
```
---

## 🔹 4. Categorias e Fornecedores

### Endpoints principais:

| Método | Endpoint               | Descrição          |
|--------|------------------------|--------------------|
| POST   | /api/categorias        | Registra categoria |
| GET    | /api/categorias        | Lista as categorias |
| GET    | /api/categorias/{id}   | Lista a categoria por id |
| PUT    | /api/categorias/{id}   | Edita a categoria por id|
| DELETE | /api/categorias/{id}   | Deleta a categoria por id |
| POST   | /api/fornecedores      | Registra o fornecedor   |
| GET    | /api/fornecedores      | Lista os fornecedores   |
| GET    | /api/fornecedores/{id} | Lista o fornecedor por id |
| PUT    | /api/fornecedores/{id} | Edita o fornecedor por id   |
| DELETE | /api/fornecedores/{id} | Deleta o fornecedor por id   |


### 📥 Exemplo de Fornecedor/Categoria

```json
{
  "nome": "Categoria x",
}
{
  "nome": "Fornecedor X",
}
```
---

## 🔥 Lógica de Estoque (Transacional)

O módulo de vendas utiliza:

```java
@Transactional
```

Isso garante que todo o processo de venda é **atômico**:

- ✔ Se todos os produtos têm estoque suficiente → **venda confirmada**
- ❌ Se qualquer item não tiver estoque suficiente → **tudo é cancelado automaticamente**

---

### ✔ Cenário de Sucesso (HTTP 201)

- Estoque suficiente  
- Venda registrada  
- Estoque atualizado  

---

### ❌ Cenário de Falha — Estoque Insuficiente (HTTP 400)

Quando a quantidade solicitada excede o estoque disponível:

```
HTTP 400 Bad Request
```

Mensagem:

```
Estoque insuficiente para o produto X.
```

---

## 🔍 Verificação Importante

Mesmo se alguns itens tenham estoque suficiente:

- ❌ Nenhuma baixa parcial será feita  
- ❌ A venda não será salva  
- ✔ O estoque permanece intacto  
- ✔ Rollback garante integridade total  

---

## 📦 Tecnologias Utilizadas

- Spring Boot  
- Spring Data JPA  
- Spring Web  
- PostgreSQL / MySQL / H2  
- Maven  
- Java 17+

## Link do video: https://drive.google.com/file/d/1Lvi1XJCN3Wm7Cx9J8A-TPspjADgdu46B/view?usp=sharing
