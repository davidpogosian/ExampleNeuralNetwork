package com.exampleNeuralNetwork;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Network {
    
    int levels;
    int[] rows;
    int[] cols;
    double[][] weights;
    double[][] activations;
    double[][] nudges;
    double[][] nudgeStash;
    double learningRate = 0.1;
    
    public Network(List<Integer> layers) {

        levels = layers.size() - 2;

        // Initialize the weights.
        Random random = new Random();
        weights = new double[layers.size() - 1][];

        for (int i = 0; i < layers.size() - 1; ++i) {
            weights[i] = new double[layers.get(i + 1) * layers.get(i)];

            for (int j = 0; j < weights[i].length; ++j) {
                weights[i][j] = random.nextDouble();
            }
        }

        // Initialize the activations.
        activations = new double[layers.size()][];
        for (int i = 0; i < layers.size(); ++i) {
            activations[i] = new double[layers.get(i)];
        }

        // Initialize nudges and nudgeStash.
        nudges = new double[layers.size() - 1][];
        nudgeStash = new double[layers.size() - 1][];
        for (int i = 0; i < nudges.length; ++i) {
            nudges[i] = new double[layers.get(i) * layers.get(i + 1)];
            nudgeStash[i] = new double[layers.get(i) * layers.get(i + 1)];
        }

        // Initialize rows and cols.
        rows = new int[layers.size() - 1];
        cols = new int[layers.size() - 1];

        for (int i = 0; i < layers.size() - 1; ++i) {
            rows[i] = layers.get(i + 1);
            cols[i] = layers.get(i);
        }

    }

    public void load(double[] input) {
        for (int i = 0; i < activations[0].length; ++i) {
            activations[0][i] = sigmoid(input[i]);
        }
    }

    public void run() {
        for (int i = 0; i < weights.length; ++i) {
            //System.out.println("\n multiply: \n");
            //System.out.printf("\n (%dx%d): \n", rows[i], cols[i]);
            //print(weights[i], rows[i], cols[i], "weights[i]");
            //System.out.println("\n by \n");
            //print(activations[i], cols[i], 1, "activations[i]");

            //System.out.printf("\n (%dx%d): \n", activations[i].length, 1);
            //System.out.println("\n raw \n");
            
            
            double[] temp = matrixProduct(weights[i], activations[i], rows[i], cols[i], activations[i].length, 1);
            for (int j = 0; j < activations[i + 1].length; ++j) {
                activations[i + 1][j] = sigmoid(temp[j]);
            }

            //System.out.println(Arrays.toString(temp));

            //System.out.println("\n after sigmoid get: \n");

            //System.out.println(Arrays.toString(activations[i + 1]));
            //print(activations[i + 1], rows[i], 1, "activations[i + 1]");

        }
    }

    public void train(MNISTReader reader, int batchSize) {
        for (int batch = 0; batch < batchSize; ++batch) {
            // Construct answers array.
            double ans = reader.getNextLabel();
            double[] answers = new double[10];
            for (int i = 0; i < 10; ++i) {
                if (i == ans) {
                    answers[i] = 1;
                } else {
                    answers[i] = 0;
                }
            }

            load(reader.getNextImage());
            run();
            double[] results = getResults();

            //print(results, 2, 1, "results");
            //print(answers, 2, 1, "answers");

            // Special treatment of the first set of nudges.
            for (int r = 0; r < rows[levels]; ++r) {

                //System.out.printf("level: %d \nrow: %d \n", levels, r);
                double costDerivative = costDerivative(results[r], answers[r]);
                //System.out.printf("costDerivative: %f \n", costDerivative);

                for (int c = 0; c < cols[levels]; ++c) {
                    nudges[levels][r * cols[levels] + c] += costDerivative * precomputedSigDer(activations[levels + 1][r]);
                    //System.out.printf("level: %d \ncol: %d \n\n", levels, r, c);

                    //System.out.printf("activations[i][c]: %f \ncostDer: %f \n=> nudges[i][r * cols[i] + c]: %f \n\n", activations[levels][c], costDerivative, nudges[levels][r * cols[levels] + c]);

                }
            }

            // Scuffed.
            //print(nudges[levels], activations[levels + 1].length, activations[levels].length, "nudges[i]");


            
            // The rest of the nudges.
            for (int i = levels - 1; i >= 0; --i) {
                
                for (int r = 0; r < rows[i]; ++r) {

                    for (int c = 0; c < cols[i]; ++c) {
                        
                        for (int j = 0; j < rows[i + 1]; ++j) {

                            //System.out.printf("level: %d \nrow: %d\ncol: %d \n j: %d \n\n", i, r, c, j);

                            double temp = nudges[i + 1][j * cols[i + 1] + r];
                            temp *= weights[i + 1][j * rows[i] + r];

                            //print(activations[i + 1], rows[i + 1], 1, "activations of right");
                            //System.out.println(i + 1);

                            temp *= precomputedSigDer(activations[i + 1][r]);
                            nudges[i][r * cols[i] + c] += temp;


                        }


                    }
                }

                // Scuffed.
                //print(nudges[i], activations[i + 1].length, activations[i].length, "nudges[i]");
            }

            // Tack on activations, stash the nudges, and reset nudges.
            for (int i = levels; i >= 0; --i) {

                for (int r = 0; r < rows[i]; ++r) {

                    for (int c = 0; c < cols[i]; ++c) {
                        
                        nudges[i][r * cols[i] + c] *= activations[i][c];
                        nudgeStash[i][r * cols[i] + c] += nudges[i][r * cols[i] + c];
                        nudges[i][r * cols[i] + c] = 0;


                    }
                }

                // Scuffed.
                //print(nudgeStash[i], activations[i + 1].length, activations[i].length, "nudgeStash[i]");
            }
        }
        
        // Apply nudges from the stash, and reset the nudgeStash.
        for (int i = levels; i >= 0; --i) {

            for (int r = 0; r < rows[i]; ++r) {

                for (int c = 0; c < cols[i]; ++c) {
                    
                    weights[i][r * cols[i] + c] -= learningRate * nudgeStash[i][r * cols[i] + c];

                    nudgeStash[i][r * cols[i] + c] = 0;


                }

            }

        }

        
        


        

    }

    public double test(MNISTReader reader) {
        System.out.println("Testing");

        return 0;
    }

    public double cost(double actual, double anticipated) {
        return Math.pow((actual - anticipated), 2);
    }

    public double costDerivative(double actual, double anticipated) {
        return 2 * (actual - anticipated);
    }

    public double sigmoid(double x) {
        return 1 / (1 + Math.pow(Math.E, -1 * x));
    }

    public double sigmoidDerivative(double x) {
        return sigmoid(x) * (1 - sigmoid(x));
    }

    public double precomputedSigDer(double sig) {
        return sig * (1 - sig);
    }

    public double[] matrixProduct(double[] a, double[] b, int rowsA, int colsA, int rowsB, int colsB) {
        int rows = rowsA;
        int cols = colsB;
        double[] result = new double[rows * cols];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                for (int k = 0; k < colsA; ++k) {
                    result[i * cols + j] += a[i * colsA + k] * b[k * colsB + j];
                }
            }
        }

        return result;
    }

    public double[] getResults() {
        return activations[activations.length - 1];
    }

    public void print(double[] arr, int rows, int cols, String msg) {
        System.out.printf("\n--- %s --- \n", msg);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                System.out.printf("%-10.3f", arr[i * cols + j]);
            }
            System.out.println();
        }
        System.out.printf("--- %s --- \n", msg);
    }

}