import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
public class Simulador {
    private NoC noc;
    private List<String> path;

    private Scanner s;
    public Simulador(){
        path = new ArrayList<>();
        s = new Scanner(System.in);
    }

    public void run() {
        startNoC();

        // Bloqueando alguns roteadores
        blockRouters();
        noc.mapGraphs();

        // Enviando um pacote da posição (0, 0) para (7, 7) com 5 variáveis
        sendPackets();

        System.out.println("At last, how many cycles would you like us to execute?");
        int cycles = s.nextInt();

        // Executando múltiplos ciclos para rotear todos os flits
        for (int i = 0; i < cycles; i++) {  // Ajuste o número de ciclos conforme necessário
            noc.routeAllFlits(i+1, path);
        }

        // Imprimindo a rota seguida pelos flits
        for(String step : path) {
            System.out.println(step);
        }


        // Imprimindo os buffers locais para verificar se os flits chegaram ao destino
        noc.printAllLocalBuffers();
    }

    public void startNoC(){
        System.out.println("What's the size of your NoC? (lines x columns)");
        int rows, cols;
        rows = s.nextInt();
        cols = s.nextInt();
        noc = new NoC(rows, cols);
    }
    public void blockRouters(){
        System.out.println("How many routers would you like to block?");
        // Lê o conteúdo do arquivo redirecionado e processa conforme necessário

        int n, rows, cols;

        n = s.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println("Coordinates for blocked router #"+(i+1));
            rows = s.nextInt();
            cols = s.nextInt();
            noc.blockRouter(rows,cols);
        }
    }

    public void sendPackets(){
        System.out.println("How many packet would you like to send?");
        int n, srcx, srcy, tgtx, tgty, data;
        n = s.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println("Please, type the *source* of packet #"+(i+1));
            srcy = s.nextInt();
            srcx = s.nextInt();
            System.out.println("Please, type the *destination* of packet #"+(i+1));
            tgty = s.nextInt();
            tgtx = s.nextInt();
            System.out.println("Please, type the *size* of packet #"+(i+1));
            data = s.nextInt();

            List<String> additions = noc.sendPacket(new int[]{srcx,srcy},new int[]{tgtx,tgty}, data, i+1);
            for(String p: additions) path.add(p);
        }
    }
}
