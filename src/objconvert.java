import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class objconvert {
	public static void main(String[] args)
	{
		File file, out;
		BufferedReader read = null;
		ArrayList<String> lines = new ArrayList<String>();
		String filename = "bunny.obj";
		String line;
		try {
			file = new File(filename);
			read = new BufferedReader(new FileReader(file));
			for (int i = 0; (line = read.readLine()) != null; i++){
				//line.replaceAll("//", "/1/");
				lines.add(line.replaceAll("//", "/1/"));
			}
			read.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			  FileWriter fstream = new FileWriter("new"+filename);
			  BufferedWriter write = new BufferedWriter(fstream);
			  for(int i = 0; i < lines.size(); i++){
				  write.write(lines.get(i));
				  write.newLine();
			  }
			  //Close the output stream
			  write.close();
			  System.out.println("Write successful");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
