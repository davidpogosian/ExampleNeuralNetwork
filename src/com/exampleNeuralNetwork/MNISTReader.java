package com.exampleNeuralNetwork;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MNISTReader {
    public static void main(String[] args) {
        try (DataInputStream imageInputStream = new DataInputStream(new FileInputStream("C:\\Users\\David\\Downloads\\archive\\train-images.idx3-ubyte"));
             DataInputStream labelInputStream = new DataInputStream(new FileInputStream("C:\\Users\\David\\Downloads\\archive\\train-labels.idx1-ubyte"))) {

            // Parse image header
            int imageMagicNumber = imageInputStream.readInt();
            int numberOfImages = imageInputStream.readInt();
            int rows = imageInputStream.readInt(); // for each image
            int cols = imageInputStream.readInt();

            // Parse label header
            int labelMagicNumber = labelInputStream.readInt();
            int numberOfLabels = labelInputStream.readInt();

            // Read and display the first image
            byte[] imageData = new byte[rows * cols];
            imageInputStream.readFully(imageData);

            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols; ++j) {
                    System.out.printf("%-4d ", imageData[i * cols + j]);
                }
                System.out.println();
            }

            // Read the corresponding label
            byte label = labelInputStream.readByte();
            System.out.println("Label: " + label);

            // Process and display the first image
            BufferedImage image = createImage(rows, cols, imageData);
            displayImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
