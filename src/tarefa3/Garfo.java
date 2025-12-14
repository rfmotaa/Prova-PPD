package tarefa3;

public class Garfo {
    private final int id;
    
    private long inicioUsoNano = 0;
    private long tempoTotalUsoNano = 0;

    public Garfo(int id) {
        this.id = id;
    }

    public int getId() { return this.id; }
    
    public void registrarInicioUso() {
        this.inicioUsoNano = System.nanoTime();
    }
    
    public void registrarFimUso() {
        this.tempoTotalUsoNano += (System.nanoTime() - inicioUsoNano);
    }

    public long getTempoTotalUsoNano() {
        return tempoTotalUsoNano;
    }
}