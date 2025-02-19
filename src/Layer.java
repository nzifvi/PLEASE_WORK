import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

class Layer {
    //Attributes:
    private static int layerCount = -1;
    private double[] inputs;
    private double[] outputs; //Need to know how many outputs (next layer size probably)
    private Neuron[] neurons;
    private double[][] connections;

    public Layer(final int neuronNo, final int inputsNo) {
        layerCount++;

        this.neurons = new Neuron[neuronNo];
        initNeurons();

        this.connections = new double[neuronNo][inputsNo];
        loadConnectionSet("resources/WeightsAndBiases/connections_Layer" + layerCount);
        this.outputs = new double[neuronNo];
    }

    public Layer(double[] inputs){
        layerCount++;

        this.outputs = inputs;
        this.inputs = inputs;
    }

    public double[] loadBiases(final String filePath) throws FileNotFoundException {
        File biasFile = NetworkFileHandler.loadFile(filePath);

        double[] biases = new double[neurons.length];

        if(biasFile == null){
            System.out.println("        ! Lost bias set for layer " + layerCount + " creating unadjusted bias set...");
            for(int i = 0; i < neurons.length; i++){
                biases[i] = 0;
            }
        }else if(biasFile != null){ // ??? <----------------------------------------------------------------------- !!!
            int i = 0;
            Scanner scanObj = new Scanner(biasFile);
            while(scanObj.hasNextLine() && i < neurons.length){
                biases[i] = scanObj.nextDouble();
            }
        }

        return biases;
    }

    public void loadConnectionSet(final String filePath) {
        File connectionSetFile = NetworkFileHandler.loadFile(filePath);

        if(connectionSetFile == null){
            System.out.println("        ! Lost connection set for layer " + layerCount + ", creating untrained connection set...");
            for(int row = 0; row < this.connections.length; row++){
                for(int col = 0; col < this.connections[row].length; col++){
                    this.connections[row][col] = 1;
                }
            }
        }else{
            try{
                int i = 0;
                double[][] connectionSetLoaded = new double[this.connections.length][this.connections[0].length];
                BufferedReader bufferedReader = new BufferedReader(new FileReader(connectionSetFile));
                String line;

                while((line = bufferedReader.readLine()) != null){
                    String[] values = line.split("\\s+");
                    double[] row = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                    connectionSetLoaded[i] = row;
                    i++;
                }

                bufferedReader.close();
                this.connections = connectionSetLoaded;

            }catch(Exception e){
                System.out.println("  ! Fatal error when attempting to read " + filePath);
                System.exit(1);
            }


        }
    }

    private void initNeurons(){
        try{
            double[] biases = loadBiases("resources/WeightsAndBiases/biases_Layer" + layerCount);
            for(int i = 0; i < neurons.length; i++){
                neurons[i] = new Neuron(biases[i]);
            }
        }catch(Exception e){
            System.out.println("  ! Fatal error attempting to read biases_Layer" + layerCount );
        }
    }

    public void beginComputation(){
        System.out.println("  ? Layer " + layerCount + " beginning computation");
        for(int i = 0; i < outputs.length; i++){
            double activation = neurons[i].actv(connections, i, inputs);
            outputs[i] = activation;
            System.out.println("    |- Neuron " + i + " fired. Value = " + activation);
        }
        System.out.println("  ! Layer " + layerCount + " computation completed");
    }

    public NetworkFileHandler.Request updateLayerBiases(final double[] newBiases){
        boolean updated = false;
        for(int i = 0; i < neurons.length; i++){
            if(neurons[i].getBias() != newBiases[i]){
                neurons[i].setBias(newBiases[i]);
                updated = true;
            }
        }

        if(updated){
            return new NetworkFileHandler.Request("resources/WeightsAndBiases/biases_Layer" + layerCount, newBiases);
        }else{
            return null;
        }
    }

    public NetworkFileHandler.Request updateLayerConnections(final double[][] newConnections){
        boolean updated = false;
        for(int row = 0; row < connections.length; row++){
            for(int col = 0; col < connections[row].length; col++){
                if(connections[row][col] != newConnections[row][col]){
                    connections[row][col] = newConnections[row][col];
                    updated = true;
                }
            }
        }

        if(updated){
            return new NetworkFileHandler.Request("resources/WeightsAndConnections/connections_Layer" + layerCount, newConnections);
        }else{
            return null;
        }
    }

    /*
    |------------------------------------------| vv GET AND SET METHODS vv |------------------------------------------|
    */


    final double[] getInputs(){
        return inputs;
    }

    final int getInputsSize(){
        return inputs.length;
    }

    final void setInputs(final double[] inputs){
        this.inputs = inputs;
    }

    final double[] getOutputs(){
        return outputs;
    }

    final int getOutputsSize(){
        return outputs.length;
    }

    final void setOutputs(final double[] outputs){
        this.outputs = outputs;
    }

    final double getInput(final int index){
        return this.inputs[index];
    }

    final void setInput(final int index, final double val){
        this.inputs[index] = val;
    }

    final Neuron[] getNeurons(){
        return neurons;
    }

    final Neuron getNeuron(final int index){
        return neurons[index];
    }

    final void setNeurons(final Neuron[] neurons){
        this.neurons = neurons;
    }

    final void setNeuron(final int index, final Neuron neuron){
        this.neurons[index] = neuron;
    }

    final double[][] getLayerConnectionSet(){
        return connections;
    }

    final double[] getIncomingNeuronConnections(final int index){
        return connections[index];
    }

    final void setConnectionSet(final double[][] connectionSet){
        this.connections = connectionSet;
    }

    final void setIncomingNeuronConnections(final int row, final double[] incomingNeuronConnections){
        this.connections[row] = incomingNeuronConnections;
    }

}

class InputLayer extends Layer{
    final int inputNeuronNo;
    public InputLayer(final double[] inputs){
        super(inputs);
        this.inputNeuronNo = inputs.length;
        System.out.println("      & Input Layer inputs loaded as outputs");
    }

    public int getInputNeuronNo(){
        return this.inputNeuronNo;
    }

}
