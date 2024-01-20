package com.exampleNeuralNetwork;

import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Network network = new Network(Arrays.asList(10, 3, 2));
        network.load(randomDoubleArray(10));
        //network.run();
        //network.getResult();
        network.train();


    }

    public static double[] randomDoubleArray(int size) {
        Random random = new Random();
        double[] output = new double[size];
        for (int i = 0; i < size; ++i) {
            output[i] = random.nextDouble(0, 1);
        }
        return output;
    }
}