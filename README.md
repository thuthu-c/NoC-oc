# NoC-oc

Descrição do Projeto da Unidade 2

Implementar um simulador de Rede em Chip em alto nível de abstração com um algoritmo de roteamento adaptativo capaz de desviar de roteadores bloqueados na rede.

Grupo: no máximo dois alunos
Valor: 10,0 (nota da Unidade 2)
Linguagem: qualquer linguagem
Não precisa de interface gráfica


Características:

Topologia: MESH-2D
Tamanho da rede: 8x8
Tamanho do flit: Cabeçalho* + Variável inteira
*Cabeçalho deve conter origem e destino

Entrada do simulador:
Origem e destino
Tamanho do pacote (quantas variáveis devem ser enviadas)
Roteador(es) bloquado(s)


Saída do simulador:
Rotas que os flits seguiram entre origem e destino
Quantidade de saltos (hops) que foram necessários para chegar ao destino


Algoritmo de roteamento adaptativo
Livre de deadlock
Livre de livelock
