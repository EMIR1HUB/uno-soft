package com.suleimanov;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author Emir Suleimanov
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: java -jar your-program.jar <input-file.gz>");
            return;
        }

        long startTime = System.nanoTime();
        String inputPath = args[0];
        String outputPath = "output.txt";

        List<String> lines  = InputProcessor.readValidUniqueLines(inputPath);
        System.out.println("Всего строк: " + lines.size());
        Map<Integer, List<Integer>> rawGroups = GroupingLogic.groupLines(lines);
        List<List<String>> groups = GroupingLogic.extractGroupedLines(lines, rawGroups);
        writeGroupsToFile(groups, outputPath);

        long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;

        System.out.println("Количество групп с более чем одним элементом: " + groups.size());
        System.out.println("Время выполнения: " + elapsedMillis + " мс");
    }

    public static void writeGroupsToFile(List<List<String>> groups, String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(String.valueOf(groups.size()));
            writer.newLine();

            for (int i = 0; i < groups.size(); i++) {
                writer.write("Группа " + (i + 1));
                writer.newLine();
                for (String line : groups.get(i)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }
}