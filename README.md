💻 API de Controle de Estoque com Módulo de Vendas

Projeto em Spring Boot

Este projeto consiste em uma API REST para gestão de estoque, ampliada com um módulo completo de Vendas, incluindo controle transacional de baixa de estoque e prevenção de inconsistências.

📌 Descrição do Projeto

A API oferece:

Cadastro e gestão de Clientes

Registro de Vendas

Registro de ItensVenda

Baixa automática de estoque ao registrar uma venda

Rollback transacional caso ocorra qualquer falha (ex.: estoque insuficiente)

Integração completa entre Cliente → Venda → ItensVenda → Produto

O sistema garante integridade dos dados e evita vendas com quantidade superior ao estoque disponível.

🚀 Requisitos para Executar

Antes de iniciar, certifique-se de ter instalado:

Java 17 ou superior

Maven

PostgreSQL, MySQL ou H2 Database

⚙️ Configuração do Projeto
1️⃣ Clonar o Repositório
git clone [SEU_LINK_DO_REPOSITORIO]
cd nome-do-seu-projeto

2️⃣ Configurar o Banco de Dados

Edite o arquivo:

src/main/resources/application.properties


Exemplo (PostgreSQL):

spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_seu_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3️⃣ Executar a Aplicação

Compile e execute:

mvn clean install
mvn spring-boot:run


A API iniciará em:

http://localhost:8080

🧭 Endpoints da API

Todos seguem o padrão:

/api/{recurso}

🔹 1. Clientes (CRUD)
Método	Endpoint	Descrição
POST	/api/clientes	Cria um novo cliente
GET	/api/clientes/{id}	Busca por ID
GET	/api/clientes	Lista todos
PUT	/api/clientes/{id}	Atualiza um cliente
DELETE	/api/clientes/{id}	Exclui
📥 Exemplo de Payload (POST /api/clientes)
{
  "nome": "Maria Silva",
  "email": "maria.silva@exemplo.com",
  "telefone": "11987654321"
}

🔹 2. Vendas

Endpoint principal:

Método	Endpoint	Descrição
POST	/api/vendas	Registra venda e baixa automática do estoque
📥 Exemplo de Payload (POST /api/vendas)
{
  "clienteId": 10,
  "itensVenda": [
    {
      "produtoId": 1,
      "quantidadeVendida": 2,
      "precoUnitario": 50.00  
    },
    {
      "produtoId": 2,
      "quantidadeVendida": 5,
      "precoUnitario": 12.50
    }
  ]
}

🔥 Lógica de Estoque (Transacional)

O módulo de vendas utiliza:

@Transactional


Isso garante que todo o processo de venda é atômico.
Ou seja:

Se todos os produtos têm estoque suficiente → ✔ venda confirmada

Se qualquer item não tiver estoque suficiente → ❌ tudo é cancelado automaticamente

✔ Cenário de Sucesso (HTTP 201)

Estoque suficiente

Venda registrada

Estoque dos produtos atualizado corretamente

❌ Cenário de Falha — Estoque Insuficiente (HTTP 400)

Envie uma venda cuja quantidade excede o estoque

A API retorna:

HTTP 400 Bad Request


Com mensagem clara, por exemplo:

Estoque insuficiente para o produto X.

🔍 Verificação Importante

Mesmo que alguns produtos tenham estoque suficiente:

Nenhuma baixa parcial será feita

A venda não será salva

O estoque permanece exatamente como antes

Isso confirma que o rollback funcionou corretamente.

📦 Tecnologias Utilizadas

Spring Boot

Spring Data JPA

Spring Web

PostgreSQL / MySQL / H2

Maven

Java 17+
