import java.io.*;
import java.util.*;
import javax.swing. *;

import java.net.*;


//class to find the computers in the network, adds them to an arrayList
class StreamGobbler extends Thread
{
	InputStream is;
	String type;
	ArrayList<String> comp;
	String thisComp;


	StreamGobbler(InputStream is, String type){ //constructor
		this.is = is;
		this.type = type;
		comp = new ArrayList<String> ();
	}

	public void run(){
		try{
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line=null;

			while ( (line = br.readLine()) != null){
					if (line.startsWith("\\") ){
						InetAddress THIS = InetAddress.getLocalHost(); //own computer...
						String tempLine = line.toLowerCase();
						tempLine = tempLine.trim();

						String tempTHIS = THIS.getHostName().toLowerCase();

						if (tempLine.substring(2).equalsIgnoreCase( tempTHIS)){
							thisComp = tempLine;
						}
						comp.add(line);
					}
			}
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}

	public ArrayList<String> getComp(){
		return comp;
	}

	public String getThisComp(){
		return thisComp;
	}
}

public class FileSharing{
	public static void main (String [] args) throws UnknownHostException, IOException, java.lang.Exception{

		//draw GUI
		JFrame frame = new JFrame ("FEAR SHARE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FileSharingWindow panel = new FileSharingWindow();
		ArrayList<String> comp = new ArrayList<String>();

		frame.getContentPane().add(panel);

		frame.setSize(800,700);
		frame.setVisible(true);

		frame.setResizable(false);
		//End draw GUI
	}

}