package com.suleimanov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emir Suleimanov
 */
public class GroupingLogic {

    public static Map<Integer, List<Integer>> groupLines(List<String> lines) {
        Map<ColumnKey, Integer> keyToFirstRow = new HashMap<>();
        UnionFind uf = new UnionFind(lines.size());

        for (int i = 0; i < lines.size(); i++) {
            parseColumns(lines.get(i), i, keyToFirstRow, uf);
        }
        return uf.getGroups();
    }

    public static void parseColumns(String line, int rowIndex, Map<ColumnKey, Integer> map, UnionFind uf) {
        int col = 0;
        int start = 0;

        for (int i = 0; i <= line.length(); i++) {
            if (i == line.length() || line.charAt(i) == ';') {
                if (start < i) {
                    String val = line.substring(start, i).trim();
                    if (!val.isEmpty() && !val.equals("\"\"")) {
                        ColumnKey key = new ColumnKey(col, val);
                        Integer other = map.putIfAbsent(key, rowIndex);
                        if (other != null) {
                            uf.union(other, rowIndex); // сразу объединяем
                        }
                    }
                }
                col++;
                start = i + 1;
            }
        }
    }

    public static List<List<String>> extractGroupedLines(List<String> lines, Map<Integer, List<Integer>> groups) {
        List<List<String>> result = new ArrayList<>();
        for (List<Integer> group : groups.values()) {
            if (group.size() > 1) { // только интересные группы
                List<String> g = new ArrayList<>();
                for (int idx : group) {
                    g.add(lines.get(idx));
                }
                result.add(g);
            }
        }

        // сортируем группы по убыванию размера
        result.sort((a, b) -> Integer.compare(b.size(), a.size()));
        return result;
    }
}
