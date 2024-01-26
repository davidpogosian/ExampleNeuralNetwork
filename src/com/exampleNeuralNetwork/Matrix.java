package com.exampleNeuralNetwork;

public class Matrix {

    int rows;
    int cols;
    Double[] data;

    public Matrix() {

    }
    
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new Double[rows * cols];
        
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                data[i * cols + j] = 0.0;
            }
        }
    }

    public double get(int row, int col) {
        return data[row * cols + col];
    }

    public void set(int row, int col, double value) {
        data[row * cols + col] = value;
    }

    public void forEach(Action action) {

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                data[i * cols + j] = action.apply(data[i * cols + j]);
            }
        }
    }

    public Matrix columnVector(Double[] arr) {
        rows = arr.length;
        cols = 1;
        data = new Double[rows];

        for (int i = 0; i < rows; ++i) {
            data[i] = arr[i];
        }

        return this;
    }

    public void add(Matrix B) {

        if ((this.rows != B.rows) || (this.cols != B.cols)) {
            throw new IllegalArgumentException("Matrix dimensions are incompatible for hadamard product.");
        }

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                data[i * cols + j] += B.data[i * B.cols + j] ;
            }
        }

    }

    public void subtract(Matrix B) {

        if ((this.rows != B.rows) || (this.cols != B.cols)) {
            throw new IllegalArgumentException("Matrix dimensions are incompatible for hadamard product.");
        }

        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                this.data[i * cols + j] -= B.data[i * B.cols + j] ;
            }
        }

    }

    public void reset() {
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                data[i * cols + j] = 0.0;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }



    public static Matrix scalar(double scalar, Matrix A) {

        Matrix scaledMatrix = new Matrix(A.rows, A.cols);

        for (int i = 0; i < A.rows; ++i) {
            for (int j = 0; j < A.cols; ++j) {
                scaledMatrix.data[i * scaledMatrix.cols + j] = A.data[i * A.cols + j] * scalar;
            }
        }

        return scaledMatrix;

    }

    public static Matrix add(Matrix A, Matrix B) {

        if ((A.rows != B.rows) || (A.cols != B.cols)) {
            throw new IllegalArgumentException("Matrix dimensions are incompatible for hadamard product.");
        }

        Matrix sum = new Matrix(A.rows, A.cols);

        for (int i = 0; i < sum.rows; ++i) {
            for (int j = 0; j < sum.cols; ++j) {
                sum.data[i * sum.cols + j] = A.data[i * A.cols + j] + B.data[i * B.cols + j] ;
            }
        }
        
        return sum;
        
    }

    public static Matrix subtract(Matrix A, Matrix B) {
        if ((A.rows != B.rows) || (A.cols != B.cols)) {
            throw new IllegalArgumentException("Matrix dimensions are incompatible for hadamard product.");
        }

        Matrix difference = new Matrix(A.rows, A.cols);

        for (int i = 0; i < difference.rows; ++i) {
            for (int j = 0; j < difference.cols; ++j) {
                difference.data[i * difference.cols + j] = A.data[i * A.cols + j] - B.data[i * B.cols + j];
            }
        }
        
        return difference;
    }

    public static Matrix dot(Matrix A, Matrix B) {
        if (A.cols != B.rows) {
            throw new IllegalArgumentException("Matrix dimensions are incompatible for dot product.");
        }

        Matrix product = new Matrix(A.rows, B.cols);

        for (int i = 0; i < product.rows; ++i) {
            for (int j = 0; j < product.cols; ++j) {
                for (int k = 0; k < A.cols; ++k) {
                    product.data[i * product.cols + j] += A.data[i * A.cols + k] * B.data[k * B.cols + j];
                }
            }
        }

        return product;
    }

    public static Matrix hadamard(Matrix A, Matrix B) {

        if ((A.rows != B.rows) || (A.cols != B.cols)) {
            throw new IllegalArgumentException("Matrix dimensions are incompatible for hadamard product.");
        }

        Matrix product = new Matrix(A.rows, A.cols);

        for (int i = 0; i < product.rows; ++i) {
            for (int j = 0; j < product.cols; ++j) {
                product.data[i * product.cols + j] = A.data[i * A.cols + j] * B.data[i * B.cols + j];
            }
        }

        return product;
    }

    public static Matrix transpose(Matrix M) {

        Matrix transpose = new Matrix(M.cols, M.rows);

        for (int i = 0; i < transpose.rows; ++i) {
            for (int j = 0; j < transpose.cols; ++j) {
                transpose.data[i * transpose.cols + j] = M.data[j * M.cols + i];
            }
        }

        return transpose;
    }

    public static Matrix vectorized(Matrix M, Action action) {

        Matrix newM = new Matrix(M.rows, M.cols);

        for (int i = 0; i < newM.rows; ++i) {
            for (int j = 0; j < newM.cols; ++j) {
                newM.data[i * newM.cols + j] = action.apply(M.data[i * M.cols + j]);
            }
        }

        return newM;
    }


    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                stringBuilder.append(String.format("%-10.5f ", data[i * cols + j]));
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append("\n");

        return stringBuilder.toString();
    }  

}
