# Sistema APA - Backend

## 📋 Sobre o Projeto

Sistema backend desenvolvido em **Spring Boot** para gerenciamento da **APA (Associação de Proteção Animal)**. O sistema oferece funcionalidades completas para controle de feirinhas de adoção, animais, usuários, doações, estoque e muito mais.

## 🚀 Tecnologias Utilizadas

### Core
- **Java 17** - Linguagem de programação
- **Spring Boot 3.4.4** - Framework principal
- **Maven** - Gerenciamento de dependências e build

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional
- **Flyway** - Versionamento e migração de banco de dados
- **JPA/Hibernate** - ORM para persistência de dados

### Segurança
- **Spring Security** - Autenticação e autorização
- **OAuth2** - Autenticação social
- **JWT (Auth0)** - Tokens de autenticação

### Performance e Cache
- **Redis** - Cache distribuído
- **Bucket4j** - Rate limiting

### Monitoramento e Logs
- **Log4j2** - Sistema de logging estruturado
- **Spring Actuator** - Monitoramento de saúde da aplicação

### Documentação
- **SpringDoc OpenAPI 3** - Documentação automática da API (Swagger)

### Desenvolvimento
- **Lombok** - Redução de código boilerplate
- **Spring DevTools** - Hot reload durante desenvolvimento
- **MailHog** - Servidor SMTP para testes de e-mail

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** com separação clara de responsabilidades:

```
src/main/java/com/apa/back/
├── core/
│   └── domain/          # Entidades e regras de negócio
│       └── entities/    # Entidades JPA
├── application/         # Casos de uso e serviços
└── infrastructure/      # Implementações técnicas
    ├── controllers/     # Endpoints REST
    ├── repositories/    # Acesso a dados
    ├── security/        # Configurações de segurança
    └── config/          # Configurações gerais
```

### Princípios Aplicados
- **SOLID** - Princípios de design orientado a objetos
- **DRY** - Don't Repeat Yourself
- **KISS** - Keep It Simple, Stupid
- **Clean Code** - Código limpo e manutenível

## 📦 Funcionalidades Principais

- ✅ Gerenciamento de Feirinhas de Adoção
- ✅ Cadastro e controle de Animais
- ✅ Sistema de Adoção
- ✅ Gerenciamento de Usuários e Permissões
- ✅ Controle de Doações
- ✅ Gestão de Estoque
- ✅ Histórico de Saúde Animal
- ✅ Sistema de Vacinação
- ✅ Cadastro de Voluntários
- ✅ Recuperação de Senha
- ✅ Rate Limiting (proteção contra abuso)
- ✅ Logs estruturados (JSON)

## 📚 Documentação Adicional

- **[INSTALLATION.md](./INSTALLATION.md)** - Guia completo de instalação e execução (LEIA AQUI PARA RODAR O PROJETO)
- **[COMO_RODAR.md](./COMO_RODAR.md)** - Instruções detalhadas
- **[EXEMPLOS_REQUEST.json](./insonia_examples.json)** - Exemplos de payloads da da API

## 🔗 Links Úteis

Após executar o projeto, acesse:

- **API Base**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **MailHog (E-mails)**: http://localhost:8025
- **Actuator Health**: http://localhost:8080/actuator/health

## 🚀 Quick Start

Para instruções detalhadas de instalação e execução, consulte o **[INSTALLATION.md](./INSTALLATION.md)**.

**Resumo rápido com Docker:**
```powershell
# 1. Crie o arquivo .env
@"
DB_NAME=apa_db
DB_USER=postgres
DB_PASSWORD=postgres123
SERVER_PORT=8080

REDIS_HOST=localhost
REDIS_PORT=6379

LOG_FOLDER=./logs/
"@ | Out-File -FilePath .env -Encoding utf8

# 2. Execute
docker-compose up -d

# 3. Acesse http://localhost:8080
```

## 📄 Estrutura do Banco de Dados

O banco de dados é gerenciado pelo **Flyway** com migrações versionadas em `src/main/resources/db/migration/`:

- Usuários e autenticação
- Animais e saúde
- Feirinhas e eventos
- Adoções e adotantes
- Doações e histórico
- Estoque e categorias
- Vacinações

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 👥 Autores

- Responsável Técnico e Desenvolvimento Back-end: **Marcelo Alexandre** (@marceloaaps)
- Documentação e Regras de Negócio: **Lucas Pinheiro** (@lucaspinheirodev)

- Responsável Criação Banco de Dados: **Augusto Machado** (@gustebor)
- Desenvolvimento Back-end & Contato com a APA: **Douglas**(@dougcm)
- Desenvolvimento Front-end: **Carlos Alberto** (@carlosalbertodev)
- Desenvolvimento Banco de Dados: **Elberty Rodrigues** (@albertohgk)


