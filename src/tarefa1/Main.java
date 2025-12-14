package tarefa1;

public class Main {
    public static void main(String[] args) {
        final int NUM_FILOSOFOS = 5;
        Garfo[] garfos = new Garfo[NUM_FILOSOFOS];
        
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Garfo(i);
        }

        // inicia os filosofos e atribui os garfos para eles
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Garfo garfoEsquerdo = garfos[i];
            Garfo garfoDireito = garfos[(i + 1) % NUM_FILOSOFOS]; 
            
            Filosofo f = new Filosofo(i, garfoEsquerdo, garfoDireito);
            new Thread(f).start();
        }
    }
}