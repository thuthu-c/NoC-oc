import java.util.ArrayList;
import java.util.List;

public class NoC {
    private final Router[][] routers;

    public NoC(int rows, int cols) {
        routers = new Router[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                routers[i][j] = new Router(i, j);
            }
        }
        setNeighbors(rows, cols);
    }

    private void setNeighbors(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Router north = (i > 0) ? routers[i - 1][j] : null;
                Router south = (i < rows - 1) ? routers[i + 1][j] : null;
                Router east = (j < cols - 1) ? routers[i][j + 1] : null;
                Router west = (j > 0) ? routers[i][j - 1] : null;
                routers[i][j].setNeighbors(north, south, east, west);
            }
        }
    }

    public void blockRouter(int x, int y) {
        routers[x][y].block();
    }

    public List<String> sendPacket(int[] source, int[] destination, int size) {
        Packet packet = new Packet(source, destination, size);
        List<String> path = new ArrayList<>();
        for (Flit flit : packet.getFlits()) {
            routers[source[0]][source[1]].routeFlit(flit, 0, path);
        }
        return path;
    }

    public void routeAllFlits() {
        for (Router[] routerRow : routers) {
            for (Router router : routerRow) {
                router.forwardFlits(0, new ArrayList<>());
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
}
