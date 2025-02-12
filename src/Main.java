import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Layer[] layers = new Layer[100];
        layers[0] = new InputLayer(3, 3);
        layers[layers.length - 1] = new Layer(3, 3);


        for(int i = 0; i < layers.length; i++){
            System.out.println("Iteration: " + i);
            if(i == 0){ //Handle input layer

                double[] inputs = {1, 1, 1}; //Load data
                layers[0].setOutputs(inputs); //Set output array to be data, allowing it to be treated as inputs by next layer
                displayLayerOutput(layers[i]);

            }else if(i == layers.length - 1){ //Handle output layer

                layers[i].setOutputs(layers[i-1].getOutputs());
                displayLayerOutput(layers[i]);
            }else{

                layers[i] = new Layer(4, 3);
                layers[i].setInputs(layers[i-1].getOutputs());
                layers[i].performInitialisation();

                layers[i].loadWeights(0, 1,1,1,1);
                layers[i].loadWeights(1, 1,1,1,1);
                layers[i].loadWeights(2, 1,1,1,1);

                layers[i].beginComputation();
                displayLayerOutput(layers[i]);
            }
        }
    }

    static void displayLayerOutput(Layer layer){
        System.out.println("Output Vector: " + Arrays.toString(layer.getOutputs()) + "\n");
    }
}
