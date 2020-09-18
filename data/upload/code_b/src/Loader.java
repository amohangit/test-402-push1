import java.util.Random;
import java.util.Scanner;
public class Loader{
	public static void load(String fileName, int max){
        /*
        Data is prepared by inserting random values 
        */
        FileUtility words = new FileUtility(fileName);
		Random rand = new Random(); 
        for (int i = 0; i < max; i++){
        	 String nos = "";
        	 for (int j = 0; j < 25; j++){
        	 	nos += rand.nextInt(max) + " ";
        	 }
             words.write(nos);
        }
        words.reset();
        System.out.println("#completed loading file");
    }
	public static void main(String[] args){
		load("../data/input1.txt", 10000);
	}
}