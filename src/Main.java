import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Layer[] layers = new Layer[3];

        System.out.println("--> Iteration: " + 0);
        double[] inputs = {1.0, 1.0}; //Load data

        layers[0] = new InputLayer(inputs);
        layers[layers.length - 1] = new OutputLayer();

        displayLayerOutput(layers[0]);

        for(int i = 1; i < layers.length; i++) {
            System.out.println("--> Iteration: " + i);
            if(i != 0 && i != layers.length - 1){ //Handle hidden layer
                double[] newInputs;
                if(i == 1){
                    InputLayer inputLayerObj = (InputLayer) layers[0];
                    newInputs = inputLayerObj.getOutputs();

                }else{
                    Layer layerObj = (Layer) layers[layers.length - 1];
                    newInputs = layerObj.getOutputs();

                }

                //Initialise the layer for this step of the loop...
                int neuronsForThisLayer = 3;
                layers[i] = new Layer(newInputs, neuronsForThisLayer);

                //Load current layer's weights...
                layers[i].loadWeights(0, 1.0, 1.0, 1.0);
                layers[i].loadWeights(1, 1.0, 1.0, 1.0);
                layers[i].loadWeights(2, 1.0, 1.0, 1.0);

                //Need to load as many arrays of weights as there are nodes in current layer.
                //Need as many weights in a weight array as there are nodes in previous layer.

                layers[i].beginComputation();
                displayLayerOutput(layers[i]);

            }else if(i == layers.length - 1){//Handle output layer
                OutputLayer outputLayerObj = (OutputLayer) layers[layers.length - 1];
                //Load previous layer's outputs as output layer's inputs
                outputLayerObj.setInputs(layers[layers.length - 1].getOutputs());

                //Load weights for output layer...

                outputLayerObj.loadWeights(0, 1.0, 1.0, 1.0);
                outputLayerObj.loadWeights(1, 1.0, 1.0, 1.0);

                outputLayerObj.beginComputation();
                displayLayerOutput(outputLayerObj);
            }
        }
    }

    static void displayLayerOutput(Layer layer){
        System.out.println("  x Output Vector: " + Arrays.toString(layer.getOutputs()) + "\n");
    }

    static void displayLayerOutput(OutputLayer layer){
        System.out.println("  x Output Vector: " + Arrays.toString(layer.getOutputs()) + "\n");
    }
}
