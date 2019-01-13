/*
 * This is one way to implement Dijkstra Algorithm in directed graph, find shortest distance between to nodes
 * Using PriorityQueue as Min-Heap
 * Author: MhJoker, Copyright (c) 2019 MhJoker. All rights reserved.
 * Welcome to communicate with me about codes, wechat: zhiwenjok_r3, email: zhiwenjok_r@yahoo.com
 */

import java.util.*;
class DijkstraAlgorithm {
    // Test cases
    public static void main(String[] args) {
        // Graph is directed and weighted, <start, end, weight>
        List<int[]> graph = Arrays.asList(new int[]{0, 4, 3}, new int[]{0, 1, 9}, new int[]{0, 2, 6}, new int[]{0, 3, 5}, new int[]{2, 1, 2}, new int[]{2, 3, 4});
        DijkstraAlgorithm test = new DijkstraAlgorithm(graph);
        System.out.println(test.queryShorestDist(0, 3));
        System.out.println(test.queryShorestDist(2, 1));
        System.out.println(test.queryShorestDist(2, 2));
        System.out.println(test.queryShorestDist(3, 0));
    }

    private int[] distance; // Record all node's shortest dist from start until now
    private Map<Integer, List<Node>> map; // map to store all neighbor nodes
    private Map<Integer, Integer> indexes; // indexes to store all nodes' index in distance
    private Queue<Node> pq; // Min heap, always pick up shortest edge(node) to update it's neighbors
    private Set<Integer> set; // Every time a node comes out from pq, add it to set

    // Node <val, dist> as <u, edge>
    class Node{
        int val, dist;
        private Node(int val, int dist) {
            this.val = val;
            this.dist = dist;
        }
    }

    private DijkstraAlgorithm(List<int[]> graph) {
        // Initiate all ds
        map = new HashMap<>();
        indexes = new HashMap<>();
        Set<Integer> tmpSet = new HashSet<>();
        for (int[] g : graph) {
            map.putIfAbsent(g[0], new ArrayList<>());
            map.get(g[0]).add(new Node(g[1], g[2]));
            tmpSet.add(g[0]);
            tmpSet.add(g[1]);
        }
        int nodeNum = tmpSet.size();
        List<Integer> tmpList = new ArrayList<>(tmpSet);
        Collections.sort(tmpList);
        for (int i = 0; i < nodeNum; ++i){
            indexes.put(tmpList.get(i), i);
        }
        // Sort accords to dist
        pq = new PriorityQueue<>((a, b) -> a.dist - b.dist);
        set = new HashSet<>();
        distance = new int[nodeNum];
    }

    // Find the shorest path between start and end
    private int queryShorestDist(int start, int end) {
        if (start == end){
            return 0;
        }
        if (!map.containsKey(start)){
            return -1;
        }
        // Every time we need to find shortest dist between s and e, distance array should be reset
        Arrays.fill(distance, Integer.MAX_VALUE);
        // Dist[start] should be 0
        pq.offer(new Node(start, 0));
        distance[indexes.get(start)] = 0;
        while (set.size() != distance.length && !pq.isEmpty()){
            // Pick up shortest one and put into set
            Node cur = pq.poll();
            int u = cur.val;
            set.add(u);
            // We find the answer
            if (u == end){
                return distance[u];
            }
            if (map.containsKey(u)){
                // Update all neighbor points next to u
                for (Node next : map.get(u)){
                    int v = next.val;
                    // Don't look back
                    if (!set.contains(v)){
                        int index = indexes.get(v);
                        int newDist = next.dist + distance[indexes.get(u)];
                        // System.out.println(newDist);
                        if (newDist < distance[index]){
                            distance[index] = newDist;
                        }
                        // Update PQ
                        pq.offer(new Node(v, distance[index]));
                    }
                }
            }
        }
        // Input invalid, return -1
        return -1;
    }
}
