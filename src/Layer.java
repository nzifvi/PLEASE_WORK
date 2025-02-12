class Layer {
    //Attributes:
    static int layerCount = 0;
    double[] inputs;
    double[] outputs; //Need to know how many outputs (next layer size probably)
    Neuron[] neurons;
    Connection[][] connections;

    boolean initComplete = false;

    //Constructors

     public Layer(final double[] inputs, final int neuronNo, final int outputsNo){
        this.inputs = inputs;
        this.neurons = new Neuron[neuronNo];
        this.connections = new Connection[inputs.length][neurons.length];
        this.outputs = new double[outputsNo];
        this.initComplete = true;
        layerCount++;
    }

    public Layer(final int neuronNo, final int outputsNo){
         this.neurons = new Neuron[neuronNo];
         this.outputs = new double[outputsNo];
         System.out.println("""
                 --> Must initialise the following attributes, using appropriate set methods...
                   1) Input array.
                 
                 Once set, call performInitialisation()"""
         );
         layerCount++;
    }

    //Encapsulation Methods

    final public void setConnectionWeight(final int row, final int col, final double newWeight){
        this.connections[row][col].setWeight(newWeight);
    }

    final public double getConnectionWeight(final int row, final int col){
         return this.connections[row][col].getWeight();
    }

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

    final Neuron getNeuron(final int pos){
         return this.neurons[pos];
    }

    final void setNeuron(final int pos, final Neuron neuron){
        this.neurons[pos] = neuron;
    }

    final Connection getConnection(final int pos){
         return this.connections[pos][0];
    }

    final void setConnection(final int row, final int col, final Connection connection){
        this.connections[row][col] = connection;
    }

    //Class Methods (non-constructor & non-encapsulation):

    final public void beginComputation(){
         if(initComplete){
             System.out.println("! Layer " + layerCount + " has begun computing");
             for(int i = 0; i < outputs.length; i++){
                 outputs[i] = neurons[i].actv(connections, i, inputs);
             }
         }
    }

    final public void performInitialisation(){

         this.connections = new Connection[inputs.length][neurons.length];
         System.out.println("    ! Initialisation completed, neural network can now be used");
         this.initComplete = true;
    }

    final public void loadWeights(final int row, final double... arr){
         if(arr.length != neurons.length){
             throw new IllegalArgumentException("Weight array must be equal to the number of neurons in this layer");
         }else{
             for(int i = 0; i < neurons.length; i++){
                 connections[row][i].setWeight(arr[i]);
             }
         }
    }

}
