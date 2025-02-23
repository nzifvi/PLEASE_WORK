import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.io.File;


public class NetworkTrainer {
    private ArrayList<Sample> sampleList = new ArrayList<>();
    private int totalSamples;
    private int correctSamples;
    private int incorrectSamples;
    NetworkFileHandler fileHandler = new NetworkFileHandler();

    //CONTROL VALUES
    final int standardisedImageWidth = 500;
    final int standardisedImageHeight = 500;

    //REWRITE TO HANDLE READING INTO ONE 3D ARRAY CORRECTLY WHILST PRESERVING CONTROL INFO ON TOP LINE:
    public NetworkTrainer() throws FileNotFoundException {
        File trainingControlData = NetworkFileHandler.loadFile("resources/TrainingSet/TrainingControlData");
        int trainingSetSize = 0;
        if(trainingControlData == null){
            System.out.println("  ! ERROR: TrainingControlData file not found. TrainingControlData file must be manually recreated");
            System.exit(1);
        }else{
            Scanner scannerObj = new Scanner(trainingControlData);
            trainingSetSize = scannerObj.nextInt();
            scannerObj.close();
        }

        for(int i = 0; i < trainingSetSize; i++){
            File trainingElement = NetworkFileHandler.loadFile("resources/TrainingSet/TrainingElement" + i);
            int elementNo = 0;
            boolean isCorrect = false;
            double[][] rArray = new double[standardisedImageHeight][standardisedImageWidth];
            double[][] gArray = new double[standardisedImageHeight][standardisedImageWidth];
            double[][] bArray = new double[standardisedImageHeight][standardisedImageWidth];

            if(trainingElement != null){
                try{
                    int redRow = 0;
                    int blueRow = 0;
                    int greenRow = 0;
                    int nCount = 0;
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(trainingElement));
                    String line;

                    while((line = bufferedReader.readLine()) != null){
                        String[] values = line.split("\\s+");
                        if(redRow == 0){
                            int[] controlInfo = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
                            elementNo = controlInfo[0];
                            if(controlInfo[1] == 1){
                                isCorrect = true;
                            }else{
                                isCorrect = false;
                            }
                            redRow++;
                        }else{
                            if(Objects.equals(values[0], "n")){
                                nCount++;
                            }else{
                                double[] matrixRow = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                                if(nCount == 0){ //Load as rArray
                                    rArray[redRow] = matrixRow;
                                    redRow++;
                                }else if(nCount == 1){
                                    gArray[greenRow] = matrixRow;
                                    greenRow++;
                                }else{
                                    bArray[blueRow] = matrixRow;
                                    blueRow++;
                                }
                            }
                        }
                    }
                    double[][][] tempArr = new double[3][standardisedImageHeight][standardisedImageWidth];
                    tempArr[0] = rArray;
                    tempArr[1] = gArray;
                    tempArr[2] = bArray;
                    sampleList.add(new Sample(elementNo, isCorrect, tempArr));

                }catch(Exception e){
                    System.out.println("  ! ERROR: Failure attempting to read trainingElement");
                    System.out.print(e);
                    System.exit(1);
                }
            }
        }
    }


    public void shuffleTrainingSet(final int shuffleTimes){
        Random randomiser = new Random();
        //Recommended: shuffle 3 * 4 (12) times

        for(int i = 0; i < shuffleTimes; i++){
            for(int j = 0; j < totalSamples; j++){
                int oldIndex = randomiser.nextInt(totalSamples);
                int newIndex = randomiser.nextInt(totalSamples);

                //boolean temp = sampleArray[oldIndex].getIsCorrectSample();
                //sampleArray[oldIndex].setIsCorrectSample(sampleArray[newIndex].getIsCorrectSample());
                //sampleArray[newIndex].setIsCorrectSample(temp);
            }
        }
    }

    public double[][][] getSample(final int pos){
        return sampleList.get(pos).getSampleArray();
    }

    public int getSampleSize(){
        return sampleList.size();
    }

}

class Sample{
    private int testNo;
    private boolean isCorrectSample;

    private double[][][] array;

    public Sample(final int testNo, final boolean isCorrectSample, final double[][][] array){
        //Update once not testing
        this.testNo = testNo;
        this.isCorrectSample = isCorrectSample;
        this.array = array;

    }

    public int getTestNo(){
        return this.testNo;
    }

    public void setTestValue(final int testNo){
        this.testNo = testNo;
    }

    public boolean getIsCorrectSample(){
        return isCorrectSample;
    }

    public void setIsCorrectSample(final boolean isCorrectSample){
        this.isCorrectSample = isCorrectSample;
    }

    public double[][][] getSampleArray(){
        return this.array;
    }

    public static void displayArray(double[][] array){
        System.out.println(Arrays.deepToString(array));
    }
}

