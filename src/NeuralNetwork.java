import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class NeuralNetwork {
    private CNNLayer[] CNNlayers;
    private FCLayer[] FCLayers;
    private boolean isDataLoaded = true;
    NetworkFileHandler networkFileHandler = new NetworkFileHandler();

    public NeuralNetwork(final int CNNLayerAmount, final int FCLayerAmount, final String targetInput) throws IOException {
        double[][][] targetInputArray = NetworkFileHandler.loadInput(
                NetworkFileHandler.loadFile("resources/Inputs/" + targetInput),
                3,
                20,
                20
        );

        CNNlayers = new CNNLayer[CNNLayerAmount];
        for(int i = 0; i < CNNlayers.length; i++){
            if(i == 0){ //Input layer

                CNNlayers[i] = new CNNLayer();
                CNNlayers[i].setInputActivationMatrix(targetInputArray); //Load input into first layer

            }else if(i > 0 && i < CNNlayers.length - 1){ //HIDDEN LAYER
                CNNlayers[i] = new CNNLayer();
            }else if(i == CNNlayers.length - 1){ //FLATTEN AND CNN OUTPUT LAYER
                CNNlayers[i] = new Flatten(
                        CNNlayers[i - 1].getOutputActivationDepth(),
                        CNNlayers[i - 1].getOutputActivationHeight(0),
                        CNNlayers[i - 1].getOutputActivationWidth(0, 0)
                        );
            }
        }
        FCLayers = new FCLayer[FCLayerAmount];
    }

    private void loadFCLayers() throws IOException {
        for(int i = 0; i < FCLayers.length; i++){
            if(i == 0){
                Flatten flatten = (Flatten) CNNlayers[CNNlayers.length - 1];
                FCLayers[i] = new FCLayer(200, flatten.getFlattenedOutputActivationMatrix().length);
            }else{
                if(i == FCLayers.length - 1){
                    FCLayers[i] = new FCLayer(1, FCLayers[i - 1].getOutputsSize());
                }else{
                    FCLayers[i] = new FCLayer(200, FCLayers[i - 1].getOutputsSize());
                }
            }
        }
    }

    public void run(){
        if(!isDataLoaded){
            System.out.println("    ! Cannot run network without data loaded");
        }else{
            int i = 0;
            boolean hasToTerminate = false;
            while(i < CNNlayers.length && !hasToTerminate){
                if(i > 0){
                    CNNlayers[i].setInputActivationMatrix(CNNlayers[i - 1].getOutputActivationMatrix());
                }
                hasToTerminate = CNNlayers[i].beginComputation(i);
                i++;
            }
            performBackPropagation();
        }
    }

    public void train() throws IOException {
        NetworkTrainer trainer = new NetworkTrainer();
        trainer.shuffleTrainingSet(12);

        for(int i = 0; i < trainer.getSampleSize(); i++){
            CNNlayers[0].setInputActivationMatrix(trainer.getSample(i));

            int j = 0;
            boolean hasToTerminate = false;
            while(j < CNNlayers.length && !hasToTerminate){ //CNN LAYERS
                if(j < CNNlayers.length - 1){
                    if(j > 0){
                        CNNlayers[i].setInputActivationMatrix(CNNlayers[j - 1].getOutputActivationMatrix());
                    }
                    hasToTerminate = CNNlayers[i].beginComputation(j);
                }else{
                    Flatten flattenLayer = (Flatten) CNNlayers[i];
                    flattenLayer.performFlatten(CNNlayers[i - 1].getOutputActivationMatrix());
                }

                //Before next layer begins computation, randomise half of current layer's outputActivationMatrix to be = to 0 (disable neuron).
                //What do I do with the true and false value of the training samples. How can they be used to validate or invalidate conclusion of CNN.

                j++;
            }
            loadFCLayers();
            for(int x = 0; x < FCLayers.length; x++){
                if(x == 0){
                    Flatten flatten = (Flatten) CNNlayers[CNNlayers.length - 1];
                    FCLayers[x].setInputs(flatten.getFlattenedOutputActivationMatrix());
                }else{
                    FCLayers[x].setInputs(FCLayers[x - 1].getOutputs());
                }
                FCLayers[x].beginComputation();
            }
            performBackPropagation();
        }
    }

    public void CCNbackPropagation(double[] dLdO, CNNLayer[] CNNLayer){
        double dOdz;
        double dzdw;
        double dLdw;

        for(int depth = 0 ; i < CNNlayers.length; depth++ ){
            double[] dLdX = new double[CNNlayers[i].getInputActivationDepth()];
            for(int row = 0; row < CNNlayers[x].getInputActivationHeight(0); row++){

                double dLdXSUM = 0;

                for(int y = 0; y < output.length; y++){
                    dOdz = NetworkMathHandler.ReLUDerivative(CNNLayer.output[x][y]);
                    dzdw = CNNLayer.input[x][y];

                    dLdw = dLdO[y] * dOdz * dzdw;

                    CNNLayer.filters[x][y] -= dLdw * learningRate;

                    dLdXSUM += dLdO[y] * dOdz * CNNLayer.filters[x][y];
                }
                dLdX[x] = dLdXSUM;
            }
        }
    }

    private void performBackPropagation(){
        System.out.println("        |- Performing Back propagation for Layers");
        /*
        * ONCE BACK PROP SET UP, REPLACE INPUTS OF UPDATES WITH THE CALCULATED ARRAYS. CURRENT INPUTS ARE TO ALLOW
        * COMPILATION FOR TESTING
        */
        for(int i = 0; i < CNNlayers.length; i++){
            NetworkFileHandler.Request req;

            req = CNNlayers[i].updateLayerFilters(this.CNNlayers[i].getFilters());
            if(req != null){
                networkFileHandler.enqueue(req);
            }
            req = CNNlayers[i].updateLayerBiases(this.CNNlayers[i].getBiases());
            if(req != null){
                networkFileHandler.enqueue(req);
            }
        }

        networkFileHandler.processQueue();
    }

    public void displayLayer(final int index){
        if(index < 0 || index >= this.CNNlayers.length){
            throw new ArrayIndexOutOfBoundsException("No such layer at index " + index + " exists");
        }else{
            String layerTitle;
            if(index == 0){
                layerTitle = "INPUT LAYER";
            }else if(index == this.CNNlayers.length - 1){
                layerTitle = "OUTPUT LAYER";
            }else{
                layerTitle = "LAYER " + index;
            }

            System.out.println("\n" + layerTitle + " DETAILS:");
            System.out.println("  BIASES:");
            System.out.println("  |- -> Biases Depth: " + this.CNNlayers[index].getBiasesSize());

            System.out.println("\n  FILTERS:");
            System.out.println("  |- -> Number of Filters: " + this.CNNlayers[index].getFiltersSize());
            System.out.println("  |- -> Filters Depth: " + this.CNNlayers[index].getFiltersDepthSize(0));
            System.out.println("  |- -> Filters Height: " + this.CNNlayers[index].getFiltersHeightSize(0, 0));
            System.out.println("  |- -> Filters Width: " + this.CNNlayers[index].getFiltersWidthSize(0,0,0));

            System.out.println("\n  INPUT & OUTPUT:");
            System.out.println("  |- -> Input Depth = " + this.CNNlayers[index].getInputActivationDepth() + ", Output Depth = " + this.CNNlayers[index].getOutputActivationDepth());
            System.out.println("  |- -> Input Height = " + this.CNNlayers[index].getInputActivationHeight(0) + ", Output Height = " + this.CNNlayers[index].getOutputActivationHeight(0));
            System.out.println("  |- -> Input Width = " + this.CNNlayers[index].getInputActivationWidth(0, 0) + ", Output Width = " + this.CNNlayers[index].getOutputActivationWidth(0, 0));
            System.out.println("LAYER FILTERS: ");
            for(int depth = 0; depth < this.CNNlayers[index].getFiltersSize(); depth++){
                System.out.println(Arrays.deepToString(this.CNNlayers[index].getFilters(depth)));
            }
            System.out.println("LAYER BIASES: ");
            System.out.println(Arrays.toString(this.CNNlayers[index].getBiases()));

        }
    }
}
