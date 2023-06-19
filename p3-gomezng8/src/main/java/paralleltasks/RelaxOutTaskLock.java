package paralleltasks;

import cse332.graph.GraphUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class RelaxOutTaskLock extends RecursiveAction {

    public static final ReentrantLock lock = new ReentrantLock();

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    private static List<Map<Integer,Integer>> g;
    private static int[] dist,dist_copy,pred;
    private final int lo,hi;
    public static int[][] h;

    public static int[][] parallel(int[][] h,List<Map<Integer,Integer>> g,int[] dist, int[] dist_copy, int[] pred) {
        pool.invoke(new RelaxOutTaskLock(h,g,dist,dist_copy,pred,0,dist.length));
        return h;
    }

    public static void sequential(List<Map<Integer,Integer>> g,Map<Integer,Integer> vertex, int[] dist, int[] dist_copy, int[] pred) {
        Set<Integer> s = vertex.keySet();
        int v = g.indexOf(vertex);
        lock.lock();
        for (Integer value : s) {
            int w = value;
            if (dist_copy[v] + vertex.get(value) < dist[w] && dist_copy[v] != GraphUtil.INF) {
                dist[w] = dist_copy[v] + vertex.get(value);
                pred[w] = v;
            }
        }
        lock.unlock();
    }

    public RelaxOutTaskLock(int[][] h,List<Map<Integer,Integer>> g,int[] dist, int[] dist_copy, int[] pred, int lo, int hi) {
        RelaxOutTaskLock.h = h;
        RelaxOutTaskLock.g = g;
        RelaxOutTaskLock.dist = dist;
        RelaxOutTaskLock.dist_copy = dist_copy;
        RelaxOutTaskLock.pred = pred;
        this.lo = lo;
        this.hi = hi;
    }


    protected void compute() {
        if(hi - lo <= RelaxOutTaskLock.CUTOFF) {
            Map<Integer,Integer> vertex = g.get(lo + (hi - lo) / 2);
            sequential(g,vertex,dist,dist_copy,pred);
        } else {
            int mid = lo + (hi - lo) / 2;

            RelaxOutTaskLock left = new RelaxOutTaskLock(h,g,dist,dist_copy,pred,lo,mid);
            RelaxOutTaskLock right = new RelaxOutTaskLock(h,g,dist,dist_copy,pred,mid,hi);

            left.fork();
            right.compute();
            left.join();

            lock.lock();
            h[0] = dist;
            h[1] = pred;
            lock.unlock();

        }
    }


}
