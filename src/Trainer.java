import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.Random;
import java.io.File;

public class Trainer {
    private Sample[] sampleArray;
    private int totalSamples;
    private int correctSamples;
    private int incorrectSamples;

    public Trainer(){
        sampleArray = new Sample[10];
        this.totalSamples = sampleArray.length;
        this.correctSamples = 0;
        boolean sampleValueTest = false;
        boolean validityValue = true;
        for(int i = 0; i < sampleArray.length; i++){
            sampleArray[i] = new Sample(sampleValueTest, validityValue );
            sampleValueTest = !sampleValueTest;
            if(i % 2 == 0){
                validityValue = !validityValue;
            }
        }
        for(int i = 0; i < totalSamples; i++){
            if(sampleArray[i].getIsCorrectSample()){
                correctSamples++;
            }
        }
        incorrectSamples = totalSamples - correctSamples;
    }

    public void displayArray(){
        System.out.print("\n\nSample Value: ");
        for(int i = 0; i < this.sampleArray.length; i++){
            System.out.print(this.sampleArray[i].getTestValue() + ", ");
        }
        System.out.print("\n isCorrect value: ");
        for(int i = 0; i < this.sampleArray.length; i++){
            System.out.print(this.sampleArray[i].getIsCorrectSample() + ", ");
        }
    }

    public void shuffleTrainingSet(final int shuffleTimes){
        Random randomiser = new Random();
        //Recommended: shuffle 3 * 4 (12) times

        for(int i = 0; i < shuffleTimes; i++){
            for(int j = 0; j < totalSamples; j++){
                int oldIndex = randomiser.nextInt(totalSamples);
                int newIndex = randomiser.nextInt(totalSamples);

                boolean temp = sampleArray[oldIndex].getIsCorrectSample();
                sampleArray[oldIndex].setIsCorrectSample(sampleArray[newIndex].getIsCorrectSample());
                sampleArray[newIndex].setIsCorrectSample(temp);
            }
        }
    }

    private File loadFile(final String filePath) throws NoSuchFileException {
        File file = new File(filePath);
        if(file.exists()){
            return file;
        }else{
            throw new NoSuchFileException("Cannot locate dependency " + filePath);
        }
    }

    private File[] loadFiles(final String filePath) throws NoSuchFileException {
        File file = new File(filePath);
        if(file.exists()){
            return file.listFiles();
        }else{
            throw new NoSuchFileException("Cannot locate dependency " + filePath);
        }
    }

    public void loadTrainingSet(){
        File[] trainingSets;
        try{
            trainingSets = loadFiles("put it here later");
        }catch(Exception e){
            System.out.println("  ! Error: cannot locate a file that the program depends on");
            System.exit(1);
        }

        double[][] rgbArray = new double[0][0]; //Determine standardised image size
        //Do rest with openCV
    }

}

class Sample{
    private boolean testValue;
    private boolean isCorrectSample;

    private double rArray;
    private double gArray;
    private double bArray;

    public Sample(final boolean testValue, final boolean isCorrectSample){
        //Update once not testing
        this.testValue = testValue;
        this.isCorrectSample = isCorrectSample;
    }

    public boolean getTestValue(){
        return testValue;
    }

    public void setTestValue(final boolean testValue){
        this.testValue = testValue;
    }

    public boolean getIsCorrectSample(){
        return isCorrectSample;
    }

    public void setIsCorrectSample(final boolean isCorrectSample){
        this.isCorrectSample = isCorrectSample;
    }
}
