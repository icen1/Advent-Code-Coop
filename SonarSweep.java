import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SonarSweep {
    public static void main(String[] args) {
        System.out.println("Gamma Read: " + mostCommonBitWithTempList('1','0'));
        System.out.println("Epsilon Read: " + mostCommonBitWithTempList('0','1'));
        System.out.println("Product is: " + binaryToDecimalConverter(mostCommonBitWithTempList('1','0'))
                                             * binaryToDecimalConverter((mostCommonBitWithTempList('0','1'))));

    }

    private static int depthDelta(List<Integer> depthList) {
        int incCount = 0;

        for(int i = 1 ; i < depthList.size(); i++) {
            if((depthList.get(i - 1) - depthList.get(i)) < 0) {
                incCount++;
            }
        }

        return incCount;
    }

    private static List<Integer> depthTriSum(List<Integer> depthList) {
        int sum = 0;
        List<Integer> triDepthSum = new ArrayList<Integer>();

        for(int i = 0; i < depthList.size(); i++) {
            for(int j = i; j <= i + 3; j++) {
                sum += j;
            }
            
            triDepthSum.add(sum);
        }
        return triDepthSum;
    }

    //CommandList for the list which has the Commands of UP,DOWN,Forward, commandMagnitude for respective mags
    private static int locationSum(List<String> commandList, List<Integer> commandMagnitude) {
        int forwardMagSum = 0; //Total sum for the magnitudes following the command Forward
        int aim = 0;           //Total sum for the magnitudes following the command UP,DOWN
        int productDepHor;     //Product of Multiplying the depth difference (up-down) by Forward's magnitude
        int totalDepth = 0;    //Difference of Depth

        for(int i = 0; i < commandList.size(); i++) {
            if(commandList.get(i).equalsIgnoreCase("forward")) {
                forwardMagSum += commandMagnitude.get(i);
                totalDepth += aim*commandMagnitude.get(i);
            } else if(commandList.get(i).equalsIgnoreCase("up")) {
                aim -= commandMagnitude.get(i);
            } else if(commandList.get(i).equalsIgnoreCase("down")){
                aim += commandMagnitude.get(i);
            } else {
                System.out.println("Unidentified Command");
            }
        }
        productDepHor = forwardMagSum * totalDepth;   //Multiplies the difference of up&down by horizontal magnitude

        return productDepHor;
    }

    /**
     * This method will return the number of depths increments
     * @param depthList is the all the Integers imported from the file as a list
     * @return the number of depths increments
     */
    private static int threeMeasurmentSlidingWindow(List<Integer> depthList) {
        int firstWindow = -1;
        int secondWindow = -1;
        int numberOfIncreases = 0;
        for(int i = 0; i < depthList.size()-3; i++) {
            firstWindow = depthList.get(i) + depthList.get(i + 1) + depthList.get(i + 2);
            secondWindow = depthList.get(i + 1) + depthList.get(i + 2) + depthList.get(i + 3);
            if(secondWindow>firstWindow) {numberOfIncreases++;}
        }
        return numberOfIncreases;
    }

    private static String mostCommonBit(List<String> binStrList) { 
        int count1 = 0;
        int count0 = 0;
        char[] commonBits = new char[12];

        for(int i = 0; i < 12; i++) {
            count1 = 0;
            count0 = 0;

            for(int j = 0; j < binStrList.size(); j++) {
                if(binStrList.get(j).charAt(i) == '1') {
                    count1++;
                } else if(binStrList.get(j).charAt(i) == '0') {
                    count0++;
                }
            }

            if(count1 > count0) {
                commonBits[i] = '1';
            } else {
                commonBits[i] = '0';
            }
        }

        return new String(commonBits);
    } 

    private static List<String> tempList = new ArrayList<String>(dayThreefileReader());
    private static String mostCommonBitWithTempList(char bitToKeep, char bitTodelete) { 
        int count1 = 0;
        int count0 = 0;

        for(int i = 0; i < 12; i++) {
            count1 = 0;
            count0 = 0;

            for(int j = 0; j < tempList.size(); j++) {
                if(tempList.get(j).charAt(i) == bitToKeep) {
                    count1++;
                } else {
                    count0++;
                }
            }

            if(count1 < count0) {
                for(int z = 0; z < tempList.size(); z++) {
                    if(tempList.get(z).charAt(i) == bitToKeep) {
                        String c = tempList.get(z);
                        tempList.remove(z);
                        z--;
                    }
                }
            } else {
                for(int z = 0; z < tempList.size(); z++) {
                    if(tempList.get(z).charAt(i) == bitTodelete) {
                        String c = tempList.get(z);
                        tempList.remove(z);
                        z--;
                    }
                }
            }
        }
        System.out.println("Temp List for: " + bitToKeep + "LIST: " +  tempList);
        return tempList.get(0);
    } 
/*      
                        ###############################
                        ####  File Reader/Utility  ####
                        ###############################
*/
    
    private static String oneComplement(String bitStr) {
        char[] bitStrComp = new char[12];

        for(int i = 0; i < bitStr.length(); i++) {
            if(bitStr.charAt(i) == '1') {
                bitStrComp[i] = '0';
            } else {
                bitStrComp[i] = '1';
            }
        }

        return new String(bitStrComp);
    }

    /**
     * Converts binary numbers to a decimal int value
     * @param binaryNumber is the binary number to be converted
     * @return the decimal value of the binary number
     */
    private static int binaryToDecimalConverter(String binaryNumber) {
        int decimal = 0;
        int power = 0;
        for(int i = binaryNumber.length() - 1; i >= 0; i--) {
            if(binaryNumber.charAt(i) == '1') {
                decimal += Math.pow(2, power);
            }
            power++;
        }
        return decimal;
    }
    
    /**
     * Reads the Dephths file and returns the contents as list of Integers.
     * @return a list of Integers that contains the depths inside the given
     *         file.
     */
    private static List<Integer> dayOnefileReader() {
        
        // Declares a list of integers to store the depths.
        List<Integer> sonarDepths = new ArrayList<Integer>();
        
        // Declares a scanner to read the file.
        File file = new File("Depths1.txt");
        
        // Declares a buffered reader to read the file.
        BufferedReader reader = null;

        // Try block to read the file.
        try {
            
            // Creates a buffered reader to read the file.
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            
            // While loop to read the file and parses it to an Integer.
            while ((text = reader.readLine()) != null) {
                sonarDepths.add(Integer.parseInt(text));
            }
        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        
        } catch (IOException e) {
            e.printStackTrace();
        
        // Catch block to close the file.
        } finally {
            
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sonarDepths;
    }

    private static List<String> commandForMovments = new ArrayList<String>();
    private static List<Integer> commandOfMovments = new ArrayList<Integer>();

    /**
     * Reads the Dephths file and returns the contents as list of Integers.
     * @return a list of Integers that contains the depths inside the given
     *         file.
     */
    private static void dayTwofileReader() {
        
        // Declares a scanner to read the file.
        File file = new File("Depths1.txt");
        
        // Declares a buffered reader to read the file.
        BufferedReader reader = null;

        // Try block to read the file.
        try {
            
            // Creates a buffered reader to read the file.
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            
            // While loop to read the file and parses it to an Integer.
            while ((text = reader.readLine()) != null) {
                String[] arrOfStr = text.split(" ", 2);
                commandForMovments.add(arrOfStr[0]);
                commandOfMovments.add(Integer.parseInt(arrOfStr[1]));
            }
        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        
        } catch (IOException e) {
            e.printStackTrace();
        
        // Catch block to close the file.
        } finally {
            
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads the Dephths file and returns the contents as list of strings.
     * @return a list of Integers that contains the depths inside the given
     *         file.
     */
    private static List<String> dayThreefileReader() {
        
        // Declares a list of integers to store the depths.
        List<String> diganosticBinaryNumbers = new ArrayList<String>();

        // Declares a scanner to read the file.
        File file = new File("Depths.txt");
        
        // Declares a buffered reader to read the file.
        BufferedReader reader = null;

        // Try block to read the file.
        try {
            
            // Creates a buffered reader to read the file.
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            
            // While loop to read the file and parses it to an Integer.
            while ((text = reader.readLine()) != null) {
                diganosticBinaryNumbers.add(text);
            }
        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        
        } catch (IOException e) {
            e.printStackTrace();
        
        // Catch block to close the file.
        } finally {
            
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return diganosticBinaryNumbers;
    }
}