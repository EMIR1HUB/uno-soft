package com.suleimanov;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import static com.suleimanov.FileFormat.GZ;
import static com.suleimanov.FileFormat.SEVEN_Z;

/**
 * @author Emir Suleimanov
 */
public class InputProcessor {

    public static List<String> readValidUniqueLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        Map<String, Integer> lineToId = new HashMap<>();

        try (BufferedReader reader = getBufferedReader(filePath)) {
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

    private static BufferedReader getBufferedReader(String path) throws IOException {
        if (path.endsWith(GZ.getName())) {
            return new BufferedReader(new InputStreamReader(
                new GZIPInputStream(new FileInputStream(path)), StandardCharsets.UTF_8),
                64 * 1024 // 64 KB буфер
            );
        }
        else if (path.endsWith(SEVEN_Z.getName())) {
            return read7zFile(path);
        }
        else { // обычный .txt или без расширения
            return new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8),
                64 * 1024
            );
        }
    }

    private static BufferedReader read7zFile(String path) throws IOException {
        SevenZFile sevenZFile = new SevenZFile(new File(path));
        SevenZArchiveEntry entry = sevenZFile.getNextEntry();

        if (entry == null || entry.getSize() > Integer.MAX_VALUE) {
            throw new IOException("7z файл пуст или слишком большой");
        }

        byte[] content = new byte[(int) entry.getSize()];
        int offset = 0;
        int bytesRead;

        while ((bytesRead = sevenZFile.read(content, offset, content.length - offset)) > 0) {
            offset += bytesRead;
        }
        sevenZFile.close();

        return new BufferedReader(new InputStreamReader(
            new ByteArrayInputStream(content), StandardCharsets.UTF_8));
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
