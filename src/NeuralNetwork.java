import java.io.IOException;
import java.util.Arrays;

public class NeuralNetwork {
    private Layer[] layers;
    private boolean isDataLoaded = true;
    private NetworkTrainer trainer = new NetworkTrainer();
    NetworkFileHandler networkFileHandler = new NetworkFileHandler();

    public NeuralNetwork(int layerAmount) throws IOException {

        layers = new Layer[layerAmount];
        //layers[0] = new InputLayer(inputs);
        //this.neuronsPerLayer = neuronsForLayers;
        for(int i = 0; i < layers.length; i++){
            if(i == 0){
                layers[i] = new Layer(0, 0);
            }else if(i == layers.length-1){
                layers[i] = new Layer(2, 2);
            }else{
                layers[i] = new Layer(1, 1);
            }
        }
    }

    public void run(){
        /* OUTDATED
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
        performBackPropagation();

         */

        if(!isDataLoaded){
            System.out.println("    ! Cannot run network without data loaded");
        }else{
            for(int i = 1; i < layers.length; i++){
                layers[i].setInputActivationMatrix(layers[i - 1].getOutputActivationMatrix());
            }
        }
    }

    private void performBackPropagation(){
        for(int i = 1; i < layers.length; i++){
            NetworkFileHandler.Request req;

            //double[] neuronBiases = new double[layers[i].getNeurons().length];
            //for(int j = 0; j < neuronsPerLayer[i]; j++){
            //   neuronBiases[j] = layers[i].getNeurons()[j].getBias();
            //}
            //req = layers[i].updateLayerBiases(neuronBiases);
            //if(req != null){
            //    networkFileHandler.enqueue(req);
            //}
            //req = layers[i].updateLayerConnections(layers[i].getLayerConnectionSet());
            //if(req != null){
            //    networkFileHandler.enqueue(req);
            //}
        }

        networkFileHandler.processQueue();
    }

    //NEEDS UPDATING
    /*
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
     */
}
