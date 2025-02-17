import java.io.IOException;
import java.util.Arrays;

public class NeuralNetwork {
    private Layer[] layers;
    private boolean isDataLoaded = true;
    private Trainer networkTrainer;
    private int[] neuronsPerLayer;

    public NeuralNetwork(final int layerNo, final double[] inputs, final int... neuronsForLayers) throws IOException {
        if(neuronsForLayers[0] != neuronsForLayers[neuronsForLayers.length-1]){
            System.out.println("  ! No. Input neurons != No. of Output neurons, overriding user choice to make amount equivalent\n");
            System.out.println("      - Must have same amount of input and output neurons");
            neuronsForLayers[neuronsForLayers.length-1] = neuronsForLayers[0];
        }


        layers = new Layer[layerNo];
        layers[0] = new InputLayer(inputs);
        this.neuronsPerLayer = neuronsForLayers;

        for(int i = 1; i < layers.length; i++){
            if(i != layers.length - 1){ //Hidden layers
                layers[i] = new Layer(neuronsForLayers[i], layers[i - 1].getOutputsSize());
            }else{ //Output layer
                layers[i] = new Layer(neuronsForLayers[0], layers[i - 1].getOutputsSize());
            }
        }
    }

    public void run(){
        if(!isDataLoaded){
            System.out.println("    ! Cannot run network without data loaded");
        }else{
            //Run
            for(int i = 1; i < layers.length; i++) {
                System.out.println("\n--> ITERATION: " + i + "\n");
                layers[i].setInputs(layers[i - 1].getOutputs()); //Load outputs of previous layer as inputs of this
                layers[i].beginComputation();
            }
        }
    }

    public void displayLayer(final int index){
        if(index < 0 || index >= this.layers.length){
            throw new ArrayIndexOutOfBoundsException("No such layer at index " + index + " exists");
        }else{
            String layerTitle;
            if(index == this.layers.length - 1){
                layerTitle = "\nOUTPUT LAYER ";
            }else if(index == 0){
                layerTitle = "\nINPUT LAYER ";
            }else{
                layerTitle = "\nLAYER " + index + " ";
            }

            System.out.println(layerTitle + "DETAILS\nIO Vectors:");
            System.out.println(" -> Input Neuron values: " + Arrays.toString(layers[index].getInputs()));
            System.out.println(" -> Output Neuron values: " + Arrays.toString(layers[index].getOutputs()));
            System.out.println("\nNeuron Biases:");

            for(int i = 0; i < layers[index].getNeurons().length; i++){
                System.out.println("  |- Neuron " + (i+1) + ": " + layers[index].getNeurons()[i].bias);
            }

            System.out.println("\nLayer Weights Set(s):");

            for(int i = 0; i < layers[index].getLayerConnectionSet().length; i++){
                System.out.print("  |- Neuron " + i + " weight set: " + Arrays.toString(layers[index].getLayerConnectionSet()[i]) + "\n");
            }

        }
    }
}
