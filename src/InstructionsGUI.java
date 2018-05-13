import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;

public class InstructionsGUI implements ActionListener {
	
	JFrame frame = new JFrame("Help Screen");	//Instructions window
	JPanel panel = new JPanel();	//Main panel
	JButton exitButton = new JButton("Back");	//Exit button to close instructions
	JLabel title = new JLabel();	//Instructions title
	ImageIcon[] instructions = new ImageIcon[3];	//Images to explain instructions
	JTextArea[] description = new JTextArea[3];	//Description for images
	JLabel[] instructionsLabel = new JLabel[3];	//Label to hold instruction images
		
	public InstructionsGUI() {
	
		//Setting up frame size
		int width = 800;
		int height = 730;
        
        //Drawing frame
        frame.setLayout(null);
        frame.setSize(width, height);
        frame.setResizable(false);
        
        //Main panel setup
        panel.setLayout(null);
        panel.setSize(800, 730);
        panel.setOpaque(false);
        
        frame.add(panel);
        
        //Sets up title
        title.setText("HOW IT WORKS");
        title.setFont(new Font("Arial", Font.BOLD, 60));
        title.setBounds(200, 20, 600, 100);
        panel.add(title);
        
        //Setting button characteristics
        exitButton.setBounds(40, 40, 125, 75);
        exitButton.addActionListener(this);
        exitButton.setFont(new Font("Serif", Font.BOLD, 30));
        panel.add(exitButton);
        
        //Creation of imageicons + resizing
        instructions[0] = new ImageIcon(new ImageIcon(Test.class.getResource("/images/Instructions1.PNG")).getImage().getScaledInstance(170, 170, 0));
        instructions[1] = new ImageIcon(new ImageIcon(Test.class.getResource("/images/Instructions2.PNG")).getImage().getScaledInstance(170, 170, 0));
        instructions[2] = new ImageIcon(new ImageIcon(Test.class.getResource("/images/Instructions3.PNG")).getImage().getScaledInstance(170, 170, 0));        
      
        //Help Screen Content
        for (int i = 0; i < description.length; i ++) {
        	//Creates description and its properties
        	description[i] = new JTextArea();
        	description[i].setFont(new Font("Arial", Font.PLAIN , 25));
        	description[i].setLineWrap(true);
        	description[i].setWrapStyleWord(true);
        	description[i].setBackground(Color.LIGHT_GRAY);
        	description[i].setEditable(false);
        	panel.add(description[i]);
        }
        
        //Setting their location on the panel
        description[0].setBounds(210, 175, 175, 170);
        description[1].setBounds(580, 175, 175, 170);
        description[2].setBounds(210, 450, 475, 170);
   
        for (int i = 0; i < instructions.length; i ++) {
        	//Assigns location and image to each label
        	instructionsLabel[i] = new JLabel(instructions[i]);
        	panel.add(instructionsLabel[i]);
        }
        
        //Setting location on the panel
        instructionsLabel[0].setBounds(30, 175, 170, 170);
        instructionsLabel[1].setBounds(400, 175, 170, 170);
        instructionsLabel[2].setBounds(30, 450, 170, 170);
                
        //Instructions text
        description[0].setText("1) Select the news sources you wish to read! Select up to 3.");
        description[1].setText("2) Select the news topic of your interest!");
        description[2].setText("3) After you fetch the articles, there will be a list of the top 8 articles. Save to txt or send to your email to read the others and view headlines.");
       
        //Setting the frame visible!
        frame.setVisible(true);
	}
	
	//Event Handler for back button
	@Override
	public void actionPerformed(ActionEvent event) {
		frame.dispose();
	} 
}