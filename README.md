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

#### Request

**URL:** `http://localhost:8080/api/flights/predict`

**Method:** `POST`

**Content-Type:** `application/json`

**Body:**

```json
{
  "companhia": "GOL",
  "origem": "GRU",
  "destino": "JFK",
  "data_partida": "2025-12-15T14:30:00",
  "distancia_km": 7800
}
```

#### Parâmetros

| Campo          | Tipo     | Obrigatório | Descrição                                              |
| -------------- | -------- | ----------- | ------------------------------------------------------ |
| `companhia`    | String   | Sim         | Nome da companhia aérea                                |
| `origem`       | String   | Sim         | Código IATA do aeroporto de origem                     |
| `destino`      | String   | Sim         | Código IATA do aeroporto de destino                    |
| `data_partida` | DateTime | Sim         | Data e hora de partida (formato ISO 8601)              |
| `distancia_km` | Double   | Sim         | Distância do voo em quilômetros (deve ser maior que 0) |

#### Response

**Status:** `200 OK`

**Body:**

```json
{
  "status": "Atraso",
  "probabilidade": 0.57
}
```

**Campos de resposta:**

| Campo           | Tipo   | Descrição                                       |
| --------------- | ------ | ----------------------------------------------- |
| `status`        | String | Status previsto do voo: "Atrasado" ou "Pontual" |
| `probabilidade` | Double | Probabilidade de atraso (0.0 a 1.0)             |

## 🧪 Exemplos de Chamadas

### Usando cURL

**Voo com alta probabilidade de atraso:**

```bash
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-Type: application/json" \
  -d '{
    "companhia": "LATAM",
    "origem": "GRU",
    "destino": "MIA",
    "data_partida": "2025-12-20T10:00:00",
    "distancia_km": 6500
  }'
```

**Voo com baixa probabilidade de atraso:**

```bash
curl -X POST http://localhost:8080/api/flights/predict \
  -H "Content-Type: application/json" \
  -d '{
    "companhia": "Azul",
    "origem": "GRU",
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
    data_partida: "2025-12-30T11:20:00",
    distancia_km: 2130,
  }),
})
  .then((response) => response.json())
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
    "distancia_km": 2520
}

response = requests.post(url, headers=headers, data=json.dumps(data))
print(response.json())
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
    "companhia": "A companhia aérea é obrigatória",
    "distancia_km": "A distância deve ser maior que 0"
  }
}
```

### Possíveis Mensagens de Validação

- **companhia:** "A companhia aérea é obrigatória"
- **origem:** "O aeroporto de origem é obrigatório"
- **destino:** "O aeroporto de destino é obrigatório"
- **data_partida:** "A data de partida é obrigatória"
- **distancia_km:** "A distância é obrigatória" ou "A distância deve ser maior que 0"

## 🔍 Como Funciona

### Arquitetura

O projeto segue uma arquitetura em camadas:

```
Controller (FlightController)
    ↓
Service (PredictionService)
    ↓
DTOs (FlightRequestDTO / FlightResponseDTO)
```

### Lógica de Predição (Versão Atual - MOCK)

**⚠️ Importante:** A versão atual utiliza uma lógica simplificada para demonstração:

- **Voos com distância > 1000 km:** Classificados como "Atrasado" com probabilidade de 85%
- **Voos com distância ≤ 1000 km:** Classificados como "Pontual" com probabilidade de 15%

**Próximos Passos:** A implementação final incluirá integração com um modelo de Machine Learning em Python para predições mais precisas.

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