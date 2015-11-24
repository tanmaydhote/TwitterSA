import java.util.ArrayList;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * Displays word forms and definitions for synsets containing the word form
 * specified on the command line. To use this application, specify the word
 * form that you wish to view synsets for, as in the following example which
 * displays all synsets containing the word form "airplane":
 * <br>
 * java TestJAWS airplane
 */
public class RunWordnet
{
	static WordNetDatabase database;
	public RunWordnet()
	{
		System.out.println("Wordnet Access");
		System.setProperty("wordnet.database.dir", "/usr/local/WordNet-3.0/dict/");
		database = WordNetDatabase.getFileInstance();
	}
	@SuppressWarnings("unchecked")
	public static ArrayList<String> GetSynonyms(String word)
	{
			Synset[] synsets = database.getSynsets(word);
			@SuppressWarnings("rawtypes")
			ArrayList synonyms = new ArrayList<String>();
			//  Display the word forms and definitions for synsets retrieved
				for (int i = 0; i < synsets.length; i++)
				{
					String[] wordForms = synsets[i].getWordForms();
					for (int j = 0; j < wordForms.length; j++)
					{
						if(!word.equals(wordForms[j]))
						synonyms.add(wordForms[j]);		
					}
				}
				return synonyms;
	}

}