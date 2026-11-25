# Gerador Clash

Sistema web para gerenciar campeonatos amadores em formato mata-mata. Permite cadastrar equipes, jogadores, alocar arenas e acompanhar o chaveamento.

Trabalho final da disciplina de Programação Web 2 — IFTM, orientado pelo Prof. Rafael Godoi Orbolato.

## Tecnologias

- Java 21, Spring Boot 4.1
- Spring Data JPA + Hibernate + PostgreSQL
- Flyway (migrações de banco)
- Thymeleaf + Tailwind CSS + Alpine.js + HTMX
- Spring Security
- JasperReports (relatórios em PDF)
- Lombok

## Pré-requisitos

- Java JDK 21+
- PostgreSQL 16+

Não precisa instalar Maven — o projeto usa o Maven Wrapper (`./mvnw`).

## Como rodar

1. Clone o repositório:
```bash
git clone https://github.com/MatheusrgsS/gerador-clash-web2.git
cd gerador-clash-web2
```

2. Crie o banco no PostgreSQL:
```sql
CREATE DATABASE gerador_clash_db;
```
As tabelas são criadas automaticamente pelo Flyway.

3. Ajuste as credenciais em `src/main/resources/application.properties` se necessário:
```properties
spring.datasource.username=postgres
spring.datasource.password=12345
```

4. Execute:
```bash
# Linux/macOS
chmod +x mvnw
./mvnw spring-boot:run

# Windows
.\mvnw.cmd spring-boot:run
```

5. Acesse: https://localhost:8443

O certificado SSL é autoassinado — o navegador vai mostrar um aviso, basta prosseguir.

## Usuários padrão

| Login | Senha | Papel |
|---|---|---|
| `admin` | `admin123` | Administrador — acesso completo |
| `organizador` | `organizador123` | Organizador — cria torneios e equipes |
| (sem login) | — | Visitante — somente leitura |

## Relatórios PDF

Relatórios de campeonatos com JasperReports, incluindo sub-relatório de jogadores por equipe.

Acesso: botão "PDF" na listagem de campeonatos ou na página de chaveamento.

## Migrações do banco

- `V1__criar_tabelas.sql` — tabelas principais (arena, equipe, jogador, campeonato, partida)
- `V2__popular_arenas.sql` — carga das 10 arenas
- `V3__criar_tabelas_usuarios.sql` — tabelas de segurança (usuario, papel)

## Autor

Matheus — Sistemas para Internet, IFTM
