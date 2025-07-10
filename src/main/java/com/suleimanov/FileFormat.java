package com.suleimanov;

/**
 * @author Emir Suleimanov
 */
public enum FileFormat {
    GZ (".gz"),
    SEVEN_Z (".7z");

    private final String name;

    FileFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "FileFormat{" +
               "name='" + name + '\'' +
               '}';
    }
}
