import java.io.IOException;
import java.text.BreakIterator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Article {

	private String headline;	//Article headline
	private String url;	//Article URL
	private String articleType;	//Type of news source (i.e. CTV News, CBC News, Toronto Star)
	private Date publishDate;	//Article's publish date
	private String category;	//Article topic category (selected by user)

	public int score;	//The article's score
	public int articleNum;	//What number the article is in the arraylist
	public int wordsNum;	//Number of words in the article

	public ArrayList<Sentence> sentences = new ArrayList<Sentence>();	//List of sentences in the article

	public Article(String url, String articleType, int articleNum, Date pubDate, String headline, String category) throws ParseException {
		//Setting information given by the parameters
		this.articleNum = articleNum;
		this.articleType = articleType;
		this.publishDate = pubDate;
		this.category = category;
		
		//If the headline or URL contains "%", add another one
		this.headline = headline.replace("%", "%%");	//When there's a "%", Java mistakens as string formatting
		this.url = url.replace("%", "%%");	//When there's a "%", Java mistakens as string formatting

		//Retrieving text from the article (different newspapers have different HTML formatting)
		//Retrieves the paragraph text from the actual article
		if (articleType.equals("CTV News"))
			RetrieveArticle("div.articleBody");
		else if (articleType.equals("Toronto Star"))
			RetrieveArticle("div.article__body.clearfix.article-story-body");
		else if (articleType.equals("CBC News"))
			if (category.equals("Sports"))	//CBC Sports category is formatted differently
				RetrieveArticle("div.story");
			else
				RetrieveArticle("div.story-content");
	}

	public void RetrieveArticle(String elements) {
		//Utilizing JSoup: https://www.youtube.com/watch?v=0s8O7jfy3c0
		try {
			//Connects to the article URL
			Document doc = Jsoup.connect(url).userAgent("Mozilla/17.0").get();
			
			//Searches and find all HTML text that matches elements
			Elements temp = doc.select(elements);

			for (Element word:temp) {
				//Pick out all <p text in the elements class
				System.out.print(word.select("p").text());
				
				//Converting the paragraph into sentences
				parseStringToSentence(word.select("p").text());
			}
		} catch (IOException e) {
			System.out.println("Not a valid URL");
		}	
	}

	//Method: Splits a paragraph into sentences
	public void parseStringToSentence(String paragraph) {
		//Splitting a paragraph into sentences: //https://docs.oracle.com/javase/7/docs/api/java/text/BreakIterator.html
		BreakIterator breakInterator = BreakIterator.getSentenceInstance(Locale.CANADA);	//Sets language format of breakIterator
		String temp = paragraph;
		breakInterator.setText(temp);
		int start = breakInterator.first();	//Sets starting point
		
		//Looping through breakIterator to break paragraph into sentences
		for (int end = breakInterator.next(); end != BreakIterator.DONE; start = end, end = breakInterator.next()) {
			sentences.add(new Sentence(temp.substring(start, end), articleNum));	//Add new sentence object
		}
	}

	//GETTERS AND SETTERS
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWordsNum() {
		return wordsNum;
	}

	public void setWordsNum(int wordsNum) {
		this.wordsNum = wordsNum;
	}

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}
	
	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
}
