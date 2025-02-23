import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

class FCLayer {
    //Attributes:
    private static int layerCount = -1;
    private double[] inputs;
    private double[] outputs; //Need to know how many outputs (next layer size probably)
    private double[] weightedSums;
    private double[][] filters;
    private double[] biases;

    public FCLayer(final int filtersNo, final int inputsNo) throws IOException {
        layerCount++;

        this.biases = new double[filtersNo];
        initBiases("resources/FCLayerDependencies/biases_FCLayer" + layerCount);

        this.filters = new double[filtersNo][inputsNo];
        loadConnectionSet("resources/FCLayerDependencies/filters_FCLayer" + layerCount);
        this.outputs = new double[filtersNo];
        this.weightedSums = new double[filtersNo];
    }

    public FCLayer(double[] inputs){
        layerCount++;

        this.outputs = inputs;
        this.inputs = inputs;
    }

    private File loadFile(final String filePath){
        try{
            File file = new File(filePath);
            if(file.exists()){
                return file;
            }else{
                return null;
            }
        }catch(Exception e){
            System.out.println("  ! Fatal error when attempting to read " + filePath);
            System.exit(1);
        }
        return null;
    }

    public void initBiases(final String filePath) throws FileNotFoundException {
        File biasFile = loadFile(filePath);

        if(biasFile == null){
            for(int i = 0; i < this.biases.length; i++){
                this.biases[i] = 1;
            }
        }else if(biasFile != null){ // ??? <----------------------------------------------------------------------- !!!
            int i = 0;
            Scanner scanObj = new Scanner(biasFile);
            while(scanObj.hasNextLine() && i < this.biases.length){
                this.biases[i] = scanObj.nextDouble();
            }
        }
    }

    //REDO <------------------------------------------------------------------------------------------------------- !!!

    /*
     * Code is messy and difficult to understand. Try and catch, plus too many nests.
     */
    public void loadConnectionSet(final String filePath) throws IOException {
        File connectionSetFile = loadFile(filePath);

        if(connectionSetFile == null){
            System.out.println("        ! Lost connection set for " + layerCount + ", creating untrained connection set...");
            for(int row = 0; row < this.filters.length; row++){
                for(int col = 0; col < this.filters[row].length; col++){
                    this.filters[row][col] = 1;
                }
            }
        }else{
            try{
                int i = 0;
                double[][] connectionSetLoaded = new double[this.filters.length][this.filters[0].length];
                BufferedReader bufferedReader = new BufferedReader(new FileReader(connectionSetFile));
                String line;

                while((line = bufferedReader.readLine()) != null){
                    String[] values = line.split("\\s+");
                    double[] row = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                    connectionSetLoaded[i] = row;
                    i++;
                }

                bufferedReader.close();
                this.filters = connectionSetLoaded;

            }catch(Exception e){
                System.out.println("  ! Fatal error when attempting to read " + filePath);
                System.exit(1);
            }


        }
    }

    public void beginComputation(){
        System.out.println("  ? Beginning computation of " + layerCount);

        for(int row = 0; row < outputs.length; row++){
            double weightedSum = 0.0;
            for(int col = 0; col < outputs.length; col++){
                weightedSum += this.outputs[row] * this.filters[row][col];
            }
            this.weightedSums[row] = weightedSum + this.biases[row];
            this.outputs[row] = NetworkMathHandler.SIGMOID_Activation(weightedSums[row]);

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
        this.inputs[index] = val;
    }

    final double[][] getFilters(){
        return filters;
    }

    final double[] getFilter(final int index){
        return filters[index];
    }

    final void setFilter(final double[][] filters){
        this.filters = filters;
    }

    final void setFilter(final double[] filter, final int index){
        this.filters[index] = filter;
    }
}


