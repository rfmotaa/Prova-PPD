package tarefa4;

public class Mesa {
    private final int N; 
    
    private enum Estado { PENSANDO, FAMINTO, COMENDO }
    private final Estado[] estado;
    
    // Construtor
    public Mesa(int n) {
        this.N = n;
        this.estado = new Estado[N];
        for (int i = 0; i < N; i++) {
            estado[i] = Estado.PENSANDO;
        }
    }
    
    private int esquerdo(int i) { return (i + N - 1) % N; }
    private int direito(int i) { return (i + 1) % N; }

    private boolean podeComer(int i) {
        return estado[i] == Estado.FAMINTO && 
               estado[esquerdo(i)] != Estado.COMENDO && 
               estado[direito(i)] != Estado.COMENDO;
    }

    public synchronized void pegarGarfos(int i) throws InterruptedException {
        estado[i] = Estado.FAMINTO;
        System.out.printf("[Filósofo %d] Tenta pegar garfos e está FAMINTO.\n", i);

        while (!podeComer(i)) {
            wait();
        }

        estado[i] = Estado.COMENDO;
        System.out.printf("[Filósofo %d] COMEÇA A COMER (Garfo %d e %d).\n", i, esquerdo(i), direito(i));
    }

    public synchronized void soltarGarfos(int i) {
        estado[i] = Estado.PENSANDO;
        System.out.printf("[Filósofo %d] Terminou de comer e solta os garfos.\n", i);

        testarVizinho(esquerdo(i));
        testarVizinho(direito(i));
    }
    
    private void testarVizinho(int i) {
        if (estado[i] == Estado.FAMINTO && podeComer(i)) {
            notifyAll(); 
        }
    }
}