import javax.swing. *;
import java.awt. *;
import java.awt.event. *;
import java.io.*;
import java.net.*;
import javax.swing.text. *; //for DefaultCaret

import java.util. *;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.*;

public class FileSharingWindow extends JPanel implements ActionListener{
	int numResults;
	JPanel resultsButtonsNetwork; //panel for the buttons and result field and the comps in network
	JPanel buttons;			 //panel for the many buttons

	JPanel networkFrame;	//panel for the list of computers in network
	JTextArea networkedComps;
	JScrollPane networkScroll;

	JTextField searchField;
	JTextArea resultField;
	JLabel titleLabel, resultLabel;

	JScrollPane scroll; //scroll bar for the resultField
	JScrollPane scrollButtons; //scroll bar for the button panel

	JButton search, deleteSearch, clear;
	JButton refreshNetworks;

	ArrayList<JButton> results; //will show download button for all results
	ArrayList<String> downPath; //holds path to download link for button click
	ArrayList<String> computers; //holds String of computer name in network

	String thisComp;

	FileSharingWindow(){ //sets default stuff, actionListener, scroll bars, etc etc
		numResults = 0;
		thisComp = "";

		results = new ArrayList<JButton>();
		downPath = new ArrayList<String>();
		computers = new ArrayList<String>();
		buttons = new JPanel(new GridLayout(results.size(), 1));

		networkFrame = new JPanel(new GridLayout(computers.size(), 1));

		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

		//add a field to put the search bar
		searchField = new JTextField("", 20);
		searchField.setMinimumSize(new Dimension(50, 25));
		searchField.setPreferredSize(new Dimension(0, 25));
		searchField.setMaximumSize(new Dimension (200,25));
		searchField.addActionListener(this);

		//add main search button
		search = new JButton ("SEARCH");
		search.addActionListener(this);

		//add button to clear all text in results
		clear = new JButton ("CLEAR RESULTS");
		clear.addActionListener(this);

		//add button to clear all text in search bar
		deleteSearch = new JButton ("DELETE SEARCH");
		deleteSearch.addActionListener(this);

		//add button to refresh list of networked computers
		refreshNetworks = new JButton ("Refresh List");
		refreshNetworks.addActionListener(this);


		//add a field for the results to be printed
		resultField = new JTextArea();
		resultField.setForeground(Color.WHITE);
		resultField.setText("Enter something to search for"); //default text
		resultField.setBackground(Color.BLACK);
		resultField.setMinimumSize(new Dimension(0, 500));
		resultField.setMaximumSize(new Dimension (500,500));
		resultField.setLineWrap(true); //text wraps around instead of creating horizontal scrollbar

		resultField.setEditable(false); //do not allow user to write in result field!

		//title of program
		titleLabel = new JLabel("Fear Share");
		titleLabel.setForeground(Color.RED); //color of the text
		titleLabel.setFont(new Font("Stencil", Font.PLAIN, 42) ); //sets font and size of title
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); //adds title to center of screen

		//shows number of results
		resultLabel = new JLabel("Results (" + numResults + ")");

		//allows the scrolling to happen automatically
		DefaultCaret caret = (DefaultCaret) resultField.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE); //means never scroll to bottom of results page, show the first ones first

		//now add the download buttons to the other panel.
		resultsButtonsNetwork = new JPanel();
		resultsButtonsNetwork.setLayout(new BoxLayout(resultsButtonsNetwork, BoxLayout.LINE_AXIS)); //items laid out horizontally

		resultsButtonsNetwork.add(buttons); //add buttons layer to main layer
		scroll = new JScrollPane (resultField);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		resultsButtonsNetwork.add(scroll);

		//add scroll bar for buttons layout
		scrollButtons = new JScrollPane(buttons);
		scrollButtons.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scrollButtons.setMinimumSize(new Dimension(160, 200)); //keeps scrolling enabled for buttons
		scrollButtons.setPreferredSize(new Dimension(160, 200)); //try to comment these lines and see what happens for
											//results with over 100 results. not pretty....

		//add a field for the computers on network to be printed
		networkedComps = new JTextArea();
		networkedComps.setForeground(Color.WHITE);
		networkedComps.setText("Computers in Network:\n\n");
		networkedComps.setBackground(Color.BLACK);
		networkedComps.setMinimumSize(new Dimension(0, 500));
		networkedComps.setMaximumSize(new Dimension (10,500));
		networkedComps.setLineWrap(true);
		networkedComps.setEditable(false); //do not allow user to write in result field!

		//adds panel to hold the textArea for the network computer and the scrollbar
		networkFrame.add(networkedComps);
		networkScroll = new JScrollPane(networkedComps);
		networkScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//add the buttons and the network stuff to the main layer to hold it all
		resultsButtonsNetwork.add(scrollButtons); //add all to main scroll bar layout
		resultsButtonsNetwork.add(networkScroll);

		//add components to panel
        add(Box.createRigidArea(new Dimension(10, 30))); //adds space
		add(titleLabel); //adds title of the program, stays the same, and on its own line
        add(Box.createRigidArea(new Dimension(10, 30))); //adds space between Title and searching stuff

		//add button for search box and other buttons on same line
		JPanel searching = createSearchPanel ();
     	add(searching); //now add that top panel

		add(Box.createRigidArea(new Dimension(10, 10))); //adds space between searching and result stuff

		JPanel resultAndRefreshButton = new JPanel ();
		resultAndRefreshButton.setLayout(new BoxLayout(resultAndRefreshButton, BoxLayout.LINE_AXIS));

		resultAndRefreshButton.add(resultLabel);
		resultAndRefreshButton.add(Box.createRigidArea(new Dimension(450, 0))); //adds space between results and searching

		resultAndRefreshButton.add (refreshNetworks); //adds refresh network button

		add(resultAndRefreshButton);

		add(Box.createRigidArea(new Dimension(10, 5))); //adds space

		//add scroll bar and buttons
		add(resultsButtonsNetwork);

		add(Box.createRigidArea(new Dimension(10, 5))); //adds space
	}

	public JPanel createSearchPanel (){
		JPanel searching = new JPanel();
        searching.setLayout(new BoxLayout(searching, BoxLayout.LINE_AXIS));

		searching.add(searchField); //add searchField to panel
        searching.add(Box.createRigidArea(new Dimension(10, 0)));

        searching.add(search); //add search button to panel
        searching.add(Box.createRigidArea(new Dimension(10, 0)));

		searching.add(deleteSearch); //add delete button to panel
  		searching.add(Box.createRigidArea(new Dimension(10, 0)));

		searching.add(clear); //add clear button to panel

		return searching;
	}

	public void setComps(ArrayList<String> comp){
		computers = comp;
	}

	public void update(){
		resultLabel.setText ("Results (" + numResults + ")");

		for (JButton j : results){
			j.setMinimumSize(new Dimension (100, 100));
			j.setMaximumSize(new Dimension (100, 100));
			buttons.add(j);
		}
	}

	public void actionPerformed (ActionEvent e){
		//e.getSource() returns the object that called it, in case you have multiple fields
		String nn = e.getActionCommand();

		if (e.getActionCommand() == "SEARCH"){ //SEARCH button hit...
			resetButtonPanel();

			String s = searchField.getText(); //get text from search field

			resultField.setText("");

			if (s.length() >= 1){
				resultField.append("Searching for files starting with: " + s + "\n\n");
			}
			else{
				resultField.append("Searching for all files...\n\n");
			}

			resultField.update(resultField.getGraphics()); //updates result field text.

			for (String comName: computers){ //for each computer in the network...
				resultField.append("Searching computer named: " + comName.trim() + "\n");
				resultField.update(resultField.getGraphics()); //updates result field
				searchComputerForString(s, comName);
			}

			resultField.update(resultField.getGraphics()); //updates result field

			if (numResults == 0){ //no files found...
				resultField.setText("No Results found."); //erases previous text
			}

			if (numResults == 0 && computers.isEmpty() != true){ //were computers in network to search, just nothing matching search item
				String message = "";
				if (s.length() >= 1)
					message = "No results found for the search of: " + s;
				else if (s.length() < 1 && computers.size() == 1)
					message = "No results found. Current computer is the only computer in the network";

				createMessageBox(message, "PLAIN_MESSAGE");
			}

			else if (numResults == 0 && computers.isEmpty() == true){ //no computers in network to search!
				String message = "Error: No computers in the network! Please refresh the list of computers.";

				createMessageBox(message, "WARNING_MESSAGE");
			}

			else{
				String message = "Total Results found: " + numResults;

				createMessageBox(message, "PLAIN_MESSAGE");
			}

			//now clear the search in search field...
			searchField.setText("");
		} //end SEARCH button clicked

		if (e.getActionCommand() == "CLEAR RESULTS"){ //clear result field
			resetButtonPanel();

			resultField.setText(""); //set text to empty
		}

		if (e.getActionCommand() == "DELETE SEARCH"){ //clear search box
			deleteSearch();
		}

		if (e.getActionCommand() == "Refresh List"){
			refreshListNetworksCommand();
		}

		if (nn.compareToIgnoreCase("Download file #") >= 0 && nn.length() >= 14){
			String tempNum = nn.substring(15);
			int buttonHitNum = Integer.parseInt(tempNum);

			//use download path to download file
			downloadFileFromClick(buttonHitNum);
		}

	}

	public void createMessageBox(String message, String type){
		if (type == "PLAIN_MESSAGE")
			JOptionPane.showMessageDialog(null,message,"ATTENTION",JOptionPane.PLAIN_MESSAGE);
		else if (type == "WARNING_MESSAGE")
			JOptionPane.showMessageDialog(null,message,"ERROR",JOptionPane.WARNING_MESSAGE);
	}

	public void downloadFileFromClick(int buttonHitNum){
		String origLoc = downPath.get(buttonHitNum);
		String fileName = ""; //title of the file, get from downPath, the simplified version

		File f = new File (origLoc);

		fileName = f.getName();

		//thisComp contains the name of this computer's ip address, acquired through refreshNetworks() below
		String newLoc = "\\\\" + thisComp + "\\Users\\Public\\" + fileName; //destination, this computer

		Path origPath = Paths.get(origLoc);
		try{
			Files.copy(origPath, Paths.get(newLoc), REPLACE_EXISTING);

			//show message of successful file copying. show orig loc, and end location.
			String message = "Copied file from:\n" + origPath + "\nto\n" + newLoc;

			createMessageBox (message, "PLAIN_MESSAGE");
		}
		catch (IOException ioe){
			createMessageBox (ioe.toString(), "WARNING_MESSAGE");
		}
	}

	public void searchComputerForString(String s, String comName){
		try{
			int tempTotal = numResults; //holds temporary total, before adding on new files
			String ns = comName.substring(2); //remove first two \\ from string
			ns = ns.trim(); //remove whitespace after and before

			InetAddress i = InetAddress.getByName(ns); //need to check if not looking through own files in
			InetAddress THIS = InetAddress.getLocalHost(); //own computer...

			if (!THIS.getHostAddress().equalsIgnoreCase(i.getHostAddress()) ){
				listFiles (new File ("\\\\" + ns + "\\Users"), s); //function call to list all files

				resultField.append("Found " + (numResults - tempTotal) + " items in " + comName + "\n");
				resultField.append("Found " + numResults + " items total.\n\n");

				update(); //redraw stuff to screen
			}

			else if (THIS.getHostAddress().equalsIgnoreCase(i.getHostAddress())){ //own computer, don't search files
				thisComp = THIS.getHostAddress(); //this stores the computer ip to the string
				resultField.append("User's own computer. Skipping search.\n\n");
			}
		}
		catch (UnknownHostException u){
			resultField.append("Found 0 items in " + comName + "\n\n");
		}

	}

	public void resetButtonPanel(){
		downPath.clear(); //resets array that holds all download path
		numResults = 0; //change results of search to 0

		for (JButton b : results){
			buttons.remove(b); //remove all buttons from the panel
		}

		results.clear(); //clear the results of previous search

		update();		//now update panel(s)
	}

	public void deleteSearch(){
		searchField.setText("");
	}

	public void refreshListNetworksCommand (){
			networkedComps.setText("Computers in Network:\n\n");
			try{
				refreshNetworks();
				for (String c : computers){
					c = c.trim(); //added to remove whitespace
					networkedComps.append(c + "\n");
				}
			}
			catch (IOException io){}

	}

	public void refreshNetworks() throws IOException{
			//below will run the command net view
			Process tr = Runtime.getRuntime().exec("net view"); //execute net view command line function

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(tr.getInputStream(), "OUTPUT");

			// kick them off
			outputGobbler.start();

			try {
				outputGobbler.join(); //wait for Thread to finish.
			}
			catch (Exception x){
			}

			computers = outputGobbler.getComp(); //now get the computers in the network found
			thisComp = outputGobbler.getThisComp();
	}

	//this function will list all files in the passed in directory using recursion.
	public void listFiles (File mainDir, String search){
		try {
			File [] list = mainDir.listFiles(); //getting all files in the main dir

			for(File f: list){
				if (f.isHidden() != true && f.isDirectory() == false){ //only show the non-hidden files, and no directories
					if (removeExtension(f.getName()).startsWith(removeExtension(search))){ //or CONTAINS instead of startsWith?
						resultField.append(numResults + ") " + f.getName());

						double fileSize = f.length() ;
						fileSize /= 1024;

						resultField.append("\nFile size: " + fileSize + " kilobytes");

						downPath.add(f.getAbsolutePath()); //adds full system path to this item to an array for later use

						//add download button for each valid filename
						String newB = "Download file #" + Integer.toString(numResults);
						newB = newB.trim(); //remove whitespace

						results.add(new JButton(newB)); //add button, each button able to be clickable
						results.get(numResults).addActionListener(this);

						//increment results to display in GUI
						numResults++;

						resultField.append ("\n\n"); //some formatting stuff in resultField text box

						int endPosition = resultField.getDocument().getLength(); //determine where the bottom of the result field is
						Rectangle bottom = resultField.modelToView(endPosition);
						resultField.scrollRectToVisible(bottom);

						resultField.update(resultField.getGraphics());
					}
				}

				if (f.isDirectory() && f.isHidden() == false){ //travel through all dir to find all files using recursion
					listFiles(f, search);
				}
			}
		}

		catch (Exception e){
		}

	}

	public static String removeExtension(String s) { //used in file searching

		String separator = System.getProperty("file.separator");
		String filename = s;

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return filename;

		return filename.substring(0, extensionIndex);
	}

}