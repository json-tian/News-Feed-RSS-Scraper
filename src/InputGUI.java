import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("serial")
public class InputGUI extends JFrame implements ActionListener {

	public static Scanner inputFile = null;	//Scanner to reading from textfiles
	public static Formatter outputFile = null;	//Formatter to write to textfiles

	public static JPanel inputPanel = new JPanel();	//Panel for newspaper source selection
	private JPanel headerPanel = new JPanel();	//Header panel (static panel for navigation tabs)

	private JLabel headerLabel = new JLabel("Article Selector");	//Header for program name
	private JLabel[] logoImageLabel = new JLabel[3];	//Images for each news source logo

	private JButton submitButton = new JButton("Fetch Articles!");	//Search button (to retrieve articles)
	private JButton exitButton = new JButton();	//Exit Button
	private JButton helpButton = new JButton();	//Help Button

	//Button skin images
	private JLabel exitImage = (new JLabel(new ImageIcon(new ImageIcon(Test.class.getResource("/images/button_exit.png")).getImage().getScaledInstance(75, 75, 0))));
	private JLabel helpImage = (new JLabel(new ImageIcon(new ImageIcon(Test.class.getResource("/images/button_help.png")).getImage().getScaledInstance(150, 75, 0))));

	//Labels to inform user
	private JLabel getNewsLabel = new JLabel("Fetch Recent News From Online Sources:");	//Information label for news source selection
	private JLabel getTopicLabel = new JLabel("Choose News Category");
	private JLabel waitLabel = new JLabel("*Fetching may take a few moments*");

	private JCheckBox[] newspaperCB = new JCheckBox[3];	//Checkboxes for newspaper source selection

	public static ArrayList<ArrayList<String>> wordsUnique = new ArrayList<ArrayList<String>>();	//Records the unique words of each article
	public static ArrayList<ArrayList<Integer>> wordsNumber = new ArrayList<ArrayList<Integer>>();	//Records the number of each unique word in each article
	public static ArrayList<Article> articles = new ArrayList<Article>();	//List of articles
	private ResultGUI resultPanel;	//Result panel to show results

	//Types of news categories
	private String[] newsTopic = {"Top Stories", "Canada", "World", "Sports", "Business", "Entertainment", "Science/Technology", "Health"};
	
	//Drop down list for news category selection
	private JComboBox<?> newsTopicCB = new JComboBox<Object>(newsTopic);

	public InputGUI() {

		//Frame Setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		setSize(1200, 800);

		//Input Panel Setup
		inputPanel.setBounds(0, 100, 1200, 800);
		inputPanel.setLayout(null);
		inputPanel.setBackground(Color.LIGHT_GRAY);
		add(inputPanel);

		//Header Panel Setup
		headerPanel.setBounds(0, 0, 1600, 100);
		headerPanel.setLayout(null);
		headerPanel.setBackground(Color.BLUE);
		add(headerPanel);

		//Header for Application Name Setup
		headerLabel.setBounds(450, 0, 300, 100);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 40));
		headerLabel.setForeground(Color.WHITE);
		headerPanel.add(headerLabel);

		//Exit Button & Exit Image setup
		exitButton.setBounds(100, 12, 75, 75);
		exitImage.setBounds(100, 12, 75, 75);
		headerPanel.add(exitButton);
		headerPanel.add(exitImage);

		//Setting exit button to transparent
		exitButton.addActionListener(this);
		exitButton.setOpaque(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setBorderPainted(false);

		//Help Button & Help Image setup
		helpButton.setBounds(200, 12, 150, 75);
		helpImage.setBounds(200, 12, 150, 75);
		headerPanel.add(helpButton);
		headerPanel.add(helpImage);

		//Setting help button to transparent
		helpButton.addActionListener(this);
		helpButton.setOpaque(false);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorderPainted(false);

		//Adding the newspaper logos onto the panel
		for (int i = 0; i < logoImageLabel.length; i ++) {
			logoImageLabel[i] = new JLabel();
			inputPanel.add(logoImageLabel[i]);
		}

		//Setting location of newspaper logos
		logoImageLabel[0].setBounds(200, 150, 300, 100);
		logoImageLabel[1].setBounds(100, 250, 300, 100);
		logoImageLabel[2].setBounds(250, 350, 300, 100);

		//Resizing imageIcons: https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
		ImageIcon imageIcon = new ImageIcon(Test.class.getResource("/images/CTVNews.png")); //Loading the image
		Image image = imageIcon.getImage(); //Transforming the image
		Image newimg = image.getScaledInstance(160, 80,  java.awt.Image.SCALE_SMOOTH); //Scaling the image
		imageIcon = new ImageIcon(newimg);

		ImageIcon imageIcon2 = new ImageIcon(Test.class.getResource("/images/TorontoStar.png")); //Loading the image
		Image image2 = imageIcon2.getImage(); //Transforming the image
		Image newimg2 = image2.getScaledInstance(250, 60,  java.awt.Image.SCALE_SMOOTH); //Scaling the image
		imageIcon2 = new ImageIcon(newimg2);

		ImageIcon imageIcon3 = new ImageIcon(Test.class.getResource("/images/CBCNews.png")); //Loading the image
		Image image3 = imageIcon3.getImage(); //Transforming the image 
		Image newimg3 = image3.getScaledInstance(90, 80,  java.awt.Image.SCALE_SMOOTH); //Scaling the image
		imageIcon3 = new ImageIcon(newimg3);  // transform it back

		//Setting labels to their transformed logos
		logoImageLabel[0].setIcon(imageIcon);
		logoImageLabel[1].setIcon(imageIcon2);
		logoImageLabel[2].setIcon(imageIcon3);

		//Submit Button & Submit Image Setup
		submitButton.addActionListener(this);
		submitButton.setBounds(600, 500, 200, 100);
		submitButton.setFont(new Font("Arial", Font.BOLD, 20));
		inputPanel.add(submitButton);
		
		//Waiting Message Setup
		waitLabel.setBounds(550, 570, 1000, 100);
		waitLabel.setFont(new Font("Arial", Font.BOLD, 20));
		inputPanel.add(waitLabel);

		//News Message Setup
		getNewsLabel.setFont(new Font("Sans Serif", Font.BOLD, 30));
		getNewsLabel.setBounds(100, 50, 750, 50);
		inputPanel.add(getNewsLabel);

		//Topic Message Setup
		getTopicLabel.setFont(new Font("Arial", Font.BOLD, 30));
		getTopicLabel.setBounds(800, 50, 750, 50);
		inputPanel.add(getTopicLabel);

		//Setting up JCheckBoxes: https://docs.oracle.com/javase/7/docs/api/javax/swing/JCheckBox.html
		for (int i = 0; i < newspaperCB.length; i ++) {
			newspaperCB[i] = new JCheckBox();
			newspaperCB[i].setBounds(400, 175 + (100 * i), 350, 50);
			newspaperCB[i].setFont(new Font("Sans Serif", Font.BOLD, 50));
			inputPanel.add(newspaperCB[i]);
		}

		//Giving each checkbox a unique name (newspaper sources)
		newspaperCB[0].setText("CTV News");
		newspaperCB[1].setText("Toronto Star");
		newspaperCB[2].setText("CBC News");

		//Setting up drop down list for news topics
		newsTopicCB.setBounds(800, 150, 325, 100);
		newsTopicCB.setFont(new Font("Arial", Font.BOLD, 30));
		inputPanel.add(newsTopicCB);
		newsTopicCB.setSelectedIndex(0);

		//Setting frame visible
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitButton) {	//If user presses "Fetch"
			//Error check - Ensures user selects at least one newspaper source
			if (newspaperCB[0].isSelected() == false && newspaperCB[1].isSelected() == false && newspaperCB[2].isSelected() == false) {
				//Display error message - need to select a news source
				JOptionPane.showMessageDialog(null, "Please select a news source!", "Message", JOptionPane.INFORMATION_MESSAGE);
			} else {
				//If CTV News is selected
				if (newspaperCB[0].isSelected() == true) {
					try {
						//Goes through each topic and retrieves articles of that topic (All RSS links are unique)
						if (newsTopicCB.getSelectedItem().equals("Top Stories"))
							retrieveLink("CTV News", "https://www.ctvnews.ca/rss/ctvnews-ca-top-stories-public-rss-1.822009");
						else if (newsTopicCB.getSelectedItem().equals("Canada"))
							retrieveLink("CTV News", "https://www.ctvnews.ca/rss/ctvnews-ca-canada-public-rss-1.822284");
						else if (newsTopicCB.getSelectedItem().equals("World"))
							retrieveLink("CTV News", "https://www.ctvnews.ca/rss/ctvnews-ca-world-public-rss-1.822289");
						else if (newsTopicCB.getSelectedItem().equals("Sports"))
							retrieveLink("CTV News", "https://www.ctvnews.ca/rss/sports/ctv-news-sports-1.3407726");
						else if (newsTopicCB.getSelectedItem().equals("Business"))
							retrieveLink("CTV News", "https://www.ctvnews.ca/rss/business/ctv-news-business-headlines-1.867648");
						else if (newsTopicCB.getSelectedItem().equals("Entertainment"))
							retrieveLink("CTV News", "https://www.ctvnews.ca/rss/ctvnews-ca-entertainment-public-rss-1.822292");
						else if (newsTopicCB.getSelectedItem().equals("Science/Technology"))
							retrieveLink("CTV News", "https://www.ctvnews.ca/rss/ctvnews-ca-sci-tech-public-rss-1.822295");
						else if (newsTopicCB.getSelectedItem().equals("Health"))
							retrieveLink("CTV News", "https://www.ctvnews.ca/rss/ctvnews-ca-health-public-rss-1.822299");
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				//If Toronto Star is selected
				if (newspaperCB[1].isSelected() == true) {
					try {
						//Goes through each topic and retrieves articles of that topic (All RSS links are unique)
						if (newsTopicCB.getSelectedItem().equals("Top Stories"))
							retrieveLink("Toronto Star", "http://www.thestar.com/content/thestar/feed.RSSManagerServlet.topstories.rss");
						else if (newsTopicCB.getSelectedItem().equals("Canada"))
							retrieveLink("Toronto Star", "http://www.thestar.com/content/thestar/feed.RSSManagerServlet.articles.news.canada.rss");
						else if (newsTopicCB.getSelectedItem().equals("World"))
							retrieveLink("Toronto Star", "http://www.thestar.com/content/thestar/feed.RSSManagerServlet.articles.news.world.rss");
						else if (newsTopicCB.getSelectedItem().equals("Sports"))
							retrieveLink("Toronto Star", "http://www.thestar.com/content/thestar/feed.RSSManagerServlet.articles.sports.rss");
						else if (newsTopicCB.getSelectedItem().equals("Business"))
							retrieveLink("Toronto Star", "http://www.thestar.com/content/thestar/feed.RSSManagerServlet.articles.business.rss");
						else if (newsTopicCB.getSelectedItem().equals("Entertainment"))
							retrieveLink("Toronto Star", "http://www.thestar.com/content/thestar/feed.RSSManagerServlet.articles.entertainment.rss");
						else if (newsTopicCB.getSelectedItem().equals("Science/Technology"))
							retrieveLink("Toronto Star", "http://www.thestar.com/content/thestar/feed.RSSManagerServlet.articles.life.technology.rss");
						else if (newsTopicCB.getSelectedItem().equals("Health"))
							retrieveLink("Toronto Star", "http://www.thestar.com/content/thestar/feed.RSSManagerServlet.articles.life.health_wellness.rss");
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				//If CBC News is selected
				if (newspaperCB[2].isSelected() == true) {
					try {
						//Goes through each topic and retrieves articles of that topic (All RSS links are unique)
						if (newsTopicCB.getSelectedItem().equals("Top Stories"))
							retrieveLink("CBC News", "http://www.cbc.ca/cmlink/rss-topstories");
						else if (newsTopicCB.getSelectedItem().equals("Canada"))
							retrieveLink("CBC News", "http://www.cbc.ca/cmlink/rss-canada");
						else if (newsTopicCB.getSelectedItem().equals("World"))
							retrieveLink("CBC News", "http://www.cbc.ca/cmlink/rss-world");
						else if (newsTopicCB.getSelectedItem().equals("Sports"))
							retrieveLink("CBC News", "http://www.cbc.ca/cmlink/rss-sports");
						else if (newsTopicCB.getSelectedItem().equals("Business"))
							retrieveLink("CBC News", "http://www.cbc.ca/cmlink/rss-business");
						else if (newsTopicCB.getSelectedItem().equals("Entertainment"))
							retrieveLink("CBC News", "http://www.cbc.ca/cmlink/rss-arts");
						else if (newsTopicCB.getSelectedItem().equals("Science/Technology"))
							retrieveLink("CBC News", "http://www.cbc.ca/cmlink/rss-technology");
						else if (newsTopicCB.getSelectedItem().equals("Health"))
							retrieveLink("CBC News", "http://www.cbc.ca/cmlink/rss-health");
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				//Opens the result panel to display results
				try {
					resultPanel = new ResultGUI();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				//Adds result panel onto frame (frame doesn't change)
				add(resultPanel);
				//The inputPanel (this) is set to invisible
				inputPanel.setVisible(false);
			}
		} else if (e.getSource() == exitButton)	//If the exit button is pressed
			//Close the program
			System.exit(0);
		else if (e.getSource() == helpButton)	//If the help button is pressed
			//Open instructions window
			new InstructionsGUI();
	}

	//RetrieveLink Method: Given the RSS, it goes through each link and creates article objects
	public void retrieveLink(String articleType, String feed) throws ParseException {
		//Utilizing JSoup: https://www.youtube.com/watch?v=0s8O7jfy3c0
		try {
			//Connects to the given url (RSS feeds) and masks as Mozilla FireFox Browser
			Document doc = Jsoup.connect(feed).userAgent("Mozilla/17.0").get();
			//Searches and find all HTML text that is in "item"
			Elements temp = doc.select("item");
			for (Element word:temp) {
				//Create new lists for the article to store all unique words, and number of the words
				wordsUnique.add(new ArrayList<String>());
				wordsNumber.add(new ArrayList<Integer>());

				//Temporary string to hold all publishing dates
				String[] tempString = word.select("pubDate").text().split(" ");

				//Converts month to a number
				Date date = null;
				//Converting month to number ("Jan" to "01"): https://stackoverflow.com/questions/24355504/get-month-number-from-month-name
				try {
					date = new SimpleDateFormat("MMM").parse(tempString[2]);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);

				//Month incremented (function returns January as 0, December as 11) 
				tempString[2] = "" + cal.get(Calendar.MONTH + 1);

				//Publishing date of article
				String pubDate = "";

				//Formatting tempString to create pubDate in the format (dd/MM/yy hh:mm:ss)
				for (int i = 1; i < 5; i ++) {
					if (i < 3)	//If it is the date or month
						pubDate += tempString[i] + "/";	//Add a slash after it
					else if (i < 4)	//If it is the year
						pubDate += tempString[i] + " ";	//Add a space after it
					else
						pubDate += tempString[i]; //Otherwise add nothing (articles already provide hh:mm:ss in right format)
				}

				//Using Date & DateFormat: https://docs.oracle.com/javase/8/docs/api/java/util/Date.html
				DateFormat format = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
				Date publishDate = format.parse(pubDate);
				System.out.println("Publish Date: " + publishDate);

				//Add a new article object
				articles.add(new Article(word.select("link").text(), articleType, wordsUnique.size()-1, publishDate, word.select("title").text(), newsTopicCB.getSelectedItem().toString()));
			}
		} catch (IOException e1) {
			//Error catch in case RSS cannot be found
			System.out.println("Cannot retrieve link.");
		}
	}
}
