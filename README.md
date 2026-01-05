# Flight on Time - Backend API

API REST desenvolvida em Spring Boot para predição de atrasos de voos. Este projeto faz parte do hackaton da Oracle Next Education (ONE) e fornece endpoints para análise preditiva de pontualidade de voos.

## 🚀 Tecnologias Utilizadas

- **Java 25**
- **Spring Boot 4.0.0**
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate
- **Spring Validation** - Validação de dados
- **Spring DevTools** - Hot reload durante desenvolvimento
- **Spring Actuator** - Monitoramento da aplicação

## 📋 Pré-requisitos

- Java 25 ou superior
- Maven 3.6+
- **API Python de Predição** rodando na porta 8000

## ⚠️ IMPORTANTE: Configurar API Python

Este backend depende da API Python de Machine Learning para funcionar. Antes de iniciar o backend, você **DEVE** configurar e rodar a API Python:

### 1. Clone o repositório da API Python

```bash
git clone https://github.com/RavyBomfim/FlightOnTime-DataScience.git
cd FlightOnTime-DataScience/API
```

### 2. Siga as instruções do README do projeto Python

Acesse o README do projeto Python e siga as instruções para:

- Instalar as dependências Python
- Configurar o ambiente
- Iniciar o servidor na porta 8000

**Link do projeto:** https://github.com/RavyBomfim/FlightOnTime-DataScience/tree/main/API

### 3. Verifique se a API Python está rodando

```bash
# Teste se a API Python está respondendo
curl http://localhost:8000
```

Somente após a API Python estar rodando, prossiga com a instalação do backend Java abaixo.

## 🔧 Instalação e Execução

#### 1. Certifique-se que a API Python está rodando

```bash
# Navegue até o diretório da API Python
cd FlightOnTime-DataScience/API

# Siga o README do projeto Python para iniciar o servidor
# A API deve estar rodando em http://localhost:8000
```

#### 2. Clone o repositório do Backend

```bash
git clone https://github.com/RavyBomfim/FlightOnTime-BackEnd.git
cd FlightOnTime-BackEnd
```

#### 3. Compile o projeto

**Windows (PowerShell):**

```powershell
.\mvnw.cmd clean install
```

**Linux/Mac:**

```bash
./mvnw clean install
```

#### 4. Execute a aplicação

**Windows (PowerShell):**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

**Nota:** Certifique-se que a API Python está rodando em `http://localhost:8000` antes de iniciar o backend.

## 📡 Endpoints da API

### POST /api/flights/predict

Realiza a predição de atraso de um voo com base nos dados fornecidos.

### GET /api/flights

Retorna a lista de todos os voos cadastrados no banco de dados, ordenados do mais recente para o mais antigo.

**URL:** `http://localhost:8080/api/flights`

**Method:** `GET`

**Response:** `200 OK`

```json
[
  {
    "id": 10,
    "airline": "Azul",
    "origin": "SSA",
    "destination": "GRU",
    "distanceKm": 1960,
    "scheduledDeparture": "2025-12-26T06:00:00",
    "scheduledArrival": "2025-12-26T08:40:00",
    "predictionResult": "Pontual",
    "predictionProbability": 0.81,
    "createdAt": "2025-12-22T16:22:42.876429",
    "updatedAt": "2025-12-22T16:22:42.876429"
  }
]
```

### GET /api/flights/{id}

Busca um voo específico pelo ID.

**URL:** `http://localhost:8080/api/flights/1`

**Method:** `GET`

**Response:** `200 OK`

### GET /api/flights/search/origin?origin={code}

Busca voos por aeroporto de origem (ex: GRU, CGH, SDU).

**URL:** `http://localhost:8080/api/flights/search/origin?origin=GRU`

**Method:** `GET`

### GET /api/flights/search/destination?destination={code}

Busca voos por aeroporto de destino.

**URL:** `http://localhost:8080/api/flights/search/destination?destination=BSB`

**Method:** `GET`

### GET /api/flights/search/ontime

Retorna todos os voos com predição de pontualidade (ontime).

**URL:** `http://localhost:8080/api/flights/search/ontime`

**Method:** `GET`

### GET /api/flights/search/route?origin={code}&destination={code}

Busca voos por rota específica (origem e destino).

**URL:** `http://localhost:8080/api/flights/search/route?origin=GRU&destination=CGH`

**Method:** `GET`

### GET /api/flights/search/delayed

Retorna todos os voos com predição de atraso.

**URL:** `http://localhost:8080/api/flights/search/delayed`

**Method:** `GET`

### DELETE /api/flights/{id}

Remove um voo do banco de dados.

**URL:** `http://localhost:8080/api/flights/1`

**Method:** `DELETE`

**Response:** `204 No Content`

---

### POST /api/flights/predict - Detalhes

Realiza a predição de atraso de um voo com base nos dados fornecidos.

#### Request

**URL:** `http://localhost:8080/api/flights/predict`

**Method:** `POST`

**Content-Type:** `application/json`

**Body:**

```json
{
  "companhia": "GOL",
  "origem": "SBGR",
  "destino": "SBBR",
  "data_partida": "2025-12-15T14:30:00"
}
```

#### Parâmetros

| Campo          | Tipo     | Obrigatório | Descrição                                              |
| -------------- | -------- | ----------- | ------------------------------------------------------ |
| `companhia`    | String   | Sim         | Código da companhia aérea (3 caracteres). Ex: GOL, TAM, AZU |
| `origem`       | String   | Sim         | Código ICAO do aeroporto de origem (4 caracteres). Ex: SBGR, SBBR |
| `destino`      | String   | Sim         | Código ICAO do aeroporto de destino (4 caracteres). Ex: SBSP, SBGL |
| `data_partida` | DateTime | Sim         | Data e hora de partida (formato ISO 8601)              |

**Nota:** A distância entre aeroportos é calculada automaticamente usando a fórmula de Haversine com base nas coordenadas geográficas.

#### Response

**Status:** `200 OK`

**Body:**

```json
{
  "predict": {
    "previsao": true,
    "probabilidade": 0.78
  },
  "weather": {
    "temperatura": "25.5°C",
    "precipitacao": "0.0mm",
    "vento": "12.5 km/h"
  }
}
```

**Campos de resposta:**

| Campo                      | Tipo    | Descrição                                       |
| -------------------------- | ------- | ----------------------------------------------- |
| `predict.previsao`         | Boolean | true = Atrasado, false = Pontual                |
| `predict.probabilidade`    | Double  | Probabilidade de atraso (0.0 a 1.0)             |
| `weather.temperatura`      | String  | Temperatura no aeroporto de origem              |
| `weather.precipitacao`     | String  | Precipitação no aeroporto de origem             |
| `weather.vento`            | String  | Velocidade do vento no aeroporto de origem      |

**Validações realiGOL",
    "origem": "SBGL",
    "destino": "SBGR",
    "data_partida": "2025-12-20T18:00:00"
  }'
```

**Voo com baixa probabilidade de atraso:**

```bash
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-Type: application/json" \
  -d '{
    "companhia": "AZU",
    "origem": "SBGR",
    "destino": "SBSP",
    "data_partida": "2025-12-18T08:30:00"
    "destino": "MIA",
    "data_partida": "2025-12-20T10:00:00",
    "distancia_km": 6500
  }'
```

**Voo com baixa probabilidade de atraso:**

```bash
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-SBGR"
    destino = "SBBR"
    data_partida = "2025-12-25T16:45:00"
    "destino": "CGH",
    "data_partida": "2025-12-18T08:30:00",
    "distancia_km": 15
  }'
```

### Usando PowerShell

```powershell
$body = @{
    companhia = "GOL"
    origem = "GRU"
    destino = "BSB"
    data_partida = "2025-12-25T16:45:00"
    distancia_km = 900
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/flights/predict" `
  -Method Post `
  -ContentType "application/json" `
  -Body $body
```

### Usando JavaScript (Fetch API)

```javascript
fetch("http://localhost:8080/api/flights/predict", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    companhia: "Azul",
    origem: "GRU",
    destino: "REC",
    data_partida:ZU",
    origem: "SBGR",
    destino: "SBRF",
    data_partida: "2025-12-30T11:20:00"esponse.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Erro:", error));
```

### Usando Python (requests)

```python
import requests
import json

url = "http://localhost:8080/api/flights/predict"
headers = {"Content-Type": "application/json"}
data = {
    "companhia": "LATAM",
    "origem": "GRU",
    "destino": "FOR",
    "data_partida": "2025-12-22T13:15:00",
    "distancia_km"TAM",
    "origem": "SBGR",
    "destino": "SBFZ",
    "data_partida": "2025-12-22T13:15:00"
```

## ⚠️ Validações e Erros

### Erros de Validação

A API valida todos os campos de entrada. Em caso de erro, retorna:

**Status:** `400 Bad Request`

**Exemplo de erro:**

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Invalid request content.",
  "instance": "/api/flights/predict",
  "errors": {
    "companhia": "O nome da companhia aérea deve ter 3 caracteres",
    "origem": "O código do aeroporto de origem deve ter 4 caracteres"
  }
}
```

### Possíveis Mensagens de Validação

**Validações de formato:**
- **companhia:** "A companhia aérea é obrigatória" ou "O nome da companhia aérea deve ter 3 caracteres"
- **origem:** "O aeroporto de origem é obrigatório" ou "O código do aeroporto de origem deve ter 4 caracteres"
- **destino:** "O aeroporto de destino é obrigatório" ou "O código do aeroporto de destino deve ter 4 caracteres"
- **data_partida:** "A data de partida é obrigatória"

**Validações de existência (Runtime):**
- **Companhia aérea inválida:** "Companhia aérea inválida: XXX" (quando o código não existe no banco)
- **Aeroporto de origem inválido:** "Aeroporto de origem não encontrado: XXXX" (quando o código não existe no banco)
- **Aeroporto de destino inválido:** "Aeroporto de destino não encontrado: XXXX" (quando o código não existe no banco)

## 🔍 Como Funciona

### Arquitetura

O projeto segue uma arquitetura em camadas:

```
Controller (FlightController)
    ↓
Service (PredictionService)
    ↓
Integration (PredictionClient / WeatherService)
    ↓
Repository (FlightRepository / AirportRepository / AirlineRepository)
    ↓
DTOs (FlightRequestDTO / FlightResponseDTO / PredictionDTO / WeatherDTO)
```

### Fluxo de Predição

1. **Validação de Entrada:** Valida formato dos códigos (3 caracteres para companhia, 4 para aeroportos)
2. **Validação de Existência:** Verifica se companhia aérea e aeroportos existem no banco de dados
3. **Cálculo de Distância:** Usa fórmula de Haversine para calcular distância entre aeroportos
4. **Chamada à API Python:** Envia dados para o modelo de Machine Learning
5. **Busca de Dados Meteorológicos:** Obtém condições climáticas do aeroporto de origem
6. **Persistência:** Salva a predição no banco de dados
7. **Resposta:** Retorna predição e dados meteorológicos ao cliente

### Integração com Machine Learning

A aplicação integra com uma API Python (FastAPI) que executa o modelo de Machine Learning treinado:
- Utiliza RestClient para comunicação HTTP
- Envia: companhia, origem, destino, data, dia da semana e distância
- Recebe: predição (boolean) e probabilidade (double)

### CORS

A aplicação está configurada para aceitar requisições das seguintes origens:

- `http://localhost:3000` (React - Create React App)
- `http://localhost:5173` (Vite)

Métodos permitidos: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`

## 🛠️ Configuração

### application.properties

```properties
spring.application.name=Flight on Time
server.port=8080
server.error.include-message=always
server.error.include-binding-errors=always
spring.mvc.problemdetails.enabled=true

# Python API Configuration
python.api.url=http://localhost:8000
python.api.timeout=30
```

### Variáveis de Ambiente (Docker)

Ao executar com Docker, você pode configurar as seguintes variáveis:

| Variável             | Padrão                  | Descrição                               |
| -------------------- | ----------------------- | --------------------------------------- |
| `PYTHON_API_URL`     | `http://localhost:8000` | URL da API Python de predição           |
| `PYTHON_API_TIMEOUT` | `30`                    | Timeout em segundos para chamadas à API |
| `JAVA_OPTS`          | `-Xmx512m -Xms256m`     | Opções da JVM (memória, GC, etc.)       |

**Exemplo de uso:**

```bash
docker run -d \
  -p 8080:8080 \
  -e PYTHON_API_URL=http://python-api:8000 \
  -e PYTHON_API_TIMEOUT=60 \
  -e JAVA_OPTS="-Xmx1g -Xms512m" \
  flightontime-backend
```

### Alterar a Porta

Para executar em outra porta, modifique o arquivo `src/main/resources/application.properties`:

```properties
server.port=9090
```

## 📊 Monitoramento

O Spring Actuator está habilitado. Endpoints de monitoramento disponíveis:

- **Health Check:** `http://localhost:8080/actuator/health`
- **Info:** `http://localhost:8080/actuator/info`

## 🏗️ Estrutura do Projeto

```
src/main/java/com/flightontime/api/
├── FlightOnTimeApplication.java    # Classe principal
├── config/
│   ├── CorsConfig.java             # Configuração de CORS
│   └── RestClientConfig.java       # Configuração do RestClient
├── controller/
│   └── FlightController.java       # Controlador REST
├── dto/
│   ├── FlightRequestDTO.java       # DTO de requisição
│   └── FlightResponseDTO.java      # DTO de resposta
└── service/
    └── PredictionService.java      # Lógica de predição e integração com Python
```

### Health Check

O container inclui health check automático que verifica o endpoint `/actuator/health` a cada 30 segundos.

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFuncionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/NovaFuncionalidade`)
5. Abra um Pull Request

## 📝 Licença

Este projeto foi desenvolvido para o hackaton da Oracle Next Education (ONE).
