package com.exampleNeuralNetwork;

public class LabeledImage {
    Double[] image;
    Double label;
    
    public LabeledImage(Double[] image, Double label) {
        this.image = image;
        this.label = label;
    }

    public Double[] getImage() {
        return image;
    }

    public void setImage(Double[] image) {
        this.image = image;
    }

    public Double getLabel() {
        return label;
    }
    
    public void setLabel(Double label) {
        this.label = label;
    }
    
}
