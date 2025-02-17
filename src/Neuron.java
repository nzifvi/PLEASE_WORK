public class Neuron {
    //Attributes:
    NetworkMathHandler actvObj = new NetworkMathHandler();
    double bias;

    //Constructors:

    public Neuron(){
        this.bias = 1;
    }

    public Neuron(double bias) {
        this.bias = bias;
    }

    //Encapsulation Methods:

    public double getBias(){
        return this.bias;
    }

    public void setBias(final double bias){
        this.bias = bias;
    }

    //Class Methods (non-constructor & non-encapsulation):

    final double actv(final double[][] connections, final int row, final double[] inputs){
        double sum = 0;
        for(int i = 0; i < inputs.length; i++){
            sum += connections[row][i] * inputs[i];
        }
        return NetworkMathHandler.TANH_Activation(sum);
    }

}
