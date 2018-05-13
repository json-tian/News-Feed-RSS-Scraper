import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

//THIS INTERFACE IS NOT USED
@SuppressWarnings("serial")
public class MailingManagementGUI extends JPanel implements ActionListener {

	public static Scanner inputFile = null;	//Scanner to read from txt files
	public static Formatter outputFile = null;	//Formatter to write to txt files

	private String[] columnNames = {"First Name", "Last Name", "Email"};	//Name of table columns
	private JTable table;	//Creating JTable
	private JLabel[] mailingListLabel = new JLabel[3];	//Label to inform user
	private JTextArea[] mailingListTA = new JTextArea[3];	//Area for user add another user
	private JScrollPane[] mailingListSP = new JScrollPane[3];	//ScrollPane for the text area above
	private JButton addButton = new JButton("Add");	//Button to add another user
	private ArrayList<String> txtContent = new ArrayList<String>();	//Holds list of words from the txt file
	private String[][] data;	//2D array to hold data values
	private JScrollPane scrollPaneTable = new JScrollPane(table);

	public MailingManagementGUI() throws ParseException {

		//Setting up frame
		setBounds(0, 100, 1600, 900);
		setLayout(null);
		setBackground(Color.GRAY);
		
		//Setting up labels and area for user input 
		for (int i = 0; i < mailingListSP.length; i++) {
			mailingListTA[i] = new JTextArea();
			mailingListSP[i] = new JScrollPane(mailingListTA[i], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			mailingListSP[i].setBounds(1000, 100 + (100 * i), 150, 50);
			mailingListLabel[i] = new JLabel();
			mailingListLabel[i].setBounds(1000, 50 + (100 * i), 150, 50);
			add(mailingListLabel[i]);
			add(mailingListSP[i]);
		}

		//Setting text for labels
		mailingListLabel[0].setText("First Name:");
		mailingListLabel[1].setText("Last Name:");
		mailingListLabel[2].setText("Email Address:");

		//Setting up add new user button
		addButton.setBounds(1000, 400, 150, 75);
		addButton.addActionListener(this);
		add(addButton);
		
		//Setting up the table
		//USING JTABLE: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html
		table = new JTable(readFromText(), columnNames);
		table.getTableHeader().setBounds(100, 200, 800, 100);	//Setting location of table header
		setLayout(null);
		
		//Adding table elements to the frame
		add(table.getTableHeader());
		add(table, BorderLayout.CENTER);

		//Setting size of the table
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		//Creating a scrollPane for the table
		scrollPaneTable.setBounds(100, 100, 700, 300);

		//Add the scrollpane to the frame
		add(scrollPaneTable);

		//Set the frame visible!
		setVisible(true);
	}

	//Reading from the txt file
	@SuppressWarnings("resource")
	public String[][] readFromText() {
		//Clear the txt words list
		txtContent.clear();
		
		try {	
			inputFile = new Scanner(new File("User.txt")).useDelimiter(",");	//Separates every comma
			while (inputFile.hasNext()) {
				txtContent.add(inputFile.next());	//Add word to the list
			}
		} catch (FileNotFoundException error) {
			//Error catch if txtFile does not exist
			System.err.println("File not found - check the file name");
		}
		data = new String[txtContent.size()/3][3]; //Create the array to fill the table
				
		int counter = 0;	//Holds index of the txt array
		//Loops through the txt list and places it into the table array
		for (int i = 0; i < (txtContent.size()/3); i ++) {	//Rows
			for (int j = 0; j < 3; j ++) {	//Columns
				data[i][j] = txtContent.get(counter);	//Set the value of table array
				System.out.println(data[i][j]);
				counter ++;
			}
		}
		return data;	//Return the table array
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//If the add user button is pressed
		if (e.getSource() == addButton) {
			addToTextFile();	//Run method to add another user
			table.repaint();	//Update the JTable
		}
	}

	//Saving information to the txt file
	public void addToTextFile() {
		try {
			outputFile = new Formatter(new File("User.txt"));

			//Adds the existing users from table array
			for (int i = 0; i < data.length; i ++) {
				outputFile.format(data[i][0] + "," + data[i][1] + "," + data[i][2] + ",\n");
			}
			
			//Add the new user that was entered in text areas
			outputFile.format(mailingListTA[0].getText() + "," + mailingListTA[1].getText() + "," + mailingListTA[2].getText());

			//Output is finished
			outputFile.close();
		} catch (FileNotFoundException error) { 
			//Error catch if file is not found
			System.err.println("File not found - check the file name");    
		}
	}
}
