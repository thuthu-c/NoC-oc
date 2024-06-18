import java.util.ArrayList;
import java.util.List;

public class Packet {
    private final int[] source;
    private final int[] destination;
    private final List<Flit> flits;

    public Packet(int[] source, int[] destination, int size) {
        this.source = source;
        this.destination = destination;
        this.flits = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            flits.add(new Flit(source, destination, i));
        }
    }

    public List<Flit> getFlits() {
        return flits;
    }
}
