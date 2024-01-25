package com.exampleNeuralNetwork;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MNISTReader implements Data<LabeledImage> {

    List<LabeledImage> labeledImages;

    int numberOfImages;
    int numberOfLabels;
    int rows;
    int cols;
    int index = 0;

    Debugger debugger;


    public MNISTReader(String imagePath, String labelPath, Debugger debugger) {

        this.debugger = debugger;

        try {
            DataInputStream imageInputStream = new DataInputStream(new FileInputStream(imagePath));
            DataInputStream labelInputStream = new DataInputStream(new FileInputStream(labelPath));
            
            imageInputStream.readInt(); // imageMagicNumber
            numberOfImages = imageInputStream.readInt();
            rows = imageInputStream.readInt();
            cols = imageInputStream.readInt();

            labelInputStream.readInt(); // labelMagicNumber
            numberOfLabels = labelInputStream.readInt();

            extractData(imageInputStream, labelInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void extractData(
        DataInputStream imageInputStream,
        DataInputStream labelInputStream) {

        labeledImages = new ArrayList<LabeledImage>();

        try {
            while ((imageInputStream.available() > 0) && (labelInputStream.available() > 0)) {

                byte[] imageData = new byte[rows * cols];
                byte label = 0;

                try {
                    imageInputStream.readFully(imageData);
                    label = labelInputStream.readByte();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // ... with a value of 0.0 representing black, a value of 1.0 representing white ... 
                Double[] doubleImageData = new Double[imageData.length];
                for (int i = 0; i < imageData.length; i++) {
                    doubleImageData[i] = ((double) (imageData[i] & 0xFF) / 255);
                }

                labeledImages.add(new LabeledImage(doubleImageData, (double) label));
                
            }

            debugger.write(String.format("MNISTReader: Packaged %d LabeledImages \n\n", labeledImages.size()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public LabeledImage getNextExample() throws EOFException {
        if (index < numberOfImages) {
            return labeledImages.get(index++);
        } else {
            throw new EOFException("Out of example LabeledImages.");
        }

    }

    @Override
    public void shuffle() {
        Collections.shuffle(labeledImages);
        index = 0;
    }


}
