package com.exampleNeuralNetwork;

import java.io.EOFException;
import java.util.List;
import java.util.Random;

public class Network {

    List<Integer> layers;
    Data<LabeledImage> trainData;
    Data<LabeledImage> testData;
    double learningRate = 0.01;
    
    int L;

    Matrix input;
    Matrix[] deltaW;
    Matrix[] deltaB;
    Matrix[] a;
    Matrix[] b;
    Matrix[] e;
    Matrix[] w;
    Matrix[] z;

    int label;

    Debugger debugger;


    public Network(Data<LabeledImage> trainData, List<Integer> layers, Debugger debugger) {

        this.layers = layers;
        this.trainData = trainData;

        L = layers.size() - 2;

        input = new Matrix(layers.get(0), 1);
        deltaW = new Matrix[L + 1];
        deltaB = new Matrix[L + 1];
        a = new Matrix[L + 1];
        b = new Matrix[L + 1];
        e = new Matrix[L + 1]; 
        w = new Matrix[L + 1];
        z = new Matrix[L + 1];

        Random random = new Random();

        for (int l = 0; l <= L; ++l) {

            deltaW[l] = new Matrix(layers.get(l + 1), layers.get(l));
            deltaB[l] = new Matrix(layers.get(l + 1), 1);
            a[l] = new Matrix(layers.get(l + 1), 1);
            b[l] = new Matrix(layers.get(l + 1), 1);
            e[l] = new Matrix(layers.get(l + 1), 1);
            w[l] = new Matrix(layers.get(l + 1), layers.get(l));
            z[l] = new Matrix(layers.get(l + 1), 1);


            b[l].forEach((x) -> random.nextGaussian());
            w[l].forEach((x) -> random.nextGaussian());            

        }

        this.debugger = debugger;


    }

    private void feedforward() {

        z[0] = Matrix.add(Matrix.dot(w[0], input), b[0]);

        a[0] = Matrix.vectorized(z[0], this::sigmoid);

        for (int l = 1; l <= L; ++l) {

            z[l] = Matrix.add(Matrix.dot(w[l], a[l - 1]), b[l]);

            a[l] = Matrix.vectorized(z[l], this::sigmoid);
        }
    }
    
    public void stochasticGradientDescent(int batchSize, int batches, int epochs, double learningRate) {

        this.learningRate = learningRate;

        for (int epoch = 0; epoch < epochs; ++epoch) {

            System.out.printf("Starting epoch %d. \n\n", epoch);

            trainData.shuffle();

            for (int batch = 0; batch < batches; ++batch) {

                for (int l = 0; l <= L; ++l) {
                    deltaW[l].reset();
                    deltaB[l].reset();
                }

                for (int example = 0; example < batchSize; ++example) {

                    load(trainData);
                    feedforward();
                    backpropogate();

                }

                // Gradient Descent.
                for (int l = 0; l <= L; ++l) {
                    Matrix nablaW = Matrix.scalar(learningRate / batchSize, deltaW[l]);
                    Matrix nablaB = Matrix.scalar(learningRate / batchSize, deltaB[l]);

                    w[l].subtract(nablaW);
                    b[l].subtract(nablaB);
                }
                
            }

            System.out.printf("Ending epoch %d. \n\n", epoch);

        }

    }

    private void backpropogate() {

        Matrix y = labelToVector();

        e[L] = Matrix.hadamard(Matrix.subtract(a[L], y), Matrix.vectorized(z[L], this::sigmoidDerivative));
        deltaW[L].add(Matrix.dot(e[L], Matrix.transpose(a[L - 1])));
        deltaB[L].add(e[L]);

        for (int l = L - 1; l > 0; --l) {
            e[l] = Matrix.hadamard(Matrix.dot(Matrix.transpose(w[l + 1]), e[l + 1]), Matrix.vectorized(z[l], this::sigmoidDerivative));
            deltaW[l].add(Matrix.dot(e[l], Matrix.transpose(a[l - 1])));
            deltaB[l].add(e[l]);
        }

        e[0] = Matrix.hadamard(Matrix.dot(Matrix.transpose(w[0 + 1]), e[0 + 1]), Matrix.vectorized(z[0], this::sigmoidDerivative));
        deltaW[0].add(Matrix.dot(e[0], Matrix.transpose(input)));
        deltaB[0].add(e[0]);

    }

    public void setTestData(Data<LabeledImage> testData) {
        this.testData = testData;
    }

    public int test(int tests) {

        int correct = 0;

        for (int test = 0; test < tests; ++test) {

            load(testData);
            feedforward();

            double max = 0.0;
            int guess = -1;
            for (int i = 0; i < layers.get(L + 1); ++i) {
                if (a[L].get(i, 0) > max) {
                    max = a[L].get(i, 0);
                    guess = i;
                }
            }

            if (guess == label) {
                correct += 1;
            }

        }        
        
        return correct;

    }



    private void load(Data<LabeledImage> data) {
        LabeledImage example;
        try {
            example = data.getNextExample();
            input.columnVector(example.getImage());
            label = example.getLabel();
        } catch (EOFException e) {
            e.printStackTrace();
        }
    }

    private Matrix labelToVector() {

        Double[] yArray = new Double[layers.get(L + 1)];

        for (int i = 0; i < yArray.length; ++i) {
            if (i == label) {
                yArray[i] = 1.0; 
            } else {
                yArray[i] = 0.0;
            }
        }

        return new Matrix().columnVector(yArray);
    }

    private double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    private double sigmoidDerivative(double z) {
        return sigmoid(z) * (1 - sigmoid(z));
    }

}