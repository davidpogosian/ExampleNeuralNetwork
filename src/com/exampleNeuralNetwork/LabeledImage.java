package com.exampleNeuralNetwork;

public class LabeledImage {
    Double[] image;
    int label;
    
    public LabeledImage(Double[] image, int label) {
        this.image = image;
        this.label = label;
    }

    public Double[] getImage() {
        return image;
    }

    public void setImage(Double[] image) {
        this.image = image;
    }

    public int getLabel() {
        return label;
    }
    
    public void setLabel(int label) {
        this.label = label;
    }
    
}
