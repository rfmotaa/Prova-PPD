package tarefa1;

import java.util.Random;

public class Filosofo implements Runnable {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;

    private final Random random = new Random();

    public Filosofo(int id, Garfo garfoEsquerdo, Garfo garfoDireito) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
    }

    private void printar(String acao) {
        System.out.printf("[Filósofo %d] %s\n", id, acao);
    }

    private void simularTempo(String estado) throws InterruptedException {
        long tempo = random.nextInt(1000); 
        printar(String.format("%s por %dms...", estado, tempo));
        Thread.sleep(tempo);
    }

    @Override
    public void run() {
        while (true) {
            try {
                simularTempo("Começa a pensar"); 
                
                // Ordem que leva ao deadlock: Garfo Esquerdo -> Garfo Direito
                // O fiosofo tentará pegar o esquerdo, mas como todos fazem isso ao mesmo tempo, nunca tem garfo direito disponível
                // O que vai gerar a posição de deadlock
                printar(String.format("Tenta pegar o Garfo Esquerdo (%d)", garfoEsquerdo.getId()));
                synchronized (garfoEsquerdo) {

                    printar(String.format("Pegou Garfo Esquerdo (%d)", garfoEsquerdo.getId()));

                    // colocando tempo de espera para aumentar a chance de deadlock
                    Thread.sleep(1000); 

                    printar(String.format("Tenta pegar o Garfo Direito (%d)", garfoDireito.getId()));
                    synchronized (garfoDireito) {
                        
                        printar("Conseguiu pegar ambos os garfos e COMEÇA A COMER"); 
                        simularTempo("Está comendo"); 
                    } 
                } 
                // Quando os blocos syncronized terminam, os recursos (garfos) são liberados automatiamente
                printar("Terminou de comer e solta ambos os garfos");   

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}