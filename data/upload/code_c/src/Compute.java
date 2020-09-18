import java.util.Random;
import java.util.Scanner;
public class Compute{
	public static void output(String fileName, int op){
        /*
        Data is prepared by outputting the final result. 
        */
        FileUtility words = new FileUtility(fileName);
		words.write(Integer.toString(op));
        words.reset();
       
    }
	public static void process(String fileName){
		FileUtility words = new FileUtility(fileName);
		int size = words.size();
		System.out.println("File size: " + size);
		for (int i = 0; i < size; i++){
			int res = 0;
			String line = words.read();	
			String[] nos = line.split(" ");
			//System.out.println("Row size: " + nos.length);
			for (String no: nos){
				res += Integer.parseInt(no);
			}
			output("../data/input2.txt", res);
		}
		words.reset();
		
	}
	public static void main(String[] args){
		process("../data/input1.txt");
	
	}
}