package net.cherokeedictionary.util;

import java.util.ArrayList;
import java.util.List;

import net.cherokeedictionary.dao.DaoCherokeeDictionary;
import net.cherokeedictionary.model.DictionaryEntry;
import net.cherokeedictionary.model.DictionaryEntry.Crossreference;
import net.cherokeedictionary.model.DictionaryEntry.EntryExample;
import net.cherokeedictionary.model.DictionaryEntry.EntryForm;
import net.cherokeedictionary.model.DictionaryEntry.EntryFormType;
import net.cherokeedictionary.model.DictionaryEntry.Note;
import net.cherokeedictionary.model.LikeSpreadsheetsRecord;

public class Migrate {
	
	private static final DaoCherokeeDictionary dao = DaoCherokeeDictionary.dao;
	static {
		DaoCherokeeDictionary.init();
	}
	
	public static void main(String[] args) {
		List<DictionaryEntry> newRecords = new ArrayList<>();
		List<LikeSpreadsheetsRecord> oldRecords = dao.getLikespreadsheetRecords();
		System.out.println("Loaded "+oldRecords.size()+" records for migration.");
		for (LikeSpreadsheetsRecord old: oldRecords) {
			DictionaryEntry entry = new DictionaryEntry();
			entry.id=old.id;
			entry.crossreferences.add(new Crossreference(old.crossreferencet));
			entry.definitions.add(old.definitiond);
			entry.examples.add(new EntryExample(old.sentencesyllr, old.sentenceq, old.sentenceenglishs));
			entry.forms.add(new EntryForm(EntryFormType.Verb3rdPrc,old.syllabaryb, old.entrytone, old.entrya));
			entry.forms.add(new EntryForm(EntryFormType.Verb1stPrc, old.vfirstpresh, old.vfirstprestone, old.vfirstpresg));
			entry.forms.add(new EntryForm(EntryFormType.Verb3rdPast,old.vthirdpastsyllj, old.vthirdpasttone, old.vthirdpasti));
			entry.forms.add(new EntryForm(EntryFormType.Verb3rdHab,old.vthirdpressylll, old.vthirdprestone, old.vthirdpresk));
			entry.forms.add(new EntryForm(EntryFormType.Verb2ndImp,old.vsecondimpersylln, old.vsecondimpertone, old.vsecondimperm));
			entry.forms.add(new EntryForm(EntryFormType.Verb3rdInf,old.vthirdinfsyllp, old.vthirdinftone, old.vthirdinfo));
			entry.forms.add(new EntryForm(EntryFormType.Other,old.nounadjpluralsyllf, old.nounadjpluraltone, old.nounadjplurale));
			entry.notes.add(new Note(old.notes));
			entry.pos=old.partofspeechc;
			entry.source=old.source;
			newRecords.add(entry);
		}
		int percent=-1;
		int size = newRecords.size();
		for (int ix=0; ix<size; ix+=100) {
			if (percent!=(100*ix)/size){
				percent=(100*ix)/size;
				System.out.println("\t\t"+percent+"%");
			}
			int s=ix;
			int e=Math.min(ix+100, size);
			System.out.println("\tUpdating pre-existing records...");
			dao.updateDictionaryEntries(newRecords.subList(s, e));
			System.out.println("\tAdding new records...");
			dao.addNewDictionaryEntriesWithId(newRecords.subList(s, e));
		}
		System.out.println("DONE");
	}
}
