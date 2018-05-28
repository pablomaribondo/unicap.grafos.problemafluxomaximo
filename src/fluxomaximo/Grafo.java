package fluxomaximo;

import java.util.LinkedList;
import java.util.Queue;

public class Grafo {

    /**
     * Função de Busca em Largura
     * @param grafo
     * @param s Vértice inicial
     * @param t Vértice final
     * @param anterior Vetor que guarda o antecessor de cada vértice, no caminho de "s" até ele
     * @return Retorna "true" se existe caminho entre "s" e "t" no grafo residual e falso caso não exista.
     */
    private boolean buscaEmLargura(int[][] grafo, int s, int t, int[] anterior) {

        boolean[] visitado = new boolean[grafo.length];

        Queue<Integer> fila = new LinkedList<Integer>();
        fila.add(s);
        visitado[s] = true;
        anterior[s] = -1;

        while (!fila.isEmpty()) {
            int v = fila.poll();
            for (int i = 0; i < grafo.length; i++) {
                if (grafo[v][i] > 0 && !visitado[i]) {
                    fila.offer(i);
                    visitado[i] = true;
                    anterior[i] = v;
                }
            }
        }
        return (visitado[t] == true);
    }
    
    /**
     * Função para achar todos os vértices alcaçáveis a partir de "s"
     * @param grafo
     * @param s Vértice inicial
     * @param visitado Vetor que diz quais vértices foram visitados
     */
    private void acharVizinhos(int[][] grafo, int s, boolean[] visitado) {
        visitado[s] = true;
        for (int i = 0; i < grafo.length; i++) {
            if (grafo[s][i] > 0 && !visitado[i]) {
                acharVizinhos(grafo, i, visitado);
            }
        }
    }

    /**
     * Função que imprime o corte mínimo
     * @param grafo
     * @param s Vértice inicial
     * @param t Vértice final
     */
    public int[][] corteMinimo(int[][] grafo, int s, int t) {
        int u, v;

        /**
         * Cria um grafo residual
         */
        int[][] grafoResidual = new int[grafo.length][grafo.length];
        /**
         * Preenche o grafo residual com as capacidades do grafo original
         */
        for (int i = 0; i < grafo.length; i++) {
            for (int j = 0; j < grafo.length; j++) {
                grafoResidual[i][j] = grafo[i][j];
            }
        }

        int[] anterior = new int[grafo.length];
        
        int fluxoMaximo = 0;

        /**
         * Caso haja caminho de "s" para "t", aumenta-se o fluxo
         */
        while (buscaEmLargura(grafoResidual, s, t, anterior)) {

            /**
             * Encontra o fluxo máximo através do caminho encontrado
             */
            int fluxo = Integer.MAX_VALUE;
            for (v = t; v != s; v = anterior[v]) {
                u = anterior[v];
                fluxo = Math.min(fluxo, grafoResidual[u][v]);
            }

            /**
             * Atualiza as capacidades residuais das arestas e as inverte ao longo do caminho 
             */
            for (v = t; v != s; v = anterior[v]) {
                u = anterior[v];
                grafoResidual[u][v] = grafoResidual[u][v] - fluxo;
                grafoResidual[v][u] = grafoResidual[v][u] + fluxo;
            }
            /**
             * Adiciona o fluxo ao fluxo máximo
             */
            fluxoMaximo += fluxo;
        }
        
        boolean[] foiVisitado = new boolean[grafo.length];
        /**
         * Acha os alcançáveis de "s"
         */
        acharVizinhos(grafoResidual, s, foiVisitado);

        /**
         * Imprime na tela o fluxo máximo e o corte mínimo
         */
        System.out.println("O fluxo máximo é: " + fluxoMaximo);
        System.out.println("O corte mínimo é: ");
        for (int i = 0; i < grafo.length; i++) {
            for (int j = 0; j < grafo.length; j++) {
                if (grafo[i][j] > 0 && foiVisitado[i] && !foiVisitado[j]) {
                    System.out.println((i + 1) + " - " + (j + 1));
                }
            }
        }
        return grafoResidual;
    }
}