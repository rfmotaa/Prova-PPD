package tarefa4;

import java.util.Random;

public class Filosofo implements Runnable {
    private final int id;
    private final Mesa mesa;
    private int refeicoes = 0;

    private final Random random = new Random();

    public Filosofo(int id, Mesa mesa) {
        this.id = id;
        this.mesa = mesa;
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
                
                // o filoso nao precisa mais de objetos garfos individuais
                // ele só precisa notificar e utilizar o método da mesa (classe monitora)
                mesa.pegarGarfos(id);

                simularTempo("Está comendo");
                this.refeicoes++;

                mesa.soltarGarfos(id);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public int getRefeicoes() {
        return this.refeicoes;
    }
}