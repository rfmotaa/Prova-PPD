### I. Introdução

O **Problema do Jantar dos Filósofos**  envolve N filósofos dispostos em círculo, cada um alternando entre pensar e comer. Para comer, um filósofo precisa de dois garfos: o que está à sua esquerda e o que está à sua direita. O desafio reside em projetar um algoritmo que garanta a **exclusão mútua** (um garfo só pode ser usado por um filósofo por vez) e, crucialmente, que evite o **deadlock** (bloqueio mútuo) e a **starvation** (inanição).

A avaliação comparou três soluções em Java: Quebra de Ordem (Tarefa 2), Limitação por Semáforos (Tarefa 3) e Monitores com Fairness (Tarefa 4).

### II. Metodologia

Todas as soluções foram implementadas em Java, utilizando N=5 filósofos e N=5 garfos.

A simulação foi executada por **5 minutos (300.000 ms)** para cada uma das três soluções (Tarefas 2, 3 e 4). As seguintes métricas foram coletadas e analisadas:

1. **Número de Refeições Total:** Contagem de quantas vezes cada filósofo completou o ciclo de comer.
2. **Tempo Médio de Espera (ms):** Tempo médio que um filósofo levou desde o início da tentativa de comer até a aquisição bem-sucedida de ambos os garfos.
3. **Distribuição Justa (CV):** Coeficiente de Variação (CV) das refeições. Um CV próximo de zero indica maior justiça (*fairness*).
4. **Taxa de Utilização dos Garfos (%):** Porcentagem do tempo total de execução que cada garfo esteve em uso.

### III. Resultados Comparativos

#### Tabela 1: Resumo das Métricas de Desempenho e Fairness

| Métrica | Tarefa 2 (Ordem) | Tarefa 3 (Semáforo N-1) | Tarefa 4 (Monitor c/ Fairness) |
| --- | --- | --- | --- |
| **Total de Refeições** | 1000 | 706 | **1085** |
| **Tempo Médio Espera** | 517.54 ms | 1104.02 ms | **385.40 ms** |
| **CV (Fairness)** | 0.1005 (Ruim) | 0.0113 (Ótimo) | **0.0105 (Melhor)** |
| **Deadlock?** | Não | Não | Não |

---

#### Tabela 2: Distribuição de Refeições por Filósofo (Starvation/Fairness)

| Filósofo | T2 (Refeições) | T3 (Refeições) | T4 (Refeições) |
| --- | --- | --- | --- |
| **P0** | 178 | 143 | 218 |
| **P1** | 193 | 139 | 213 |
| **P2** | 212 | 141 | 217 |
| **P3** | 233 | 143 | 217 |
| **P4** | 184 | 140 | 220 |
| **Média** | 200 | 141.2 | 217 |

*Obs: A solução da **Tarefa 1** gerou deadlock imediatamente e não completou nenhuma refeição.*

#### Gráfico 1: Taxa Média de Utilização dos Garfos

| Solução | Garfo Mínimo (%) | Garfo Máximo (%) | Média (%) |
| --- | --- | --- | --- |
| **Tarefa 2 (Ordem)** | 65.34% (G4) | 91.64% (G0) | 81.85% |
| **Tarefa 3 (Semáforo)** | 88.67% (G2) | 89.78% (G1) | **89.39%** |
| **Tarefa 4 (Monitor)** | 68.70% (G3) | 71.72% (G2) | 70.30% |

### IV. Análise Crítica das Soluções

| Critério | Tarefa 2 (Ordem Diferente) | Tarefa 3 (Semáforos N-1) | Tarefa 4 (Monitores c/ Fairness) |
| --- | --- | --- | --- |
| **Prevenção de Deadlock** | Eficaz. Quebra a Espera Circular. | Eficaz. Nega a condição de Posse e Espera, garantindo sempre um garfo livre. | Eficaz. Nega a Espera Circular, concedendo recursos de forma atômica (Vizinhos não comendo). |
| **Prevenção de Starvation** | Fraca. CV de 0.1005 e grande disparidade (P3 vs P4). O filósofo com ordem inversa (P4) e seus vizinhos sofrem mais. | Forte. CV de 0.0113. A limitação centralizada garante uma distribuição justa das oportunidades de sentar. | **Mais Forte.** CV de 0.0105. A lógica de `wait()` e `notifyAll()` garante que filósofos famintos sejam acordados e tenham prioridade (melhor fairness). |
| **Performance (Throughput)** | Alto (1000 refeições). O paralelismo é maximizado. | Baixo (706 refeições). O Semáforo (N-1) limita o paralelismo, forçando mais serialização. | **Mais Alto** (1085 refeições). O monitor coordena o acesso eficientemente e com menor contenção. |
| **Complexidade de Implementação** | Baixa. Apenas uma pequena alteração na classe `Filosofo`. | Média. Requer a introdução de `Semaphore.acquire()`/`release()` e tratamento de exceções. | Alta. Requer uma classe Monitor centralizada, gerenciamento de estados (`FAMINTO`), e o uso correto de `wait()`/`notifyAll()`/`synchronized`. |
| **Uso de Recursos** | Alto. Utilização desigual, mas alta (Média de 81.85% de uso). | Alto. Utilização muito alta e estável (Média de 89.39%). | Moderado. Utilização mais baixa (Média de 70.30%), pois a regra estrita de "vizinhos não podem comer" pode levar a mais ciclos ociosos (`PENSANDO`). |

### V. Conclusão 

1. **Tarefa 2 (Quebra de Ordem):**
* **Cenário Adequado:** Sistemas onde a **simplicidade** e o **alto throughput** são primordiais, e uma distribuição de recursos *ligeiramente* injusta é aceitável. É a solução mais rápida e simples.


2. **Tarefa 3 (Semáforos N-1):**
* **Cenário Adequado:** Sistemas onde a **fairness extrema** e a **facilidade de garantia de não-deadlock** são essenciais, mas o volume de processamento (throughput) pode ser sacrificado. Sua alta utilização dos garfos indica que, embora lento, o sistema está sempre trabalhando próximo ao limite imposto.


3. **Tarefa 4 (Monitores c/ Fairness):**
* **Cenário Adequado:** Sistemas **críticos** que exigem o **melhor equilíbrio** entre **alta performance** (maior throughput: 1085 refeições) e a **garantia estrita de fairness** (melhor CV: 0.0105), prevenindo starvation. É a solução mais robusta e eficiente em termos de tempo de espera médio, mas possui a maior complexidade de desenvolvimento.
