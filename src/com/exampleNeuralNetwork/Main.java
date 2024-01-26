package com.exampleNeuralNetwork;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        
        Debugger debugger = new Debugger("logs/network.txt");

        MNISTReader trainReader = new MNISTReader(
            "archive/train-images.idx3-ubyte", 
            "archive/train-labels.idx1-ubyte",
            debugger
        );

        MNISTReader testReader = new MNISTReader(
            "archive/t10k-images.idx3-ubyte", 
            "archive/t10k-labels.idx1-ubyte",
            debugger
        );

        Network network = new Network(
            trainReader, 
            Arrays.asList(784, 30, 10), 
            debugger
        );

        network.stochasticGradientDescent(10, 6000, 1, 3.0);

        network.setTestData(testReader);


        int tests = 10000;
        System.out.printf("Successful tests: %d / %d \n\n", network.test(tests), tests);




        debugger.close();

    }
}