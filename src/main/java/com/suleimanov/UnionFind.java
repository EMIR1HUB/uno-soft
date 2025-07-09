package com.suleimanov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emir Suleimanov
 */
public class UnionFind {
    private final int[] parent;

    public UnionFind(int size) {
        this.parent = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rx = find(x);
        int ry = find(y);
        if (rx != ry) {
            parent[ry] = rx;
        }
    }

    public Map<Integer, List<Integer>> getGroups() {
        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < parent.length; i++) {
            int root = find(i);
            groups.computeIfAbsent(root, k -> new ArrayList<>()).add(i);
        }
        return groups;
    }
}
