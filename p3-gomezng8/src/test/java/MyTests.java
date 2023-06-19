import cse332.interfaces.BellmanFordSolver;
import cse332.graph.GraphUtil;
import solvers.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Parser;

public class MyTests {

    static final int X = GraphUtil.INF;

    @Test
    public void myTest() {
        int[][] testMat = new int[][]{{X, 1, X},
                                      {1, X, 1},
                                      {X, 1, X}};
        List<Map<Integer,Integer>> t = Parser.parseInverse(testMat);
        for(Map m: t) {
            System.out.println(m.toString());
        }
    }
}
