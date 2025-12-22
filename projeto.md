Setor de negócio

Aviação Civil / Logística / Transporte Aéreo

Empresas aéreas, aeroportos e passageiros que dependem da pontualidade dos voos.

Descrição do projeto

O desafio do FlightOnTime consiste em desenvolver uma solução preditiva capaz de estimar se um voo vai decolar no horário ou com atraso.

O time de Data Science criará um modelo que aprende padrões a partir de dados históricos de voos (companhia aérea, aeroporto, horário, dia da semana, etc.), e o time de Back-End construirá uma API que disponibiliza essa previsão em tempo real, permitindo que outros sistemas consultem facilmente se um voo tem risco de atraso.

Necessidade do cliente (explicação não técnica)

Todos que viajam de avião — e especialmente as companhias aéreas e aeroportos — sofrem com atrasos.

Esses atrasos causam insatisfação nos passageiros, custos extras para as empresas e problemas de logística (como conexões perdidas e remanejamentos de voos).

O cliente quer prever, com base em dados do voo (origem, destino, horário, companhia aérea, etc.), qual é a probabilidade de o voo atrasar para se preparar com antecedência:

Passageiros podem receber alertas antes de sair de casa.

Companhias aéreas podem ajustar a operação e minimizar o impacto.

Aeroportos podem planejar melhor o uso da infraestrutura.

Validação de mercado

Prever atrasos é uma aplicação real e valiosa de ciência de dados em transporte.

Companhias aéreas e startups do setor usam modelos preditivos semelhantes para:

melhorar a pontualidade e o planejamento de frota;

reduzir custos operacionais e reclamações;

aumentar a satisfação do cliente com informações mais transparentes.

Mesmo um modelo simples pode ser útil, pois ajuda a identificar horários ou aeroportos com maior risco de atraso — um diferencial para o setor aéreo.

Expectativa para este hackathon

Público: alunos iniciantes em tecnologia, sem experiência profissional na área, que já estudaram Back-end (Java, Spring, APIs REST, persistência) e Data Science (Python, Pandas, scikit-learn, modelagem supervisionada).

Objetivo: criar um MVP (produto mínimo viável) que recebe informações de um voo e retorna se ele provavelmente será Pontual ou Atrasado.

Escopo sugerido: classificação binária (0 = Pontual, 1 = Atrasado) usando um dataset simples e limpo.

Entregáveis desejados

Notebook (Jupyter/Colab) do time de Data Science, contendo:

Exploração e limpeza de dados (EDA);

Criação de variáveis relevantes (ex.: hora do voo, dia da semana, aeroporto de origem/destino, companhia aérea);

Treinamento de um modelo preditivo (ex.: Logistic Regression, Random Forest);

Avaliação do desempenho (Acurácia, Precisão, Recall, F1-score);

Exportação do modelo serializado (joblib/pickle).

Aplicação Back-End (API REST) desenvolvida em Java (Spring Boot), contendo:

Endpoint /predict que recebe informações de um voo e retorna a previsão;

Integração com o modelo de DS (direta ou via microserviço separado);

Tratamento de erros e respostas padronizadas em JSON.

Documentação mínima (README):

Como executar o projeto;

Dependências e versões das ferramentas;

Exemplos de requisição e resposta;

Dataset utilizado (com link ou descrição).

Demonstração funcional:

Mostrar a API em ação (via Postman, cURL ou uma interface simples).

Explicar brevemente o processo (dados → modelo → previsão).

Funcionalidades exigidas (MVP)

O serviço deve expor um endpoint que retorna o status do voo a qual o texto pertence e a probabilidade associada a esse status:

Endpoint principal: POST /predict

Entrada (JSON):

{

"companhia": "AZ",

"origem": "GIG",

"destino": "GRU",

"data_partida": "2025-11-10T14:30:00",

"distancia_km": 350

}

Saída (JSON):

{

"previsao": "Atrasado",

"probabilidade": 0.78

}

Carregamento do modelo preditivo: o back-end deve conseguir usar o modelo (carregado localmente ou via microserviço DS).

Validação de entrada: garantir que todos os campos necessários estejam presentes e corretos.

Resposta clara: previsão + probabilidade em formato decimal (0 a 1).

Exemplos de uso: Postman/cURL com 3 exemplos reais (um pontual, um atrasado, um com erro).

README funcional: com passos simples para rodar localmente.

Funcionalidades opcionais

Endpoint GET /stats: retorna estatísticas agregadas (ex.: % de voos atrasados no dia).

Persistência: salvar histórico de previsões e requisições em um banco (H2/PostgreSQL).

Dashboard visual (Streamlit/HTML): mostra, em tempo real, a taxa de atrasos prevista.

Integração com API externa de clima: adicionar condições meteorológicas como feature do modelo.

Batch prediction: aceitar um arquivo CSV com vários voos e devolver as previsões em lote.

Explicabilidade: retornar as variáveis mais importantes na decisão (ex.: “Horário da tarde e aeroporto GIG aumentam o risco”).

Containerização: rodar o sistema completo com Docker/Docker Compose.

Testes automatizados: unitários e de integração simples.

Orientações técnicas para alunos

Recomenda-se deixar o modelo leve e o escopo controlado, para não ultrapassar os limites de always free do OCI.

Time de Data Science

Monte ou escolha um dataset próprio com informações de voos (ex.: companhia aérea, origem, destino, horário, distância);

Utilize Python, Pandas e scikit-learn para análise e modelagem;

Criar features a partir dos dados (ex.: hora, dia da semana, tipo de companhia aérea);

Escolher modelo de classificação simples (LogisticRegression ou RandomForestClassifier);

Validar o modelo com dados separados (treino/teste);

Salvar o modelo (joblib.dump) e garantir que ele possa ser carregado externamente.

Time de Back-End

Construir uma API REST (em Java com Spring Boot).

Implementar um endpoint (ex: /predict ) que recebe o JSON de um voo e retorna a previsão.

Integrar o modelo de Data Science:

via microserviço Python (FastAPI/Flask), ou

carregando o modelo exportado (ONNX, para times Java avançados).

Validar entradas e retornar respostas JSON consistentes.

Contrato de integração (definido entre DS e BE)

Recomendamos definir o contrato de integração logo no início do hackathon. Segue um exemplo:

Entrada padrão:

{

"companhia": "AZ",

"origem": "GIG",

"destino": "GRU",

"data_partida": "2025-11-10T14:30:00",

"distancia_km": 350

}

Saída padrão:

{

"previsao": "Pontual",

"probabilidade": 0.22

}