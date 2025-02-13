import java.util.Arrays;
import java.io.File;


public class Main {
    public static void main(String[] args) {
        //Handle input layer BEFORE loop begins.
          //Handle data input...

        Layer[] layers = new Layer[3];

        System.out.println("--> Iteration: " + 0);
        double[] inputs = {1.0, 3.1, 1.3}; //Load data
        //Changing number of inputs causes error, no clue why :)

        layers[0] = new InputLayer(inputs);

        Layer.displayLayerOutput("  x Output Vector: ", layers[0].getOutputs());

        for(int i = 1; i < layers.length; i++) {
            System.out.println("--> Iteration: " + i);
            if(i != 0 && i != layers.length - 1){ //Handle hidden layer
                //Initialise the layer for this step of the loop...
                int neuronsForThisLayer = 3;
                //layers[i] = new Layer(newInputs, neuronsForThisLayer);
                layers[i] = new Layer(layers[i - 1].getOutputs(), neuronsForThisLayer);

                //Load current layer's weights...

                //Need to load as many arrays of weights as there are nodes in current layer.
                //Need as many weights in a weight array as there are nodes in previous layer.

                layers[i].beginComputation();
                Layer.displayLayerOutput("  x Output Vector: ", layers[i].getOutputs());

            }else if(i == layers.length - 1){//Handle output layer
                layers[layers.length - 1] = new Layer(layers[i - 1].getOutputs(), inputs.length);
                Layer outputLayerObj = layers[layers.length - 1];

                outputLayerObj.beginComputation();
                Layer.displayLayerOutput("  x Output Vector: ", outputLayerObj.getOutputs());
            }
        }
    }
}
