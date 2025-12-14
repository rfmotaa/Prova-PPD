package tarefa2;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_FILOSOFOS = 5;
        Garfo[] garfos = new Garfo[NUM_FILOSOFOS];
        Filosofo[] filosofos = new Filosofo[NUM_FILOSOFOS];
        Thread[] threads = new Thread[NUM_FILOSOFOS];

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Garfo(i);
        }

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Garfo garfoEsquerdo = garfos[i];
            Garfo garfoDireito = garfos[(i + 1) % NUM_FILOSOFOS]; 
            
            filosofos[i] = new Filosofo(i, garfoEsquerdo, garfoDireito);
            threads[i] = new Thread(filosofos[i]);
            threads[i].start();
        }

        Thread.sleep(120000); // tempo de 2 min

        for (Thread t : threads) {
            t.interrupt(); 
        }
        
        Thread.sleep(2000); // tempo inserido para não bagunçar os logs no terminal
        // mostrar as estatísticas
        System.out.println("\n--- ESTATÍSTICAS DE REFEIÇÕES (2 MINUTOS) ---");
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            System.out.printf("Filósofo %d comeu %d vezes.\n", i, filosofos[i].getRefeicoes());
        }
    }
}