/*
A program written by Professor Mohan to help facilitate file
operations. 
This program needs no modification. 
*/
import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Arrays;
public class FileUtility {
	private Path path;
	private int index = 0;
	// If you would like direct access to this list of lines, you can get it by changing this variable to public. Then you can call .lines on any instance of FileUtility and use regular list methods like get(int index)
	private List<String> lines;
	/**
	* Creates a FileUtility object that uses the specified filename
	*
	* Note: make sure the file that you are using has a newline at the very end
	*/
	public FileUtility(String filename) {
		try {
			path = Paths.get(filename);
			if(Files.notExists(path))
				Files.createFile(path);
			lines = Files.readAllLines(path);
		} catch(IOException ex) {
			throw new Error("Could not read " + path + ": " + ex);
		}
	}
	/**
	* Returns a single line from the file, sequentially (like the Scanner)
	*/
	public String read() {
		//check if out of bounds, throw error if so
		if(index >= size()) throw new Error("read past the end of the file, reset before this happens with reset()!");
		return lines.get(index++);
	}
	/**
	* Resets line counter so that read() starts from the beginning of the file again
	*/
	public void reset() {
		index = 0;
		//refresh file just in case of outside changes
		try {
			lines = Files.readAllLines(path);
		} catch(IOException ex) {
			throw new Error("Could not read " + path + ": " + ex);
		}
	}
	
	/**
	* Appends the given line to the end of the file
	*/
	public void write(String line) {
		try {
			//append string to file
			Files.write(path, Arrays.asList(line), Charset.forName("UTF-8"), StandardOpenOption.APPEND);
			//re-read file to ensure it's up to date (could have added more than one line)
			lines = Files.readAllLines(path);
		} catch(IOException ex) {
			throw new Error("Could not access " + path + ": " + ex);
		}
	}
	
	/**
	* Replace the text at the given lineNumber (which starts at 0 for the first line) with the given String
	*/
	public void update(int lineNumber, String line) {
		try {
			//replace line
			lines.set(lineNumber, line);
			//write new file
			Files.write(path, lines, Charset.forName("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch(IOException ex) {
			throw new Error("Could not update " + path + ": " + ex);
		}
	}
	
	/**
	* Returns how many lines there are in the file
	*/
	public int size() {
		return lines.size();
	}
}