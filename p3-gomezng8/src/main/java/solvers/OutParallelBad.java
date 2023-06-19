package solvers;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;
import cse332.interfaces.BellmanFordSolver;
import main.Parser;
import paralleltasks.ArrayCopyTask;
import paralleltasks.RelaxOutTaskBad;
import java.util.List;
import java.util.Map;


public class OutParallelBad implements BellmanFordSolver {

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

        int[][] h = new int[2][dist.length];
        for(int i = 0; i < g.size(); i++) {
            dist_copy = ArrayCopyTask.copy(dist);
            h = RelaxOutTaskBad.relax(h,g,dist,dist_copy,pred);
            dist = h[0];
            pred = h[1];
        }
        return GraphUtil.getCycle(pred);
    }

}
