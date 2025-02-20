import java.io.File;
import java.util.ArrayList;
import java.io.FileWriter;

public class NetworkFileHandler {
    ArrayList<Request> fileQueue = new ArrayList<>();

    public static class Request {
        private String filePath;
        private double[] array1 = null;
        private double[][][] array2 = null;

        public Request(String filePath, double[] array){
            this.filePath = filePath;
            this.array1 = array;
        }

        public Request(String filePath, double[][][] array){
            this.filePath = filePath;
            this.array2 = array;
        }

        public double getElement(final int index){
            return array1[index];
        }

        public double getElement(final int depth, final int row, final int col){
            return array2[depth][row][col];
        }

        public double[] getArray1(){
            return array1;
        }

        public double[][] getArray2HeightXWidth(int index){
            return array2[index];
        }

        public double[][][] getArray2DepthXHeightXWidth(){
            return array2;
        }

        public String getFilePath(){
            return filePath;
        }
    }

    public void enqueue(Request request){
        int i = 0;
        boolean existsInQueue = false;

        while(i < fileQueue.size() && !existsInQueue){
            if(fileQueue.get(i).getFilePath().equals(request.getFilePath())){
                existsInQueue = true;
                fileQueue.set(i, request);
            }
        }
        if(!existsInQueue){
            fileQueue.add(request);
        }
    }

    public static File loadFile(final String filePath){
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

    public void processQueue(){
        for(int i = 0; i < fileQueue.size(); i++){
            try{
                Request requestToWrite = fileQueue.removeFirst();
                FileWriter fw = new FileWriter(requestToWrite.getFilePath());
                if(requestToWrite.getArray2DepthXHeightXWidth() == null){ //Write bias request

                    double[] arrayToWrite = requestToWrite.getArray1();
                    for(int row = 0; row < arrayToWrite.length; row++){
                        fw.write(arrayToWrite[row] + "\n");
                    }
                    fw.close();

                }else if(requestToWrite.getArray1() == null){//Write layer weight set request

                    double[][][] arrayToWrite = requestToWrite.getArray2DepthXHeightXWidth();
                    for(int depth = 0; depth < arrayToWrite.length; depth++){
                        for(int row = 0; row < arrayToWrite[depth].length; row++){
                            for(int col = 0; col < arrayToWrite[depth][row].length; col++){
                                fw.write(arrayToWrite[depth][row][col] + " ");
                            }
                            fw.write("\n");
                        }
                        fw.write("n\n");
                    }
                }
            }catch(Exception e){
                System.out.println("  ! ERROR: Fatal error occurred when attempting to process fileQueue");
                System.exit(1);

            }
        }
    }
}
