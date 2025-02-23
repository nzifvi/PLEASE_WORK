import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) throws IOException {
        NeuralNetwork neuralNetwork1 = new NeuralNetwork(6, 4 , "Input1");
        //Use 13, 12th must be Flatten
        //CNN Layer amount = 13,
        //FC Layer amount = 4?

        neuralNetwork1.run();

        neuralNetwork1.displayLayer(3);

        //NetworkTrainer trainer = new NetworkTrainer();
        //Sample req = trainer.getSample(0);
        //Sample.displayArray(req.getArray("r"));
    }
}
