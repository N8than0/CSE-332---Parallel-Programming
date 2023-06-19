package paralleltasks;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxInTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;
    private static List<Map<Integer,Integer>> g;
    private static int[] dist,dist_copy,pred;
    private final int lo,hi;
    public static int[][] h;


    public static int[][] parallel(int[][] h,List<Map<Integer,Integer>> g,int[] dist, int[] dist_copy, int[] pred) {
        pool.invoke(new RelaxInTask(h,g,dist,dist_copy,pred,0,g.size()));
        return h;
    }


    public RelaxInTask(int[][] h,List<Map<Integer,Integer>> g, int[] dist, int[] dist_copy, int[] pred, int lo, int hi) {
        RelaxInTask.g = g;
        RelaxInTask.dist = dist;
        RelaxInTask.dist_copy = dist_copy;
        RelaxInTask.pred = pred;
        RelaxInTask.h = h;
        this.lo = lo;
        this.hi = hi;
    }

    public static void sequential(List<Map<Integer,Integer>> g, Map<Integer,Integer> vertex, int[] dist, int[] dist_copy, int[] pred) {
        Set<Integer> s = vertex.keySet();
        int v = g.indexOf(vertex);
        for (Integer value : s) {
            int w = value;
            if (dist_copy[w] + vertex.get(w) < dist[v] && dist_copy[w] != GraphUtil.INF) {
                dist[v] = dist_copy[w] + vertex.get(value);
                pred[v] = w;
            }
        }
    }

    protected void compute() {
        if (hi - lo <= RelaxInTask.CUTOFF) {
            Map<Integer, Integer> vertex = g.get(lo + (hi - lo) / 2);
            sequential(g, vertex, dist, dist_copy, pred);
        } else {
            int mid = lo + (hi - lo) / 2;

            RelaxInTask left = new RelaxInTask(h, g, dist, dist_copy, pred, lo, mid);
            RelaxInTask right = new RelaxInTask(h, g, dist, dist_copy, pred, mid, hi);

            left.fork();
            right.compute();
            left.join();

            h[0] = dist;
            h[1] = pred;

        }
    }
}
