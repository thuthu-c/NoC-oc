import java.util.ArrayList;
import java.util.List;

public class NoC {
    private final Router[][] routers;
    private final int rows, cols;

    public NoC(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        routers = new Router[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                routers[i][j] = new Router(j, i, rows, cols);
            }
        }
        setNeighbors(rows, cols);
    }

    private void setNeighbors(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Router south = (i > 0) ? routers[i - 1][j] : null;
                Router north = (i < rows - 1) ? routers[i + 1][j] : null;
                Router east = (j < cols - 1) ? routers[i][j + 1] : null;
                Router west = (j > 0) ? routers[i][j - 1] : null;
                routers[i][j].setNeighbors(north, south, east, west);
            }
        }
    }

    public void blockRouter(int x, int y) {
        routers[y][x].block();
    }

    public List<String> sendPacket(int[] source, int[] destination, int size) {
        Packet packet = new Packet(source, destination, size);
        List<String> path = new ArrayList<>();
        for (Flit flit : packet.getFlits()) {
            routers[source[0]][source[1]].routeFlit(flit, 0, path);
        }
        return path;
    }

    public void routeAllFlits(int hops, List<String> path) {
        path.add("Beginning cycle #"+ hops);
        for (Router[] routerRow : routers) {
            for (Router router : routerRow) {
                router.resetFlits();
            }
        }
        for (Router[] routerRow : routers) {
            for (Router router : routerRow) {
                router.forwardFlits(hops, path);
            }
        }
    }

    public void printAllLocalBuffers() {
        for (Router[] routerRow : routers) {
            for (Router router : routerRow) {
                router.printLocalBuffer();
            }
        }
    }

    public void mapGraphs(){
        int[][] graph = new int[rows*cols][4];
        int nrouter=-1;

        for (Router[] routerRow : routers) {
            for (Router router : routerRow) {
                int neighborsLeft = 4;
                ++nrouter;
                if(router.east == null || router.east.blocked){
                    --neighborsLeft;
                } else {
                    graph[nrouter][neighborsLeft-4] = nrouter + 2;
                }
                if(router.north == null || router.north.blocked){
                    --neighborsLeft;
                } else {
                    graph[nrouter][neighborsLeft-3] = nrouter + cols + 1;
                }
                if(router.west == null || router.west.blocked){
                    --neighborsLeft;
                } else {
                    graph[nrouter][neighborsLeft-2] = nrouter;
                }
                if(router.south == null || router.south.blocked){
                    --neighborsLeft;
                } else {
                    graph[nrouter][neighborsLeft-1] = nrouter - cols + 1;
                }
            }
        }
        for (Router[] routerRow : routers) {
            for (Router router : routerRow) {
                router.setGraph(graph);
            }
        }
    }
}
