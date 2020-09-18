import java.util.Random;
import java.util.Scanner;
public class Result{
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
		int res = 0;
		for (int i = 0; i < size; i++){
			String line = words.read();	
			String[] nos = line.split(" ");
			//System.out.println("Row size: " + nos.length);
			for (String no: nos){
				res += Integer.parseInt(no);
			}
			
		}
		output("../data/output.txt", res);
		words.reset();

		
	}
	public static void main(String[] args){
		process("../data/input2.txt");
	
	}
}