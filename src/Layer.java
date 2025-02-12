class Layer {
    //Attributes:
    private static int layerCount = 0;
    private double[] inputs;
    private double[] outputs; //Need to know how many outputs (next layer size probably)
    private Neuron[] neurons;
    private double[][] connections;

    boolean initComplete = false;

    //Constructors & Dependencies

    public Layer(final double[] inputs, final int neuronNo, final int outputsNo){
        this.inputs = inputs;
        this.neurons = new Neuron[neuronNo];
        initNeurons();
        this.connections = new double[inputs.length][neurons.length];
        this.outputs = new double[outputsNo];
        this.initComplete = true;
        layerCount++;
    }

    public Layer(final int neuronNo, final int outputsNo){
         this.neurons = new Neuron[neuronNo];
         initNeurons();
         this.outputs = new double[outputsNo];
         layerCount++;
    }

    private void initNeurons(){
        for(int i = 0; i < neurons.length; i++){
            neurons[i] = new Neuron();
        }
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
         this.inputs = inputs;
    }

    final double[] getOutputs(){
         return this.outputs;
    }

    final void setOutputs(final double[] outputs){
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

    final double getConnection(final int row, final int col){
         return this.connections[row][col];
    }

    final void setConnection(final int row, final int col, final double weight){
        this.connections[row][col] = weight;
    }

    //Class Methods (non-constructor & non-encapsulation):

    final public void beginComputation(){
         if(initComplete){
             System.out.println("  ! Layer " + layerCount + " has begun computing");
             for(int i = 0; i < outputs.length; i++){
                 outputs[i] = neurons[i].actv(connections, i, inputs);
             }
         }
    }

    final public void performInitialisation(){

         this.connections = new double[inputs.length][neurons.length];
         this.initComplete = true;
         System.out.println("    ! Connection matrix of " + inputs.length + " by " + neurons.length + " initialised");
    }

    final public void loadWeights(final int row, final double... arr){
         if(arr.length != neurons.length){
             throw new IllegalArgumentException("Weight array must be equal to the number of neurons in this layer");
         }else{
             for(int i = 0; i < neurons.length; i++){
                 connections[row][i] = arr[i];
             }
             System.out.println("    ! Weights loaded for row " + row  + " in layer " + layerCount);
         }
    }

}

class InputLayer extends Layer{
    public InputLayer(final int neuronNo, final int outputsNo){
        super(neuronNo, outputsNo);
    }


}
