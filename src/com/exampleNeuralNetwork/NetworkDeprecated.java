// package com.exampleNeuralNetwork;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Random;

// public class NetworkV1 {
    
//     int levels;
//     int[] rows;
//     int[] cols;
//     double[][] weights;
//     double[][] activations;
//     double[][] nudges;
//     double[][] nudgeStash;
//     double learningRate = 0.1;

//     Boolean debugMode = true;
//     Debugger debugger;
    
//     public NetworkV1(List<Integer> layers, Debugger debugger) {
//         this.debugger = debugger;

//         levels = layers.size() - 2;

//         // Initialize the weights.
//         Random random = new Random();
//         weights = new double[layers.size() - 1][];

//         for (int i = 0; i < layers.size() - 1; ++i) {
//             weights[i] = new double[layers.get(i + 1) * layers.get(i)];

//             for (int j = 0; j < weights[i].length; ++j) {
//                 // Change here!
//                 //weights[i][j] = random.nextDouble();
//                 //weights[i][j] = 0.01;
//                 weights[i][j] = random.nextDouble(0.001, 0.01);
//             }
//         }

//         // Initialize the activations.
//         activations = new double[layers.size()][];
//         for (int i = 0; i < layers.size(); ++i) {
//             activations[i] = new double[layers.get(i)];
//         }

//         // Initialize nudges and nudgeStash.
//         nudges = new double[layers.size() - 1][];
//         nudgeStash = new double[layers.size() - 1][];
//         for (int i = 0; i < nudges.length; ++i) {
//             nudges[i] = new double[layers.get(i) * layers.get(i + 1)];
//             nudgeStash[i] = new double[layers.get(i) * layers.get(i + 1)];
//         }

//         // Initialize rows and cols.
//         rows = new int[layers.size() - 1];
//         cols = new int[layers.size() - 1];

//         for (int i = 0; i < layers.size() - 1; ++i) {
//             rows[i] = layers.get(i + 1);
//             cols[i] = layers.get(i);
//         }

//     }

//     public void load(double[] input) {
//         for (int i = 0; i < activations[0].length; ++i) {
//             activations[0][i] = sigmoid(input[i]);
//         }
//     }

//     public void run() {
//         for (int i = 0; i < weights.length; ++i) {
//             //System.out.println("\n multiply: \n");
//             //System.out.printf("\n (%dx%d): \n", rows[i], cols[i]);
//             //print(weights[i], rows[i], cols[i], "weights[i]");
//             //System.out.println("\n by \n");
//             //print(activations[i], cols[i], 1, "activations[i]");

//             //System.out.printf("\n (%dx%d): \n", activations[i].length, 1);
//             //System.out.println("\n raw \n");
            
            
//             // double[] temp = matrixProduct(weights[i], activations[i], rows[i], cols[i], activations[i].length, 1);
//             // for (int j = 0; j < activations[i + 1].length; ++j) {
//             //     activations[i + 1][j] = sigmoid(temp[j]);
//             // }

//             //System.out.println(Arrays.toString(temp));

//             //System.out.println("\n after sigmoid get: \n");

//             //System.out.println(Arrays.toString(activations[i + 1]));
//             //print(activations[i + 1], rows[i], 1, "activations[i + 1]");

//         }
//     }

//     public void train(MNISTReader reader, int batchSize) {
//         System.out.println("Training.");
//         debugger.write("Training. \n\n\n");

//         if (debugMode) {
//             debugger.write("Weights before training. \n\n\n");
//             for (int i = levels; i >= 0; --i) {
//                 debugger.write(matrixToString(weights[i], rows[i], cols[i], String.format("Weights[%d]", i)));
//             }
//         }

//         for (int round = 0; round < batchSize; ++round) {
//             System.out.printf("Round: %d. \n", round);
//             debugger.write(String.format("Round: %d. \n", round));

//             // Construct answers array.
//             double ans = reader.getNextLabel();

//             // Change.
//             // ans = 1;

//             double[] answers = new double[rows[rows.length - 1]];
//             for (int i = 0; i < rows[rows.length - 1]; ++i) {
//                 if (i == ans) {
//                     answers[i] = 1;
//                 } else {
//                     answers[i] = 0;
//                 }
//             }

//             load(reader.getNextImage());
//             run();
//             double[] results = getResults();

//             if (debugMode) {
//                 debugger.write(matrixToString(activations[0], 28, 28, "Input Layer."));
//             }

//             if (debugMode) {
//                 debugger.write(matrixToString(results, rows[rows.length - 1], 1, "Results."));
//                 debugger.write(matrixToString(answers, rows[rows.length - 1], 1, "Answers."));

//                 debugger.write("Working on the last set of nudges. \n\n\n");
//             }

//             // Special treatment of the first set of nudges.
//             for (int r = 0; r < rows[levels]; ++r) {

//                 //System.out.printf("level: %d \nrow: %d \n", levels, r);
//                 double costDerivative = costDerivative(results[r], answers[r]);
//                 //System.out.printf("costDerivative: %f \n", costDerivative);

//                 for (int c = 0; c < cols[levels]; ++c) {
//                     double temp = precomputedSigDer(activations[levels + 1][r]);
//                     //System.out.printf("precomputedSigDer: %f \n", temp);
//                     nudges[levels][r * cols[levels] + c] += costDerivative * temp;
//                     //System.out.printf("level: %d \ncol: %d \n\n", levels, r, c);

//                     //System.out.printf("activations[i][c]: %f \ncostDer: %f \n=> nudges[i][r * cols[i] + c]: %f \n\n", activations[levels][c], costDerivative, nudges[levels][r * cols[levels] + c]);

//                 }
//             }

//             // Scuffed.
//             //print(nudges[levels], activations[levels + 1].length, activations[levels].length, "nudges[levels]");

//             if (debugMode) {
//                 debugger.write("Working on the rest of the nudges. \n\n\n");
//             }
            
//             // The rest of the nudges.
//             for (int i = levels - 1; i >= 0; --i) {
//                 if (debugMode) {
//                     debugger.write(String.format("Working on nudges[%d]. \n\n\n", i));
//                 }

                
//                 for (int r = 0; r < rows[i]; ++r) {

//                     for (int c = 0; c < cols[i]; ++c) {
                        
//                         for (int j = 0; j < rows[i + 1]; ++j) {

//                             //System.out.printf("level: %d \nrow: %d\ncol: %d \n j: %d \n\n", i, r, c, j);

//                             double temp = nudges[i + 1][j * cols[i + 1] + r];
//                             temp *= weights[i + 1][j * rows[i] + r];

//                             //print(activations[i + 1], rows[i + 1], 1, "activations of right");
//                             //System.out.println(i + 1);

//                             temp *= precomputedSigDer(activations[i + 1][r]);
//                             nudges[i][r * cols[i] + c] += temp;

//                             if (debugMode) {
//                                 debugger.write(String.format("nudges[%d][%d * %d + %d] += %f; Right side is (%f * %f * %f). Activation is: %f. \n", i, r, cols[i], c, temp, nudges[i + 1][j * cols[i + 1] + r], weights[i + 1][j * rows[i] + r], precomputedSigDer(activations[i + 1][r]), activations[i + 1][r]));
//                             }


//                         }


//                     }
//                 }

//                 // Scuffed.
//                 //print(nudges[i], activations[i + 1].length, activations[i].length, "nudges[i]");
//             }

//             // Tack on activations, stash the nudges, and reset nudges.
//             for (int i = levels; i >= 0; --i) {

//                 for (int r = 0; r < rows[i]; ++r) {

//                     for (int c = 0; c < cols[i]; ++c) {
                        
//                         nudges[i][r * cols[i] + c] *= activations[i][c];
//                         nudgeStash[i][r * cols[i] + c] += nudges[i][r * cols[i] + c];
//                         nudges[i][r * cols[i] + c] = 0;


//                     }
//                 }

//                 // Scuffed.
//                 //print(nudgeStash[i], activations[i + 1].length, activations[i].length, "nudgeStash[i]");
//             }
//         }
        
//         if (debugMode) {
//             debugger.write("End of Batch. \n\n\n");
//         }

//         // Apply nudges from the stash, and reset the nudgeStash.
//         for (int i = levels; i >= 0; --i) {

//             if (debugMode) {
//                 debugger.write(matrixToString(nudgeStash[i], rows[i], cols[i], String.format("NudgeStash[%d]", i)));
//             }

//             for (int r = 0; r < rows[i]; ++r) {

//                 for (int c = 0; c < cols[i]; ++c) {
                    
//                     //System.out.printf("Chainging weight by: %f \n", learningRate * nudgeStash[i][r * cols[i] + c] / batchSize);
//                     weights[i][r * cols[i] + c] -= learningRate * nudgeStash[i][r * cols[i] + c] / batchSize;

//                     nudgeStash[i][r * cols[i] + c] = 0;


//                 }

//             }

//             //print(weights[i], activations[i + 1].length, activations[i].length, "weights[i]");


//         }

//         if (debugMode) {
//             debugger.write("Weights after training. \n\n\n");
//             for (int i = levels; i >= 0; --i) {
//                 debugger.write(matrixToString(weights[i], rows[i], cols[i], String.format("Weights[%d]", i)));
//             }
//         }




//         // Run again with improved weights.

//         // System.out.println("IMPROVED RUN \n\n\n");

//         // load(reader.getNextImage());
//         // run();
//         // double[] results = getResults();
//         // double[] answers = new double[rows[rows.length - 1]];

//         // print(results, rows[rows.length - 1], 1, "results");
//         // print(answers, rows[rows.length - 1], 1, "answers");

        
        


        

//     }

//     public double test(MNISTReader reader, int batchSize) {
//         System.out.println("Testing.");

//         int correct = 0;

//         for (int batch = 0; batch < batchSize; ++batch) {

//             double ans = reader.getNextLabel();
//             double[] answers = new double[rows[rows.length - 1]];
//             for (int i = 0; i < rows[rows.length - 1]; ++i) {
//                 if (i == ans) {
//                     answers[i] = 1;
//                 } else {
//                     answers[i] = 0;
//                 }
//             }

//             load(reader.getNextImage());
//             run();

//             //print(weights[1], 16, 16, "weights");

//             double[] results = getResults();

//             System.out.printf("Correct answer: %f \n", ans);
//             debugger.write(matrixToString(answers, rows[rows.length - 1], 1, "Answers."));
//             debugger.write(matrixToString(results, rows[rows.length - 1], 1, "Results."));

//             // Find which index has highest activation.
//             int highestIndex = 0;
//             double highestActivation = 0;
//             for (int i = 0; i < rows[rows.length - 1]; ++i) {
//                 if (results[i] > highestActivation) {
//                     highestActivation = results[i];
//                     highestIndex = i;
//                 }
//             }

//             if (highestIndex == ans) {
//                 correct += 1;
//             }
//         }

//         return correct / batchSize;
//     }

//     public double cost(double actual, double anticipated) {
//         return Math.pow((actual - anticipated), 2);
//     }

//     public double costDerivative(double actual, double anticipated) {
//         return 2 * (actual - anticipated);
//     }

//     public double sigmoid(double x) {
//         return 1 / (1 + Math.pow(Math.E, -1 * x));
//     }

//     public double sigmoidDerivative(double x) {
//         return sigmoid(x) * (1 - sigmoid(x));
//     }

//     public double precomputedSigDer(double sig) {
//         return sig * (1 - sig);
//     }

    

//     public double[] getResults() {
//         return activations[activations.length - 1];
//     }

//     public String matrixToString(double[] matrix, int rows, int cols, String message) {
//         StringBuilder stringBuilder = new StringBuilder();

//         stringBuilder.append(String.format("=== %s === \n", message));
//         for (int i = 0; i < rows; ++i) {
//             for (int j = 0; j < cols; ++j) {
//                 stringBuilder.append(String.format("%-10.5f ", matrix[i * cols + j]));
//             }
//             stringBuilder.append("\n");
//         }
//         stringBuilder.append(String.format("=== end of %s === \n", message));

//         stringBuilder.append("\n\n");

//         return stringBuilder.toString();
//     }

// }