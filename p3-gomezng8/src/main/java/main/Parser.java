package main;

import cse332.exceptions.NotYetImplementedException;
import cse332.graph.GraphUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Parser {

    /**
     * Parse an adjacency matrix into an adjacency list.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight
     */
    public static List<Map<Integer, Integer>> parse(int[][] adjMatrix) {
        List<Map<Integer,Integer>> adjList = new ArrayList<>();
        //Outer loop for the rows
        for(int i = 0; i < adjMatrix.length; i++) {
            int[] currentRow = adjMatrix[i];
            HashMap<Integer, Integer> currentIndex = new HashMap<>();
            //Inner loop for the indices of each row
            for(int j = 0; j < adjMatrix.length; j++) {
                //if this index in j is an edge of i, add it to the map
                //in the form of insert(index of edge,weight)
                if(currentRow[j] != GraphUtil.INF) {
                    currentIndex.put(j, currentRow[j]);
                }
            }
            //add the HashMap to the ArrayList
            adjList.add(i,currentIndex);
        }
        return adjList;
    }

    /**
     * Parse an adjacency matrix into an adjacency list with incoming edges instead of outgoing edges.
     * @param adjMatrix Adjacency matrix
     * @return Adjacency list of maps from node to weight with incoming edges
     */
    public static List<Map<Integer, Integer>> parseInverse(int[][] adjMatrix) {
        List<Map<Integer,Integer>> adjList = new ArrayList<>();
        //Initialize all the incoming edge HashMaps
        for(int i = 0; i < adjMatrix.length; i++) {
            adjList.add(i, new HashMap<>());
        }
        //Search through adjMatrix for outgoing edges
        for(int i = 0; i < adjMatrix.length; i++) {
            int[] currentRow = adjMatrix[i];
            //Search through the current row of adjMatrix for outgoing edges
            for(int j = 0; j < currentRow.length; j++) {
                if(currentRow[j] != GraphUtil.INF) {
                    //Add the edge to our ArrayList<Map> in the form of:
                    //adjList.get(outgoing edge row index).put(incoming edge row index, found edge weight)
                    adjList.get(j).put(i,currentRow[j]);
                }
            }
        }
        return adjList;
    }

}
