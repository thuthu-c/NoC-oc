import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Deque;

public class Router {
    private final int[] position;
    public Router north, south, east, west;
    private final Deque<Flit> bufferN;
    private final Deque<Flit> bufferS;
    private final Deque<Flit> bufferE;
    private final Deque<Flit> bufferW;
    private final Queue<Flit> bufferLocal;
    public boolean blocked;
    private int[][] graph;
    private int rows, cols;

    public Router(int x, int y, int rows, int cols) {
        this.position = new int[]{x, y};
        this.bufferN = new LinkedList<>();
        this.bufferS = new LinkedList<>();
        this.bufferE = new LinkedList<>();
        this.bufferW = new LinkedList<>();
        this.bufferLocal = new LinkedList<>();
        this.blocked = false;
        this.rows = rows;
        this.cols = cols;
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
        path.add("Flit "+flit.getData()+" form packet "+flit.packet+" at Router (" + position[0] + "," + position[1] + ")");
        if (destination[0] == position[0] && destination[1] == position[1]) {
            bufferLocal.add(flit);
            path.add("Flit "+flit.getData()+" form packet "+flit.packet+" arrived at destination in " + (flit.hop()-1) + " hops");
            return;
        } else {
            int nextStep = decideNextRouter(destination);

            if (nextStep == 1) bufferW.add(flit);
            else if(nextStep == cols) bufferS.add(flit);
            else if(nextStep == -cols) bufferN.add(flit);
            else if(nextStep == -1) bufferE.add(flit);
            else path.add("Cannot route flit from (" + position[0] + "," + position[1] +")");
        }
    }

    private int decideNextRouter(int[] destination){
        int vertices = graph.length;
        int[] prev = new int[vertices];

        //O código da BFS a seguir foi feita por e está disponível no site do Geeks For Geeks. Foi alterada para cálculo de distâncias apenas.
        // Create a queue for BFS
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[vertices];

        int startNode = position[1] * cols + position[0];
        //double-checking

        // Mark the current node as visited and enqueue it
        visited[startNode] = true;
        queue.add(startNode);

        // Iterate over the queue
        while (!queue.isEmpty()) {
            // Dequeue a vertex from queue and print it
            int currentNode = queue.poll();

            // Get all adjacent vertices of the dequeued
            // vertex currentNode If an adjacent has not
            // been visited, then mark it visited and
            // enqueue it
            for (int neighbor : graph[currentNode]) {
                if(neighbor == 0) break;
                if (!visited[neighbor-1]) {
                    prev[neighbor-1] = currentNode;
                    visited[neighbor-1] = true;
                    queue.add(neighbor-1);
                }
            }
        }
        int currentNode = destination[1] * cols + destination[0];
        while(true){
            if(prev[currentNode] == startNode){
                return startNode - currentNode;
            } else {
                currentNode = prev[currentNode];
            }
        }
    }

    public void setGraph(int[][] graph){
        this.graph = graph;
    }

    public void forwardFlits(int hops, List<String> path) {
        if (!bufferN.isEmpty() && north != null) {
            Flit f = bufferN.poll();
            if(!f.moved) north.routeFlit(f, f.hop(), path);
            else bufferN.push(f);
            f.moved = true;
        }
        if (!bufferS.isEmpty() && south != null) {
            Flit f = bufferS.poll();
            if(!f.moved) south.routeFlit(f, f.hop(), path);
            else bufferS.push(f);
            f.moved = true;
        }
        if (!bufferE.isEmpty() && east != null) {
            Flit f = bufferE.poll();
            if(!f.moved) east.routeFlit(f, f.hop(), path);
            else bufferE.push(f);
            f.moved = true;
        }
        if (!bufferW.isEmpty() && west != null) {
            Flit f = bufferW.poll();
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
            t.moved = false;
        }

        for (Flit t : bufferN){

            t.moved = false;
        }
        for (Flit t : bufferS){
            t.moved = false;
        }
    }

    public void printLocalBuffer() {
        while (!bufferLocal.isEmpty()) {
            Flit flit = bufferLocal.poll();
        }
    }
}
