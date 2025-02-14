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

    public Layer(final double[] inputs, final int neuronNo) throws IOException {
        layerCount++;

        this.inputs = inputs;
        this.neurons = new Neuron[neuronNo];
        initNeurons();
        this.connections = new double[neuronNo][inputs.length];
        loadConnectionSet("resources/connections_Layer" + layerCount);
        this.outputs = new double[neuronNo];
    }

    public Layer(final int neuronNo) throws IOException {
        layerCount++;

        this.neurons = new Neuron[neuronNo];
        initNeurons();
        loadConnectionSet("resources/connections_Layer" + layerCount);
        this.outputs = new double[neuronNo];
    }

    public Layer(double[] inputs){
        layerCount++;
        this.outputs = inputs;
    }

    private File loadFile(final String filePath){
        try{
            File file = new File(filePath);
            if(file.exists()){
                return file;
            }else{
                return null;
            }
        }catch(Exception e){
            System.out.println("  ! Fatal error when attempting to read " + filePath);
            System.exit(1);
        }
        return null;
    }

    public double[] loadBiases(final String filePath) throws FileNotFoundException {
        File biasFile = loadFile(filePath);

        double[] biases = new double[neurons.length];

        if(biasFile == null){
            for(int i = 0; i < neurons.length; i++){
                biases[i] = 1;
            }
        }else if(biasFile != null){
            int i = 0;
            Scanner scanObj = new Scanner(biasFile);
            while(scanObj.hasNextLine() && i < neurons.length){
                biases[i] = scanObj.nextDouble();
            }
        }

        return biases;
    }

    public void loadConnectionSet(final String filePath) throws IOException {
        File connectionSetFile = loadFile(filePath);

        if(connectionSetFile == null){
            for(int row = 0; row < this.connections.length; row++){
                for(int col = 0; col < this.connections[row].length; col++){
                    this.connections[row][col] = 1;
                }
            }
        }else{
            double[][] connectionSetLoaded = new double[this.connections.length][this.connections[0].length];
            int i = 0;
            BufferedReader bufferedReader = null;

            try{
                bufferedReader = new BufferedReader(new FileReader(connectionSetFile));
            }catch(Exception e){
                System.out.println("  ! Fatal error when attempting to read " + connectionSetFile);
                System.exit(1);
            }
            String line;

            while((line = bufferedReader.readLine()) != null){
                String[] values = line.split("\\s+");
                double[] row = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                connectionSetLoaded[i] = row;
                i++;
            }

            bufferedReader.close();
            this.connections = connectionSetLoaded;
        }
    }

    private void initNeurons(){
        double[] biases = null;
        try{
            biases = loadBiases("resources/biases_Layer" + layerCount);
        }catch(Exception e){
            System.out.println("  ! Fatal error attempting to read biases_Layer" + layerCount );
        }
        for(int i = 0; i < neurons.length; i++){
            neurons[i] = new Neuron(biases[i]);
        }
    }




    public void beginComputation(){
        for(int i = 0; i < outputs.length; i++){
            double activation = neurons[i].actv(connections, i, inputs);
            outputs[i] = activation;
        }
    }




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
