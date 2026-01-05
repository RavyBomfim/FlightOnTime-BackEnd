# Changelog - FlightOnTime BackEnd

Histórico de melhorias e implementações do projeto.

---

## [2026-01-05] - Melhorias de Validação e Cálculo Automático

### ✨ Adicionado

#### 1. Validação de Aeroportos e Companhias Aéreas

- **Validação de Existência:** Agora o sistema verifica se os códigos de aeroportos (origem e destino) e companhia aérea existem no banco de dados **antes** de enviar para a API Python
- **Localização:** Implementado em `PredictionService.predict()`
- **Benefícios:**
  - Detecta erros mais cedo no fluxo
  - Evita chamadas desnecessárias à API de predição
  - Retorna mensagens de erro específicas ao usuário

**Mensagens de erro implementadas:**

- "Aeroporto de origem não encontrado: {code}"
- "Aeroporto de destino não encontrado: {code}"
- "Companhia aérea inválida: {code}"

#### 2. Cálculo Automático de Distância

- **Remoção do campo `distancia_km` do Request:** O usuário não precisa mais informar a distância
- **Cálculo usando Haversine:** Implementado método `calculateDistanceKm()` que usa a fórmula de Haversine para calcular a distância geodésica entre dois aeroportos
- **Precisão:** Baseado nas coordenadas geográficas (latitude/longitude) dos aeroportos
- **Localização:** `PredictionService.calculateDistanceKm()`

**Fórmula de Haversine:**

```java
private double calculateDistanceKm(Airport origin, Airport destination) {
    final int EARTH_RADIUS_KM = 6371;

    double latDistance = Math.toRadians(destination.getAirportLatitude() - origin.getAirportLatitude());
    double lonDistance = Math.toRadians(destination.getAirportLongitude() - origin.getAirportLongitude());

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(origin.getAirportLatitude()))
            * Math.cos(Math.toRadians(destination.getAirportLatitude()))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_KM * c;
}
```

#### 3. Simplificação da API

**Request ANTES:**

```json
{
  "companhia": "GOL",
  "origem": "SBGR",
  "destino": "SBBR",
  "data_partida": "2025-12-15T14:30:00",
  "distancia_km": 850
}
```

**Request AGORA:**

```json
{
  "companhia": "GOL",
  "origem": "SBGR",
  "destino": "SBBR",
  "data_partida": "2025-12-15T14:30:00"
}
```

### 🔧 Modificado

#### 1. Remoção de Validação Redundante

- Removida verificação duplicada de aeroporto no `WeatherService`
- A validação agora acontece apenas uma vez no `PredictionService`
- Princípio: validar no ponto de entrada do fluxo

**Antes:** Validava em PredictionService → WeatherService (redundante)
**Depois:** Valida apenas em PredictionService

#### 2. Fluxo de Predição Otimizado

**Novo fluxo:**

1. Recebe requisição
2. **Valida companhia aérea** (existência no banco)
3. **Valida aeroporto de origem** (existência no banco)
4. **Valida aeroporto de destino** (existência no banco)
5. **Calcula distância automaticamente** (Haversine)
6. Envia para API Python
7. Busca dados meteorológicos
8. Salva no banco
9. Retorna resposta

### 📝 Documentação Atualizada

#### Arquivos atualizados:

- ✅ `README.md` - Exemplos de request sem distancia_km
- ✅ `README.md` - Novos códigos ICAO (4 caracteres) para aeroportos
- ✅ `README.md` - Documentação de validações
- ✅ `README.md` - Exemplos de erro atualizados
- ✅ `implementacoes.md` - Status das funcionalidades
- ✅ `CHANGELOG.md` - Criado este arquivo

### 🐛 Correções

#### 1. Correção de Tipos

- Ajustado cast de `double` para `int` na distância calculada
- Implementação correta: `(int) Math.round(distanceKmDouble)`
- Garante compatibilidade com `PredictionRequest` e `Flight.distanceKm`

#### 2. Logs Melhorados

- Adicionado log de distância calculada
- Adicionado log de validação de aeroportos
- Melhor rastreabilidade do fluxo de execução

### 🎯 Impacto das Mudanças

#### Para o Usuário da API:

✅ **Mais simples:** Não precisa calcular ou informar distância
✅ **Mais seguro:** Valida se aeroportos/companhias existem antes de processar
✅ **Mais rápido:** Detecta erros imediatamente
✅ **Mais preciso:** Distância calculada com precisão geodésica

#### Para o Desenvolvedor:

✅ **Menos código duplicado:** Validação centralizada
✅ **Melhor separação de responsabilidades:** Cada serviço tem uma função clara
✅ **Mais manutenível:** Lógica de cálculo isolada em método privado
✅ **Mais testável:** Métodos bem definidos e coesos

---

## [Anteriormente] - Funcionalidades Base

### Implementado

- ✅ Endpoint POST /api/flights/predict
- ✅ Integração com API Python via RestClient
- ✅ Integração com OpenMeteo para dados meteorológicos
- ✅ Endpoint GET /api/flights/stats (estatísticas agregadas)
- ✅ Endpoints de consulta (por origem, destino, rota, status)
- ✅ Persistência com JPA/Hibernate e H2
- ✅ Documentação com OpenAPI/Swagger
- ✅ Containerização com Docker
- ✅ Cache de estatísticas para performance
- ✅ CORS configurado
- ✅ Tratamento de erros global

---

## Próximas Melhorias Sugeridas

### 🔜 Curto Prazo

- [ ] Adicionar testes unitários e de integração
- [ ] Implementar batch prediction (CSV)
- [ ] Adicionar Postman Collection exportada
- [ ] Criar vídeo/GIF demonstrativo

### 🎯 Médio Prazo

- [ ] Implementar explicabilidade (SHAP/LIME)
- [ ] Dashboard visual com gráficos
- [ ] Suporte a PostgreSQL para produção
- [ ] Health checks completos

### 🚀 Longo Prazo

- [ ] Sistema de notificações
- [ ] API de comparação entre companhias
- [ ] Análise de tendências temporais
- [ ] ML model retraining automation
