package org.example;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFileWriter {
    private String fileName;

    public JsonFileWriter(String fileName) {
        this.fileName = fileName;
    }

    public void writeToJsonFile(String json) {
        try (FileWriter fileWriter = new FileWriter(fileName, true)) {
            fileWriter.write(json + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

