

public class Word {

	public String word;	//The actual word
	public int articleNum;	//The article index in which the word is from

	public Word(String word, int articleNum) {
		//Setting information given by parameters
		this.word = word;
		this.articleNum = articleNum;
		this.word = this.word.trim();

		//Clearing the word from any punctuation so that they can be compared and counted
		if (this.word.contains("”") == true)
			replaceWord('”');

		if (this.word.contains("“") == true)
			replaceWord('“');

		if (this.word.endsWith(".") == true) 
			replaceWord('.');

		if (this.word.endsWith("?") == true)
			replaceWord('?');

		if (this.word.endsWith(",") == true)
			replaceWord(',');

		if (this.word.contains("(") == true)
			replaceWord('(');

		if (this.word.contains(")") == true)
			replaceWord(')');

		if (this.word.endsWith("’") || this.word.endsWith("‘") == true) {
			this.word = this.word.substring(0, this.word.length()-1);
		}

		if (this.word.startsWith("’") || this.word.startsWith("‘") == true) {
			this.word = this.word.substring(1, this.word.length());
		}
		addWord();	//Adding word to list
	}
	
	//Method to remove the punctuation
	public void replaceWord(char replace) {
		this.word = this.word.replace(replace, ' ');	//Replacing char with empty space
		this.word = this.word.trim();	//Removing the empty space
	}
	
	//Method that adds word to the list of unique words
	public void addWord() {
		//Change the word to lowercase to compare in results screen
		word = word.toLowerCase();

		boolean duplicate = false;	//Boolean to record if there is a duplicate word

		//Checks if the word already exists in the unique words list
		for (int i = 0; i < InputGUI.wordsUnique.get(articleNum).size(); i ++) {
			
			//If word already exists in the list
			if (word.equals(InputGUI.wordsUnique.get(articleNum).get(i))) {	
				duplicate = true;
				//Increment the number of that word
				InputGUI.wordsNumber.get(articleNum).set(i, InputGUI.wordsNumber.get(articleNum).get(i) + 1);
				break;
			}
		}
		
		//If the word is unique, add it to the unique words list
		if (duplicate == false) {
			InputGUI.wordsUnique.get(articleNum).add(word);
			InputGUI.wordsNumber.get(articleNum).add(1);
		}
	}
}
