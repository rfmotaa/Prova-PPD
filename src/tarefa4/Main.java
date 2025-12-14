package tarefa4;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int NUM_FILOSOFOS = 5;
        Filosofo[] filosofos = new Filosofo[NUM_FILOSOFOS];
        Thread[] threads = new Thread[NUM_FILOSOFOS];
        Mesa mesa = new Mesa(NUM_FILOSOFOS);


        for (int i = 0; i < NUM_FILOSOFOS; i++) {            
            filosofos[i] = new Filosofo(i, mesa);
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