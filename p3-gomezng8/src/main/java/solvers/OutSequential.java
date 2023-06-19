package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OutSequential implements BellmanFordSolver {

    public List<Integer> solve(int[][] adjMatrix, int source) {
        List<Map<Integer, Integer>> g = Parser.parse(adjMatrix);
        int[] dist = new int[adjMatrix.length];
        int[] pred = new int[adjMatrix.length];
        int[] dist_copy = new int[adjMatrix.length];
        for(Map vertex : g) {
            int v = g.indexOf(vertex);
            dist[v] = GraphUtil.INF;
            pred[v] = -1;
            dist[source] = 0;
        }

        for(int i = 0; i < g.size(); i++) {
            for (Map m : g) {
                    dist_copy[g.indexOf(m)] = dist[g.indexOf(m)];
                }
            for (Map vertex : g) {
                Set s = vertex.keySet();
                int v = g.indexOf(vertex);
                for(Object value : s) {
                    int w = (int) value;
                    if(dist_copy[v] + (int) vertex.get(value) < dist[w] &&
                        dist_copy[v] != GraphUtil.INF) {
                        dist[w] = dist_copy[v] + (int) vertex.get(value);
                        pred[w] = v;
                    }
                }
            }
        }
        return GraphUtil.getCycle(pred);
    }

}
