package com.exampleNeuralNetwork;

import java.io.EOFException;

public interface Data<T> {
    
    T getNextExample() throws EOFException;
    void shuffle();

}
