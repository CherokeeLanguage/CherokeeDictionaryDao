package net.cherokeedictionary.test;

import java.util.List;

import net.cherokeedictionary.dao.DaoCherokeeDictionary;
import net.cherokeedictionary.model.DictionaryEntry;
import net.cherokeedictionary.model.SearchField;
import net.cherokeedictionary.model.SearchIndex;

public class SimpleQueryTester {
	private static final DaoCherokeeDictionary dao = DaoCherokeeDictionary.dao;

	static {
		DaoCherokeeDictionary.Util.init();
	}

	public static void main(String[] args) {
		List<Integer> ids;
		String[] testQueries = { "ᏩᎭᏯ ᏥᏍᏚ", "\"Ꮎ ᎠᏂᏧᏣ\"", "+Ꮎ +ᎠᏂᏧᏣ", "-Ꮎ +ᎠᏂᏧᏣ", "Ꮎ ᎠᏂᏧᏣ", "Wahaya Jisdu",
				"Wahaya Tsisdu", "\"Na anichuja\"", "\"Na anitsutsa\"", "Wolf Rabbit", "\"The boys\"", "-the +boys",
				"dog", "dogs", "dog*", "ᎠᏓᎾᏩᏍᏗᎭ" };

		for (SearchIndex index : SearchIndex.values()) {
			System.out.println("Searching index " + index.name() + " [" + index.getTable() + "]");
			for (String query : testQueries) {
				ids = dao.search(index, SearchField.All, query);
				if (ids.size() == 0) {
					System.out.println("\t" + query + " - no results.");
					continue;
				}
				System.out.println("\t" + query + ", " + ids.size() + " results.");
				List<DictionaryEntry> entries = dao.entriesById(ids);
				for (DictionaryEntry entry : entries) {
					System.out.println("\t\t" + entry.forms.get(0).syllabary + " [" + entry.forms.get(0).pronunciation
							+ "] " + entry.definitions.get(0) + " [" + entry.id + "]");
					if (entry.examples.size()>0) {
						System.out.println("\t\t\t" + entry.examples.get(0).english);
					}
				}
			}
		}
	}
}

/*
 * SELECT * FROM articles WHERE MATCH (title,body) -> AGAINST ('+MySQL -YourSQL'
 * IN BOOLEAN MODE);
 */