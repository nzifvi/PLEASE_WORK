import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Layer{

    //SETTINGS
    final int noOfLayerDependencies = 4;

    //CLASS ATTRIBUTES
    private static int layerNum = -1;
    private int layerType;
    private int sublayerType;

    //Convolution layerConvolutor = new Convolution();
    Pool layerPool = new Pool();

    double[][][] inputActivationMatrix = null;
    double[][][] outputActivationMatrix = null;

    double[][][] filters = null;
    double[] biases = null;

    public Layer(final int layerType, final int sublayerType){
        layerNum++;

        this.layerType = layerType;
        this.sublayerType = sublayerType;

        if(layerType > 0){ //0 means input, 1 means hidden, 2 means output
            initDependencies();
        }
    }

    private void initDependencies(){
        int[] controlValues = new int[noOfLayerDependencies];
        File dependenciesFile = NetworkFileHandler.loadFile("resources/LayerDependencies/LayerControls/Controls_Layer" + layerNum);
        if(dependenciesFile == null){
            System.out.println("  ! FATAL ERROR: Layer Control info from resources/LayerDependencies/LayerControls/Controls_Layer " + layerNum + " cannot be located.");
            System.exit(1);
        }else{
            try{
                Scanner scanner = new Scanner(dependenciesFile);
                while(scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    controlValues[layerNum] = Integer.parseInt(line);
                }
                scanner.close();
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }

        int filterHeight = controlValues[0];
        int filterWidth = controlValues[1];
        int filterDepth = controlValues[2];
        int biasDepth = controlValues[3];

        try{
            initFilter(filterHeight, filterWidth, filterDepth);
            initBiases(biasDepth);
        }catch(Exception e){
            System.out.println("  ! FATAL ERROR: Error occurred during initialisation of Layer " + layerNum + " dependencies");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initFilter(final int filterHeight, final int filterWidth, final int filterDepth) throws IOException {
        this.filters = new double[filterDepth][filterHeight][filterWidth];

        File file = NetworkFileHandler.loadFile("resources/LayerDependencies/LayerFilters/Filters_Layer" + layerNum);
        if(file ==null){
            System.out.println("    ! Filter for Layer " + layerNum + " presumed lost, constructing untrained filter to replace...");
            for(int depth = 0; depth < filters.length; depth++){
                for(int row = 0; row < filters[depth].length; row++){
                    for(int col = 0; col < filters[depth].length; col++){
                        this.filters[depth][row][col] = 1;
                    }
                }
            }
        }else{
            int nCount = 0;
            int row = 0;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while(line = bufferedReader.readLine() != null && nCount < filterDepth){
                if(line.equals("n")){
                    nCount++;
                }else{
                    String[] values = line.split("\\s+");
                    double[] matrixRow = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                    this.filters[nCount][row] = matrixRow;
                    row++;
                }
            }
        }
    }

    private void initBiases(final int biasDepth) throws FileNotFoundException {
        this.biases = new double[biasDepth];
        File file = NetworkFileHandler.loadFile("resources/LayerDependencies/LayerBiases/Biases_Layer" + layerNum);
        if(file ==null){
            System.out.println("    ! Biases for Layer " + layerNum + " presumed lost, constructing unadjusted biases to replace...");
            for(int row = 0; row < biases.length; row++){
                this.biases[row] = 0;
            }
        }else{
            Scanner scanner = new Scanner(file);
            int i = 0;
            while(scanner.hasNextLine() && i < biasDepth){
                String line = scanner.nextLine();
                this.biases[i] = Double.parseDouble(line);
                i++;
            }
            scanner.close();
        }
    }

    //UPDATE TO USE CONVOLUTION AND POOL -------------------------------------------------------------------------------
    public void beginComputation(){
        System.out.println("  ? Layer " + layerNum + " beginning computation");
        //for(int i = 0; i < outputs.length; i++){
            //double activation = neurons[i].actv(connections, i, inputs);
            //outputs[i] = activation;
            //System.out.println("    |- Neuron " + i + " fired. Value = " + activation);
        //}
        //System.out.println("  ! Layer " + layerCount + " computation completed");
    }


    public NetworkFileHandler.Request updateLayerBiases(final double[][][] newFilters){
        boolean updated = false;
        int depth = 0;
        while(depth < this.filters.length && !updated){
            int row = 0;
            while(row < this.filters[depth].length && !updated){
                int col = 0;
                while(col < this.filters[depth].length && !updated){
                    if(this.filters[depth][row][col] != newFilters[depth][row][col]){
                        this.filters = newFilters;
                        updated = true;
                    }
                    col++;
                }
                row++;
            }
            depth++;
        }
        if(updated){
            return new NetworkFileHandler.Request("resources/LayerDependencies/LayerFilters/Filters_Layer" + layerNum, newFilters);
        }else{
            return null;
        }
    }

    public NetworkFileHandler.Request updateBiases(final double[] newBiases){
        boolean updated = false;
        int i = 0;
        while(i < this.biases.length && !updated){
            if(this.biases[i] != newBiases[i]){
                this.biases = newBiases;
                updated = true;
            }
            i++;
        }

        if(updated){
            return new NetworkFileHandler.Request("resources/WeightsAndConnections/connections_Layer" + layerNum, newBiases);
        }else{
            return null;
        }
    }

    public void setInputActivationMatrix(final double[][][] inputActivationMatrix){
        this.inputActivationMatrix = inputActivationMatrix;
    }

    public double[][][] getInputActivationMatrix(){
        return inputActivationMatrix;
    }

    public void setOutputActivationMatrix(final double[][][] outputActivationMatrix){
        this.outputActivationMatrix = outputActivationMatrix;
    }

    public double[][][] getOutputActivationMatrix(){
        return outputActivationMatrix;
    }

    public void setFilters(final double[][][] filters){
        this.filters = filters;
    }

    public double[][][] getFilters(){
        return filters;
    }

    public void setBiases(final double[] biases){
        this.biases = biases;
    }

    public double[] getBiases(){
        return biases;
    }
}

//NEED TO TEST ---------------------------------------------------------------------------------------------------------
class Convolution{
    double[][] outputActivationMatrix;

    public Convolution(final int outputDepth, final int outputHeight, final int outputWidth){
        this.outputActivationMatrix = new double[outputHeight][outputWidth];
    }

    public void performConvolution(double[][] filter, double bias){
        for(int stepNo = 0; stepNo < filter.length; stepNo++){
            double[][] spatialSegment = getSpatialSegment(filter, stepNo);
            for(int row = 0; row < spatialSegment.length; row++){
                for(int col = 0; col < spatialSegment[row].length; col++){
                    outputActivationMatrix[row][col] = spatialSegment[row][col] * filter[row][col] + bias;
                }
            }
        }
        applyActivationFunction();
    }

    public double[][] getOutputActivationMatrix(){
        return outputActivationMatrix;
    }

    private double[][] getSpatialSegment(double[][] filter, int topLeft){
        double[][] spatialSegment = new double[5][5];
        for(int row = topLeft; row < topLeft + filter.length; row++){
            for(int col = 0; col < filter[row].length; col++){
                spatialSegment[row][col] = filter[row][col];
            }
        }
        return spatialSegment;
    }

    private void applyActivationFunction(){
        for(int row = 0; row < outputActivationMatrix.length; row++){
            for(int col = 0; col < outputActivationMatrix[row].length; col++){
                outputActivationMatrix[row][col] = NetworkMathHandler.TANH_Activation(outputActivationMatrix[row][col]);
            }
        }
    }

}

//NEED TO IMPLEMENT ----------------------------------------------------------------------------------------------------
class Pool{
    public double[][][] poolActivationMatrix(double[][][] activationMatrix){
        return null;
    }
}