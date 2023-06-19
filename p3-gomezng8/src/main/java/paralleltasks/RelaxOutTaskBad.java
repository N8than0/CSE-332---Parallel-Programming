package paralleltasks;

import cse332.graph.GraphUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class RelaxOutTaskBad extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private static List<Map<Integer,Integer>> g;
    private static int[] dist,dist_copy,pred;
    private final int lo,hi;
    public static int[][] h;


    public static int[][] relax(int[][] h,List<Map<Integer,Integer>> g,int[] dist, int[] dist_copy, int[] pred) {
        pool.invoke(new RelaxOutTaskBad(h,g,dist,dist_copy,pred,0,g.size()));
        return h;
    }


   public RelaxOutTaskBad(int[][] h,List<Map<Integer,Integer>> g, int[] dist, int[] dist_copy, int[] pred, int lo, int hi) {
        RelaxOutTaskBad.g = g;
        RelaxOutTaskBad.dist = dist;
        RelaxOutTaskBad.dist_copy = dist_copy;
        RelaxOutTaskBad.pred = pred;
        RelaxOutTaskBad.h = h;
        this.lo = lo;
        this.hi = hi;
   }

    public static void sequential(List<Map<Integer,Integer>> g, Map<Integer,Integer> vertex, int[] dist, int[] dist_copy, int[] pred) {
        Set<Integer> s = vertex.keySet();
        int v = g.indexOf(vertex);
        for (Integer value : s) {
            int w = value;
            if (dist_copy[v] + vertex.get(w) < dist[w] && dist_copy[v] != GraphUtil.INF) {
                dist[w] = dist_copy[v] + vertex.get(w);
                pred[w] = v;
            }
        }
    }

    protected void compute() {
        if(hi - lo <= RelaxOutTaskBad.CUTOFF) {
            Map<Integer,Integer> vertex = g.get(lo + (hi - lo) / 2);
            sequential(g,vertex,dist,dist_copy,pred);
        } else {
            int mid = lo + (hi - lo) / 2;

            RelaxOutTaskBad left = new RelaxOutTaskBad(h,g,dist,dist_copy,pred,lo,mid);
            RelaxOutTaskBad right = new RelaxOutTaskBad(h,g,dist,dist_copy,pred,mid,hi);

            left.fork();
            right.compute();
            left.join();

            h[0] = dist;
            h[1] = pred;

        }
    }







}
