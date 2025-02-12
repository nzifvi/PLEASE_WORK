public class Main {
    public static void main(String[] args) {
        Layer[] layers = new Layer[3];
        layers[0] = new Layer(3, 3);
        layers[layers.length - 1] = new Layer(3, 3);


        for(int i = 0; i < layers.length; i++){
            if(i == 0){
                //Load data
                double[] outputs = {1, 1, 1};
                layers[0].setOutputs(outputs);
            }else if(i == layers.length - 1){
                layers[i].setOutputs(layers[i-1].getOutputs());
                for(int j = 0; j < 3; j++){
                    System.out.println(layers[j].getOutputs());
                }
            }else{

                layers[i] = new Layer(4, 3);
                layers[i].setInputs(layers[i-1].getOutputs());
                layers[i].performInitialisation();

                layers[i].loadWeights(0, 1,1,1,1);
                layers[i].loadWeights(1, 1,1,1,1);
                layers[i].loadWeights(2, 1,1,1,1);

                layers[i].beginComputation();

            }
        }
    }
}
