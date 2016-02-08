package net.cherokeedictionary.test;

import java.util.List;

import net.cherokeedictionary.dao.DaoCherokeeDictionary;
import net.cherokeedictionary.model.DictionaryEntry;
import net.cherokeedictionary.model.DictionaryEntry.EntryExample;
import net.cherokeedictionary.model.DictionaryEntry.EntryForm;
import net.cherokeedictionary.model.SearchField;
import net.cherokeedictionary.model.SearchIndex;
import net.cherokeedictionary.util.DaoUtils;

public class SimpleQueryTester {
	private static final DaoCherokeeDictionary dao = DaoCherokeeDictionary.dao;

	static {
		DaoCherokeeDictionary.Util.init();
	}

	public static void main(String[] args) {
		List<Integer> ids;
		String[] testQueries = {".*ᎾᏕ[[:>:]]",};//"[[:<:]]ᎬᏂ[[:>:]]"};// { ".*(Ꭵ|Ꭼ|Ꮂ|Ꮈ|Ꮕ|Ꮢ|Ꮫ|Ꮲ|Ꮸ|Ꮾ|Ᏼ)ᏂᏗ.*" };
		/*
		 * , "(ᏩᎭᏯ|ᏥᏍᏚ)", "Ꮎ ᎠᏂᏧᏣ", "(Wahaya|Jisdu)",
				"(Wahaya|Tsisdu)", "Na anichuja", "Na anitsutsa", "(Wolf|Rabbit)",
				"The boys", "dog", "dogs", "ᎠᏓᎾᏩᏍᏗᎭ", "tsa[a-zA-Z]+gi", "ᏣᎳ" };
		 */

		for (SearchIndex index : SearchIndex.values()) {
//			if (!SearchIndex.Syllabary.equals(index)){
//				continue;
//			}
			System.out.println("Searching index " + index.name() + " [" + index.getTable() + "]");
			for (String query : testQueries) {
				ids = dao.search(index, SearchField.All, query);
				if (ids.size() == 0) {
					System.out.println("\twhere concat("+SearchField.All.getFields()+") rlike '" + query + "' - no results.");
					continue;
				}
				System.out.println("\twhere concat("+SearchField.All.getFields()+") rlike '" + query + "', " + ids.size() + " results.");
				List<DictionaryEntry> entries = dao.entriesById(ids);
				for (DictionaryEntry entry : entries) {
					EntryForm entryForm = entry.forms.get(0);
					System.out.println("\t\t" + entryForm.syllabary + " ["
							+ DaoUtils.unicodePronunciation(entryForm.pronunciation) + "] "
							+ entry.definitions.get(0) + " [" + entry.id + "]");
					if (entry.examples.size() > 0) {
						EntryExample entryExample = entry.examples.get(0);
						if (entryExample != null) {
							if (!isBlank(entryExample.syllabary)) {
								System.out.println("\t\t\t" + entryExample.syllabary);
							}
							if (!isBlank(entryExample.latin)) {
								System.out.println("\t\t\t" + entryExample.latin);
							}
							if (!isBlank(entryExample.english)) {
								System.out.println("\t\t\t" + entryExample.english);
							}
						}
					}
				}
			}
		}
	}
	public static boolean isBlank(String txt) {
		if (txt==null) {
			return true;
		}
		return txt.replaceAll("\\s+", "").isEmpty();
	}
}

/*
 * SELECT * FROM articles WHERE MATCH (title,body) -> AGAINST ('+MySQL -YourSQL'
 * IN BOOLEAN MODE);
 */