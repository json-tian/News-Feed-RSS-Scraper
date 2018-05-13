import java.util.ArrayList;

public class Sentence {

	public String sentence;	//The actual sentence
	public int article;	//The article number
	public static ArrayList <Word> words = new ArrayList<Word>();	//List of words in the sentence

	public Sentence(String sentence, int article) {
		//Setting information given by parameters
		this.sentence = sentence;
		this.article = article;

		//Splits sentence every empty space
		String[] sentenceWords = sentence.split(" ");    
		
		//For every word in the sentence
		for (String temp : sentenceWords) {
			//Checks if it contains at least one letter in the alphabet (not an empty line) - https://stackoverflow.com/questions/14278170/how-to-check-whether-a-string-contains-at-least-one-alphabet-in-java
			boolean atleastOneAlpha = temp.matches(".*[a-zA-Z]+.*");
			
			//If there is at least one letter
			if (atleastOneAlpha == true)
				words.add(new Word(temp, article));	//Add this word to the list of words
		}
	}
}
