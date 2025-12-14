package tarefa2;

import java.util.Random;

public class Filosofo implements Runnable {
    private final int id;
    private final Garfo garfoEsquerdo;
    private final Garfo garfoDireito;
    private int refeicoes = 0;

    // metricas para tarefa 5
    private long inicioEsperaNano = 0;
    private long totalTempoEsperaNano = 0;
    private int tentativasComer = 0;

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
                
                inicioEsperaNano = System.nanoTime();
                tentativasComer++;
                
                // apenas o quarto filosofo vai começar pelo garfo da direita
                // o ato de pegar garfo foi movido para um método separado para fins de organização de codigo
                if (this.id == 4) {
                    pegarGarfos(garfoDireito, garfoEsquerdo);
                } else {
                    pegarGarfos(garfoEsquerdo, garfoDireito);
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void pegarGarfos(Garfo primeiro, Garfo segundo) throws InterruptedException {

        printar(String.format("Tenta pegar o Primeiro Garfo (%d)", primeiro.getId())); 
        synchronized (primeiro) {
            primeiro.registrarInicioUso();
            printar(String.format("Pegou Primeiro Garfo (%d)", primeiro.getId()));
            
            printar(String.format("Tenta pegar o Segundo Garfo (%d)", segundo.getId())); 
            synchronized (segundo) {
                segundo.registrarInicioUso();

                long tempoEsperaAtual = System.nanoTime() - inicioEsperaNano;
                totalTempoEsperaNano += tempoEsperaAtual;
                
                printar("Conseguiu pegar ambos os garfos e COMEÇA A COMER"); 
                simularTempo("Está comendo"); 
                this.refeicoes++; 
                
                segundo.registrarFimUso();
            } 
            primeiro.registrarFimUso();
        } 
        printar("Terminou de comer e solta ambos os garfos");
    }

    public int getRefeicoes() {
        return this.refeicoes;
    }

    public double getTempoMedioEsperaMs() {
        if (tentativasComer == 0) return 0.0;
        return (totalTempoEsperaNano / (double) tentativasComer) / 1_000_000.0;
    }
}