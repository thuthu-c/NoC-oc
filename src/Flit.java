public class Flit {
    private final int[] source;
    private final int[] destination;
    private final int data;
    public boolean moved;

    public Flit(int[] source, int[] destination, int data) {
        this.source = source;
        this.destination = destination;
        this.data = data;
        this.moved = false;
    }

    public int[] getSource() {
        return source;
    }

    public int[] getDestination() {
        return destination;
    }

    public int getData() {
        return data;
    }
}
