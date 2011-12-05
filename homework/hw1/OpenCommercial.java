/* OpenCommercial.java */

import java.net.*;
import java.io.*;

/**  A class that provides a main function to read five lines of a commercial
 *   Web page and print them in reverse order, given the name of a company.
 */

class OpenCommercial {

  /** Prompts the user for the name X of a company (a single string), opens
   *  the Web site corresponding to www.X.com, and prints the first five lines
   *  of the Web page in reverse order.
   *  @param arg is not used.
   *  @exception Exception thrown if there are any problems parsing the 
   *             user's input or opening the connection.
   */
  public static void main(String[] arg) throws Exception {

    BufferedReader keyboard;
    String inputLine;
    String webAddress;
    URL webAdd;

    keyboard = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Please enter the name of a company (without spaces): ");
    System.out.flush();        /* Make sure the line is printed immediately. */
    inputLine = keyboard.readLine();

    webAddress = "http://www." + inputLine + ".com/";
    webAdd = new URL(webAddress);
    InputStream ins = webAdd.openStream();
    InputStreamReader isr = new InputStreamReader(ins);
    BufferedReader siteReader = new BufferedReader(isr);

    String line1 = siteReader.readLine();
    String line2 = siteReader.readLine();
    String line3 = siteReader.readLine();
    String line4 = siteReader.readLine();
    String line5 = siteReader.readLine();
    
    String outputLine = line5 + " " + line4 + " " + line3 + " " + line2 + " " + line1;

    System.out.println(outputLine);
  }
}
