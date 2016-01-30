package net.cherokeedictionary.util;

import java.util.List;

import net.cherokeedictionary.dao.DaoCherokeeDictionary;
import net.cherokeedictionary.model.DictionaryEntry;

public class SimpleIndexer {
	
	private static final DaoCherokeeDictionary dao = DaoCherokeeDictionary.dao;
	static {
		DaoCherokeeDictionary.Util.init();
	}
	
	public static void main(String[] args) {
		List<DictionaryEntry> forIndexing;
		int size=0;
		int sum=0;
		do {
			forIndexing=dao.needsIndexing(100);
			if (forIndexing==null) {
				break;
			}
			size = forIndexing.size();
			sum+=size;
			System.out.println("Indexing "+size+" records.");
			
			System.out.println("\tAdded "+DaoUtils.getInsertCount(dao.addNewIndexLatinEntriesById(forIndexing))+" Latin/Translit records.");
			System.out.println("\tUpdated "+DaoUtils.getUpdateCount(dao.updateIndexLatinEntriesById(forIndexing))+" Latin/Translit records.");
			
			System.out.println("\tAdded "+DaoUtils.getInsertCount(dao.addNewIndexSyllabaryEntriesById(forIndexing))+" Syllabary records.");
			System.out.println("\tUpdated "+DaoUtils.getUpdateCount(dao.updateIndexSyllabaryEntriesById(forIndexing))+" Syllabary records.");
			
			System.out.println("\tAdded "+DaoUtils.getInsertCount(dao.addNewIndexEnglishEntriesById(forIndexing))+" English records.");
			System.out.println("\tUpdated "+DaoUtils.getUpdateCount(dao.updateIndexEnglishEntriesById(forIndexing))+" English records.");
			
			System.out.println("\t\tIndex marked "+DaoUtils.getUpdateCount(dao.updateIndexMarksById(forIndexing))+" records.");
			System.out.println();
		} while (size!=0);
		System.out.println("No more records need indexing.");
		System.out.println("\t"+sum+" records were processed.");
	}
}
