# 🏆 Gerador Clash - Sistema Gerador de Campeonatos Amadores

Projeto desenvolvido para a disciplina de **Programação Web 2** no **Instituto Federal do Triângulo Mineiro (IFTM)**, sob orientação do **Prof. Rafael Godoi Orbolato**.

O **Gerador Clash** é uma aplicação web focada no gerenciamento de torneios esportivos amadores em formato eliminatório (**mata-mata**), permitindo cadastrar equipes e seus atletas, alocar arenas oficiais e acompanhar o chaveamento das partidas.

---

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Framework:** Spring Boot 4.1.x
- **Persistência & Banco de Dados:**
  - Spring Data JPA / Hibernate
  - PostgreSQL 16+
  - Flyway Migration (versionamento de banco de dados)
- **Front-end:**
  - Thymeleaf + Thymeleaf Layout Dialect
  - Tailwind CSS
  - Alpine.js (menus retráteis e modais)
  - HTMX (interações dinâmicas)
  - SweetAlert2
- **Utilitários:** Lombok, Jakarta Bean Validation

---

## 📋 Pré-requisitos

Para rodar este projeto em qualquer máquina, você precisará ter instalado:

1. **Java JDK 21** ou superior:
   - Verifique com o comando:
     ```bash
     java -version
     ```
2. **PostgreSQL 16** (ou versão compatível):
   - Serviço rodando na porta padrão `5432`.
3. **Git** (para clonar o repositório).

> **Nota:** Não é necessário ter o Maven instalado globalmente na máquina; o projeto utiliza o **Maven Wrapper** (`mvnw` / `mvnw.cmd`) incluso no repositório.

---

## 🛠️ Passo a Passo para Execução

### 1. Clonar o Repositório
```bash
git clone https://github.com/MatheusrgsS/gerador-clash-web2.git
cd gerador-clash-web2
```

### 2. Criar o Banco de Dados no PostgreSQL
Acesse o seu PostgreSQL via terminal (`psql`) ou pelo **pgAdmin** e crie a base de dados do projeto:

```sql
CREATE DATABASE gerador_clash_db;
```

*(Não é necessário criar tabelas manualmente! O **Flyway** executará as migrações automaticamente ao iniciar a aplicação, criando o esquema e semeando as 10 arenas oficiais).*

### 3. Configurar as Credenciais do Banco
Abra o arquivo `src/main/resources/application.properties` e verifique se as credenciais correspondem ao seu PostgreSQL local:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gerador_clash_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```
*Se a senha do seu usuário `postgres` for diferente de `postgres`, altere o campo `spring.datasource.password`.*

### 4. Executar a Aplicação

#### No Windows (PowerShell / CMD):
```powershell
.\mvnw.cmd spring-boot:run
```

#### No Linux / macOS:
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

---

## 🌐 Acessando o Sistema (Seguro via HTTPS)

Assim que a mensagem `Started GeradorClashApplication` aparecer no console, acesse no navegador via **HTTPS**:

- **Página Inicial (Lobby & Arenas):** [https://localhost:8443/](https://localhost:8443/)
- **Tela de Login:** [https://localhost:8443/login](https://localhost:8443/login)
- **Gerenciamento de Usuários (Exclusivo ADMIN):** [https://localhost:8443/usuarios](https://localhost:8443/usuarios)
- **Listagem de Equipes:** [https://localhost:8443/equipes](https://localhost:8443/equipes)
- **Meus Torneios:** [https://localhost:8443/campeonatos](https://localhost:8443/campeonatos)

> **Nota sobre o certificado HTTPS:** Como o certificado SSL local foi autoassinado para desenvolvimento, o navegador exibirá um alerta de segurança na primeira vez. Basta clicar em "Avançado" e prosseguir para `localhost`.

---

## 🔐 Credenciais Padrão para Avaliação (Etapa 6 - Spring Security)

| Tipo de Usuário | Login | Senha | Nível de Acesso |
|---|---|---|---|
| **Administrador** | `admin` | `admin123` | Acesso completo. Gerencia e cadastra novos usuários (`/usuarios`), equipes e campeonatos. |
| **Organizador** | `organizador` | `organizador123` | Cria novos torneios, cadastra equipes e insere placares. Sem acesso à gestão de usuários. |
| **Visitante** (sem login) | - | - | Acesso público apenas para leitura (Home/Arenas, Chaveamento e Equipes). |

---

## 📂 Estrutura das Migrações de Banco (Flyway)

- `V1__criar_tabelas.sql`: Criação das tabelas de `arena`, `equipe`, `jogador`, `campeonato`, `partida` e associações.
- `V2__popular_arenas.sql`: Carga inicial das 10 arenas oficiais do torneio.
- `V3__criar_tabelas_usuarios.sql`: Criação das tabelas de segurança (`usuario`, `papel`, `usuario_papel`) e inserção dos papéis `ROLE_ADMIN` e `ROLE_ORGANIZADOR`.

---

## 👨‍💻 Autor

- **Matheus** - Aluno de Sistemas para Internet / IFTM
- **Professor Orientador:** Rafael Godoi Orbolato (`profgodoi`)
