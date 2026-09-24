package py.edu.uc.lp3.cs2;

/** Modo de disparo de un rifle. Cada modo sabe cuántas balas consume por disparo. */
public enum ModoDisparo {
    SEMI(1),
    RAFAGA(3),
    AUTOMATICO(5);

    private final int balasPorDisparo;

    ModoDisparo(int balasPorDisparo) {
        this.balasPorDisparo = balasPorDisparo;
    }

    public int balasPorDisparo() {
        return balasPorDisparo;
    }
}
