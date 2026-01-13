# 🔧 Guia de Instalação e Execução - Sistema APA Backend

Este guia fornece instruções detalhadas para instalar e executar o projeto, com **preferência pelo Docker**.

---

## 📋 Índice

1. [Pré-requisitos](#-pré-requisitos)
2. [Instalação com Docker (Recomendado)](#-instalação-com-docker-recomendado)
3. [Instalação Local (Alternativa)](#-instalação-local-alternativa)
4. [Comandos Úteis](#-comandos-úteis)
5. [Troubleshooting](#-troubleshooting)

---

## 🎯 Pré-requisitos

### Para rodar com Docker (Método Recomendado)

Você **só precisa de**:
- ✅ **Docker Desktop** instalado ([Download aqui](https://www.docker.com/products/docker-desktop/))
- ✅ Git (opcional, para clonar o repositório)

**Vantagens:**
- ❌ **NÃO precisa** instalar Java
- ❌ **NÃO precisa** instalar Maven
- ❌ **NÃO precisa** instalar PostgreSQL
- ❌ **NÃO precisa** instalar Redis
- ✅ Tudo é automaticamente configurado!

### Para rodar localmente (Método Alternativo)

Se preferir **não** usar Docker, você precisará instalar manualmente:
- Java 17 ou superior
- Maven 3.8+
- PostgreSQL 15
- Redis 7

---

## 🐳 Instalação com Docker (Recomendado)

### Passo 1: Instalar o Docker Desktop (Windows)

Se você ainda não tem o Docker instalado:

#### 1.1. Baixar o Instalador

- Acesse: https://www.docker.com/products/docker-desktop/
- Clique em **"Download for Windows"**
- Baixe o arquivo `Docker Desktop Installer.exe`

#### 1.2. Executar o Instalador

1. Execute o arquivo `Docker Desktop Installer.exe`
2. Marque a opção **"Use WSL 2 instead of Hyper-V"** (recomendado)
3. Clique em **OK** e aguarde a instalação
4. **Reinicie o computador** quando solicitado

#### 1.3. Iniciar o Docker Desktop

1. Abra o **Docker Desktop** pelo menu iniciar
2. Aguarde até o ícone da baleia na barra de tarefas ficar verde
3. Aceite os termos de serviço se solicitado

#### 1.4. Verificar Instalação

Abra o PowerShell e execute:

```powershell
docker --version
docker-compose --version
docker ps
```

**Saída esperada:**
```
Docker version 24.x.x, build xxxxx
Docker Compose version v2.x.x
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
```

✅ **Se os comandos funcionaram, você está pronto!**

---

### Passo 2: Clonar o Repositório

```powershell
# Navegar para o diretório de trabalho
cd E:\Workspaces\ws-java

# Clonar o repositório (se ainda não clonou)
git clone <url-do-repositorio> sistema-apa-back

# Entrar na pasta do projeto
cd sistema-apa-back
```

---

### Passo 3: Entender o docker-compose.yml

O arquivo `docker-compose.yml` já está configurado e faz **tudo automaticamente**:

**Serviços que serão criados:**

| Serviço | Imagem | Porta | Descrição |
|---------|--------|-------|-----------|
| **postgres** | postgres:15 | 5432 | Banco de dados PostgreSQL |
| **redis** | redis:7 | 6379 | Cache Redis |
| **mailhog** | mailhog/mailhog | 1025, 8025 | Servidor de e-mail para testes |
| **api** | (compilado do código) | 8080 | Aplicação Spring Boot |

**O que o Docker Compose faz:**

1. 📥 Baixa as imagens necessárias
2. 🗄️ Cria um volume para persistir dados do PostgreSQL
3. 🌐 Cria uma rede para os containers se comunicarem
4. 🔨 **Compila o projeto** usando Maven dentro de um container
5. ▶️ Executa a aplicação Spring Boot

---

### Passo 4: Criar o Arquivo .env

Crie um arquivo `.env` na **raiz do projeto** com as credenciais do banco de dados:

#### Opção A: Criar pelo PowerShell (mais rápido)

```powershell
@"
DB_NAME=apa_db
DB_USER=postgres
DB_PASSWORD=postgres123
"@ | Out-File -FilePath .env -Encoding utf8
```

#### Opção B: Criar manualmente

```powershell
# Criar o arquivo
New-Item -Path .env -ItemType File

# Editar no Notepad
notepad .env
```

Adicione o seguinte conteúdo:

```env
DB_NAME=apa_db
DB_USER=postgres
DB_PASSWORD=postgres123
```

**Salve e feche o arquivo.**

---

### Passo 5: Iniciar Todos os Serviços 🚀

Execute o comando mágico:

```powershell
docker-compose up -d
```

**O que acontece automaticamente:**

```
🔄 Etapa 1: Baixando imagens Docker (primeira vez)
   ├─ postgres:15
   ├─ redis:7
   ├─ mailhog/mailhog
   └─ maven:3.9.6-eclipse-temurin-17

🔄 Etapa 2: Criando volumes e redes

🔄 Etapa 3: Iniciando containers
   ├─ PostgreSQL (porta 5432)
   ├─ Redis (porta 6379)
   └─ MailHog (portas 1025 e 8025)

🔄 Etapa 4: Compilando o projeto
   ├─ Baixando dependências do Maven (~500MB)
   ├─ Compilando código Java
   └─ Criando arquivo .jar

🔄 Etapa 5: Iniciando a aplicação (porta 8080)
   ├─ Conectando ao PostgreSQL
   ├─ Executando migrações Flyway
   └─ Iniciando Spring Boot

✅ PRONTO!
```

**⏱️ IMPORTANTE: A primeira execução pode demorar 5-15 minutos!**
- Docker baixa ~2GB de imagens
- Maven baixa ~500MB de dependências
- Compilação pode levar alguns minutos

**Execuções seguintes são rápidas (30-60 segundos).**

---

### Passo 6: Acompanhar a Inicialização

Para ver os logs em tempo real:

```powershell
# Ver logs de todos os serviços
docker-compose logs -f

# Ver apenas logs da API (recomendado)
docker-compose logs -f api

# Ver logs do PostgreSQL
docker-compose logs -f postgres
```

**Para sair dos logs:** Pressione `Ctrl + C`

**Aguarde até ver esta mensagem:**
```
Started BackApplication in X.XXX seconds
```

✅ **Quando aparecer, a aplicação está pronta!**

---

### Passo 7: Verificar se Está Tudo Rodando

```powershell
# Listar containers em execução
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

✅ **Todos devem mostrar `Up` no status!**

---

### Passo 8: Testar a Aplicação

#### No Navegador:

- ✅ **API Health:** http://localhost:8080/actuator/health
- ✅ **Swagger UI:** http://localhost:8080/swagger-ui.html
- ✅ **MailHog UI:** http://localhost:8025

#### No PowerShell:

```powershell
# Testar health check
curl http://localhost:8080/actuator/health

# Deve retornar: {"status":"UP"}
```

🎉 **Se funcionou, PARABÉNS! O projeto está rodando!**

---

### Passo 9: Explorar o Banco de Dados

#### Conectar via psql:

```powershell
# Conectar ao PostgreSQL dentro do container
docker exec -it apa_postgres psql -U postgres -d apa_db
```

**Comandos úteis dentro do psql:**

```sql
\dt                                  -- Lista todas as tabelas
SELECT * FROM flyway_schema_history; -- Ver migrações executadas
\d feirinhas                         -- Ver estrutura da tabela feirinhas
SELECT * FROM usuarios;              -- Ver usuários
\q                                   -- Sair
```

#### Conectar via ferramentas gráficas (pgAdmin, DBeaver):

- **Host:** localhost
- **Port:** 5432
- **Database:** apa_db
- **Username:** postgres
- **Password:** postgres123

---

### Passo 10: Explorar o Redis

```powershell
# Conectar ao Redis
docker exec -it redis redis-cli
```

**Comandos úteis:**

```bash
ping       # Deve retornar: PONG
keys *     # Listar todas as chaves
quit       # Sair
```

---

### Passo 11: Ver E-mails de Teste

Acesse o **MailHog**: http://localhost:8025

Qualquer e-mail enviado pela aplicação aparecerá aqui automaticamente!

---

## 📋 Comandos Úteis do Docker Compose

### Gerenciamento de Containers

```powershell
# Ver logs em tempo real
docker-compose logs -f api

# Ver status dos containers
docker-compose ps

# Ver uso de CPU/memória
docker stats
```

### Parar e Iniciar

```powershell
# Parar todos os containers (mantém os dados)
docker-compose stop

# Iniciar containers parados
docker-compose start

# Reiniciar todos os containers
docker-compose restart

# Reiniciar apenas a API
docker-compose restart api
```

### Reconstruir

```powershell
# Reconstruir a API após mudanças no código
docker-compose up -d --build api

# Reconstruir tudo do zero
docker-compose down
docker-compose up -d --build
```

### Limpeza

```powershell
# Parar e remover containers (mantém volumes/dados)
docker-compose down

# Parar e remover TUDO (inclusive dados do banco!)
docker-compose down -v

# Remover imagens não utilizadas
docker image prune -a
```

### Executar Comandos em Containers

```powershell
# Abrir terminal no container da API
docker exec -it apa_api bash

# Abrir terminal no PostgreSQL
docker exec -it apa_postgres bash

# Executar comando Maven na API
docker exec -it apa_api mvn dependency:tree
```

---

## 🔄 Fluxo de Desenvolvimento Diário

### Iniciar o dia de trabalho:

```powershell
# Iniciar todos os serviços
docker-compose up -d

# Acompanhar logs
docker-compose logs -f api
```

### Fazer mudanças no código:

```powershell
# Após editar o código, reconstruir a API
docker-compose up -d --build api

# Ver se compilou sem erros
docker-compose logs -f api
```

### Reiniciar o banco (limpar dados):

```powershell
# CUIDADO: Isto apaga todos os dados!
docker-compose down -v
docker-compose up -d
```

### Parar no fim do dia:

```powershell
# Parar todos os containers (dados são mantidos)
docker-compose stop
```

### Recomeçar no dia seguinte:

```powershell
# Iniciar containers parados (rápido!)
docker-compose start
```

---

## 💻 Instalação Local (Alternativa)

Se você **não** quer usar Docker, siga estas instruções.

### Pré-requisitos

Instale manualmente:

1. **Java 17**: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
2. **Maven 3.8+**: https://maven.apache.org/download.cgi
3. **PostgreSQL 15**: https://www.postgresql.org/download/
4. **Redis 7**: Use Docker ou Memurai (https://www.memurai.com/)

### Verificar Instalações

```powershell
java -version    # Deve mostrar versão 17.x
mvn -version     # Deve mostrar versão 3.x
psql --version   # Deve mostrar versão 15.x
```

---

### Passo 1: Clonar o Repositório

```powershell
cd E:\Workspaces\ws-java
git clone <url-do-repositorio> sistema-apa-back
cd sistema-apa-back
```

---

### Passo 2: Configurar o Banco de Dados

#### 2.1. Criar o Banco

```powershell
# Conectar ao PostgreSQL
psql -U postgres

# Dentro do psql, executar:
CREATE DATABASE apa_db;

# Listar bancos (para confirmar)
\l

# Sair
\q
```

#### 2.2. Configurar Variáveis de Ambiente

No PowerShell:

```powershell
# Definir para a sessão atual
$env:DB_NAME="apa_db"
$env:DB_USER="postgres"
$env:DB_PASSWORD="sua_senha_postgres"
$env:SERVER_PORT="8080"
$env:REDIS_HOST="localhost"
$env:REDIS_PORT="6379"
$env:LOG_FOLDER="./logs/"
```

**Para tornar permanente (Execute como Administrador):**

```powershell
[System.Environment]::SetEnvironmentVariable('DB_NAME', 'apa_db', 'User')
[System.Environment]::SetEnvironmentVariable('DB_USER', 'postgres', 'User')
[System.Environment]::SetEnvironmentVariable('DB_PASSWORD', 'sua_senha', 'User')
[System.Environment]::SetEnvironmentVariable('SERVER_PORT', '8080', 'User')
[System.Environment]::SetEnvironmentVariable('REDIS_HOST', 'localhost', 'User')
[System.Environment]::SetEnvironmentVariable('REDIS_PORT', '6379', 'User')
[System.Environment]::SetEnvironmentVariable('LOG_FOLDER', './logs/', 'User')
```

**Depois, reinicie o PowerShell.**

---

### Passo 3: Iniciar o Redis

#### Opção A: Com Docker

```powershell
docker run -d --name redis -p 6379:6379 redis:7

# Verificar
docker ps
```

#### Opção B: Memurai (Windows)

```powershell
# Se instalou Memurai
net start Memurai
```

**Testar:**

```powershell
redis-cli ping
# Deve retornar: PONG
```

---

### Passo 4: Iniciar MailHog (Opcional)

```powershell
docker run -d --name mailhog -p 1025:1025 -p 8025:8025 mailhog/mailhog
```

Acesse: http://localhost:8025

---

### Passo 5: Entender o pom.xml

O arquivo `pom.xml` é o coração do projeto Maven. Ele contém:

- **Informações do Projeto:**
  - `groupId`: com.apa
  - `artifactId`: back
  - `version`: 0.0.1-SNAPSHOT
  
- **Dependências:**
  - Spring Boot Starters (Web, JPA, Security)
  - PostgreSQL Driver
  - Redis
  - Log4j2
  - Flyway
  - JWT
  - E muito mais...

- **Plugins:**
  - Maven Compiler Plugin
  - Spring Boot Maven Plugin

**Ver o arquivo:**

```powershell
notepad pom.xml
```

---

### Passo 6: Baixar Dependências

```powershell
# Navegar para a pasta do projeto
cd E:\Workspaces\ws-java\sistema-apa-back

# Baixar todas as dependências
mvn dependency:resolve
```

**Primeira execução:** Pode demorar! Maven baixa centenas de MB.

---

### Passo 7: Compilar o Projeto

```powershell
mvn clean install
```

**O que acontece:**

1. `clean`: Remove compilações antigas
2. `compile`: Compila o código
3. `test`: Executa testes
4. `package`: Cria o arquivo `.jar`
5. `install`: Instala no repositório local

**Saída esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time:  XX s
```

---

### Passo 8: Executar a Aplicação

#### Opção A: Com Maven (Desenvolvimento)

```powershell
mvn spring-boot:run
```

#### Opção B: Executando o JAR

```powershell
java -jar target/back-0.0.1-SNAPSHOT.jar
```

#### Opção C: Via IDE (IntelliJ IDEA)

1. Abra o projeto no IntelliJ
2. Localize a classe principal (anotada com `@SpringBootApplication`)
3. Clique com botão direito → **Run**

---

### Passo 9: Verificar se Está Rodando

**Aguarde até ver:**

```
Started BackApplication in X seconds
```

**Testar:**

- http://localhost:8080/actuator/health
- http://localhost:8080/swagger-ui.html

```powershell
curl http://localhost:8080/actuator/health
```

---

### Passo 10: Verificar o Banco de Dados

```powershell
psql -U postgres -d apa_db

# Dentro do psql:
\dt                                  # Listar tabelas
SELECT * FROM flyway_schema_history; # Ver migrações
```

---

## 🐛 Troubleshooting

### Problema: API não inicia no Docker

**Solução:**

```powershell
# Ver logs detalhados
docker-compose logs api

# Reiniciar apenas a API
docker-compose restart api

# Se persistir, reconstruir do zero
docker-compose down -v
docker-compose up -d --build
```

---

### Problema: Porta 8080 já está em uso

**Solução 1: Matar o processo**

```powershell
# Verificar qual processo está usando
netstat -ano | findstr :8080

# Matar o processo (substitua <PID>)
taskkill /PID <PID> /F
```

**Solução 2: Alterar a porta**

No `docker-compose.yml`:

```yaml
api:
  ports:
    - "8081:8080"  # Muda porta externa para 8081
```

Ou no `application.properties`:

```properties
server.port=8081
```

---

### Problema: Erro de conexão com PostgreSQL

**Verificações:**

```powershell
# Verificar se o container está rodando
docker-compose ps

# Ver logs do PostgreSQL
docker-compose logs postgres

# Testar conexão manual
docker exec -it apa_postgres psql -U postgres -d apa_db
```

**Se usar instalação local:**

- Verifique se o PostgreSQL está rodando
- Confirme usuário e senha
- Verifique se o banco `apa_db` existe

---

### Problema: Erro de conexão com Redis

**Docker:**

```powershell
# Verificar se Redis está rodando
docker ps | findstr redis

# Testar conexão
docker exec -it redis redis-cli ping
# Deve retornar: PONG
```

**Local:**

```powershell
redis-cli ping
```

---

### Problema: Erro "Docker daemon not running"

**Solução:**

1. Abra o **Docker Desktop**
2. Aguarde o ícone da baleia ficar verde
3. Tente novamente

---

### Problema: Erro nas migrações Flyway

**Solução 1: Resetar o banco**

```powershell
# Com Docker
docker-compose down -v
docker-compose up -d

# Local
psql -U postgres -c "DROP DATABASE apa_db;"
psql -U postgres -c "CREATE DATABASE apa_db;"
```

**Solução 2: Forçar baseline**

Em `application.properties`:

```properties
spring.flyway.baseline-on-migrate=true
```

---

### Problema: Erro de compilação Maven

**Verificações:**

```powershell
# Verificar versão do Java
java -version  # Deve ser 17 ou superior

# Limpar cache do Maven
mvn clean

# Forçar atualização de dependências
mvn clean install -U

# Se tudo falhar, limpar repositório local
rm -r ~/.m2/repository
mvn clean install
```

---

### Limpar Tudo e Começar do Zero

**Com Docker:**

```powershell
# Parar e remover tudo
docker-compose down -v

# Remover imagens
docker image prune -a

# Reconstruir
docker-compose up -d --build
```

**Local:**

```powershell
# Limpar projeto
mvn clean

# Apagar banco
psql -U postgres -c "DROP DATABASE apa_db;"
psql -U postgres -c "CREATE DATABASE apa_db;"

# Recompilar
mvn clean install
mvn spring-boot:run
```

---

## 📚 Comandos Maven Úteis

```powershell
# Ver todas as dependências (árvore)
mvn dependency:tree

# Baixar dependências sem compilar
mvn dependency:resolve

# Compilar sem executar testes
mvn clean package -DskipTests

# Executar apenas testes
mvn test

# Executar um teste específico
mvn test -Dtest=NomeDaClasseTest

# Ver informações do projeto
mvn help:effective-pom

# Atualizar dependências
mvn versions:display-dependency-updates
```

---

## 🌐 Portas Utilizadas

| Serviço | Porta | URL |
|---------|-------|-----|
| API Spring Boot | 8080 | http://localhost:8080 |
| Swagger UI | 8080 | http://localhost:8080/swagger-ui.html |
| PostgreSQL | 5432 | jdbc:postgresql://localhost:5432/apa_db |
| Redis | 6379 | redis://localhost:6379 |
| MailHog SMTP | 1025 | smtp://localhost:1025 |
| MailHog UI | 8025 | http://localhost:8025 |

---

## 📝 Próximos Passos

Após instalar e executar o projeto com sucesso:

1. ✅ Explore a **Swagger UI**: http://localhost:8080/swagger-ui.html
2. ✅ Teste os endpoints da API
3. ✅ Consulte o **README.md** para entender a arquitetura
4. ✅ Leia a documentação adicional em `RESUMO_IMPLEMENTACAO.md`

---

## 🆘 Precisa de Ajuda?

Se encontrar problemas:

1. Verifique a seção **Troubleshooting** acima
2. Consulte os logs: `docker-compose logs -f api`
3. Verifique se todas as portas estão livres
4. Confirme que o Docker Desktop está rodando

**Boa sorte! 🚀**

