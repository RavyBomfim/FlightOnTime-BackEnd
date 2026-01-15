# 🐳 Docker - FlightOnTime Backend

Guia completo para executar o projeto usando Docker.

---

## 📋 Pré-requisitos

- **Docker** 20.10+ instalado
- **Docker Compose** 2.0+ instalado
- **API Python** rodando (ou configurada no Docker)

### Verificar Instalação

```bash
docker --version
docker-compose --version
```

---

## 🚀 Opções de Execução

### Opção 1: Docker Compose (Desenvolvimento) - RECOMENDADO

Inclui backend + PostgreSQL local + pgAdmin

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/FlightOnTime-BackEnd.git
cd FlightOnTime-BackEnd

# 2. Configure variáveis de ambiente
cp .env.docker .env
# Edite .env com suas configurações

# 3. Suba todos os serviços
docker-compose up -d

# 4. Veja os logs
docker-compose logs -f backend
```

**Serviços disponíveis:**
- Backend: http://localhost:8080
- PostgreSQL: localhost:5432
- pgAdmin: http://localhost:5050 (admin@flightontime.com / admin123)
- Swagger: http://localhost:8080/swagger-ui.html

### Opção 2: Docker Compose (Produção)

Apenas backend, conectando ao Neon PostgreSQL

```bash
# 1. Configure .env com credenciais Neon
cat > .env << EOF
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://seu-host.neon.tech:5432/neondb?sslmode=require
DATABASE_USERNAME=seu-usuario
DATABASE_PASSWORD=sua-senha
JWT_SECRET=sua-chave-secreta-32-caracteres
GOOGLE_CLIENT_ID=seu-google-client-id
PYTHON_API_URL=http://seu-python-api.com
EOF

# 2. Suba em modo produção
docker-compose -f docker-compose.prod.yml up -d

# 3. Veja status
docker-compose -f docker-compose.prod.yml ps
```

### Opção 3: Docker Build Manual

```bash
# 1. Build da imagem
docker build -t flightontime-backend:latest .

# 2. Execute o container
docker run -d \
  --name flightontime-backend \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e JWT_SECRET=MinhaChaveSecreta32Caracteres! \
  -e PYTHON_API_URL=http://host.docker.internal:8000 \
  flightontime-backend:latest

# 3. Veja logs
docker logs -f flightontime-backend
```

---

## 🔧 Configuração

### Variáveis de Ambiente (.env)

```env
# Profile (dev ou prod)
SPRING_PROFILES_ACTIVE=dev

# Database
DATABASE_URL=jdbc:postgresql://postgres:5432/flightdb
DATABASE_USERNAME=flightuser
DATABASE_PASSWORD=flightpass

# JWT
JWT_SECRET=sua-chave-minimo-32-caracteres

# Google OAuth
GOOGLE_CLIENT_ID=seu-google-client-id

# Python API
PYTHON_API_URL=http://host.docker.internal:8000
```

### Conectar à API Python no Host

O Docker Compose está configurado para acessar `host.docker.internal:8000`, permitindo que o container acesse a API Python rodando na máquina host.

**Windows/Mac:** Funciona automaticamente  
**Linux:** Adicione ao docker-compose.yml:
```yaml
extra_hosts:
  - "host.docker.internal:172.17.0.1"
```

---

## 📊 Gerenciar Serviços

### Comandos Docker Compose

```bash
# Subir serviços
docker-compose up -d

# Ver logs
docker-compose logs -f
docker-compose logs -f backend  # apenas backend

# Parar serviços
docker-compose stop

# Parar e remover containers
docker-compose down

# Parar e remover volumes (CUIDADO: apaga dados)
docker-compose down -v

# Rebuild após mudanças no código
docker-compose up -d --build

# Ver status dos serviços
docker-compose ps

# Executar comando no container
docker-compose exec backend sh

# Ver uso de recursos
docker stats
```

### Acessar PostgreSQL no Docker

```bash
# Via psql no container
docker-compose exec postgres psql -U flightuser -d flightdb

# Via pgAdmin
# Acesse http://localhost:5050
# Login: admin@flightontime.com / admin123
# Adicione servidor:
#   Host: postgres
#   Port: 5432
#   Database: flightdb
#   Username: flightuser
#   Password: flightpass
```

---

## 🗄️ Banco de Dados

### Opção 1: PostgreSQL Local (Docker)

O `docker-compose.yml` já inclui PostgreSQL e carrega automaticamente o script `populate-neon-complete.sql`.

**Dados carregados:**
- 91 aeroportos brasileiros
- 3 companhias aéreas
- 1 usuário teste

### Opção 2: Neon PostgreSQL (Produção)

```bash
# 1. Configure .env
DATABASE_URL=jdbc:postgresql://seu-host.neon.tech:5432/neondb?sslmode=require
DATABASE_USERNAME=seu-usuario
DATABASE_PASSWORD=sua-senha
SPRING_PROFILES_ACTIVE=prod

# 2. Execute script SQL no Neon SQL Editor
# Copie conteúdo de: src/main/resources/db/migration/populate-neon-complete.sql

# 3. Suba aplicação
docker-compose -f docker-compose.prod.yml up -d
```

---

## 🧪 Testes

### Testar Backend

```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI
open http://localhost:8080/swagger-ui.html

# Registrar usuário
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "teste@email.com", "password": "senha123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "teste@email.com", "password": "senha123"}'
```

---

## 🐛 Troubleshooting

### Problema: Container não inicia

```bash
# Ver logs detalhados
docker-compose logs backend

# Verificar se portas estão em uso
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Linux/Mac

# Rebuild do zero
docker-compose down -v
docker-compose up -d --build
```

### Problema: Não conecta ao PostgreSQL

```bash
# Verificar se PostgreSQL está rodando
docker-compose ps postgres

# Testar conexão
docker-compose exec postgres pg_isready -U flightuser

# Ver logs do PostgreSQL
docker-compose logs postgres
```

### Problema: API Python não responde

```bash
# Verificar se API Python está rodando no host
curl http://localhost:8000

# No Windows/Mac, use host.docker.internal
# No Linux, pode precisar do IP do host
```

### Problema: Out of Memory

```bash
# Aumentar memória do Docker Desktop
# Settings > Resources > Memory > 4GB+

# Ou limitar JVM no container
docker-compose exec backend sh -c 'java -Xms256m -Xmx512m -jar app.jar'
```

### Problema: Build lento

```bash
# Usar cache do Maven
docker-compose build --no-cache backend

# Ou baixar dependências separadamente
docker-compose run --rm backend mvn dependency:go-offline
```

---

## 🔐 Segurança

### Produção

**Checklist:**
- [ ] Trocar senhas padrão
- [ ] Usar JWT_SECRET forte (32+ caracteres aleatórios)
- [ ] Configurar CORS para domínios específicos
- [ ] Usar HTTPS (proxy reverso com Nginx/Traefik)
- [ ] Não expor portas desnecessárias
- [ ] Configurar logs e monitoramento
- [ ] Backup automático do banco

### Gerar JWT Secret Seguro

```bash
# Linux/Mac
openssl rand -base64 32

# PowerShell
[Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }))
```

---

## 📈 Monitoramento

### Logs

```bash
# Logs em tempo real
docker-compose logs -f

# Últimas 100 linhas
docker-compose logs --tail=100 backend

# Logs desde timestamp
docker-compose logs --since 2026-01-15T10:00:00 backend
```

### Health Checks

```bash
# Status dos containers
docker-compose ps

# Health check manual
curl http://localhost:8080/actuator/health

# Métricas (se habilitado)
curl http://localhost:8080/actuator/metrics
```

### Uso de Recursos

```bash
# Stats em tempo real
docker stats

# Uso de disco
docker system df

# Limpar recursos não usados
docker system prune -a
```

---

## 🚀 Deploy

### Docker Hub

```bash
# 1. Login
docker login

# 2. Tag da imagem
docker tag flightontime-backend:latest seu-usuario/flightontime-backend:1.0.0

# 3. Push
docker push seu-usuario/flightontime-backend:1.0.0

# 4. Pull em servidor
docker pull seu-usuario/flightontime-backend:1.0.0
```

### AWS ECS / Azure Container Instances

Consulte documentação específica da plataforma para deploy de containers Docker.

---

## 📦 Estrutura de Arquivos Docker

```
FlightOnTime-BackEnd/
├── Dockerfile                    # Imagem da aplicação
├── .dockerignore                 # Arquivos ignorados no build
├── docker-compose.yml            # Desenvolvimento (completo)
├── docker-compose.prod.yml       # Produção (apenas backend)
├── .env.docker                   # Template de variáveis
└── DOCKER.md                     # Este arquivo
```

---

## ⚡ Otimizações

### Multi-stage Build

O `Dockerfile` usa multi-stage build:
- **Stage 1**: Compila com Maven (imagem pesada)
- **Stage 2**: Runtime com JRE apenas (imagem leve)

**Resultado:** Imagem final ~350MB vs ~1GB sem multi-stage

### Cache de Dependências

Copia `pom.xml` primeiro para cachear download de dependências Maven.

### Alpine Linux

Usa imagens Alpine (menor footprint):
- `eclipse-temurin:21-jre-alpine` (runtime)
- `postgres:16-alpine` (banco)

---

## 🎓 Comandos Úteis

```bash
# Ver imagens
docker images

# Ver containers (todos)
docker ps -a

# Remover container
docker rm flightontime-backend

# Remover imagem
docker rmi flightontime-backend:latest

# Limpar tudo (CUIDADO)
docker system prune -a --volumes

# Exportar logs para arquivo
docker-compose logs > logs.txt

# Executar bash no container
docker-compose exec backend sh

# Copiar arquivo do container
docker cp flightontime-backend:/app/logs/app.log ./
```

---

## 📚 Referências

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)

---

**Desenvolvido com ❤️ para o Hackaton Oracle Next Education (ONE)**
