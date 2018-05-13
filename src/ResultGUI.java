import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SuppressWarnings("serial")
public class ResultGUI extends JPanel implements ActionListener {

	public static Scanner inputFile = null;	//Initializing scanner to read txt files
	public static Formatter outputFile = null;	//Initializing formatter to write txt files

	private JLabel recipientLabel = new JLabel("Recipient:");	//Label to inform user where to input email
	private JLabel emailNotifyLabel = new JLabel();	//Label to inform user of email limitations
	private JLabel categoryLabel = new JLabel();	//Label to display chosen category
	private JLabel topEightLabel = new JLabel();	//Label to inform user of rankings

	private JLabel[] wordsLabel;	//Word count for each article
	private JLabel[] scoreLabel;	//Score display for each article
	private JLabel[] numberLabel;	//Ranking number for each article
	private JButton[] linkButton;	//Link for each article

	private JTextArea emailInputTA = new JTextArea();	//Email input area
	private JScrollPane emailInputSP = new JScrollPane(emailInputTA);	//ScrollPane for the email input

	private JButton returnButton = new JButton();	//Button to return to InputGUI
	private JButton saveTxtButton = new JButton();	//Button to save to txt file
	private JButton sendEmailButton = new JButton();	//Button to send to email

	//Label images for buttons (button skins)
	private JLabel returnImage = (new JLabel(new ImageIcon(new ImageIcon(Test.class.getResource("/images/button_return.png")).getImage().getScaledInstance(300, 100, 0))));
	private JLabel saveTxtImage = (new JLabel(new ImageIcon(new ImageIcon(Test.class.getResource("/images/button_save-as-txt.png")).getImage().getScaledInstance(200, 50, 0))));
	private JLabel sendEmailImage = (new JLabel(new ImageIcon(new ImageIcon(Test.class.getResource("/images/button_send-email.png")).getImage().getScaledInstance(200, 50, 0))));

	public static ArrayList <String> topicWords = new ArrayList<String>();	//List of interesting key words

	public ResultGUI() throws ParseException {

		//Results Panel Setup
		setBounds(0, 100, 1200, 800);
		setLayout(null);
		setBackground(Color.LIGHT_GRAY);

		//Save TXT & Save TXT Image Setup
		saveTxtButton.setBounds(450, 500, 200, 50);
		saveTxtImage.setBounds(450, 500, 200, 50);
		add(saveTxtImage);
		add(saveTxtButton);

		//Setting the button transparent
		saveTxtButton.addActionListener(this);
		saveTxtButton.setOpaque(false);
		saveTxtButton.setContentAreaFilled(false);
		saveTxtButton.setBorderPainted(false);

		//Send Email & Save Email Image Setup
		sendEmailButton.setBounds(450, 550, 200, 50);
		sendEmailImage.setBounds(450, 550, 200, 50);
		sendEmailButton.addActionListener(this);
		add(sendEmailImage);
		add(sendEmailButton);

		//Setting the button transparent
		sendEmailButton.setOpaque(false);
		sendEmailButton.setContentAreaFilled(false);
		sendEmailButton.setBorderPainted(false);

		//Setting up email message
		emailNotifyLabel.setBounds(150, 600, 1000, 50);
		emailNotifyLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		emailNotifyLabel.setText("*Sending email function is blocked on school Wi-Fi & YRDSB Recipients. May take a few moments to send*");
		add(emailNotifyLabel);

		//Setting up category label to display topic
		categoryLabel.setBounds(700, 25, 500, 50);
		categoryLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		categoryLabel.setText("Category: " + InputGUI.articles.get(0).getCategory());
		add(categoryLabel);

		//Setting up label to inform user of rankings
		topEightLabel.setBounds(400, 25, 500, 50);
		topEightLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		topEightLabel.setText("Top 8 Articles");
		add(topEightLabel);

		//Setting up email input (textarea and scrollpane)
		emailInputSP.setBounds(825, 550, 200, 50);
		emailInputTA.setFont(new Font("Arial", Font.BOLD, 20));
		add(emailInputSP);

		//Setting up recipient label
		recipientLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));
		recipientLabel.setBounds(675, 550, 200, 50);
		add(recipientLabel);

		//Retrieves interesting key words from wikipedia
		retrieveControversialTopics();

		//Loops through all the articles
		for (int i = 0; i < InputGUI.articles.size(); i ++) {
			CountWords(i);	//Counts number of words
			ScoreArticle(i);	//Gives the article a score
		}

		bubbleSort(InputGUI.articles);	//Sorts the article by score

		//Displays article rankings
		if (InputGUI.articles.size() > 8) { //Limiting the display to 8 articles if greater than 8
			numberLabel = new JLabel[8];
			wordsLabel = new JLabel[8];
			scoreLabel = new JLabel[8];
			linkButton = new JButton[8];

		} else {	//If less than 8 articles, display them all
			numberLabel = new JLabel[InputGUI.articles.size()];
			wordsLabel = new JLabel[InputGUI.articles.size()];
			scoreLabel = new JLabel[InputGUI.articles.size()];
			linkButton = new JButton[InputGUI.articles.size()];
		}

		//Loops through the number of rankings and sets up all the text and labels required
		for (int i = 0; i < numberLabel.length; i ++) {
			//Label to display rankings
			numberLabel[i] = new JLabel();
			numberLabel[i].setBounds(200, 85 + (50*i), 200, 25);
			numberLabel[i].setFont(new Font("Sans Serif", Font.BOLD, 30));
			numberLabel[i].setText((i+1) + ". ");
			add(numberLabel[i]);

			//Label to display number of words
			wordsLabel[i] = new JLabel();
			wordsLabel[i].setBounds(300, 50 + (50*i), 800, 100);
			wordsLabel[i].setFont(new Font("Sans Serif", Font.BOLD, 30));
			wordsLabel[i].setText("Words: " + InputGUI.articles.get(i).getWordsNum());
			add(wordsLabel[i]);

			//Label to display score
			scoreLabel[i] = new JLabel();
			scoreLabel[i].setBounds(500, 50 + (50*i), 800, 100);
			scoreLabel[i].setFont(new Font("Sans Serif", Font.BOLD, 30));
			scoreLabel[i].setText("Score: " + InputGUI.articles.get(i).getScore());
			add(scoreLabel[i]);

			//Button to go to article link
			linkButton[i] = new JButton();
			linkButton[i].setBounds(700, 80 + (50*i), 350, 40);
			linkButton[i].setFont(new Font("Sans Serif", Font.BOLD, 20));
			linkButton[i].setText("Click Here To View Article");
			linkButton[i].addActionListener(this);
			add(linkButton[i]);
		}

		//Setting Return Button & Return Image
		returnButton.addActionListener(this);
		returnImage.setBounds(100, 500, 300, 100);
		returnButton.setBounds(100, 500, 300, 100);
		add(returnImage);
		add(returnButton);

		//Setting the button transparent
		returnButton.setOpaque(false);
		returnButton.setContentAreaFilled(false);
		returnButton.setBorderPainted(false);

		//Setting the panel visible!
		setVisible(true);
	}

	//BubbleSort Method to sort articles by score
	private void bubbleSort(ArrayList<Article> articles) {  
		int temp = articles.size();  
		for(int i = 0; i < temp; i ++){ 
			for(int j = 1; j < (temp-i); j ++){  
				if(articles.get(j-1).getScore() < articles.get(j).getScore()){  //If previous article has less score than current article
					//Swap the two articles
					Collections.swap(InputGUI.wordsNumber, j-1, j);	//Swapping the number each unique words to match article number
					Collections.swap(InputGUI.wordsUnique, j-1, j);	//Swapping the unique words to match article number
					Collections.swap(articles, j-1, j);	//Swapping article objects
				}  
			}  
		}  
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == returnButton) {	//If the return button is pressed
			//Result panel invisible, input panel visible
			setVisible(false);
			InputGUI.inputPanel.setVisible(true);

			//Clearing all the articles and article related lists
			InputGUI.wordsUnique.clear();
			InputGUI.articles.clear();
			InputGUI.wordsNumber.clear();
			topicWords.clear();
		} else if (e.getSource() == saveTxtButton) 	//If user presses "SaveToTxt" Button
			SaveToTextFile();	//Run the save method
		else if (e.getSource() == sendEmailButton) {	//If user presses "SendtoEmail" Button
			//Validating if email is valid: http://crunchify.com/how-to-validate-email-address-using-java-mail-api/
			boolean isValid = false;
			try {
				InternetAddress internetAddress = new InternetAddress(emailInputTA.getText());	//Retrieve email address from textarea
				internetAddress.validate();
				isValid = true;	//Goes to this line if it's valid, catches if it isn't valid
			} catch (AddressException e1) {
				//Display error message
				JOptionPane.showMessageDialog(null, "Error. Please check entered email address and make sure you are not connected to school internet", "Message", JOptionPane.INFORMATION_MESSAGE);
			}
			if (isValid == true)	//If the email is valid
				SendEmail();	//Run the sendEmail method
		}

		//If the user presses on any link buttons
		for (int i = 0; i < linkButton.length; i ++) {
			if (e.getSource() == linkButton[i]) {
				try {
					OpenWebLink(i); //Run the method to open the link
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	//Retrieves the interesting key words from a Wikipedia Article called "List Of Controversial Issues"
	public void retrieveControversialTopics() {
		//Utilizing JSoup: https://www.youtube.com/watch?v=0s8O7jfy3c0
		try {
			//Connects to the Wikipedia article and masks as Mozilla FireFox Browser
			Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Wikipedia:List_of_controversial_issues").userAgent("Mozilla/17.0").get();

			//Searches and filters everything that's in the following class and selects everything starting with "li"
			Elements temp = doc.select("div.div-col.columns.column-count.column-count-2").select("li");

			for (Element word:temp) {
				String wordTemp = word.select("a[href][title]").attr("title");	//Selecting word in the following HTML format
				wordTemp = wordTemp.toLowerCase();	//Turn the word to all lowercase (so comparison doesn't take upper/lower case into account)
				topicWords.add(wordTemp);	//Add the word to the arraylist
			}

		} catch (IOException e) {
			//Error Catch message
			System.out.println("Invalid URL");
		}
	}

	//MAIN SCORING ALGORITHM - assigns an article a score
	public void ScoreArticle(int article) throws ParseException {
		//Scores the article (relevance)
		int score = 0;

		//Loops through the article's sentences and each of the interesting/controversial words
		for (int x = 0; x < InputGUI.articles.get(article).sentences.size(); x ++) {
			for (int y = 0; y < topicWords.size(); y ++) {	

				//If one of the controversial/interesting words is in one of the sentences
				if (InputGUI.articles.get(article).sentences.get(x).sentence.contains(topicWords.get(y))) {
					score += 1;	//Increment the score
				}
			}
		}

		//To keep things consistent: if articles are greater than 1000 words, divide ratios. If smaller, multiply ratios.
		score = (int) (score * (1000 / (double) InputGUI.articles.get(article).getWordsNum()));

		//Relevancy: scores are reduced if publish dates are old. Older they are, the smaller decimal multiplied
		score = (int) (score * checkTimeDifference(article));

		InputGUI.articles.get(article).setScore(score);	//Sets the article score
	}

	//Counts number of words in a given article
	public void CountWords(int article) {
		int sum = 0;
		//Loops through the number of words of an article
		for (int x = 0; x < InputGUI.wordsNumber.get(article).size(); x ++)
			sum += InputGUI.wordsNumber.get(article).get(x);	//Added to a running total

		InputGUI.articles.get(article).setWordsNum(sum);	//Sets the amount of words for the article
	}

	//Saves results to a txt file
	public void SaveToTextFile() {
		try {
			outputFile = new Formatter(new File("Results.txt"));	//Create a new txt file

			//Loop through all articles
			for (int i = 0; i < InputGUI.articles.size(); i ++)
				//Formatting:
				outputFile.format((i+1) + ". Score: " + InputGUI.articles.get(i).getScore() + "\t" + InputGUI.articles.get(i).getHeadline() + "%n" + InputGUI.articles.get(i).getUrl() + "%n%n");

			outputFile.close();	//Close output: finished
		} catch (FileNotFoundException error) { 
			//Error catch if file does not exist
			System.err.println("File not found - check the file name");    
		}

		//Tell user that the results have been saved
		JOptionPane.showMessageDialog(null, "Results have been saved as “Results.txt”", "Message", JOptionPane.INFORMATION_MESSAGE);

		//Opening a txt file programmatically: https://www.youtube.com/watch?v=051kyFmEs2c
		ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "Results.txt");
		try {
			pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Opens the link of a given article: https://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
	public void OpenWebLink(int articleNum) throws URISyntaxException {
		//Checks if it can open a link
		if (Desktop.isDesktopSupported()) {
			try {
				//Opens link of the article's URL
				Desktop.getDesktop().browse(new URI(InputGUI.articles.get(articleNum).getUrl()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	//Method to send email
	public void SendEmail () {
		//Sending mail programmatically: https://coderanch.com/t/551106/java/configure-localhost-send-email
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				//Username, Password for the sender
				return new PasswordAuthentication("jason.newspaper.bot@gmail.com","newspaperbot");
			}
		});

		try {
			Message message = new MimeMessage(session);
			//Sender email address
			message.setFrom(new InternetAddress("jason.newspaper.bot@gmail.com"));

			//Receiver email address
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailInputTA.getText()));

			//Getting the current system date: https://beginnersbook.com/2013/05/current-date-time-in-java/
			DateFormat df = new SimpleDateFormat("dd/MM/yy");
			Date dateobj = new Date();

			//Formatting message
			message.setSubject(df.format(dateobj) + " - Newspaper Selection: " + InputGUI.articles.get(0).getCategory()); //Date - Newspaper Selection

			//Temporary string to write the email subject
			String temp = "";
			temp ="<html><body><h3> Here are today's selection of news! </h3><br>";	//Formatting email message with HTML
			for (int i = 0; i < InputGUI.articles.size(); i ++)
				//Writing the message content
				temp += (i+1) + ". " + InputGUI.articles.get(i).getHeadline() +"<br>" + "Score: " + InputGUI.articles.get(i).getScore() + "\t" + InputGUI.articles.get(i).getUrl() + "<br><br>";
			
			//Formatting email message with HTML
			temp += "</body></html>";
			message.setContent(temp,"text/html");
			
			Transport.send(message);	//Sending the message

			System.out.println("Done");
			//Message to inform user that process is complete
			JOptionPane.showMessageDialog(null, "Results have been sent to " + emailInputTA.getText(), "Message", JOptionPane.INFORMATION_MESSAGE);

		} catch (MessagingException e) {
			//Error catch if it takes too long
			System.out.print("Messaging error. Ensure you are not connected on school internet");
		}
	}

	//Checks difference between publish date of an article and system time
	//Returns a number from 0-1, depending on when the article was published
	public double checkTimeDifference (int articleNum) throws ParseException {
		//Retrieving current system date: https://beginnersbook.com/2013/05/current-date-time-in-java/
		//Retrieve current date
		DateFormat df = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
		Date currentDate = new Date();
		System.out.println(df.format(currentDate));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");

		//Setting the first date (publish date), and second date(current date)
		Date firstDate = sdf.parse(df.format(InputGUI.articles.get(articleNum).getPublishDate()));
		Date secondDate = sdf.parse(df.format(currentDate));

		//Calculating difference between two dates: https://stackoverflow.com/questions/1555262/calculating-the-difference-between-two-java-date-instances
		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		//Difference in hours between articles
		System.out.println(diff);

		if (1 - (diff * 0.01) > 0)	//If it has been less than 100 hours since publish date
			return 1 - (diff * 0.01);	//Each hour reduces 0.01 (this will be multiplied onto the score)
		else
			return 0;	//If it has been more than 100 hours since publish date, return 0
	}
}
