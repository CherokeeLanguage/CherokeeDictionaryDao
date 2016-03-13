package net.cherokeedictionary.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import net.cherokeedictionary.dao.DaoCherokeeDictionary;
import net.cherokeedictionary.model.DictionaryEntry;
import net.cherokeedictionary.model.DictionaryEntry.EntryExample;

public class GetCedCorpus {
	public static void main(String[] args) throws IOException {
		new File("output").mkdirs();
		File englishCorpus = new File("output/ced.en");
		File syllabaryCorpus = new File("output/ced.chr4");
		List<String> english = new ArrayList<>();
		List<String> cherokee = new ArrayList<>();
		DaoCherokeeDictionary.init();
		DaoCherokeeDictionary dao = DaoCherokeeDictionary.dao;
		List<DictionaryEntry> entries = dao.getRecordsForSource("ced");
		System.out.println("Loaded "+entries.size()+" CED entries.");
		for (DictionaryEntry entry: entries) {
			if (entry.examples==null) {
				continue;
			}
			for (EntryExample example: entry.examples) {
				if (example.english==null||example.english.trim().isEmpty()){
					continue;
				}
				if (example.syllabary==null||example.syllabary.trim().isEmpty()){
					continue;
				}
				english.add(example.english);
				cherokee.add(example.syllabary);
			}
		}
		OutputStreamWriter osw_en = new OutputStreamWriter(new FileOutputStream(englishCorpus), "UTF-8");
		OutputStreamWriter osw_chr = new OutputStreamWriter(new FileOutputStream(syllabaryCorpus), "UTF-8");
		
		example: for (int index=0; index<english.size(); index++) {
			String en = english.get(index);
			String chr = cherokee.get(index);
			en=en.replaceAll("<.*?>", "");
			en=en.replaceAll("\\s+", " ");
			chr=chr.replaceAll("<.*?>", "");
			chr=chr.replaceAll("\\s+", " ");
//			if (en.contains(".")) {
//				int en_count = en.length()-en.replace(".", "").length();
//				int chr_count = chr.length()-chr.replace(".","").length();
//				if (en_count>1 && en_count==chr_count) {
//					for (String e: en.split("\\.")){
//						english.add(e+".");
//					}
//					for (String e: chr.split("\\.")){
//						cherokee.add(e+".");
//					}
//					continue example;
//				}
//			}
//			if (en.contains("!")) {
//				int en_count = en.length()-en.replace("!", "").length();
//				int chr_count = chr.length()-chr.replace("!","").length();
//				if (en_count>1 && en_count==chr_count) {
//					for (String e: en.split("!")){
//						english.add(e+"!");
//					}
//					for (String e: chr.split("!")){
//						cherokee.add(e+"!");
//					}
//					continue example;
//				}
//			}
//			if (en.contains("?")) {
//				int en_count = en.length()-en.replace("?", "").length();
//				int chr_count = chr.length()-chr.replace("?","").length();
//				if (en_count>1 && en_count==chr_count) {
//					for (String e: en.split("\\?")){
//						english.add(e+"?");
//					}
//					for (String e: chr.split("\\?")){
//						cherokee.add(e+"?");
//					}
//					continue example;
//				}
//			}
			osw_en.write(en);
			osw_en.write("\n");
			osw_chr.write(chr);
			osw_chr.write("\n");
		}
		osw_en.close();
		osw_chr.close();
	}
}
