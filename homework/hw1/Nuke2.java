import java.io.*;

class Nuke2 {
    public static void main(String[] arg) throws Exception {
	
	BufferedReader keyboard;
	String inputLine;
	String outputLine;

	keyboard = new BufferedReader(new InputStreamReader(System.in));
	
	inputLine = keyboard.readLine();

	outputLine = inputLine.substring(0,2) + inputLine.substring(3);

	System.out.println(outputLine);
    }
}