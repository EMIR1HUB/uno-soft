package com.suleimanov;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * @author Emir Suleimanov
 */
public class InputProcessor {

    public static List<String> readValidUniqueLines(String gzFilePath) throws IOException {
        List<String> lines = new ArrayList<>();
        Map<String, Integer> lineToId = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            new GZIPInputStream(new FileInputStream(gzFilePath)), StandardCharsets.UTF_8))
        ) {
            String line;
            int lineIndex = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                boolean valid = isValid(line);
                if (!valid) {
                    continue;
                }
                if (!lineToId.containsKey(line)) {
                    lineToId.put(line, lineIndex++);
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    private static boolean isValid(String line) {
        if (line == null || line.isEmpty()) {
            return false;
        }

        // удаляем обёртку в двойных кавычках, если есть
        if (line.startsWith("\"") && line.endsWith("\"") && line.length() > 1) {
            line = line.substring(1, line.length() - 1);
        }

        // проверим, что осталась хотя бы одна полезная часть (не только ; и пробелы)
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c != ';' && c != ' ') {
                return true;
            }
        }
        return false;
    }
}
