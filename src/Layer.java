import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

class Layer {
    //Attributes:
    private static int layerCount = -1;
    private double[] inputs;
    private double[] outputs; //Need to know how many outputs (next layer size probably)
    private Neuron[] neurons;
    private double[][] connections;

    boolean initComplete = false;

    //Constructors & Dependencies

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
        this.outputs = new double[neuronNo];
        this.initComplete = true;
    }

    public Layer(final int neuronNo){
        layerCount++;

         this.neurons = new Neuron[neuronNo];
         initNeurons();
         System.out.println("      & A layer's initNeurons was called");
         this.outputs = new double[neuronNo];
    }

    private void initNeurons() {
        try{
            double[] biases = loadBiasesFromFile("resources/biases_Layer" + layerCount);

            for(int i = 0; i < neurons.length; i++){
                neurons[i] = new Neuron();
                if(biases[i] == 0){ //Assume either new neuron or a failure to load has occurred:
                    neurons[i].setBias(1);
                }else{ //Load saved bias:
                    neurons[i].setBias(biases[i]);
                }
            }
        }catch(Exception e){
            System.out.println("! Fatal error occurred when attempting to read biases_Layer" + layerCount);
            System.exit(1);
        }
    }

    private void initConnections() throws IOException {
        double[][] weightsForThisLayer = new double[neurons.length][inputs.length];
        int i = 0;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(loadFile("resources/connections_Layer" + layerCount)));
        String line;
        while((line = bufferedReader.readLine()) != null){
            String[] values = line.split("\\s+");
            double[] row = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
            weightsForThisLayer[i] = row;
            i++;
        }

        bufferedReader.close();
        this.connections = weightsForThisLayer;
    }

    //Encapsulation Methods

    /*
    final public void setConnectionWeight(final int row, final int col, final double newWeight){
        this.connections[row][col].setWeight(newWeight);
    }

    final public double getConnectionWeight(final int row, final int col){
         return this.connections[row][col].getWeight();
    }

     */

    final double[] getInputs(){
         return this.inputs;
    }

    final void setInputs(final double[] inputs){
        System.out.println("      & layer" + layerCount + " setInputs called, inputs have been loaded");
         this.inputs = inputs;
         displayLayerOutput("  x Input Vector: ", this.inputs);
    }

    final double[] getOutputs(){
         return this.outputs;
    }

    final void setOutputs(final double[] outputs){
        System.out.println("      & layer" + layerCount + " setOutputs called, outputs have been loaded");
         this.outputs = outputs;
    }

    final double getOutput(final int index){
        return this.outputs[index];
    }

    final void setOutput(final int index, final double value){
        this.outputs[index] = value;
    }

    final int getOutputSize(){
        return this.outputs.length;
    }

    final Neuron getNeuron(final int pos){
         return this.neurons[pos];
    }

    final void setNeuron(final int pos, final Neuron neuron){
        this.neurons[pos] = neuron;
    }

    final void setNeurons(final int amount){
        this.neurons = new Neuron[amount];
        initNeurons();
    }

    final double getConnection(final int row, final int col){
         return this.connections[row][col];
    }

    final void setConnection(final int row, final int col, final double weight){
        this.connections[row][col] = weight;
    }

    //Class Methods (non-constructor & non-encapsulation):

    public void beginComputation(){
        System.out.println("\n  & layer" + layerCount + " beginComputation called");
         if(initComplete){
             System.out.println("    ! Layer " + layerCount + " has begun computing");
             for(int i = 0; i < outputs.length; i++){
                 double activation = neurons[i].actv(connections, i, inputs);
                 System.out.println("          & Neuron " + i + " actv has returned " + activation);
                 outputs[i] = activation;
             }
         }
    }

    final public void performInitialisation(){

         this.connections = new double[inputs.length][neurons.length];
         this.initComplete = true;
         System.out.println("    ! Connection matrix of " + inputs.length + " by " + neurons.length + " initialised");
    }

    final public void loadWeights(final int row, final double... arr){
        System.out.println("\n      & layer" + layerCount + " loadWeights called");
        for(int i = 0; i < inputs.length; i++){
            connections[row][i] = arr[i];
        }
        System.out.println("    ! Weights loaded for row " + row  + " in layer " + layerCount);
        displayLayerOutput("        x Weight loaded: ", this.connections[row]);
    }

    public static void displayLayerOutput(String statement, double[] arr){
        System.out.println(statement + Arrays.toString(arr));
    }

    private File loadFile(final String fileName) throws IOException {
        File file = new File(fileName);
        if(file.exists()){
            return file;
        }else {
            System.out.println("! Cannot locate file " + fileName + ", creating new file");
            file.createNewFile();
            return file;
        }
    }

    public double[] loadBiasesFromFile(final String fileName) throws IOException {
        File file = loadFile(fileName);
        double[] biasesForThisLayer = new double[neurons.length];
        int i = 0;
        Scanner scanObj = new Scanner(file);

        while(scanObj.hasNextLine() && i < neurons.length){
            biasesForThisLayer[i] = Double.parseDouble(scanObj.nextLine());
            i++;
        }

        return biasesForThisLayer;
    }

    public void writeToFile(final String fileName, final double[] arrayToWrite) throws IOException{
        //IMPLEMENT
    }

    public void writeToFile(final String fileName, final double[][] arrayToWrite) throws IOException{
        //IMPLEMENT
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

class OutputLayer extends Layer{
    double[] inputs;
    double[][] connections;

    public OutputLayer(){
        super(0);
    }

    public OutputLayer(final int neuronNo, final double[] inputs, final double[][] connections){
        super(neuronNo);
        this.inputs = inputs;
        this.connections = connections;
    }

    @Override
    public void beginComputation(){
        System.out.println("      & Output Layer beginComputation called");
        for(int row = 0; row < this.getOutputSize(); row++){
            double activation = getNeuron(row).actv(connections, row, inputs);
            System.out.println("          & Neuron " + row + " actv has returned " + activation);
            this.setOutput(row, activation);
        }
    }

    public void test(){

    }
}
