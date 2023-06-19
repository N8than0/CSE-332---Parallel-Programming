package paralleltasks;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ArrayCopyTask extends RecursiveAction {

    public static final ForkJoinPool pool = new ForkJoinPool();
    public static final int CUTOFF = 1;

    public static int[] copy(int[] src) {
        int[] dst = new int[src.length];
        pool.invoke(new ArrayCopyTask(src, dst, 0, src.length));
        return dst;
    }

    private static int[] src, dst;
    private final int lo, hi;

    @SuppressWarnings("ManualArrayCopy")
    public static void sequential(int[] src, int[] dst, int lo, int hi) {
        for(int i = lo; i < hi; i++) {
            dst[i] = src[i];
        }
    }

    public ArrayCopyTask(int[] src, int[] dst, int lo, int hi) {
        ArrayCopyTask.src = src;
        ArrayCopyTask.dst = dst;
        this.lo = lo;
        this.hi = hi;
    }

    protected void compute() {
        if(hi - lo <= ArrayCopyTask.CUTOFF) {
            sequential(src,dst,lo,hi);
        } else {
            int mid = lo + (hi - lo) / 2;
            ArrayCopyTask left = new ArrayCopyTask(src,dst,lo,mid);
            ArrayCopyTask right = new ArrayCopyTask(src,dst,mid,hi);
            left.fork();
            right.compute();
            left.join();
        }
    }
}
