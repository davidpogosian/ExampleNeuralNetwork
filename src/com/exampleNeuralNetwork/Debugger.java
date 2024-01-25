package com.exampleNeuralNetwork;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Debugger {

    FileOutputStream fileOutputStream;

    public Debugger(String filePath) {
        try {
            fileOutputStream = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(String content) {
        try {
            fileOutputStream.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
