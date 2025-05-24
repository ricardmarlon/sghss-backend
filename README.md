# 🏥 SGHSS Backend: Sistema de Gestão Hospitalar e de Serviços de Saúde (VidaPlus)

Olá! Seja bem-vindo(a) ao repositório do **Back-end do SGHSS (Sistema de Gestão Hospitalar e de Serviços de Saúde)**, um projeto desenvolvido como parte da minha jornada na disciplina de Projetos da UNINTER. Aqui, você encontrará o coração da aplicação, responsável por toda a lógica de negócio, persistência de dados e segurança da informação para a nossa instituição fictícia, a VidaPlus.

Este trabalho foi uma oportunidade incrível para aplicar na prática os conceitos de Análise e Desenvolvimento de Sistemas, com uma imersão profunda na **Engenharia de Software** e nas melhores práticas para construir um sistema robusto e seguro. Minha ênfase principal foi justamente no Back-end, e este repositório é o resultado desse esforço.

## 🚀 Tecnologias Utilizadas

Para dar vida a este Back-end, escolhi ferramentas e frameworks que são pilares no desenvolvimento moderno:

- **Linguagem de Programação:** Java 17
- **Framework:** Spring Boot 3.2.5 (com Spring Web, Spring Data JPA, Spring Security)
- **Banco de Dados:** H2 Database (em memória, perfeito para desenvolvimento e testes!)
- **ORM (Object-Relational Mapping):** Hibernate (via Spring Data JPA)
- **Gerenciador de Dependências:** Apache Maven
- **Dependências Auxiliares:** Lombok (para me ajudar a escrever menos código repetitivo), JJWT (para a segurança dos tokens), e Spring Boot Starter Validation (para garantir que os dados estejam sempre certinhos).

## ✨ Visão Geral da Arquitetura

O sistema foi concebido com uma arquitetura de microsserviço monolítico, mas modular, organizada em camadas bem definidas. Isso me ajudou a manter o código limpo, separado por responsabilidades, e fácil de entender (e de dar manutenção!):

- **`Controller`:** É a porta de entrada da API. Recebe as requisições HTTP, valida os dados e "conversa" com a camada de serviço.
- **`Service`:** Onde a magia da lógica de negócio acontece! Aqui, as regras são aplicadas e as operações são orquestradas, interagindo com o banco de dados via repositórios.
- **`Repository`:** A camada que fala diretamente com o banco de dados. Graças ao Spring Data JPA, as operações de salvar, buscar e deletar dados se tornam muito mais simples.
- **`Model`:** São as classes que representam nossas entidades (como `Paciente` e `Usuario`), que são o espelho das tabelas no banco de dados.
- **`DTO`:** Pequenos objetos que transportam dados de forma otimizada entre as camadas, garantindo que a API não exponha mais do que o necessário.

## 🔑 Autenticação e Segurança (LGPD em Foco!)

A segurança foi uma das minhas maiores preocupações. Dada a natureza sensível dos dados de saúde, implementei um sistema robusto de **autenticação e autorização** utilizando **Spring Security e JSON Web Tokens (JWT)**. Isso significa que apenas usuários válidos podem acessar informações protegidas, e tudo é feito pensando na **conformidade com a LGPD**.

## 🚦 Endpoints da API

A API é acessada via HTTP e possui os seguintes endpoints principais:

### **Endpoints de Autenticação (`/api/auth`)**

- `POST /api/auth/register`

  - **Objetivo:** Permite que novos usuários criem uma conta no sistema. A senha é criptografada automaticamente!

  - **Exemplo de Body:**

    ```
    {
        "username": "seu_usuario",
        "email": "seu.email@exemplo.com",
        "password": "senha_segura"
    }
    ```

  - **Resposta:** `201 Created` (sucesso), `409 Conflict` (usuário/e-mail duplicado).

- `POST /api/auth/login`

  - **Objetivo:** Autentica o usuário e, se tudo estiver certo, retorna um JWT para você usar em todas as requisições protegidas.

  - **Exemplo de Body:**

    ```
    {
        "username": "seu_usuario",
        "password": "senha_segura"
    }
    ```

  - **Resposta:** `200 OK` (com o token JWT), `401 Unauthorized` (credenciais inválidas).

### **Endpoints de Gerenciamento de Pacientes (`/api/pacientes`)**

**Atenção:** Todos esses endpoints exigem um JWT válido no cabeçalho `Authorization: Bearer SEU_TOKEN_AQUI`.

- `POST /api/pacientes`

  - **Objetivo:** Cadastra um novo paciente no sistema.

  - **Exemplo de Body:**

    ```
    {
        "nome": "Ana Clara",
        "cpf": "11122233344",
        "email": "ana.clara@email.com",
        "telefone": "987654321",
        "endereco": "Rua das Acácias, 456"
    }
    ```

  - **Resposta:** `201 Created` (sucesso), `409 Conflict` (CPF/e-mail duplicado).

- `GET /api/pacientes`

  - **Objetivo:** Lista todos os pacientes cadastrados.
  - **Resposta:** `200 OK` (com a lista de pacientes).

- `GET /api/pacientes/{id}`

  - **Objetivo:** Busca os detalhes de um paciente específico pelo seu ID.
  - **Resposta:** `200 OK` (paciente encontrado), `404 Not Found` (paciente não existe).

- `PUT /api/pacientes/{id}`

  - **Objetivo:** Atualiza os dados de um paciente existente. (O CPF é imutável para garantir a integridade!)

  - **Exemplo de Body:**

    ```
    {
        "nome": "Ana Clara Nova",
        "email": "ana.nova@email.com"
    }
    ```

  - **Resposta:** `200 OK` (atualizado), `404 Not Found` (ID não existe).

- `DELETE /api/pacientes/{id}`

  - **Objetivo:** Remove um paciente do sistema.
  - **Resposta:** `204 No Content` (sucesso, sem retorno de conteúdo), `404 Not Found` (ID não existe).

## ⚙️ Como Rodar o Projeto (Passo a Passo)

Para você explorar o Back-end do SGHSS na sua máquina:

1. **Pré-requisitos:**

   - Java Development Kit (JDK) 17 ou superior.
   - Apache Maven.
   - Sua IDE favorita (IntelliJ IDEA, VS Code, Eclipse).
   - Postman ou Insomnia (para testar a API).

2. **Clone o Repositório:**

   ```
   git clone https://github.com/ricardmarlon/sghss-backend.git
   cd sghss-backend
   ```

3. **Configurações Essenciais:**

   - Abra o arquivo `src/main/resources/application.properties`.
   - **Crucial:** Altere o valor de `app.jwtSecret` para uma string **longa e aleatória** de sua escolha. Isso é vital para a segurança! `app.jwtSecret=minhaChaveSuperSecretaParaAssinarTokensSGHSS`

4. **Compilar e Iniciar a Aplicação:**

   - Abra o terminal na raiz do projeto (onde está o `pom.xml`).

   - Execute os comandos Maven:

     ```
     mvn clean install
     mvn spring-boot:run
     ```

   - A aplicação deverá iniciar na porta `8080`. Se você vir `Started SghssApplication in ...`, está tudo pronto!

## 🧪 Como Testar no Postman

Para verificar as funcionalidades da API, siga o guia detalhado que está no meu documento do projeto na seção "Plano de Testes". Basicamente:

1. **Registre um usuário:** `POST http://localhost:8080/api/auth/register`
2. **Faça login:** `POST http://localhost:8080/api/auth/login` (copie o `accessToken` retornado!).
3. **Use o token:** Inclua o `accessToken` como `Authorization: Bearer SEU_TOKEN` nos headers de todas as requisições para `/api/pacientes`.
4. **Explore os endpoints:** Cadastre, liste, consulte, atualize e delete pacientes, experimentando cenários de sucesso e erro.

## 🤝 Contribuições

Este projeto é um marco na minha formação e representa muito do que aprendi. Se tiver alguma sugestão, ideia ou melhoria, sinta-se à vontade para entrar em contato ou abrir uma "issue"!
