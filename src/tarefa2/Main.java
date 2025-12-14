package tarefa2;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_FILOSOFOS = 5;

        Garfo[] garfos = new Garfo[NUM_FILOSOFOS];
        Filosofo[] filosofos = new Filosofo[NUM_FILOSOFOS];
        Thread[] threads = new Thread[NUM_FILOSOFOS];

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Garfo(i);
        }

        long inicioTesteNano = System.nanoTime();

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Garfo garfoEsquerdo = garfos[i];
            Garfo garfoDireito = garfos[(i + 1) % NUM_FILOSOFOS]; 
            
            filosofos[i] = new Filosofo(i, garfoEsquerdo, garfoDireito);
            threads[i] = new Thread(filosofos[i]);
            threads[i].start();
        }

        Thread.sleep(300000); // tempo de 2 min (ou 5 min para fazer a tarefa 5)

        for (Thread t : threads) {
            t.interrupt(); 
        }
        
        Thread.sleep(2000); // tempo inserido para não bagunçar os logs no terminal

        long tempoRealTesteNano = System.nanoTime() - inicioTesteNano;

        System.out.println("\n--- ANÁLISE COMPARATIVA (TAREFA 2) ---");

        int[] refeicoes = new int[NUM_FILOSOFOS];
        double totalTempoEspera = 0;
        
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            refeicoes[i] = filosofos[i].getRefeicoes();
            System.out.printf("Filósofo %d comeu %d vezes.\n", i, refeicoes[i]);
            totalTempoEspera += filosofos[i].getTempoMedioEsperaMs();
        }
        
        double cv = calcularCoeficienteVariacao(refeicoes);
        System.out.printf("\n* Distribuição Justa (Coeficiente de Variação - CV): %.4f\n", cv);
        System.out.printf("* Tempo Médio de Espera (Total Filósofos): %.2f ms\n", totalTempoEspera / NUM_FILOSOFOS);


        System.out.println("\n* Taxa de Utilização dos Garfos:");
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            long tempoUsoGarfo = garfos[i].getTempoTotalUsoNano();
            double utilizacao = (tempoUsoGarfo / (double) tempoRealTesteNano) * 100.0;
            System.out.printf("  Garfo %d: %.2f%% de utilização\n", i, utilizacao);
        }
    }

    private static double calcularCoeficienteVariacao(int[] refeicoes) {
        int N = refeicoes.length;
        if (N == 0) return 0.0;

        double soma = 0;
        for (int r : refeicoes) { soma += r; }
        double media = soma / N;
        if (media == 0) return 0.0;

        double somaDiferencaQuadrada = 0;
        for (int r : refeicoes) {
            somaDiferencaQuadrada += pow(r - media, 2);
        }

        double desvioPadrao = sqrt(somaDiferencaQuadrada / N);
        return desvioPadrao / media;
    }
}