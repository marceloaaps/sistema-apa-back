# Sistema APA - Back-end

Sistema back-end para gerenciamento da APA (Associação de Proteção Animal).

## 🚀 Início Rápido (TL;DR)

### ⭐ Método Recomendado: Docker Compose

**Você SÓ precisa do Docker instalado!** O docker-compose já configura tudo automaticamente:
- ✅ PostgreSQL 15
- ✅ Redis 7
- ✅ MailHog (servidor de e-mail para testes)
- ✅ Java 17 + Maven (para compilar)
- ✅ Aplicação Spring Boot

```powershell
# 1. Crie o arquivo .env na raiz do projeto
@"
DB_NAME=apa_db
DB_USER=postgres
DB_PASSWORD=postgres123
"@ | Out-File -FilePath .env -Encoding utf8

# 2. Execute (primeira vez demora ~10 min para baixar tudo e compilar)
docker-compose up -d

# 3. Acompanhe os logs
docker-compose logs -f api

# 4. Acesse a aplicação
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# MailHog: http://localhost:8025
```

**Pronto! Não precisa instalar Java, Maven, PostgreSQL ou Redis!** 🎉

---

### Método Alternativo: Ambiente Local

<details>
<summary>👉 Clique aqui se preferir rodar sem Docker (requer instalação manual)</summary>

```powershell
# Pré-requisitos: Java 17, Maven, PostgreSQL e Redis instalados

# 1. Configure as variáveis de ambiente
$env:DB_NAME="apa_db"
$env:DB_USER="postgres"
$env:DB_PASSWORD="sua_senha"

# 2. Crie o banco de dados
psql -U postgres -c "CREATE DATABASE apa_db;"

# 3. Inicie o Redis
docker run -d -p 6379:6379 redis:7

# 4. Compile e rode
mvn clean install
mvn spring-boot:run
```

</details>

---

## 📋 Pré-requisitos

### Para rodar com Docker Compose (Recomendado)

Você só precisa de:
- **Docker Desktop** ([Download](https://www.docker.com/products/docker-desktop/))

### Para rodar localmente (Opcional)

- **Java 17** ou superior ([Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL 15** ([Download](https://www.postgresql.org/download/))
- **Redis 7** ([Download](https://redis.io/download/))

## 🔨 Instalação do Docker Desktop (Windows)

### Instalar Docker Desktop - Passo a Passo

1. **Baixar o instalador**
   - Acesse https://www.docker.com/products/docker-desktop/
   - Clique em **Download for Windows**

2. **Executar o instalador**
   - Execute o arquivo `Docker Desktop Installer.exe`
   - Marque a opção **"Use WSL 2 instead of Hyper-V"** (recomendado)
   - Clique em **Ok** e aguarde a instalação

3. **Reiniciar o computador**
   - Após a instalação, reinicie o Windows

4. **Iniciar o Docker Desktop**
   - Abra o Docker Desktop pelo menu iniciar
   - Aguarde o Docker inicializar (ícone da baleia na barra de tarefas fica verde)
   - Aceite os termos de serviço se solicitado

5. **Verificar instalação**
   - Abra o PowerShell e execute:
     ```powershell
     docker --version
     docker-compose --version
     docker ps
     ```
   
   **Saída esperada:**
   ```
   Docker version 24.x.x
   Docker Compose version v2.x.x
   CONTAINER ID   IMAGE   COMMAND   CREATED   STATUS   PORTS   NAMES
   ```

✅ **Pronto! Agora você pode rodar o projeto com docker-compose.**

---

<details>
<summary>📦 Instalação Manual (somente se NÃO quiser usar Docker)</summary>

### 1. Instalar Java 17

1. Acesse https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
2. Baixe o **Windows x64 Installer** (jdk-17_windows-x64_bin.exe)
3. Execute o instalador e siga as instruções
4. Configure a variável de ambiente `JAVA_HOME`:
   ```powershell
   [System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-17', 'Machine')
   [System.Environment]::SetEnvironmentVariable('Path', $env:Path + ';C:\Program Files\Java\jdk-17\bin', 'Machine')
   ```
5. Verifique: `java -version`

### 2. Instalar Maven

1. Acesse https://maven.apache.org/download.cgi
2. Baixe o **Binary zip archive**
3. Extraia para `C:\Program Files\Apache\maven`
4. Configure variáveis de ambiente:
   ```powershell
   [System.Environment]::SetEnvironmentVariable('MAVEN_HOME', 'C:\Program Files\Apache\maven', 'Machine')
   [System.Environment]::SetEnvironmentVariable('Path', $env:Path + ';C:\Program Files\Apache\maven\bin', 'Machine')
   ```
5. Verifique: `mvn -version`

### 3. Instalar PostgreSQL

1. Acesse https://www.postgresql.org/download/windows/
2. Baixe e execute o instalador do PostgreSQL 15
3. Anote a senha do usuário `postgres`
4. Verifique: `psql --version`

### 4. Instalar Redis

Use o Docker para Redis ou instale o Memurai:
```powershell
docker run -d -p 6379:6379 --name redis redis:7
```

</details>

## 🚀 Como Rodar o Projeto

### ⭐ Opção Recomendada: Docker Compose - Passo a Passo Detalhado

Esta é a forma **mais fácil e rápida**! O Docker Compose configura automaticamente todos os serviços necessários (PostgreSQL, Redis, MailHog e a API). **Você não precisa instalar Java, Maven, PostgreSQL ou Redis manualmente!**

#### Passo 1: Verificar se o Docker Desktop está Rodando

Abra o PowerShell e verifique:

```powershell
# Verificar se o Docker está instalado e rodando
docker --version
docker-compose --version
docker ps
```

**Se os comandos funcionarem, você está pronto!** ✅

**Se não funcionar:**
- Verifique se o Docker Desktop está aberto e inicializado
- Ou volte para a seção de instalação do Docker Desktop

#### Passo 2: Clonar o Repositório (se ainda não clonou)

```powershell
cd E:\Workspaces\ws-java
git clone <url-do-repositorio>
cd sistema-apa-back
```

#### Passo 3: Entender o que o docker-compose.yml faz

O arquivo `docker-compose.yml` já está configurado e faz tudo automaticamente:

1. **Cria e configura o PostgreSQL 15** - Banco de dados
2. **Cria e configura o Redis 7** - Cache
3. **Cria e configura o MailHog** - Servidor de e-mail para testes
4. **Compila o projeto** - Usa Maven dentro do container
5. **Executa a aplicação** - Roda o Spring Boot

Veja o conteúdo:

```powershell
cat docker-compose.yml
```

#### Passo 4: Criar o Arquivo .env

Crie um arquivo `.env` na raiz do projeto com as credenciais do banco de dados:

**Opção A: Criar diretamente pelo PowerShell**

```powershell
@"
DB_NAME=apa_db
DB_USER=postgres
DB_PASSWORD=postgres123
"@ | Out-File -FilePath .env -Encoding utf8
```

**Opção B: Criar manualmente**

```powershell
# Criar arquivo
New-Item -Path .env -ItemType File

# Editar
notepad .env
```

E adicione:
```
DB_NAME=apa_db
DB_USER=postgres
DB_PASSWORD=postgres123
```

#### Passo 5: Iniciar Todos os Serviços

Execute o comando mágico:

```powershell
docker-compose up -d
```

**O que acontece (automaticamente!):**

1. 📥 Docker baixa as imagens necessárias (primeira vez)
   - `postgres:15` - Banco de dados
   - `redis:7` - Cache
   - `mailhog/mailhog` - Servidor de e-mail
   - `maven:3.9.6` - Para compilar
   - `eclipse-temurin:17` - Java 17

2. 🗄️ Cria volume para persistir dados do PostgreSQL

3. 🌐 Cria rede Docker para os containers se comunicarem

4. 🚀 Inicia todos os containers:
   - PostgreSQL (porta 5432)
   - Redis (porta 6379)
   - MailHog (portas 1025 e 8025)
   
5. 🔨 **Compila o projeto** usando Maven
   - Baixa todas as dependências do `pom.xml`
   - Compila o código Java
   - Cria o arquivo `.jar`

6. ▶️ Executa a aplicação Spring Boot (porta 8080)

**⏱️ IMPORTANTE:** A primeira execução pode demorar **5 a 15 minutos**!
- Maven baixa ~500MB de dependências
- Docker baixa ~2GB de imagens

**Execuções seguintes são rápidas (30-60 segundos).**

#### Passo 6: Acompanhar a Inicialização

Veja os logs em tempo real:

```powershell
# Ver logs de todos os serviços
docker-compose logs -f

# Ver apenas logs da API (recomendado)
docker-compose logs -f api

# Ver apenas logs do PostgreSQL
docker-compose logs -f postgres
```

**Para sair dos logs:** Pressione `Ctrl + C`

**Aguarde até ver esta mensagem nos logs:**
```
Started BackApplication in X.XXX seconds
```

#### Passo 7: Verificar se Tudo Está Rodando

```powershell
# Listar containers rodando
docker-compose ps
```

**Saída esperada:**

```
NAME            IMAGE                STATUS         PORTS
apa_postgres    postgres:15         Up 2 minutes   0.0.0.0:5432->5432/tcp
apa_api         sistema-apa-back    Up 1 minute    0.0.0.0:8080->8080/tcp
redis           redis:7             Up 2 minutes   0.0.0.0:6379->6379/tcp
mailhog         mailhog/mailhog     Up 2 minutes   0.0.0.0:1025->1025/tcp, 0.0.0.0:8025->8025/tcp
```

Todos devem mostrar `Up` no status! ✅

#### Passo 8: Testar a Aplicação

**No navegador, acesse:**

- ✅ **API Health:** http://localhost:8080/actuator/health
- ✅ **Swagger UI:** http://localhost:8080/swagger-ui.html
- ✅ **MailHog UI:** http://localhost:8025

**Ou teste pelo PowerShell:**

```powershell
# Testar health check
curl http://localhost:8080/actuator/health

# Deve retornar algo como: {"status":"UP"}
```

**Se funcionou, PARABÉNS! 🎉 O projeto está rodando!**

#### Passo 9: Explorar o Banco de Dados

Conecte ao PostgreSQL que está rodando no container:

```powershell
# Conectar via psql
docker exec -it apa_postgres psql -U postgres -d apa_db

# Dentro do psql, execute:
\dt                                  # Lista todas as tabelas
SELECT * FROM flyway_schema_history; # Ver migrações executadas
\d usuarios                          # Ver estrutura da tabela usuarios
\q                                   # Sair
```

**Ou use ferramentas gráficas** (pgAdmin, DBeaver):
- **Host:** localhost
- **Port:** 5432
- **Database:** apa_db
- **Username:** postgres
- **Password:** postgres123

#### Passo 10: Explorar o Redis

```powershell
# Conectar ao Redis
docker exec -it redis redis-cli

# Dentro do redis-cli:
ping       # Deve retornar PONG
keys *     # Listar todas as chaves
quit       # Sair
```

#### Passo 11: Testar Envio de E-mails

Acesse o MailHog: http://localhost:8025

Qualquer e-mail enviado pela aplicação aparecerá aqui!

---

### 📋 Comandos Úteis do Docker Compose

```powershell
# Ver logs em tempo real
docker-compose logs -f api

# Ver status dos containers
docker-compose ps

# Parar todos os containers (mantém os dados)
docker-compose stop

# Reiniciar todos os containers
docker-compose start

# Reiniciar apenas a API
docker-compose restart api

# Parar e remover containers (mantém os volumes/dados)
docker-compose down

# Parar e remover TUDO (inclusive dados do banco!)
docker-compose down -v

# Reconstruir a API após mudanças no código
docker-compose up -d --build api

# Ver uso de CPU/memória dos containers
docker stats

# Executar comandos dentro de um container
docker exec -it apa_postgres bash
docker exec -it apa_api bash
```

---

### 🔄 Fluxo de Desenvolvimento Diário

**Iniciar o ambiente:**
```powershell
docker-compose up -d
docker-compose logs -f api  # Acompanhar inicialização
```

**Fazer mudanças no código e recompilar:**
```powershell
docker-compose up -d --build api
```

**Ver logs em tempo real:**
```powershell
docker-compose logs -f api
```

**Parar tudo no fim do dia:**
```powershell
docker-compose stop
```

**Recomeçar no outro dia:**
```powershell
docker-compose start
```

---

### 🐛 Troubleshooting Docker

**Problema: API não inicia**

```powershell
# Ver logs detalhados
docker-compose logs api

# Ver status
docker ps -a

# Reiniciar apenas a API
docker-compose restart api
```

**Problema: Porta 8080 já está em uso**

```powershell
# Verificar qual processo está usando a porta
netstat -ano | findstr :8080

# Matar o processo (substitua <PID> pelo número retornado)
taskkill /PID <PID> /F
```

Ou altere a porta no `docker-compose.yml`:
```yaml
api:
  ports:
    - "8081:8080"  # Altera porta externa para 8081
```

**Problema: Erro de compilação no Maven**

```powershell
# Limpar e reconstruir do zero
docker-compose down -v
docker-compose up -d --build
```

**Problema: Banco de dados com erro**

```powershell
# Resetar o banco (CUIDADO: apaga todos os dados!)
docker-compose down -v
docker-compose up -d
```

**Problema: "Docker daemon not running"**

- Abra o Docker Desktop
- Aguarde inicializar
- Tente novamente

**Limpar tudo e começar do zero:**

```powershell
# Parar e remover tudo
docker-compose down -v

# Remover imagens não utilizadas
docker image prune -a

# Reiniciar
docker-compose up -d
```

---

<details>
<summary>💻 Opção Alternativa: Ambiente Local (sem Docker)</summary>

### Pré-requisitos

Você precisa instalar manualmente:
- Java 17
- Maven 3.6+
- PostgreSQL 15
- Redis 7

(Veja a seção "Instalação Manual" acima)

### Passo a Passo

### Passo a Passo

#### 1. Clonar o Repositório

```powershell
cd E:\Workspaces\ws-java
git clone <url-do-repositorio>
cd sistema-apa-back
```

#### 2. Verificar o Arquivo pom.xml

O arquivo `pom.xml` é o coração do projeto Maven. Ele contém:
- Dependências do projeto (Spring Boot, PostgreSQL, Redis, etc.)
- Configurações de build
- Plugins necessários

Abra o arquivo `pom.xml` para verificar:

```powershell
notepad pom.xml
```

**Informações importantes no pom.xml:**
- **Java Version:** `<java.version>17</java.version>` (confirme que você tem Java 17+)
- **Spring Boot Version:** `3.4.4`
- **Artifact:** `back` (nome do projeto)
- **Dependencies:** Spring Security, JPA, PostgreSQL, Redis, etc.

#### 3. Configurar o Banco de Dados PostgreSQL

##### 3.1. Criar o Banco de Dados

Abra o pgAdmin ou conecte-se via terminal:

```powershell
# Conectar ao PostgreSQL
psql -U postgres

# Dentro do psql, criar o banco:
CREATE DATABASE apa_db;

# Listar bancos para confirmar:
\l

# Sair:
\q
```

##### 3.2. Configurar Variáveis de Ambiente

No PowerShell, configure as variáveis de ambiente:

```powershell
# Definir variáveis para a sessão atual
$env:DB_NAME="apa_db"
$env:DB_USER="postgres"
$env:DB_PASSWORD="sua_senha_postgres"
$env:SERVER_PORT="8080"
```

**Ou crie um arquivo `.env`** na raiz do projeto:

```properties
DB_NAME=apa_db
DB_USER=postgres
DB_PASSWORD=sua_senha_postgres
SERVER_PORT=8080
```

**Para tornar as variáveis permanentes no Windows:**

```powershell
# Execute como Administrador:
[System.Environment]::SetEnvironmentVariable('DB_NAME', 'apa_db', 'User')
[System.Environment]::SetEnvironmentVariable('DB_USER', 'postgres', 'User')
[System.Environment]::SetEnvironmentVariable('DB_PASSWORD', 'sua_senha', 'User')
[System.Environment]::SetEnvironmentVariable('SERVER_PORT', '8080', 'User')
```

Depois, **reinicie o PowerShell** para aplicar as mudanças.

#### 4. Iniciar o Redis

##### Opção A: Usando Docker

```powershell
# Verificar se o Docker está rodando
docker ps

# Iniciar Redis
docker run -d --name redis -p 6379:6379 redis:7

# Verificar se está rodando
docker ps
```

##### Opção B: Usando instalação local

Se instalou Redis/Memurai localmente, inicie o serviço:

```powershell
# Para Memurai
net start Memurai
```

**Testar conexão com Redis:**

```powershell
redis-cli ping
# Deve retornar: PONG
```

#### 5. Iniciar MailHog (Opcional - para testes de e-mail)

```powershell
docker run -d --name mailhog -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

Acesse a interface web: http://localhost:8025

#### 6. Baixar Dependências do Maven

Antes de compilar, o Maven precisa baixar todas as dependências listadas no `pom.xml`:

```powershell
# Navegue até a pasta do projeto
cd E:\Workspaces\ws-java\sistema-apa-back

# Baixar dependências
mvn dependency:resolve
```

Este comando irá:
- Ler o arquivo `pom.xml`
- Baixar todas as dependências (Spring Boot, PostgreSQL driver, etc.)
- Armazenar no repositório local Maven (~/.m2/repository)

**Primeira execução pode demorar!** O Maven baixa centenas de MB de dependências.

#### 7. Compilar o Projeto

Agora compile o projeto completo:

```powershell
mvn clean install
```

**O que acontece neste comando:**
1. `clean`: Remove a pasta `target/` com compilações antigas
2. `compile`: Compila o código-fonte Java
3. `test`: Executa todos os testes unitários
4. `package`: Cria o arquivo `.jar`
5. `install`: Instala o `.jar` no repositório local Maven

**Saída esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time:  XX s
```

**Se houver erro:**
- Verifique se o Java 17 está instalado: `java -version`
- Verifique se o Maven está configurado: `mvn -version`
- Verifique se há erros de compilação no código

#### 8. Executar a Aplicação

Agora você tem 3 opções para executar:

##### Opção A: Usando Maven Spring Boot Plugin (Desenvolvimento - Recomendado)

```powershell
mvn spring-boot:run
```

##### Opção B: Executando o JAR gerado

```powershell
java -jar target/back-0.0.1-SNAPSHOT.jar
```

##### Opção C: Usando a IDE (IntelliJ IDEA)

1. Abra o projeto no IntelliJ
2. Localize a classe principal (anotada com `@SpringBootApplication`)
3. Clique com botão direito → Run

**Saída esperada no console:**

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.4.4)

...
INFO  c.a.back.BackApplication - Started BackApplication in X seconds
```

#### 9. Verificar se a Aplicação Está Rodando

Abra um navegador e acesse:

- **API Health:** http://localhost:8080/actuator/health
- **Swagger UI:** http://localhost:8080/swagger-ui.html

Ou teste via PowerShell:

```powershell
curl http://localhost:8080/actuator/health
```

#### 10. Verificar o Banco de Dados

O Flyway criará automaticamente todas as tabelas na primeira execução.

Conecte-se ao banco e verifique:

```powershell
psql -U postgres -d apa_db

# Dentro do psql:
\dt
# Lista todas as tabelas criadas

SELECT * FROM flyway_schema_history;
# Mostra todas as migrações executadas
```

#### 11. Testar a API

Você pode usar:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Postman** ou **Insomnia** (há um arquivo `insomnia_examples.json` no projeto)
- **cURL** via PowerShell

Exemplo de teste:

```powershell
# Testar endpoint de health
curl http://localhost:8080/actuator/health

# Se houver endpoint público:
curl http://localhost:8080/api/public
```

</details>

## 🌐 Acessar a Aplicação

Após iniciar a aplicação, você pode acessar:

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **MailHog (UI)**: http://localhost:8025

## 📦 Entendendo o Arquivo pom.xml

O arquivo `pom.xml` (Project Object Model) é o arquivo de configuração central do Maven. Ele define:

### Informações do Projeto

```xml
<groupId>com.apa</groupId>
<artifactId>back</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name>back</name>
```

- **groupId:** Identificador do grupo/organização
- **artifactId:** Nome do artefato (projeto)
- **version:** Versão atual do projeto
- **name:** Nome legível do projeto

### Dependências Principais

O `pom.xml` declara todas as bibliotecas que o projeto usa:

#### Spring Boot Starters

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

**Principais starters usados:**
- **spring-boot-starter-web:** Para criar APIs REST
- **spring-boot-starter-data-jpa:** Para persistência com JPA/Hibernate
- **spring-boot-starter-security:** Para autenticação e autorização
- **spring-boot-starter-oauth2-client:** Para OAuth2

#### Banco de Dados

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Driver JDBC do PostgreSQL para conectar ao banco de dados.

#### Migrações (Flyway)

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

Gerencia versões do schema do banco de dados.

#### Cache (Redis)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

Para usar Redis como cache.

### Comandos Maven Úteis

```powershell
# Ver todas as dependências
mvn dependency:tree

# Baixar dependências sem compilar
mvn dependency:resolve

# Limpar projeto (remove target/)
mvn clean

# Compilar código-fonte
mvn compile

# Executar testes
mvn test

# Criar JAR (sem testes)
mvn package -DskipTests

# Instalar no repositório local
mvn install

# Ver informações do projeto
mvn help:effective-pom
```

## 🧪 Executar Testes

Para executar todos os testes:

```bash
mvn test
```

Para executar testes de uma classe específica:

```bash
mvn test -Dtest=NomeDaClasseTest
```

## 📦 Compilar para Produção

```bash
mvn clean package -DskipTests
```

O arquivo `.jar` será gerado em `target/back-0.0.1-SNAPSHOT.jar`

## 🔧 Configurações Importantes

### Portas Utilizadas

| Serviço | Porta |
|---------|-------|
| API Spring Boot | 8080 |
| PostgreSQL | 5432 |
| Redis | 6379 |
| MailHog SMTP | 1025 |
| MailHog UI | 8025 |

### CORS

O projeto está configurado para aceitar requisições das seguintes origens:
- http://localhost:3000
- http://localhost:5173
- http://localhost:8080

Para adicionar mais origens, edite a propriedade `cors.originPatterns` em `application.properties`.

## ⚠️ Problemas Comuns

### Porta 8080 já está em uso

Altere a porta em `application.properties` ou na variável de ambiente:

```properties
SERVER_PORT=8081
```

### Erro de conexão com PostgreSQL

- Verifique se o PostgreSQL está rodando
- Confirme que as credenciais em `.env` estão corretas
- Verifique se o banco de dados foi criado

### Erro de conexão com Redis

- Verifique se o Redis está rodando: `redis-cli ping` (deve retornar PONG)
- Confirme que está na porta 6379

### Flyway migration error

Se houver erro nas migrações do Flyway, você pode:

1. Limpar o banco e deixar o Flyway recriar:
```sql
DROP DATABASE db_name;
CREATE DATABASE db_name;
```

2. Ou forçar um baseline:
```properties
spring.flyway.baseline-on-migrate=true
```

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **PostgreSQL** (Banco de dados)
- **Flyway** (Migrações de banco)
- **Redis** (Cache)
- **Lombok**
- **Swagger/OpenAPI** (Documentação)
- **Docker** (Containerização)

## 📝 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/apa/back/
│   │   ├── domain/          # Entidades e regras de negócio
│   │   ├── presentation/    # Controllers e DTOs
│   │   ├── infrastructure/  # Repositórios e configurações
│   │   └── usecase/         # Casos de uso
│   └── resources/
│       ├── application.properties
│       ├── app.key          # Chave privada JWT
│       ├── app.pub          # Chave pública JWT
│       └── db/migration/    # Scripts Flyway
└── test/                    # Testes unitários e de integração
```

## 🔐 Segurança

O projeto utiliza autenticação JWT com chaves RSA (app.key e app.pub). 
**IMPORTANTE:** Em produção, gere novas chaves e mantenha-as em segredo.

## 📄 Licença

Este projeto é de propriedade da APA (Associação de Proteção Animal).

---

**Observação:** Antes de adicionar novas dependências, verifique a compatibilidade com Java 17 e Spring Boot 3.4.4.
