# ✈️ Flight on Time - Backend API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-blue.svg)](https://neon.tech)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

API REST desenvolvida em Spring Boot para **predição inteligente de atrasos de voos** utilizando Machine Learning. Este projeto faz parte do hackaton da Oracle Next Education (ONE) e oferece uma solução completa para análise preditiva de pontualidade de voos com integração a dados meteorológicos em tempo real.

## 🎯 Sobre o Projeto

O FlightOnTime resolve um problema crítico na aviação: **prever atrasos de voos antes que eles aconteçam**. Com base em dados históricos, características do voo e condições meteorológicas, a API fornece predições precisas que ajudam:

- ✈️ **Passageiros**: Planejar melhor suas viagens e conexões
- 🏢 **Companhias Aéreas**: Otimizar operações e reduzir custos
- 🛫 **Aeroportos**: Gerenciar recursos e infraestrutura com eficiência

### 🌟 Diferenciais

- 🤖 **Machine Learning Avançado**: Modelo treinado com dados históricos reais
- 🌤️ **Dados Meteorológicos em Tempo Real**: Integração com OpenMeteo
- 📊 **Estatísticas Completas**: Análise detalhada por companhia, rota, aeroporto e data
- 🔐 **Segurança Robusta**: JWT + Google OAuth + Rate Limiting
- 📈 **Alta Performance**: Cache inteligente e otimizações de queries
- 🗄️ **Neon PostgreSQL**: Banco serverless para produção
- 📚 **Documentação Completa**: Swagger/OpenAPI integrado

---

## 🚀 Tecnologias Utilizadas

### Backend

- **Java 21** - Linguagem de programação
- **Spring Boot 3.2.5** - Framework principal
- **Spring Security + JWT** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **H2 Database** - Banco em memória (desenvolvimento)
- **PostgreSQL (Neon)** - Banco serverless (produção)

### Segurança

- **JWT (JSON Web Token)** - Autenticação stateless
- **Google OAuth 2.0** - Login social
- **BCrypt** - Criptografia de senhas
- **Bucket4j** - Rate limiting (50 req/min)

### Ferramentas

- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate
- **Bean Validation** - Validação de dados
- **RestClient** - Comunicação HTTP
- **SpringDoc OpenAPI** - Documentação interativa
- **JaCoCo** - Cobertura de testes (58%)

### Integrações

- **FastAPI Python** - Modelo de Machine Learning
- **OpenMeteo API** - Dados meteorológicos
- **Google Identity Services** - OAuth 2.0

---

## 📋 Pré-requisitos

- **Java 21** ou superior
- **Maven 3.6+**
- **API Python de Machine Learning** rodando na porta 8000 (obrigatório)

### ⚠️ Configurar API Python de ML

Este backend **depende obrigatoriamente** da API Python que contém o modelo de Machine Learning.

```bash
# Clone o repositório da API Python
git clone https://github.com/RavyBomfim/FlightOnTime-DataScience.git
cd FlightOnTime-DataScience/API

# Siga as instruções para instalar e executar
# A API deve rodar em http://localhost:8000
```

📚 **Repositório:** https://github.com/RavyBomfim/FlightOnTime-DataScience

---

## 🔧 Instalação e Execução

### Opção 1: Docker (RECOMENDADO) 🐳

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/FlightOnTime-BackEnd.git
cd FlightOnTime-BackEnd

# 2. Configure variáveis de ambiente
cp .env.docker .env
# Edite .env se necessário

# 3. Suba todos os serviços (backend + PostgreSQL + pgAdmin)
docker-compose up -d

# 4. Acesse a aplicação
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# pgAdmin: http://localhost:5050
```

📖 **Documentação Docker completa**: Ver [DOCKER.md](DOCKER.md)

### Opção 2: Execução Local (Maven)

#### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/FlightOnTime-BackEnd.git
cd FlightOnTime-BackEnd
```

#### 2. Configure variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
# JWT Configuration
JWT_SECRET=sua-chave-secreta-minimo-32-caracteres

# Database (PostgreSQL/Neon para produção)
DATABASE_URL=jdbc:postgresql://seu-host.neon.tech:5432/neondb?sslmode=require
DATABASE_USERNAME=seu-usuario
DATABASE_PASSWORD=sua-senha

# Google OAuth
GOOGLE_CLIENT_ID=seu-google-client-id

# Python API
PYTHON_API_URL=http://localhost:8000

# Environment
SPRING_PROFILES_ACTIVE=dev
```

#### 3. Compile e execute

**Windows:**
```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

#### 4. Acesse a aplicação

- **API Backend**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console** (dev): http://localhost:8080/h2-console

**Windows:**

```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw clean install
./mvnw spring-boot:run
```

### 4. Acesse a aplicação

- **API Backend**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console** (dev): http://localhost:8080/h2-console

---

## 🔐 Autenticação e Segurança

### Endpoints Públicos (Sem Autenticação)

- `POST /api/auth/register` - Registrar usuário
- `POST /api/auth/login` - Login com email/senha
- `POST /api/auth/google` - Login com Google OAuth
- `GET /swagger-ui.html` - Documentação
- `GET /h2-console/**` - Console H2 (apenas dev)

### Endpoints Protegidos (Requerem JWT)

Todos os endpoints `/api/flights/**` requerem token JWT no header `Authorization: Bearer <token>`.

### Fluxo de Autenticação

#### 1. Registrar Usuário

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email": "usuario@email.com", "password": "senha123"}'
```

#### 2. Login (Email/Senha)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "usuario@email.com", "password": "senha123"}'
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 3. Login com Google OAuth 🆕

**Fluxo:**

1. Frontend implementa Google Sign-In
2. Usuário autentica com conta Google
3. Google retorna ID Token
4. Frontend envia token para backend
5. Backend valida e retorna JWT da aplicação

**Endpoint:**

```bash
curl -X POST http://localhost:8080/api/auth/google \
  -H "Content-Type: application/json" \
  -d '{"token": "ID_TOKEN_DO_GOOGLE"}'
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Exemplo Frontend (JavaScript):**

```html
<script src="https://accounts.google.com/gsi/client" async defer></script>

<script>
  function handleCredentialResponse(response) {
    const googleToken = response.credential;

    fetch("http://localhost:8080/api/auth/google", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ token: googleToken }),
    })
      .then((res) => res.json())
      .then((data) => {
        localStorage.setItem("token", data.token);
        console.log("Login com Google realizado!");
      });
  }

  window.onload = function () {
    google.accounts.id.initialize({
      client_id: "SEU_GOOGLE_CLIENT_ID",
      callback: handleCredentialResponse,
    });

    google.accounts.id.renderButton(document.getElementById("buttonDiv"), {
      theme: "outline",
      size: "large",
    });
  };
</script>

<div id="buttonDiv"></div>
```

**Configurar Google Client ID:**

1. Acesse [Google Cloud Console](https://console.cloud.google.com)
2. Crie um projeto e ative Google+ API
3. Crie credenciais OAuth 2.0
4. Configure origens autorizadas
5. Adicione o Client ID no `.env`

#### 4. Usar o Token

```bash
curl -X GET http://localhost:8080/api/flights \
  -H "Authorization: Bearer SEU_TOKEN"
```

### Recursos de Segurança

- ✅ Senhas criptografadas com BCrypt
- ✅ Rate Limiting: 50 requisições/minuto por IP
- ✅ CORS configurado
- ✅ Validação de entrada com regex
- ✅ Token expira em 1 hora

---

## 📡 Endpoints da API

### 🔮 Predição de Voos

#### POST /api/flights/predict

Realiza predição de atraso usando Machine Learning.

🔒 **Requer autenticação JWT**

**Request:**

```json
{
  "companhia": "G3",
  "origem": "SBGR",
  "destino": "SBBR",
  "data_partida": "2026-01-20T14:30:00"
}
```

**Campos:**

- `companhia` (String, 2-3 chars): Código IATA (G3, LA, AD)
- `origem` (String, 4 chars): Código ICAO origem (SBGR, SBSP)
- `destino` (String, 4 chars): Código ICAO destino
- `data_partida` (DateTime): ISO 8601 format

**Response:**

```json
{
  "predict": {
    "predictionResult": "Atrasado",
    "predictionProbability": 0.78
  },
  "weather": {
    "temperature": "25.5°C",
    "precipitation": "2.0mm",
    "windSpeed": "15.0 km/h"
  }
}
```

**Validações automáticas:**

- ✅ Formato de códigos (regex)
- ✅ Existência de companhia/aeroportos no banco
- ✅ **Cálculo automático de distância** (Haversine)
- ✅ Dados meteorológicos em tempo real

---

### 🗂️ Consulta de Voos

| Método | Endpoint                                                     | Descrição           |
| ------ | ------------------------------------------------------------ | ------------------- |
| GET    | `/api/flights`                                               | Lista todos os voos |
| GET    | `/api/flights/{id}`                                          | Busca voo por ID    |
| GET    | `/api/flights/search/origin?origin={code}`                   | Voos por origem     |
| GET    | `/api/flights/search/destination?destination={code}`         | Voos por destino    |
| GET    | `/api/flights/search/route?origin={code}&destination={code}` | Voos por rota       |
| GET    | `/api/flights/search/ontime`                                 | Voos pontuais       |
| GET    | `/api/flights/search/delayed`                                | Voos atrasados      |
| DELETE | `/api/flights/{id}`                                          | Remove voo          |

🔒 **Todos requerem autenticação JWT**

---

### 📊 Estatísticas

#### GET /api/flights/stats

Retorna estatísticas completas agregadas.

🔒 **Requer autenticação JWT**

**Response:**

```json
{
  "overall": {
    "totalFlights": 150,
    "totalDelayed": 45,
    "totalOnTime": 105,
    "delayRate": 30.0
  },
  "byDate": [...],
  "byAirline": [...],
  "byOrigin": [...],
  "byDestination": [...],
  "byRoute": [...]
}
```

**Análises:**

- 📈 Estatísticas gerais
- 📅 Análise por data
- ✈️ Análise por companhia
- 🛫 Análise por aeroporto origem/destino
- 🛤️ Análise por rota

**Performance:** Cache automático

---

### 🏢 Companhias e Aeroportos

| Método | Endpoint               | Descrição                  |
| ------ | ---------------------- | -------------------------- |
| GET    | `/api/airlines`        | Lista todas as companhias  |
| GET    | `/api/airlines/{code}` | Busca companhia por código |
| GET    | `/api/airports`        | Lista todos os aeroportos  |
| GET    | `/api/airports/{code}` | Busca aeroporto por código |

---

## 🧪 Exemplos Práticos

### JavaScript (Fetch)

```javascript
// Login
const loginResponse = await fetch("http://localhost:8080/api/auth/login", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    email: "usuario@email.com",
    password: "senha123",
  }),
});
const { token } = await loginResponse.json();

// Predição
const response = await fetch("http://localhost:8080/api/flights/predict", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  },
  body: JSON.stringify({
    companhia: "G3",
    origem: "SBGR",
    destino: "SBBR",
    data_partida: "2026-01-20T14:30:00",
  }),
});
const data = await response.json();
console.log(data);
```

### Python

```python
import requests

# Login
response = requests.post(
    "http://localhost:8080/api/auth/login",
    json={"email": "usuario@email.com", "password": "senha123"}
)
token = response.json()["token"]

# Predição
headers = {
    "Content-Type": "application/json",
    "Authorization": f"Bearer {token}"
}
data = {
    "companhia": "G3",
    "origem": "SBGR",
    "destino": "SBBR",
    "data_partida": "2026-01-20T14:30:00"
}
response = requests.post(
    "http://localhost:8080/api/flights/predict",
    headers=headers,
    json=data
)
print(response.json())
```

### cURL

```bash
# Login
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "usuario@email.com", "password": "senha123"}' \
  | jq -r '.token')

# Predição
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "companhia": "G3",
    "origem": "SBGR",
    "destino": "SBBR",
    "data_partida": "2026-01-20T14:30:00"
  }'
```

---

## 🗄️ Banco de Dados

### Ambientes

#### 1. H2 Database (Desenvolvimento)

- **Tipo**: In-memory
- **Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:flightdb`
- **Username**: `sa`
- **Password**: _(vazio)_
- **Perfil**: `dev` (padrão)

```bash
# Rodar com H2
mvn spring-boot:run
```

#### 2. Neon PostgreSQL (Produção) 🆕

- **Tipo**: PostgreSQL Serverless
- **Site**: https://neon.tech
- **Perfil**: `prod`

**Setup Rápido:**

1. **Crie conta gratuita no Neon**: https://neon.tech
2. **Crie um banco de dados**
3. **Configure .env**:
   ```env
   DATABASE_URL=jdbc:postgresql://seu-host.neon.tech:5432/neondb?sslmode=require
   DATABASE_USERNAME=seu-usuario
   DATABASE_PASSWORD=sua-senha
   SPRING_PROFILES_ACTIVE=prod
   ```
4. **Execute script SQL**: Copie e execute `src/main/resources/db/migration/populate-neon-complete.sql` no Neon SQL Editor
5. **Execute aplicação**:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

**Dados Inclusos no Script:**

- 91 aeroportos brasileiros com coordenadas
- 3 companhias aéreas (GOL, LATAM, AZUL)
- 1 usuário admin (teste@example.com / senha: 123456)

### Schema

**Entidades:**

- `airlines` - Companhias aéreas
- `airports` - Aeroportos com coordenadas GPS
- `flights` - Histórico de predições
- `users` - Usuários (email/senha ou Google OAuth)

---

## 🏗️ Arquitetura

### Camadas

```
┌─────────────────────────────────────┐
│     PRESENTATION LAYER              │
│  Controllers + DTOs + Validation    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      BUSINESS LOGIC LAYER           │
│  Services + Business Rules          │
└──────────────┬──────────────────────┘
               │
      ┌────────┴────────┐
      ▼                 ▼
┌─────────────┐   ┌─────────────┐
│ INTEGRATION │   │ REPOSITORY  │
│   LAYER     │   │   LAYER     │
└─────────────┘   └─────────────┘
```

### Fluxo de Predição

1. **Controller** recebe request e valida formato
2. **PredictionService** valida companhia/aeroportos no banco
3. **Cálculo automático de distância** (Haversine)
4. **PredictionClient** chama API Python (ML)
5. **WeatherService** busca dados meteorológicos
6. **FlightRepository** persiste resultado
7. **Controller** retorna resposta ao cliente

### Cálculo de Distância (Haversine)

```java
private double calculateDistanceKm(Airport origin, Airport destination) {
    final int EARTH_RADIUS_KM = 6371;

    double latDistance = Math.toRadians(
        destination.getLatitude() - origin.getLatitude()
    );
    double lonDistance = Math.toRadians(
        destination.getLongitude() - origin.getLongitude()
    );

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(origin.getLatitude()))
            * Math.cos(Math.toRadians(destination.getLatitude()))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_KM * c;
}
```

---

## 🧪 Testes

### Executar Testes

```bash
# Todos os testes
mvn test

# Gerar relatório de cobertura
mvn clean test jacoco:report
```

### Cobertura

- **Total**: 24 testes automatizados
- **Services**: 58% de cobertura
- **Controllers**: 36% de cobertura
- **Security**: 77% de cobertura

**Tipos:**

- ✅ Testes unitários
- ✅ Testes de integração
- ✅ Testes de validação
- ✅ Testes de segurança

---

## ⚠️ Tratamento de Erros

### 400 Bad Request - Validação

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "errors": {
    "companhia": "Código deve conter apenas letras maiúsculas",
    "origem": "Código do aeroporto inválido"
  }
}
```

### 400 Bad Request - Negócio

```json
{
  "message": "Aeroporto de origem não encontrado: XXXX"
}
```

### 401 Unauthorized

```json
{
  "message": "Token inválido ou expirado"
}
```

### 429 Too Many Requests

```json
{
  "message": "Rate limit exceeded. Try again in 1 minute."
}
```

### 503 Service Unavailable

```json
{
  "message": "Serviço de predição indisponível. Verifique se a API Python está rodando."
}
```

---

## 🔧 Configuração Avançada

### Variáveis de Ambiente

```properties
# Application
SERVER_PORT=8080

# JWT
JWT_SECRET=sua_chave_minimo_32_caracteres
JWT_EXPIRATION=3600000

# Database (Produção)
DATABASE_URL=jdbc:postgresql://host:5432/db?sslmode=require
DATABASE_USERNAME=usuario
DATABASE_PASSWORD=senha

# Google OAuth
GOOGLE_CLIENT_ID=seu_client_id

# Python API
PYTHON_API_URL=http://localhost:8000
PYTHON_API_TIMEOUT=30

# Environment
SPRING_PROFILES_ACTIVE=dev
```

---

## 🐛 Troubleshooting

### Problema: API Python não responde

**Solução:**

1. Verifique: `curl http://localhost:8000`
2. Veja logs do serviço Python
3. Confirme porta 8000 está livre

### Problema: Token JWT expirou

**Solução:**

- Tokens expiram em 1 hora
- Faça login novamente

### Problema: Companhia/Aeroporto não encontrado

**Solução:**

- Verifique códigos: 2-3 chars (companhia), 4 chars (aeroporto)
- Use `/api/airlines` e `/api/airports` para ver disponíveis
- Use códigos ICAO (SBGR, não GRU)

### Problema: Rate limit exceeded

**Solução:**

- Aguarde 1 minuto (limite: 50 req/min)

### Problema: Erro de conexão com Neon

**Solução:**

1. Verifique credenciais no `.env`
2. Confirme `?sslmode=require` na URL
3. Teste conexão no Neon SQL Editor

---

## 📚 Documentação Adicional

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Checklist Final**: Ver [CHECKLIST_FINAL.md](CHECKLIST_FINAL.md)

---

## 📊 Histórico de Versões

### v1.0.0 (2026-01-15) - Release Produção

**Implementado:**

- ✅ Sistema completo de predição com ML
- ✅ Autenticação JWT + Google OAuth
- ✅ Estatísticas agregadas com cache
- ✅ Integração meteorológica
- ✅ Cálculo automático de distância
- ✅ Rate limiting e segurança
- ✅ Suporte Neon PostgreSQL (produção)
- ✅ H2 Database (desenvolvimento)
- ✅ 24 testes automatizados
- ✅ Documentação completa

**Melhorias:**

- Validação antecipada (fail-fast)
- Cálculo geodésico de distância
- Cache inteligente de estatísticas
- Queries otimizadas
- Logs estruturados

---

## 👥 Autores

Desenvolvido para o **Hackaton Oracle Next Education (ONE)** pela equipe AlcateIA

---

## 📄 Licença

Este projeto está sob a licença MIT.

---

## 🙏 Agradecimentos

- Oracle Next Education (ONE)
- FastAPI Python para API de ML
- OpenMeteo para dados meteorológicos
- Neon para PostgreSQL serverless
- Spring Boot Community

---

**Desenvolvido por AlcateIA para o Hackaton Oracle Next Education (ONE)**
