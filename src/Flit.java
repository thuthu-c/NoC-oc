public class Flit {
    private final int[] source;
    private final int[] destination;
    private final int data;
    public boolean moved;
    private int hops;
    public int packet;

    public Flit(int[] source, int[] destination, int data, int packet) {
        this.source = source;
        this.destination = destination;
        this.data = data;
        this.moved = false;
        hops = 0;
        this.packet = packet;
    }

    public int[] getSource() {
        return source;
    }

    public int[] getDestination() {
        return destination;
    }
    public int hop(){
        ++hops;
        return hops;
    }

    public int getData() {
        return data;
    }
}
