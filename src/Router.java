import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Deque;

public class Router {
    private final int[] position;
    private Router north, south, east, west;
    private final Deque<Flit> bufferN;
    private final Deque<Flit> bufferS;
    private final Deque<Flit> bufferE;
    private final Deque<Flit> bufferW;
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
        //System.out.println("Flit "+flit.hashCode()+" at Router (" + position[0] + "," + position[1] + ") hash "+hashCode());
        path.add("Flit "+flit.getData()+" at Router (" + position[0] + "," + position[1] + ")");
        if (destination[0] == position[0] && destination[1] == position[1]) {
            bufferLocal.add(flit);
            path.add("Flit "+flit.getData()+" arrived at destination in " + (flit.hop()-1) + " hops");
            return;
        } else {
            if (destination[0] > position[0] && east != null && !east.blocked) {
                //System.out.println("Sending Flit "+flit.hashCode()+" East!");
                bufferE.add(flit);
            } else if (destination[0] < position[0] && west != null && !west.blocked) {
                //System.out.println("Sending Flit "+flit.hashCode()+" West!");
                bufferW.add(flit);
            } else if (destination[1] > position[1] && north != null && !north.blocked) {

                bufferN.add(flit);
                //System.out.println("Resetting North! "+bufferN.size() + " Of router "+hashCode());
                //System.out.println("Sending Flit "+flit.hashCode()+" North! "+bufferN.size()+" Of router "+hashCode());
            } else if (destination[1] < position[1] && south != null && !south.blocked) {
                //System.out.println("Sending Flit "+flit.hashCode()+" South!");
                bufferS.add(flit);
            } else {
                path.add("Cannot route flit from (" + position[0] + "," + position[1] +")");
            }
        }
    }

    public void forwardFlits(int hops, List<String> path) {
        //System.out.println("Forwarding North! "+bufferN.size() + " Of router "+hashCode());
        if (!bufferN.isEmpty() && north != null) {
            Flit f = bufferN.poll();
            //System.out.println("Was Flit "+f.hashCode()+" from buffer "+bufferN.size()+" moved? "+f.moved);
            if(!f.moved) north.routeFlit(f, f.hop(), path);
            else bufferN.push(f);
            f.moved = true;
        }
        if (!bufferS.isEmpty() && south != null) {
            Flit f = bufferS.poll();
            //System.out.println("Was Flit "+f.hashCode()+" moved? "+f.moved);
            if(!f.moved) south.routeFlit(f, f.hop(), path);
            else bufferS.push(f);
            f.moved = true;
        }
        if (!bufferE.isEmpty() && east != null) {
            Flit f = bufferE.poll();
            //System.out.println("Was Flit "+f.hashCode()+" moved? "+f.moved);
            if(!f.moved) east.routeFlit(f, f.hop(), path);
            else bufferE.push(f);
            f.moved = true;
        }
        if (!bufferW.isEmpty() && west != null) {
            Flit f = bufferW.poll();
            //System.out.println("Was Flit "+f.hashCode()+" moved? "+f.moved);
            if(!f.moved) west.routeFlit(f, f.hop(), path);
            else bufferW.push(f);
            f.moved = true;
        }
    }
    public void resetFlits(){
        for (Flit t : bufferE){
            //System.out.println("Resetting East! "+t.hashCode());
            t.moved = false;
        }
        for (Flit t : bufferW){
            //System.out.println("Resetting West! "+t.hashCode());
            t.moved = false;
        }
        //System.out.println("Resetting North! "+bufferN.size() + " Of router "+hashCode());
        for (Flit t : bufferN){
            //System.out.println("Resetting "+t.hashCode());
            t.moved = false;
        }
        for (Flit t : bufferS){
            //System.out.println("Resetting South! "+t.hashCode());
            t.moved = false;
        }
    }

    public void printLocalBuffer() {
        while (!bufferLocal.isEmpty()) {
            Flit flit = bufferLocal.poll();
            //System.out.println("Flit received at (" + position[0] + ", " + position[1] + "): " + flit.hashCode());
        }
    }
}
