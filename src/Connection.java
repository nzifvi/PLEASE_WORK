import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Connection {
    double weight;

    //Constructors:

    public Connection(){

    }

    public Connection(final double weight){
        this.weight = weight;
    }

    //Encapsulation Methods:
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

public Layer(final double[] outputs){
    layerCount++;

    this.outputs = outputs;
    displayLayerOutput("  x Input Vector: ", this.inputs);
}

public Layer(final double[] inputs, final int neuronNo){
    layerCount++;

    this.inputs = inputs;
    displayLayerOutput("  x Input Vector: ", this.inputs);
    this.neurons = new Neuron[neuronNo];
    initNeurons();
    this.connections = new double[neuronNo][inputs.length];
    initConnections();
    this.outputs = new double[neuronNo];
}

public Layer(final int neuronNo){
    layerCount++;

    this.neurons = new Neuron[neuronNo];
    initNeurons();
    initConnections();
    this.outputs = new double[neuronNo];
}

private File loadFile(final String filePath){
    File file = new File(filePath);
    if(filePath.exists()){
        return file;
    }else{
        return null;
    }
}

public double[] loadBiases(final String filePath){
    File biasFile = loadFile(filePath);

    double[] biases = new double[neurons.length];

    if(biasFile != null){
        for(int i = 0; i < neurons.length; i++){
            biases[i] = 1;
        }
    }else if(biasFile == null){
        int i = 0;
        Scanner scanObj = new Scanner(biasFile);
        while(scanObj.hasNextLine() && i < neurons.length){
            biases[i] = scanObj.nextDouble();
        }
    }

    return biases;
}

public double[][] loadConnectionSet(final String filePath){
    File connectionSetFile = loadFile(filePath);

    if(connectionSetFile != null){
        for(int row = 0; row < this.connections.length; row++){
            for(int col = 0; col < this.connections[row].length; col++){
                this.connections[row][col] = 1;
            }
        }
    }else{
        double[][] connectionSetLoaded = new double[this.connections.length][this.connections[0].length];
        int i = 0;
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
    }
}

private void initNeurons(){
    double[] biases = loadBiases("resources/biases_Layer" + layerCount);
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
    this.input[index] = val;
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