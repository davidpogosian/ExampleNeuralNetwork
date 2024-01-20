package com.exampleNeuralNetwork;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MNISTReader {

    int numberOfImages;
    int rows;
    int cols;
    int numberOfLabels;

    DataInputStream imageInputStream;
    DataInputStream labelInputStream;

    public MNISTReader() {
        try {
            imageInputStream = new DataInputStream(new FileInputStream("C:\\Users\\David\\Downloads\\archive\\train-images.idx3-ubyte"));
            labelInputStream = new DataInputStream(new FileInputStream("C:\\Users\\David\\Downloads\\archive\\train-labels.idx1-ubyte"));
            // Parse image header
            int imageMagicNumber = imageInputStream.readInt();
            numberOfImages = imageInputStream.readInt();
            rows = imageInputStream.readInt(); // For each image.
            cols = imageInputStream.readInt();

            // Parse label header
            int labelMagicNumber = labelInputStream.readInt();
            numberOfLabels = labelInputStream.readInt();

            // Read and display the first image
            

            // for (int i = 0; i < rows; ++i) {
            //     for (int j = 0; j < cols; ++j) {
            //         System.out.printf("%-4d ", imageData[i * cols + j]);
            //     }
            //     System.out.println();
            // }

            // Read the corresponding label
            // byte label = labelInputStream.readByte();
            // System.out.println("Label: " + label);

            // Process and display the first image
            // BufferedImage image = createImage(rows, cols, imageData);
            // displayImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getNextLabel() {
        byte label = 0;
        try {
            label = labelInputStream.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (double) label;
    }

    public double[] getNextImage() {
        byte[] imageData = new byte[rows * cols];
        try {
            imageInputStream.readFully(imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Convert byte array to double array.
        double[] doubleImageData = new double[imageData.length];
        for (int i = 0; i < imageData.length; i++) {
            doubleImageData[i] = (double) (imageData[i] & 0xFF);  // Ensure positive values
        }
    
        return doubleImageData;
    }
    
    private static BufferedImage createImage(int width, int height, byte[] imageData) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        image.getRaster().setDataElements(0, 0, width, height, imageData);
        return image;
    }

    private static void displayImage(BufferedImage image) {
        JFrame frame = new JFrame("MNIST Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getNumberOfLabels() {
        return numberOfLabels;
    }

    public void setNumberOfLabels(int numberOfLabels) {
        this.numberOfLabels = numberOfLabels;
    }


}
