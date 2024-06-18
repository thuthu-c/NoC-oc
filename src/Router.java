import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Router {
    private final int[] position;
    private Router north, south, east, west;
    private final Queue<Flit> bufferN;
    private final Queue<Flit> bufferS;
    private final Queue<Flit> bufferE;
    private final Queue<Flit> bufferW;
    private final Queue<Flit> bufferLocal;
    private boolean blocked;

    public Router(int x, int y) {
        this.position = new int[]{x, y};
        this.bufferN = new LinkedList<>();
        this.bufferS = new LinkedList<>();
        this.bufferE = new LinkedList<>();
        this.bufferW = new LinkedList<>();
        this.bufferLocal = new LinkedList<>();
        this.blocked = false;
    }

    public void setNeighbors(Router north, Router south, Router east, Router west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    public void block() {
        this.blocked = true;
    }

    public void routeFlit(Flit flit, int hops, List<String> path) {
        if (blocked) {
            path.add("Router (" + position[0] + "," + position[1] + ") is blocked");
            return;
        }
        int[] destination = flit.getDestination();
        path.add("Router (" + position[0] + "," + position[1] + ")");
        if (destination[0] == position[0] && destination[1] == position[1]) {
            bufferLocal.add(flit);
            path.add("Arrived at destination in " + hops + " hops");
        } else {
            if (destination[0] > position[0] && east != null && !east.blocked) {
                bufferE.add(flit);
            } else if (destination[0] < position[0] && west != null && !west.blocked) {
                bufferW.add(flit);
            } else if (destination[1] > position[1] && north != null && !north.blocked) {
                bufferN.add(flit);
            } else if (destination[1] < position[1] && south != null && !south.blocked) {
                bufferS.add(flit);
            } else {
                path.add("Cannot route flit from (" + position[0] + "," + position[1] + ")");
            }
        }
    }

    public void forwardFlits(int hops, List<String> path) {
        if (!bufferN.isEmpty() && north != null) north.routeFlit(bufferN.poll(), hops + 1, path);
        if (!bufferS.isEmpty() && south != null) south.routeFlit(bufferS.poll(), hops + 1, path);
        if (!bufferE.isEmpty() && east != null) east.routeFlit(bufferE.poll(), hops + 1, path);
        if (!bufferW.isEmpty() && west != null) west.routeFlit(bufferW.poll(), hops + 1, path);
    }

    public void printLocalBuffer() {
        while (!bufferLocal.isEmpty()) {
            Flit flit = bufferLocal.poll();
            System.out.println("Flit received at (" + position[0] + ", " + position[1] + "): " + flit.getData());
        }
    }
}
