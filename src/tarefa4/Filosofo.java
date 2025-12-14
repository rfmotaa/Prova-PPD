package tarefa4;

import java.util.Random;

public class Filosofo implements Runnable {
    private final int id;
    private final Mesa mesa;
    private int refeicoes = 0;

    private long inicioEsperaNano = 0;
    private long totalTempoEsperaNano = 0;
    private int tentativasComer = 0;
    
    private long inicioComerNano = 0;
    private long totalTempoComendoNano = 0;

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
                inicioEsperaNano = System.nanoTime();
                tentativasComer++;
                
                mesa.pegarGarfos(id);
                long tempoEsperaAtual = System.nanoTime() - inicioEsperaNano;
                totalTempoEsperaNano += tempoEsperaAtual;

                inicioComerNano = System.nanoTime();

                simularTempo("Está comendo");
                this.refeicoes++;

                totalTempoComendoNano += (System.nanoTime() - inicioComerNano);

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
    
    public double getTempoMedioEsperaMs() {
        if (tentativasComer == 0) return 0.0;
        return (totalTempoEsperaNano / (double) tentativasComer) / 1_000_000.0;
    }
    
    public long getTempoTotalComendoNano() {
        return totalTempoComendoNano;
    }
}