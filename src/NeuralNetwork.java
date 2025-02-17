public class NeuralNetwork {
    private Layer[] layers;
    boolean isDataLoaded = false;
    Trainer networkTrainer;

    public NeuralNetwork(final int layerNo){
        layers = new Layer[layerNo];
        networkTrainer = new Trainer();
        //Setting up layers...#
        // -> layers[0] always input layer


    }

    public void run(){
        if(!isDataLoaded){
            System.out.println("    ! Cannot run network without data loaded");
        }else{
            //Run
        }
    }


}
