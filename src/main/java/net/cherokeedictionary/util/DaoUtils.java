package net.cherokeedictionary.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DaoUtils {
	
	public static final JsonConverter json;
	
	static {
		json = new JsonConverter();
	}
	
	public static int getUpdateCount(int[] counts) {
		int sum=0;
		for (int c: counts) {
			sum+=c;
		}
		return sum;
	}
	
	public static int getInsertCount(int[] ids) {
		int sum=0;
		for (int id: ids) {
			if (id!=0) {
				sum++;
			}
		}
		return sum;
	}
	
	public static String unicodePronunciation(String pronunciation) {
		if (pronunciation==null) {
			return null;
		}
		pronunciation=pronunciation.replace("a.", "ạ");
		pronunciation=pronunciation.replace("e.", "ẹ");
		pronunciation=pronunciation.replace("i.", "ị");
		pronunciation=pronunciation.replace("o.", "ọ");
		pronunciation=pronunciation.replace("u.", "ụ");
		pronunciation=pronunciation.replace("v.", "ṿ");
		pronunciation=pronunciation.replace("A.", "Ạ");
		pronunciation=pronunciation.replace("E.", "Ẹ");
		pronunciation=pronunciation.replace("I.", "Ị");
		pronunciation=pronunciation.replace("O.", "Ọ");
		pronunciation=pronunciation.replace("U.", "Ụ");
		pronunciation=pronunciation.replace("V.", "Ṿ");
		pronunciation=pronunciation.replace("1", "¹");
		pronunciation=pronunciation.replace("2", "²");
		pronunciation=pronunciation.replace("3", "³");
		pronunciation=pronunciation.replace("4", "⁴");
		pronunciation=pronunciation.replace("?", "ɂ");
		return pronunciation;
	}
	
	public static String asciiPronunciation(String pronunciation) {
		pronunciation=pronunciation.replace("ạ", "a.");
		pronunciation=pronunciation.replace("ẹ", "e.");
		pronunciation=pronunciation.replace("ị", "i.");
		pronunciation=pronunciation.replace("ọ", "o.");
		pronunciation=pronunciation.replace("ụ", "u.");
		pronunciation=pronunciation.replace("ṿ", "v.");
		pronunciation=pronunciation.replace("Ạ", "A.");
		pronunciation=pronunciation.replace("Ẹ", "E.");
		pronunciation=pronunciation.replace("Ị", "I.");
		pronunciation=pronunciation.replace("Ọ", "O.");
		pronunciation=pronunciation.replace("Ụ", "U.");
		pronunciation=pronunciation.replace("Ṿ", "V.");
		pronunciation=pronunciation.replace("¹", "1");
		pronunciation=pronunciation.replace("²", "2");
		pronunciation=pronunciation.replace("³", "3");
		pronunciation=pronunciation.replace("⁴", "4");
		pronunciation=pronunciation.replace("ɂ", "?");
		return pronunciation;
	}
	
	public static java.sql.Timestamp asSqlTimestamp(Date date) {
		return date!=null?new java.sql.Timestamp(date.getTime()):null;
	}
	
	public static java.sql.Date asSqlDate(Date date) {
		return date!=null?new java.sql.Date(date.getTime()):null;
	}
	
	public static java.util.Date asDate(java.sql.Date sqlDate) {
		return sqlDate != null ? new java.util.Date(sqlDate.getTime()) : null;
	}

	public static java.util.Date asDate(java.sql.Timestamp sqlTimestamp) {
		return sqlTimestamp != null ? new java.util.Date(sqlTimestamp.getTime())
				: null;
	}
	
	public static String syllabaryTranslit(String syllabary) {
		if (syllabary==null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(syllabary.length()*3);
		for (char character: syllabary.toCharArray()) {
			String letter = String.valueOf(character);
			if (chr2trans.containsKey(letter)){
				sb.append(chr2trans.get(letter));
				continue;
			}
			sb.append(letter);
		}
		return sb.toString();
	}
	
	private static final Map<String, String> chr2trans = _chr2translit_init();
	private static Map<String, String> _chr2translit_init() {
		Map<String, String> chr2trans = new HashMap<>();
		
		int ix = 0;
		String letter;
		String prefix;
		char chrStart = 'Ꭰ';
		String[] vowels = new String[6];

		vowels[0] = "a";
		vowels[1] = "e";
		vowels[2] = "i";
		vowels[3] = "o";
		vowels[4] = "u";
		vowels[5] = "v";

		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, vowels[ix]);
		}

		chr2trans.put("Ꭶ", "ga");
		chr2trans.put("Ꭷ", "ka");

		prefix = "g";
		chrStart = 'Ꭸ';
		for (ix = 1; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix - 1));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		prefix = "h";
		chrStart = 'Ꭽ';
		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		prefix = "l";
		chrStart = 'Ꮃ';
		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		prefix = "m";
		chrStart = 'Ꮉ';
		for (ix = 0; ix < 5; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		chr2trans.put("Ꮎ", "na");
		chr2trans.put("Ꮏ", "hna");
		chr2trans.put("Ꮐ", "nah");

		prefix = "n";
		chrStart = 'Ꮑ';
		for (ix = 1; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix - 1));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		prefix = "qu";
		chrStart = 'Ꮖ';
		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		chr2trans.put("Ꮜ", "sa");
		chr2trans.put("Ꮝ", "s");

		prefix = "s";
		chrStart = 'Ꮞ';
		for (ix = 1; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix - 1));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		chr2trans.put("Ꮣ", "da");
		chr2trans.put("Ꮤ", "ta");
		chr2trans.put("Ꮥ", "de");
		chr2trans.put("Ꮦ", "te");
		chr2trans.put("Ꮧ", "di");
		chr2trans.put("Ꮨ", "ti");
		chr2trans.put("Ꮩ", "do");
		chr2trans.put("Ꮪ", "du");
		chr2trans.put("Ꮫ", "dv");
		chr2trans.put("Ꮬ", "dla");

		prefix = "hl";
		chrStart = 'Ꮭ';
		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		prefix = "tl";
		chrStart = 'Ꮭ';
		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}
		
		prefix = "ts";
		chrStart = 'Ꮳ';
		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		prefix = "w";
		chrStart = 'Ꮹ';
		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}

		prefix = "y";
		chrStart = 'Ꮿ';
		for (ix = 0; ix < 6; ix++) {
			letter = Character.toString((char) (chrStart + ix));
			chr2trans.put(letter, prefix + vowels[ix]);
		}
		return chr2trans;
	}
}
