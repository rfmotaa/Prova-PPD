package tarefa3;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Filosofo implements Runnable {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private final Semaphore limiteAcesso;
    private int refeicoes = 0;

    private long inicioEsperaNano = 0;
    private long totalTempoEsperaNano = 0;
    private int tentativasComer = 0;

    private final Random random = new Random();

    public Filosofo(int id, Garfo garfoEsquerdo, Garfo garfoDireito, Semaphore limiteAcesso) {
        this.id = id;
        this.garfoEsquerdo = garfoEsquerdo;
        this.garfoDireito = garfoDireito;
        this.limiteAcesso = limiteAcesso;
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

                printar("Tenta adquirir permissão para sentar à mesa (Semáforo)");
                limiteAcesso.acquire();
                printar("Adquiriu permissão. Tentando pegar garfos...");


                printar(String.format("Tenta pegar o Garfo Esquerdo (%d)", garfoEsquerdo.getId()));
                synchronized (garfoEsquerdo) {
                    garfoEsquerdo.registrarInicioUso(); 
                    printar(String.format("Pegou Garfo Esquerdo (%d)", garfoEsquerdo.getId()));

                    Thread.sleep(10); 

                    printar(String.format("Tenta pegar o Garfo Direito (%d)", garfoDireito.getId()));
                    synchronized (garfoDireito) {
                        garfoDireito.registrarInicioUso(); 
                        
                        long tempoEsperaAtual = System.nanoTime() - inicioEsperaNano;
                        totalTempoEsperaNano += tempoEsperaAtual;
                        
                        // 3. COMER
                        printar("Conseguiu pegar ambos os garfos e COMEÇA A COMER"); 
                        simularTempo("Está comendo"); 
                        this.refeicoes++;
                        
                        garfoDireito.registrarFimUso();
                    } 
                    garfoEsquerdo.registrarFimUso();
                } 
                
                printar("Terminou de comer e solta ambos os garfos");  
                limiteAcesso.release(); 

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
}