package net.cherokeedictionary.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.cherokeedictionary.dao.DaoCherokeeDictionary;
import net.cherokeedictionary.model.DictionaryEntry;

public class SimpleIndexer {
	
	private static final DaoCherokeeDictionary dao = DaoCherokeeDictionary.dao;
	static {
		DaoCherokeeDictionary.Util.init();
	}
	
	public static void main(String[] args) {
		
		int size=0;
		int sum=0;
		do {
			List<DictionaryEntry> forIndexing=dao.needsIndexing(100);
			if (forIndexing==null) {
				break;
			}
			Set<Integer> forIndexingIds = new HashSet<>();
			for (DictionaryEntry entry: forIndexing) {
				forIndexingIds.add(entry.id);
			}
			size = forIndexing.size();
			sum+=size;
			System.out.println("Indexing "+size+" records.");
			
			indexEnglish(forIndexing, forIndexingIds);
			indexLatin(forIndexing, forIndexingIds);
			indexSyllabary(forIndexing, forIndexingIds);
			
			System.out.println("\t\tIndex marked "+DaoUtils.getUpdateCount(dao.updateIndexMarksById(forIndexing))+" records.");
			System.out.println();
		} while (size!=0);
		System.out.println("No more records need indexing.");
		System.out.println("\t"+sum+" records were processed.");
	}

	private static void indexLatin(List<DictionaryEntry> forIndexing, Set<Integer> forIndexingIds) {
		List<DictionaryEntry> forAdding=new ArrayList<>();
		List<DictionaryEntry> forUpdating=new ArrayList<>();
		Set<Integer> ids = new HashSet<>(forIndexingIds);
		if (ids.size()!=0) {
			ids.retainAll(dao.existingLatinIds(ids));
		}
		Iterator<DictionaryEntry> ientry = forIndexing.iterator();
		while (ientry.hasNext()) {
			DictionaryEntry entry=ientry.next();
			if (!ids.contains(entry.id)){
				forAdding.add(entry);
				continue;
			}
			forUpdating.add(entry);
		}
		
		System.out.println("\tAdded "+DaoUtils.getInsertCount(dao.addNewIndexLatinEntriesById(forAdding))+" Latin/Translit records.");
		System.out.println("\tUpdated "+DaoUtils.getUpdateCount(dao.updateIndexLatinEntriesById(forUpdating))+" Latin/Translit records.");		
	}
	private static void indexSyllabary(List<DictionaryEntry> forIndexing, Set<Integer> forIndexingIds) {
		List<DictionaryEntry> forAdding=new ArrayList<>();
		List<DictionaryEntry> forUpdating=new ArrayList<>();
		Set<Integer> ids = new HashSet<>(forIndexingIds);
		if (ids.size()!=0) {
			ids.retainAll(dao.existingSyllabaryIds(ids));
		}
		Iterator<DictionaryEntry> ientry = forIndexing.iterator();
		while (ientry.hasNext()) {
			DictionaryEntry entry=ientry.next();
			if (!ids.contains(entry.id)){
				forAdding.add(entry);
				continue;
			}
			forUpdating.add(entry);
		}
		
		System.out.println("\tAdded "+DaoUtils.getInsertCount(dao.addNewIndexSyllabaryEntriesById(forAdding))+" Syllabary records.");
		System.out.println("\tUpdated "+DaoUtils.getUpdateCount(dao.updateIndexSyllabaryEntriesById(forUpdating))+" Syllabary records.");		
	}
	private static void indexEnglish(List<DictionaryEntry> forIndexing, Set<Integer> forIndexingIds) {
		List<DictionaryEntry> forAdding=new ArrayList<>();
		List<DictionaryEntry> forUpdating=new ArrayList<>();
		Set<Integer> ids = new HashSet<>(forIndexingIds);
		if (ids.size()!=0) {
			ids.retainAll(dao.existingEnglishIds(ids));
		}
		Iterator<DictionaryEntry> ientry = forIndexing.iterator();
		while (ientry.hasNext()) {
			DictionaryEntry entry=ientry.next();
			if (!ids.contains(entry.id)){
				forAdding.add(entry);
				continue;
			}
			forUpdating.add(entry);
		}
		
		System.out.println("\tAdded "+DaoUtils.getInsertCount(dao.addNewIndexEnglishEntriesById(forAdding))+" English records.");
		System.out.println("\tUpdated "+DaoUtils.getUpdateCount(dao.updateIndexEnglishEntriesById(forUpdating))+" English records.");		
	}
}
