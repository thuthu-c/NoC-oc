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

        int[] destination = flit.getDestination();
        if (blocked) {
            path.add("Router (" + position[0] + "," + position[1] + ") is blocked");
            return;
        }

        path.add("Flit "+flit.getData()+" at Router (" + position[0] + "," + position[1] + ")");
        if (destination[0] == position[0] && destination[1] == position[1]) {
            bufferLocal.add(flit);
            path.add("Arrived at destination in " + hops + " hops");
            return;
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
                path.add("Cannot route flit from (" + position[0] + "," + position[1] +")");
            }
        }
    }

    public void forwardFlits(int hops, List<String> path) {
        if (!bufferN.isEmpty() && north != null) {
            Flit f = bufferN.poll();
            if(!f.moved) north.routeFlit(f, hops + 1, path);
            f.moved = true;
        }
        if (!bufferS.isEmpty() && south != null) {
            Flit f = bufferS.poll();
            if(!f.moved) south.routeFlit(f, hops + 1, path);
            f.moved = true;
        }
        if (!bufferE.isEmpty() && east != null) {
            Flit f = bufferE.poll();
            if(!f.moved) east.routeFlit(f, hops + 1, path);
            f.moved = true;
        }
        if (!bufferW.isEmpty() && west != null) {
            Flit f = bufferW.poll();
            if(!f.moved) west.routeFlit(f, hops + 1, path);
            f.moved = true;
        }
    }
    public void resetFlits(){
        Queue<Flit> buffer = new LinkedList<>();
        buffer.addAll(bufferE);
        while (!buffer.isEmpty()){
            Flit t = buffer.poll();
            t.moved = false;
        }
        buffer.addAll(bufferW);
        while (!buffer.isEmpty()){
            Flit t = buffer.poll();
            t.moved = false;
        }
        buffer.addAll(bufferN);
        while (!buffer.isEmpty()){
            Flit t = buffer.poll();
            t.moved = false;
        }
        buffer.addAll(bufferS);
        while (!buffer.isEmpty()){
            Flit t = buffer.poll();
            t.moved = false;
        }
    }

    public void printLocalBuffer() {
        while (!bufferLocal.isEmpty()) {
            Flit flit = bufferLocal.poll();
            System.out.println("Flit received at (" + position[0] + ", " + position[1] + "): " + flit.getData());
        }
    }
}
