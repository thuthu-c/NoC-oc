import java.util.List;

public class Simulador {
    public static void main(String[] args) {
        NoC noc = new NoC(8, 8);
        // Bloqueando alguns roteadores
        noc.blockRouter(1, 1);
        noc.blockRouter(2, 2);
        noc.blockRouter(3, 3);

        // Enviando um pacote da posição (0, 0) para (7, 7) com 5 variáveis
        List<String> path = noc.sendPacket(new int[]{0, 0}, new int[]{7, 7}, 5);

        // Executando múltiplos ciclos para rotear todos os flits
        for (int i = 0; i < 100; i++) {  // Ajuste o número de ciclos conforme necessário
            noc.routeAllFlits();
        }

        // Imprimindo a rota seguida pelos flits
        for (String step : path) {
            System.out.println(step);
        }

        // Imprimindo os buffers locais para verificar se os flits chegaram ao destino
        noc.printAllLocalBuffers();
    }
}
