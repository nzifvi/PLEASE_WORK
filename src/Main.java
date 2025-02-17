import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        NeuralNetwork neuralNetwork1 = new NeuralNetwork(3, new double[]{1.0, 1.0}, new int[]{2, 3, 2});

        neuralNetwork1.run();

        neuralNetwork1.displayLayer(2);
    }
}
